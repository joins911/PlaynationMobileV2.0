package com.myapps.playnation.Adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.graphics.Color;
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
import com.myapps.playnation.Classes.DataSection;
import com.myapps.playnation.Classes.NewsFeedItem;
public class BubbleMessageAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<NewsFeedItem> msgesList;
	TextView messageText, messageTime;
	private Calendar lastDate = Calendar.getInstance();
	private Context context;
	private RelativeLayout wrapper;

	public BubbleMessageAdapter(Context context, ArrayList<NewsFeedItem> args) {
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
		NewsFeedItem item = (NewsFeedItem) msgesList.get(position);
		View row = convertView;
		if (item != null) {
			if (item.isSection()) {
				DataSection ds = (DataSection) item;
				TextView txDate = new TextView(context);
				txDate.setText(ds.getKey_Title());
				txDate.setGravity(Gravity.CENTER);
				txDate.setTextColor(Color.BLACK);
				txDate.setTextSize(20);
				txDate.setOnClickListener(null);
				txDate.setOnLongClickListener(null);
				txDate.setSelected(false);
				txDate.setLongClickable(false);
				txDate.setEnabled(false);
				txDate.setClickable(false);
				txDate.setFocusable(false);
				row = txDate;
			} else {
				row = inflater.inflate(R.layout.component_message_bubbles,
						parent, false);
				wrapper = (RelativeLayout) row.findViewById(R.id.wrapper);

				messageText = (TextView) row.findViewById(R.id.comment);
				messageTime = (TextView) row.findViewById(R.id.lblMsgDate);
				Message message = (Message) item;
				messageText.setText(message.getContent());

				messageTime.setText(message.getDate().getTime().getHours()
						+ ":" + message.getDate().getTime().getMinutes());
				messageText
						.setBackgroundResource(message.getSide() ? R.drawable.bubble_yellow
								: R.drawable.bubble_green);
				wrapper.setGravity(message.getSide() ? Gravity.LEFT
						: Gravity.RIGHT);
			}
		}
		return row;
	}
}
