package clientserverchat.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;
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
import javax.swing.text.DefaultCaret;


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
	
	private SwingWorker<List<Message>, Void> messageFetcher = null;
	private final int FETCH_TIME_INTERVAL = 1000;
	private long lastFetchTime = 0;
	private Timer fetchMessageTimer;
	
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
		chatArea = new JTextArea(20, 100);
		chatArea.setLineWrap(true);
		chatArea.setEditable(false);
//		DefaultCaret caret = (DefaultCaret)chatArea.getCaret();
//		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		// scrolling
		chatScrollPane = new JScrollPane(chatArea);
		chatScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		chatScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		// message input
		userMessage = new JTextField(80);
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
				List<Message> messes = new ArrayList<>();
				for (int i = 0; i < 10; i++) {
					messes.add(new Message(username, new Date(), "stuff", i));
				}
				return messes;
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
				for (Message m : messages) {
					sb.append(m.getId()).append(" - ").append(m.getDate())
					.append(" ").append(m.getSender()).append("\n").append(m.getContent()).append("\n");
				}
				chatArea.append(sb.toString());
				messageFetcher = null;
			}
			
		};
		messageFetcher.execute();
	}
	
	/*private void updateMessages() {
		JScrollBar scrollBar = chatScrollPane.getVerticalScrollBar();
		if (scrollBar.getValue() == scrollBar.getMinimum())
			updatePrevMessages();
	}*/
	
/*	private void updatePrevMessages() {
		System.out.println("Update");
		if (firstMessageID == 0)
			return;
		if (System.currentTimeMillis() - prevMessageLastUpdated < PREV_UPDATE_LIMIT)
			return;
		prevMessageLastUpdated = System.currentTimeMillis();
		int n = 50;
		firstMessageID -= n;
		StringBuilder sb = new StringBuilder();
		for (int i = firstMessageID; i < firstMessageID + n; i++) {
			sb.append(i).append(": Mock message\n");
		}
		String newMessages = sb.toString();
		System.out.println("Start: " + firstMessageID);
		int originalPos = chatArea.getCaretPosition();
		System.out.println("Caret at: " + originalPos);
		chatArea.insert(newMessages, 0);

		System.out.println("After insert msg: " + chatArea.getCaretPosition());
		chatArea.setCaretPosition(newMessages.length());
		
		System.out.println(newMessages.length());
		System.out.println("Change pos: " + chatArea.getCaretPosition());
	}*/
	
}
