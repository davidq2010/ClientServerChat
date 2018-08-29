package clientserverchat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	private int port;

	public Server(int port) {
		this.port = port;
	}

	/**
	 * Start the server on the specified port.
	 * @throws IOException when server cannot start
	 */
	public void start() throws IOException {
		try (ServerSocket server = new ServerSocket(port)) {
			MessageHub messageHub = new MessageHub();

			// Listen for new connection and delegate new client to handler thread
			while(true) {
				Socket client = server.accept();
				ClientHandler clientHandler = new ClientHandler(client, messageHub);
				Thread handlerThread = new Thread(clientHandler);
				handlerThread.start();
			}
		}
	}
}
