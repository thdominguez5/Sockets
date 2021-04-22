package com.everis.Sockets;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;


public class Cliente {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MarcoCliente mimarco=new MarcoCliente();
		
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}


class MarcoCliente extends JFrame{
	
	public MarcoCliente(){

		setBounds(600,300,280,350);
				
		LaminaMarcoCliente milamina=new LaminaMarcoCliente();
		
		add(milamina);
		
		setVisible(true);
		}	
	
}

class LaminaMarcoCliente extends JPanel implements Runnable{
	
	public LaminaMarcoCliente(){

		nick = new JTextField(5);

		add(nick);

		JLabel texto=new JLabel("-CHAT-");

		add(texto);

		ip = new JTextField(8);

		add(ip);

		campoChat = new JTextArea(12,20);

		add(campoChat);
	
		campo1=new JTextField(20);
	
		add(campo1);		
	
		miboton=new JButton("Enviar");

		EnviaTexto mievento = new EnviaTexto();

		miboton.addActionListener(mievento);
		
		add(miboton);

		Thread miHilo = new Thread(this);

		miHilo.start();
		
	}

	@Override
	public void run() {

		try{

			ServerSocket servidorCliente = new ServerSocket(9090);

			Socket cliente;

			PaqueteEnvio paqueteRecibido;

			//Flujo de datos de entrada capaz de transportar objetos
			while (true) {

				cliente = servidorCliente.accept();

				ObjectInputStream flujoEntrada =new ObjectInputStream(cliente.getInputStream());

				paqueteRecibido = (PaqueteEnvio) flujoEntrada.readObject();

				campoChat.append("\n" + paqueteRecibido.getNick() + ": " + paqueteRecibido.getMensaje());

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private class EnviaTexto implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			//System.out.println(campo1.getText());

			try {
				//creo el socket
				Socket miSocket = new Socket("127.0.0.1", 9999);

				//creo objeto wrapper para enviar mi información
				PaqueteEnvio datos = new PaqueteEnvio();

				datos.setNick(nick.getText());

				datos.setIp(ip.getText());

				datos.setMensaje(campo1.getText());

				//creo mi flujo de datos de salida

				ObjectOutputStream paquete_datos = new ObjectOutputStream(miSocket.getOutputStream());

				// envio mi objeto serializado al flujo de salida
				paquete_datos.writeObject(datos);

				miSocket.close();


			}catch (UnknownHostException uhe){
				uhe.printStackTrace();
			}
			catch (IOException ioe) {
				System.out.println(ioe.getMessage());
			}

		}
	}
		
	private JTextField campo1, nick, ip;

	private JTextArea campoChat;
	
	private JButton miboton;
	
}

class PaqueteEnvio implements Serializable {

	private String nick, ip, mensaje;

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
}
