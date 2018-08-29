package clientserverchat.client;

import java.util.Date;

public class Message {
	private String sender;
	private Date date;
	private String content;
	private int id;

	public String getSender() {
		return sender;
	}

	public Date getDate() {
		return date;
	}

	public String getContent() {
		return content;
	}

	public int getId() {
		return id;
	}

	public Message(String sender, Date date, String content, int id) {
		this.sender = sender;
		this.date = date;
		this.content = content;
		this.id = id;
	}
	
	
}
