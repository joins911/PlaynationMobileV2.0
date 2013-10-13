package com.myapps.playnation.Adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.myapps.playnation.R;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Operations.HelperClass;

public class HomeMsgesAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<Bundle> msgesList;
	ViewHolder holder;

	public HomeMsgesAdapter(Context context, ArrayList<Bundle> args) {
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
		View view = convertView;
		Bundle mapEntry = msgesList.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.component_comment_elist_layout,
					parent, false);
			holder.img = (ImageView) view.findViewById(R.id.imgEvent);
			holder.txtUser = (TextView) view
					.findViewById(R.id.gamesCL_commentUsername_TView);
			holder.txtContent = (TextView) view
					.findViewById(R.id.gamesCL_commentText_TView);
			holder.txtDate = (TextView) view
					.findViewById(R.id.gamesCL_commentTime_TView);
			view.setTag(holder);
		} else
			holder = (ViewHolder) view.getTag();
		if (mapEntry != null) {
			holder.txtUser.setText(""
					+ mapEntry.getString(Keys.MessageAnotherPerson));
			SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy");

			if (mapEntry.containsKey(Keys.MessageTime)
					|| !mapEntry.getString(Keys.MessageTime).equalsIgnoreCase(
							"null")
					|| mapEntry.getString(Keys.MessageTime)
							.equalsIgnoreCase(""))
				holder.txtDate.setText(HelperClass.convertTime(
						Integer.parseInt(mapEntry.getString(Keys.MessageTime)),
						sdf));
			else
				holder.txtDate.setText(sdf.format(Calendar.getInstance()));

			holder.txtContent.setText(mapEntry.getString(Keys.MessageText));
		}
		// return the entire view
		return view;
	}

	static class ViewHolder {
		TextView txtUser, txtDate, txtContent;
		ImageView img;
	}

}
