package clientserverchat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;

import clientserverchat.server.Message;
import clientserverchat.server.ClientHandler;

public class ServerForClient {
	
	private Socket client;
	private BufferedReader reader;
	private PrintWriter writer;
	
	public ServerForClient(String serverHost, int port) {
		try {
			this.client = new Socket(serverHost, port);
		} catch (UnknownHostException e) {
			System.out.println("Could not connect to " + serverHost + 
					" on port " + port);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			this.reader = new BufferedReader(
						new InputStreamReader(client.getInputStream()));
		} catch (IOException e) {
			System.out.println("Problem creating input stream for socket");
			e.printStackTrace();
		}
		try {
			this.writer = new PrintWriter(
					client.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public String getMessages(int startID, int endID) {
		// Construct GET request and send to ClientHandler
		writer.println("GET");
		writer.println(startID);
		writer.println(endID);
		writer.println(ClientHandler.END_OF_MESSAGE);
		
		try {
			while(!(reader.readLine().equals(ClientHandler.END_OF_MESSAGE))) {
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Send request to ClientHandler
		return null;
	}
	
	public void sendMessage(Message message) {
		// Construct POST request
		// Send request to ClientHandler
	}
}
