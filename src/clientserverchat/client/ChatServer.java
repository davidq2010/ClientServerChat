package clientserverchat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatServer {

	public final static String END_OF_MESSAGE = "\u0004";
	public final static DateFormat DATE_FORMAT = new SimpleDateFormat(
			"dd-MM-yyyy HH:mm:ss");	

	private Socket         serverSocket;
	private BufferedReader in;
	private PrintWriter    out;

	public ChatServer(String serverHost, int port) 
			throws UnknownHostException, IOException {

		this.serverSocket = new Socket(serverHost, port);
		this.in = new BufferedReader(
				new InputStreamReader(serverSocket.getInputStream()));
		this.out = new PrintWriter(
				serverSocket.getOutputStream(), true);
	}

	public List<Message> getMessages(int startID, int endID) throws IOException {
		System.out.println(System.currentTimeMillis() + ": get " + startID + " - " + endID);
		// Construct GET request and send to ClientHandler
		out.println("GET");
		out.println(startID);
		out.println(endID);
		out.println(END_OF_MESSAGE);

		List<Message> messageList = new ArrayList<>();

		String line = in.readLine();
		System.out.println(line);

		// If response is Error, throw exception
		if (line.equals("Error")) {
			// consume the rest of message
			while (!END_OF_MESSAGE.equals(in.readLine()));
			throw new IllegalArgumentException("Invalid ID"); 
		}

		 while(!END_OF_MESSAGE.equals(line)) {
			// Construct message to add to messageList 
			int id = Integer.valueOf(line);
			String sender = in.readLine();
			Date date;
			try {
				date = DATE_FORMAT.parse(in.readLine());
			} catch (ParseException e) {
				throw new IllegalArgumentException("Failed to parse response");
			}
			String content = in.readLine();
			messageList.add(new Message(sender, date, content, id));

			line = in.readLine();
		}
		
		return messageList;
	}

	public boolean sendMessage(String sender, String content) {
		// Construct POST request and send to ClientHandler
		out.println("POST");
		out.println(sender);
		out.println(content);
		out.println(END_OF_MESSAGE);

		try {
			String response = in.readLine();
			while (!END_OF_MESSAGE.equals(in.readLine()));
			if (response.equals("OK"))
				return true;
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
