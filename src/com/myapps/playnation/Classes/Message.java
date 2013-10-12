package com.myapps.playnation.Classes;

import java.util.Calendar;

public class Message implements NewsFeedItem {

	public Calendar mDate;
	public String content;
	public boolean left;

	public Message(String content, boolean left) {
		mDate = Calendar.getInstance();
		this.content = content;
		this.left = left;
	}

	public Message(Calendar date, String content, boolean left) {
		mDate = date;
		this.content = content;
		this.left = left;
	}

	public Calendar getDate() {
		return mDate;
	}

	public String getContent() {
		return content;
	}

	public boolean getSide() {
		return left;
	}

	@Override
	public boolean isSection() {
		return false;
	}

}
