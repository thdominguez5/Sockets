package com.everis.Sockets;

import javax.swing.*;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor  {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		MarcoServidor mimarco=new MarcoServidor();
		
		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
	}	
}

class MarcoServidor extends JFrame implements Runnable {
	
	public MarcoServidor(){

		setBounds(1200,300,280,350);				
			
		JPanel milamina= new JPanel();
		
		milamina.setLayout(new BorderLayout());
		
		areatexto=new JTextArea();
		
		milamina.add(areatexto,BorderLayout.CENTER);
		
		add(milamina);
		
		setVisible(true);

		Thread miHilo = new Thread(this);

		miHilo.start();
		
		}
	
	private	JTextArea areatexto;

	@Override
	public void run() {
		//System.out.println("Estoy a la escucha");

		try {
			ServerSocket servidor = new ServerSocket(9999);

			String nick, ip, mensaje;

			PaqueteEnvio paquete_recibido;

			while (true){

				Socket miSocket = servidor.accept();

				ObjectInputStream paquete_datos = new ObjectInputStream(miSocket.getInputStream());

				paquete_recibido = (PaqueteEnvio) paquete_datos.readObject();

				nick = paquete_recibido.getNick();

				ip = paquete_recibido.getIp();

				mensaje = paquete_recibido.getMensaje();

				areatexto.append("\n" + nick + ": " + mensaje + " para: " + ip);

				Socket enviaDestinatarioSocket = new Socket(ip, 9090);

				ObjectOutputStream paqueteReenvio = new ObjectOutputStream(enviaDestinatarioSocket.getOutputStream());

				paqueteReenvio.writeObject(paquete_recibido);

				enviaDestinatarioSocket.close();

				miSocket.close();
			}

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
