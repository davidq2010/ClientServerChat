package clientserverchat.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MessageHub {
	private List<Message> messages = 
			Collections.synchronizedList(new ArrayList<>());
	
	public synchronized void addMessage(String sender, Date date, String content) {
		Message message = new Message(sender, date, content, messages.size());
		messages.add(message);
	}
	
	public synchronized Message getMessage(int id) {
		if(id < 0) {
			id += messages.size();
		}
		return messages.get(id);
	}
	
	public synchronized List<Message> getMessages(int startID, int endID) {
		if (startID < 0)
			startID += messages.size();
		if (startID < 0)
			startID = 0;
		if (endID < 0)
			endID += messages.size();
		return Collections.unmodifiableList(messages.subList(startID, endID + 1));
	}
}
