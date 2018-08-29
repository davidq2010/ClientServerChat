package clientserverchat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientHandler implements Runnable {

	private Socket clientSocket;
	
	public ClientHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	
	@Override
	public void run() {
		
		try (
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(clientSocket.getInputStream()));
		) {
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(!Thread.interrupted()) {
			
		}
	}
	
}
