package clientserverchat.client;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginGUI extends JPanel {
	
	private JLabel usernameLabel;
	
	private JButton submitButton;
	
	private JTextField usernameField;
	
	public LoginGUI(ActionListener nameSubmitAction) {
		usernameLabel = new JLabel("Name");
		usernameField = new JTextField(16);
		submitButton = new JButton("Submit");
		submitButton.addActionListener(nameSubmitAction);
		
		this.setLayout(new BorderLayout(5, 10));
		this.add(usernameLabel, BorderLayout.WEST);
		this.add(usernameField, BorderLayout.CENTER);
		this.add(submitButton, BorderLayout.EAST);
	}
	
	public String getUsername() {
		return usernameField.getText();
	}
}
