package com.myapps.playnation.Adapters;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myapps.playnation.R;
import com.myapps.playnation.Adapters.HomeMsgesAdapter.ViewHolder;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Classes.Message;

public class BubbleMessageAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<Message> msgesList;
	TextView messageText, messageTime;
	private LinearLayout wrapper;

	public BubbleMessageAdapter(Context context, ArrayList<Message> args) {
		msgesList = args;
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
		View row = inflater.inflate(R.layout.component_message_bubbles,
				parent, false);
		wrapper = (LinearLayout) row.findViewById(R.id.wrapper);


		messageText = (TextView) row.findViewById(R.id.comment);

		messageText.setText(message.getContent());

		messageText
				.setBackgroundResource(message.getSide() ? R.drawable.bubble_yellow
						: R.drawable.bubble_green);
		wrapper.setGravity(message.getSide() ? Gravity.LEFT : Gravity.RIGHT);

		return row;
	}
}
