package clientserverchat.client;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Main().showLoginGUI());
	}
	
	private JFrame frame;
	private LoginGUI loginGUI;
	private ChatGUI  chatGUI;
	private String username;
	
	private void showLoginGUI() {
		Action nameSubmitAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				username = loginGUI.getUsername();
				showChatGUI();
			}
		};
		loginGUI = new LoginGUI(nameSubmitAction);
		frame = new JFrame("Chat Client");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridBagLayout());
		frame.getContentPane().add(loginGUI, new GridBagConstraints());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private void showChatGUI() {
		System.out.println(username);
		frame.getContentPane().removeAll();
		frame.getContentPane().setLayout(new BorderLayout());
		chatGUI = new ChatGUI(null, username);
		frame.getContentPane().add(chatGUI, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.repaint();
	}
}
