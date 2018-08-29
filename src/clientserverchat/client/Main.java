package clientserverchat.client;

import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Main().showLoginGUI());
	}
	
	private JFrame frame;
	private LoginGUI loginGUI;
	private String username;
	
	private void showLoginGUI() {
		ActionListener nameSubmitAction = e -> {
			username = loginGUI.getUsername();
			showChatGUI();
		};
		loginGUI = new LoginGUI(nameSubmitAction);
		frame = new JFrame("Chat Client");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.add(loginGUI);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private void showChatGUI() {
		System.out.println(username);
		System.exit(0);
	}
}
