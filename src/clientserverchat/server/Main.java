package clientserverchat.server;

import java.io.IOException;

public class Main {
	
	static int DEFAULT_PORT = 6969;

	public static void main(String[] args) throws IOException {
		new Server(DEFAULT_PORT).start();
	}

}
