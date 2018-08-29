package clientserverchat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Receive and and response to request from a client
 */
public class ClientHandler implements Runnable {

	private Socket clientSocket;
	
	public final String END_OF_MESSAGE = "\u0004";
	
	private final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	private MessageHub messageHub;
	
	public ClientHandler(Socket clientSocket, MessageHub messageHub) {
		this.clientSocket = clientSocket;
		this.messageHub = messageHub;
	}
	
	@Override
	public void run() {
		
		try (
			BufferedReader in = new BufferedReader(
					new InputStreamReader(clientSocket.getInputStream()));
				
			PrintWriter out = new PrintWriter(
					clientSocket.getOutputStream(), true);
		) {
			while(!Thread.interrupted()) {
				/*
				 *  Building the request of everything until END_OF_MESSAGE
				 */
				StringBuilder requestBuilder = new StringBuilder();
				String line = in.readLine();
				// empty request
				if (END_OF_MESSAGE.equals(line))
					continue;
				requestBuilder.append(line);
				
				// remaining lines of the request
				while (!END_OF_MESSAGE.equals(line))
					requestBuilder.append("\n").append(line);
				String request = requestBuilder.toString();
				
				/*
				 * Process request and send response
				 */
				System.out.println(request);
				String response;
				try {
					response = processRequest(request);
				} catch (Exception e) {
					response = "Error\n" + END_OF_MESSAGE;
				}
				out.println(response);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String processRequest(String request) {
		String[] lines = request.split("\n");
		
		if (lines[0].equals("GET")) {
			try {
				int startID = Integer.valueOf(lines[1]);
				int endID   = Integer.valueOf(lines[2]);
				return processGetRequest(startID, endID);
			} catch (Exception e) {
				throw new IllegalArgumentException("Invalid request");
			}
		}
		else if (lines[0].equals("POST")) {
			String sender = lines[1];
			Date date = new Date(); // date when message is received
			String content = lines[2];
			messageHub.addMessage(sender, date, content);
			return "OK\n" + END_OF_MESSAGE;
		}
		throw new IllegalArgumentException("Invalid request");
	}
	
	private String processGetRequest(int startID, int endID) {
		List<Message> messages = messageHub.getMessages(startID, endID);
		
		StringBuilder responseBuilder = new StringBuilder();
		for(Message message : messages) {
			responseBuilder.append(message.getId()).append("\n")
			.append(message.getSender()).append("\n")
			.append(DATE_FORMAT.format(message.getDate())).append("\n")
			.append(message.getContent()).append("\n");
		}
		responseBuilder.append(END_OF_MESSAGE);
		return responseBuilder.toString();
	}
}