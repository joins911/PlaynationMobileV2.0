package com.myapps.playnation.Adapters;

import java.util.ArrayList;

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


public class HomeMsgesAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<Bundle> msgesList;
	ViewHolder holder;


	public HomeMsgesAdapter(Context context,
			ArrayList<Bundle> args) {
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
			holder.img = (ImageView) view.findViewById(R.id.gamesCL_commentImage);			
			holder.txtUser = (TextView) view.findViewById(R.id.gamesCL_commentUsername_TView);
			holder.txtContent = (TextView) view.findViewById(R.id.gamesCL_commentText_TView);
			holder.txtDate = (TextView) view.findViewById(R.id.gamesCL_commentTime_TView);
			view.setTag(holder);			
		}
		else holder = (ViewHolder) view.getTag();
		holder.img.setImageResource(R.drawable.msgclose);
		if(mapEntry!=null)
		{
		holder.txtUser.setText("" + mapEntry.getString(Keys.PLAYERNICKNAME));
		holder.txtDate.setText(mapEntry.getString(Keys.MessageTime));
		holder.txtContent.setText(mapEntry.getString(Keys.MessageText));
		}
		// return the entire view
		return view;
	}

	static class ViewHolder{
		TextView txtUser,txtDate,txtContent;
		ImageView img;
	}

}
