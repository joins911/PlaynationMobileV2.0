package com.myapps.playnation.main;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Calendar;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.myapps.playnation.R;
import com.myapps.playnation.Adapters.BubbleMessageAdapter;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Classes.Message;
import com.myapps.playnation.Classes.NewsFeedItem;
import com.myapps.playnation.Operations.Configurations;
import com.myapps.playnation.Operations.DataConnector;
import com.myapps.playnation.Operations.HelperClass;

public class MessagesActivity extends Activity {

	boolean fake = false;
	private View footer;
	private LayoutInflater inflater;
	private EditText commentText;
	private String ID_FRIEND = "";
	private DataConnector con;
	private BubbleMessageAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.component_mainlist);
		TextView noMesgText = (TextView) findViewById(R.id.noFriendsText);
		ListView listView = (ListView) findViewById(R.id.mainList);
		inflater = LayoutInflater.from(MessagesActivity.this);
		footer = inflater.inflate(R.layout.component_comment_footer, null);
		footer.setBackgroundColor(Color.TRANSPARENT);
		con = DataConnector.getInst();

		try {
			adapter = new BubbleMessageAdapter(getApplicationContext(),
					buildList());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (fake)
			noMesgText.setText("Nothing fetched from DB :(");
		else
			noMesgText.setVisibility(View.GONE);
		listView.setDividerHeight(0);
		if (Configurations.getConfigs().getApplicationState() == 0) {
			Button commandButton = (Button) footer
					.findViewById(R.id.wallsF_commBut);
			commentText = (EditText) footer
					.findViewById(R.id.wallsF_comment_EBox);

			commandButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!commentText.getText().toString().isEmpty()) {
						con.sendMessage(commentText.getText().toString(),
								Configurations.CurrentPlayerID, ID_FRIEND);

						commentText.setText("");
					}
				}
			});

			listView.addFooterView(footer);
		}
		listView.setAdapter(adapter);
	}

	public ArrayList<NewsFeedItem> buildList() throws NumberFormatException,
			ParseException {

		Bundle args = getIntent().getExtras();

		ArrayList<NewsFeedItem> replies = new ArrayList<NewsFeedItem>();
		if (args != null && args.size() != 0) {
			for (int i = 0; i < args.size(); i++) {

				boolean answer;
				Calendar date = HelperClass.convertTime(Integer.parseInt(args
						.getBundle("" + i).getString(Keys.MessageTime)));

				String[] arr = args.getBundle("" + i)
						.getString(Keys.MessageParticipants).split(",");

				if (arr[0].equalsIgnoreCase(Configurations.CurrentPlayerID)) {
					if (arr.length > 1) {
						ID_FRIEND = arr[1];
					}
				}
				String content = args.getBundle("" + i).getString(
						Keys.MessageText);

				if (args.getBundle("" + i).getString(Keys.ID_PLAYER)
						.equals(Configurations.CurrentPlayerID))
					answer = false;
				else
					answer = true;
				replies.add(new Message(date, content, answer));

			}
			fake = false;
		} else {
			replies = buildFakeList();
			fake = true;
		}
		return HelperClass.createHeaderListViewMsg(replies);
	}

	public ArrayList<NewsFeedItem> buildFakeList() {
		ArrayList<NewsFeedItem> replies = new ArrayList<NewsFeedItem>();
		replies.add(new Message("yoyoyoy", true));
		replies.add(new Message("hey", false));
		replies.add(new Message("what's up", false));
		replies.add(new Message("you know :P", true));
		replies.add(new Message("yea, i know :D", false));
		return replies;
	}

}
