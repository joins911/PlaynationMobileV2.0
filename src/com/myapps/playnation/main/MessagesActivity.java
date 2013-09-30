package com.myapps.playnation.main;

import java.util.ArrayList;

import com.myapps.playnation.R;
import com.myapps.playnation.Adapters.BubbleMessageAdapter;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Classes.Message;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class MessagesActivity extends Activity {

	boolean fake = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.component_mainlist);
		TextView noMesgText = (TextView) findViewById(R.id.noFriendsText);
		ListView listView = (ListView) findViewById(R.id.mainList);

		BubbleMessageAdapter adapter = new BubbleMessageAdapter(
				getApplicationContext(), buildList());
		listView.setAdapter(adapter);
		if(fake) noMesgText.setText("Nothing fetched from DB :(");
	}

	public ArrayList<Message> buildList() {
		Bundle args = getIntent().getExtras();
		ArrayList<Message> replies = new ArrayList<Message>();
		if (args != null && args.size()!=0) {
			for (int i = 0; i < args.size(); i++) {
				boolean answer;
				String date = args.getBundle("" + i)
						.getString(Keys.MessageTime);
				String content = args.getBundle("" + i).getString(
						Keys.MessageText);
				if (i % 2 == 0)
					answer = true;
				else
					answer = false;

				replies.add(new Message(date, content, answer));
			}
		} else{
			replies = buildFakeList();
			fake = true;
		}
		return replies;
	}

	public ArrayList<Message> buildFakeList() {
		ArrayList<Message> replies = new ArrayList<Message>();
		replies.add(new Message("yoyoyoy", true));
		replies.add(new Message("hey", false));
		replies.add(new Message("what's up", false));
		replies.add(new Message("you know :P", true));
		replies.add(new Message("yea, i know :D", false));
		return replies;
	}

}
