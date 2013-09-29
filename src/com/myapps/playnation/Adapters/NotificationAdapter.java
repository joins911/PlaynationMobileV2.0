package com.myapps.playnation.Adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.myapps.playnation.R;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Operations.HelperClass;

public class NotificationAdapter extends BaseAdapter implements IShowMore {
	LayoutInflater inflater;
	ArrayList<Bundle> notifDataCollection;
	int count;
	boolean showMore = true;

	public NotificationAdapter(Activity act, ArrayList<Bundle> map) {
		this.notifDataCollection = map;
		inflater = (LayoutInflater) act
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		count = 10;
	}

	@Override
	public int getCount() {
		if (notifDataCollection.size() >= count)
			return count;
		else
			return notifDataCollection.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View mView = inflater.inflate(R.layout.component_notification, null);
		TextView txMsg = (TextView) mView.findViewById(R.id.txtNotificationMsg);
		TextView txMsgCon = (TextView) mView
				.findViewById(R.id.txtNotificationMsgCon);
		TextView txDate = (TextView) mView
				.findViewById(R.id.txtNotificationDate);
		Button btnAccept = (Button) mView.findViewById(R.id.btnNotAccept);
		Button btnDecline = (Button) mView.findViewById(R.id.btnNotDecline);
		if (notifDataCollection != null) {
			Bundle arg = notifDataCollection.get(position);
			txMsg.setText(arg.getString(Keys.NotificationType));
			txMsgCon.setText(HelperClass.returnNotificationMessage(arg));
			txDate.setText(arg.getString(Keys.NotificationTime));

		}
		btnAccept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("Accept");
			}
		});
		btnDecline.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("Decline");
			}
		});
		return mView;
	}

	@Override
	public void showMore() {
		if (showMore)
			if (count + 5 <= notifDataCollection.size())
				count = count + 5;
			else {
				count = notifDataCollection.size();
				showMore = false;
			}
	}

	@Override
	public boolean canShowMore() {
		return showMore;
	}

}
