package clientserverchat.client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.Timer;


/**
 * GUI for the main chat room
 */
public class ChatGUI extends JPanel {
	
	private JTextArea   chatArea;
	private JScrollPane chatScrollPane;
	private JTextField  userMessage;
	private JButton     submitMessageBtn;
	
	private ChatServer  server;
	private String      username;
	
	private int lastMessageID = -1;
	
	private SwingWorker<List<Message>, Void> messageFetcher = null;
	private final int FETCH_TIME_INTERVAL = 1000;
	private Timer fetchMessageTimer;
	
	private DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd HH:mm");
	
	public ChatGUI(ChatServer server, String username) {
		this.server = server;
		this.username = username;
		init();
	}
	
	private void init() {
		/*
		 *  init components
		 */
		// messages display
		chatArea = new JTextArea(30, 50);
		chatArea.setLineWrap(true);
		chatArea.setFont(new Font("Consolas", 0, 15));
		chatArea.setEditable(false);
//		DefaultCaret caret = (DefaultCaret)chatArea.getCaret();
//		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		// scrolling
		chatScrollPane = new JScrollPane(chatArea);
		chatScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		chatScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		// message input
		userMessage = new JTextField(45);
		userMessage.setFont(new Font("Consolas", 0, 15));
		submitMessageBtn = new JButton("Send");
		Action sendMessageAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				server.sendMessage(username, userMessage.getText());
				userMessage.setText("");
			}
		};
		userMessage.addActionListener(sendMessageAction);
		submitMessageBtn.addActionListener(sendMessageAction);
		
		/*
		 *  layout
		 */
		super.setLayout(new BorderLayout());
		super.add(chatScrollPane, BorderLayout.CENTER);
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.add(userMessage, BorderLayout.CENTER);
		bottomPanel.add(submitMessageBtn, BorderLayout.EAST);
		super.add(bottomPanel, BorderLayout.SOUTH);
		
		/*
		 * Message update
		 */
		fetchMessageTimer = new Timer(FETCH_TIME_INTERVAL, e -> updateMessages());
		fetchMessageTimer.setRepeats(true);
		fetchMessageTimer.setInitialDelay(0);
		fetchMessageTimer.start();
	}
	
	private void updateMessages() {
		if (messageFetcher != null)
			return;
		messageFetcher = new SwingWorker<List<Message>, Void>() {

			@Override
			protected List<Message> doInBackground() throws Exception {
				int startID = lastMessageID + 1;
				int endID   = -1;
				return server.getMessages(startID, endID);
			}
			
			@Override
			protected void done() {
				StringBuilder sb = new StringBuilder();
				List<Message> messages;
				try {
					messages = get();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
					return;
				}
				if (messages.size() != 0)
					lastMessageID = messages.get(messages.size()-1).getId();
				for (Message m : messages) {
					sb.append(DATE_FORMAT.format(m.getDate()))
					  .append("  ")
					  .append(m.getSender()).append(":\n")
					  .append(m.getContent()).append("\n");
				}
				chatArea.append(sb.toString());
				messageFetcher = null;
			}
			
		};
		messageFetcher.execute();
	}
}
