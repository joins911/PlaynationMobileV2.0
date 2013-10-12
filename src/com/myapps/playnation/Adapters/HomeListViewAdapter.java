package com.myapps.playnation.Adapters;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.myapps.playnation.R;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Fragments.TabHosts.Home.XHomeGamesFragment;
import com.myapps.playnation.Fragments.TabHosts.Players.PlayerGamesFragment;
import com.myapps.playnation.Operations.LoadImage;

public class HomeListViewAdapter extends BaseAdapter {
	private ArrayList<Bundle> generalList;

	private static int TYPE_HEADER = 0;
	private static int TYPE_CHILD = 1;
	private LayoutInflater inflater;

	public HomeListViewAdapter(Context context, ArrayList<Bundle> list,Fragment obj) {
		super();
		this.generalList = list;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (generalList != null)
			return generalList.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub.
		if (generalList != null)
			return generalList.get(position);
		else
			return 0;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if (position == 0)
			return TYPE_HEADER;
		else
			return TYPE_CHILD;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView,
			final ViewGroup viewGroup) {
		View view = convertView;
		TextView txEHeadline;
		TextView txELocation;
		TextView txText;
			view = inflater.inflate(R.layout.component_homemsg_elist_layout,
					viewGroup, false);

			txEHeadline = (TextView) view.findViewById(R.id.txEHeadline);
			txELocation = (TextView) view.findViewById(R.id.txELocation);
			txText = (TextView) view.findViewById(R.id.txText);

			ImageView img = (ImageView) view.findViewById(R.id.imgEvent);

			final Bundle mapEntry = generalList.get(position);
			if (mapEntry != null) {
				// txEHeadline.setText("" + mapEntry.getString(Keys.GAMENAME));
				// txELocation.setText(mapEntry.getString(Keys.GAMETYPE));
				// txText.setText(Html.fromHtml(HelperClass
				// .checkGameComments(mapEntry)));
				String imageUrl = mapEntry.getString(Keys.EventIMAGEURL);
				img.setTag(imageUrl);
				new LoadImage(mapEntry.getString(Keys.ID_GAME), "game",
						Keys.gamesTable, imageUrl, img, "games").execute(img);
			}

		return view;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
