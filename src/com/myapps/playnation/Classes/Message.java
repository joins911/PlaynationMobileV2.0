package com.myapps.playnation.Classes;

public class Message {

	public String mDate;
	public String content;
	public boolean left;

	public Message(String content, boolean left) {
		mDate = "";
		this.content = content;
		this.left = left;
	}

	public Message(String date, String content, boolean left) {
		mDate = date;
		this.content = content;
		this.left = left;
	}

	public String getDate() {
		return mDate;
	}

	public String getContent() {
		return content;
	}

	public boolean getSide() {
		return left;
	}
}
