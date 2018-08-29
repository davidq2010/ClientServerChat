package clientserverchat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import clientserverchat.server.ClientHandler;
import clientserverchat.server.Message;

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
	
	public List<Message> getMessages(int startID, int endID) {
		// Construct GET request and send to ClientHandler
		writer.println("GET");
		writer.println(startID);
		writer.println(endID);
		writer.println(ClientHandler.END_OF_MESSAGE);
		
		List<Message> messageList = new ArrayList<>();
		
		try {
			String line = reader.readLine();
			
			// If response is Error, throw exception
			if (line.equals("Error\n")) {
				throw new IllegalArgumentException("Invalid request"); 
			}
			
			// Construct message to add to messageList 
			while(!(line.equals(ClientHandler.END_OF_MESSAGE))) { 
				int id = Integer.valueOf(line);
				
				line = reader.readLine();
				String sender = line;
				
				line = reader.readLine();
				Date date = ClientHandler.DATE_FORMAT.parse(line);
				
				line = reader.readLine();
				String content = line;
				
				messageList.add(new Message(sender, date, content, id));
					
				line = reader.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Send request to ClientHandler
		return messageList;
	}
	
	public boolean sendMessage(String sender, String content) {
		// Construct POST request and send to ClientHandler
		writer.println("POST");
		writer.println(sender);
		writer.println(content);
		writer.println(ClientHandler.END_OF_MESSAGE);

		try {
			String response = reader.readLine();
			if (response.equals("Error\n")) {
				return false;
			}
			else if (response.equals("OK\n")) {
				return true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
