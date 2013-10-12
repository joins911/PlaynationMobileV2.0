package com.myapps.playnation.Adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.myapps.playnation.R;
import com.myapps.playnation.Classes.Message;
import com.myapps.playnation.Operations.HelperClass;

public class BubbleMessageAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<Message> msgesList;
	TextView messageText, messageTime;
	private String lastDate = "";
	private Context context;
	private RelativeLayout wrapper;

	public BubbleMessageAdapter(Context context, ArrayList<Message> args) {
		msgesList = args;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return msgesList.size();
	}

	@Override
	public Object getItem(int position) {
		return msgesList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Message message = msgesList.get(position);

		View row = inflater.inflate(R.layout.component_message_bubbles, parent,
				false);
		wrapper = (RelativeLayout) row.findViewById(R.id.wrapper);

		messageText = (TextView) row.findViewById(R.id.comment);
		messageTime = (TextView) row.findViewById(R.id.lblMsgDate);

		messageText.setText(message.getContent());
		lastDate = HelperClass.convertTime(Integer.parseInt(message.getDate()),
				new SimpleDateFormat("MMM dd,yyyy"));
		if (!lastDate.equalsIgnoreCase(message.getDate())) {
			TextView mainDate = new TextView(context);
			mainDate.setText(HelperClass.convertTime(Integer.parseInt(message
					.getDate()), new SimpleDateFormat("MMM dd,yyyy")));

			mainDate.setGravity(Gravity.CENTER);

			wrapper.addView(mainDate, RelativeLayout.ABOVE);
			lastDate = HelperClass.convertTime(Integer.parseInt(message
					.getDate()), new SimpleDateFormat("MMM dd,yyyy"));
		}

		messageTime.setText(HelperClass.convertTime(Integer.parseInt(message
				.getDate()), new SimpleDateFormat("HH:mm")));
		messageText
				.setBackgroundResource(message.getSide() ? R.drawable.bubble_yellow
						: R.drawable.bubble_green);
		wrapper.setGravity(message.getSide() ? Gravity.LEFT : Gravity.RIGHT);

		return row;
	}
}
