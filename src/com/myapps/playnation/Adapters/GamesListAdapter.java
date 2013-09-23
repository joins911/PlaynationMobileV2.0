package com.myapps.playnation.Adapters;

import java.util.ArrayList;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.myapps.playnation.Classes.ListsHelper;
import com.myapps.playnation.Operations.Configurations;
import com.myapps.playnation.Operations.LoadImage;
import com.myapps.playnation.main.MainActivity;

/**
 * 
 * @author viperime Adapter for the Games ListView which sets up all the items
 *         in the list
 */
public class GamesListAdapter extends ListsHelper implements MyBaseAdapter {
	LayoutInflater inflater;
	ImageView thumb_image;
	ArrayList<Bundle> gamesDataCollection;
	JSONArray gamesArray;
	ViewHolder holder;
	int count;
	boolean showMore = true;

	public GamesListAdapter(Activity act, ArrayList<Bundle> map) {
		super(map);		
		this.gamesDataCollection = getNewList();
		inflater = (LayoutInflater) act
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// count = 10;
	}
	
	public GamesListAdapter(Activity act, ArrayList<Bundle> map, int initialIndex) {
		super(map);		
		this.gamesDataCollection = getNewList(initialIndex);
		inflater = (LayoutInflater) act
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// count = 10;
	}

	@Override
	public ArrayList<Bundle> getList() {
		return gamesDataCollection;
	}

	@Override
	public int getCount() {
		/*
		 * if (gamesDataCollection.size() >= count) return count; else
		 */
		return gamesDataCollection.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public boolean canShowMore() {
		return super.canShowMore();
	}

	@Override
	public void showMore() {
		gamesDataCollection.addAll(getNextItems());
		/*
		 * if (showMore) if (count + 5 <= gamesDataCollection.size()) count =
		 * count + 5; else { count = gamesDataCollection.size(); showMore =
		 * false; }
		 */
	}

	/**
	 * @param position
	 *            : The position of the View within the list
	 * @param parent
	 *            : The parent of the List
	 */
	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.component_mainlist_itemlayout, null);
			holder = new ViewHolder();

			holder.tvGameName = (TextView) vi
					.findViewById(R.id.gameList_GameName_TView);
			holder.tvGameType = (TextView) vi
					.findViewById(R.id.gameList_GameType_TView);
			holder.tvGameDate = (TextView) vi
					.findViewById(R.id.gameList_GameDate_TView);
			holder.tvImage = (ImageView) vi
					.findViewById(R.id.gameList_GameImage);

			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}

		// Setting all values in listview
		if (gamesDataCollection != null) {
			holder.tvGameName.setText(gamesDataCollection.get(position)
					.getString(Keys.GAMENAME));
			holder.tvGameType.setText(gamesDataCollection.get(position)
					.getString(Keys.GAMETYPE));
			holder.tvGameDate.setText(gamesDataCollection.get(position)
					.getString(Keys.GAMEDATE));
			String imageUrl = gamesDataCollection.get(position).getString(
					Keys.EventIMAGEURL);

			holder.tvImage.setTag(imageUrl);
			new LoadImage(gamesDataCollection.get(position).getString(
					Keys.ID_GAME), "game", Keys.gamesTable, imageUrl,
					holder.tvImage, "games").execute(holder.tvImage);
			holder.tvImage.setMaxWidth(Keys.globalMaxandMinImageSize);
			holder.tvImage.setMinimumWidth(Keys.globalMaxandMinImageSize);
			holder.tvImage.setMaxHeight(Keys.globalMaxandMinImageSize);
			holder.tvImage.setMinimumHeight(Keys.globalMaxandMinImageSize);
		}

		return vi;
	}

	static class ViewHolder {
		TextView tvGameName, tvGameType, tvGameDate;
		ImageView tvImage;
	}

}
