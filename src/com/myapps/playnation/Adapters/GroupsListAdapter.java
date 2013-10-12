package com.myapps.playnation.Adapters;

import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myapps.playnation.R;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Operations.LoadImage;

public class GroupsListAdapter extends ListsHelper implements IShowMore {
	LayoutInflater inflater;
	ImageView thumb_image;
	ArrayList<Bundle> groupsDataCollection;
	JSONArray groupsArray;
	ViewHolder holder;
	int count;
	boolean showMore = true;

	public GroupsListAdapter(Activity act, ArrayList<Bundle> map) {
		super(map);
		this.groupsDataCollection = getNewList();
		inflater = (LayoutInflater) act
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		count = 10;
	}

	public ArrayList<Bundle> getGamesList() {
		return groupsDataCollection;
	}

	@Override
	public int getCount() {

		if (groupsDataCollection.size() >= count)
			return count;
		else

			return groupsDataCollection.size();
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
	public void showMore() {
		groupsDataCollection.addAll(getNextItems());

		if (showMore)
			if (count + 5 <= groupsDataCollection.size())
				count = count + 5;
			else {
				count = groupsDataCollection.size();
				showMore = false;
			}

	}

	@Override
	public boolean canShowMore() {
		return super.canShowMore();
	}

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

		if (groupsDataCollection != null) {
			holder.tvGameName.setText(groupsDataCollection.get(position)
					.getString(Keys.GROUPNAME));
			holder.tvGameType.setText(groupsDataCollection.get(position)
					.getString(Keys.GROUPTYPE)
					+ "  "
					+ groupsDataCollection.get(position).getString(
							Keys.GROUPTYPE2));
			holder.tvGameDate.setText(groupsDataCollection.get(position)
					.getString(Keys.GROUPDATE));
			String imageUrl = groupsDataCollection.get(position).getString(
					Keys.EventIMAGEURL);
			holder.tvImage.setTag(imageUrl);
			new LoadImage(groupsDataCollection.get(position).getString(
					Keys.ID_GROUP), "group", Keys.groupsTable, imageUrl,
					holder.tvImage, "groups").execute(holder.tvImage);
		}
		return vi;
	}

	static class ViewHolder {
		ImageView tvImage;
		TextView tvGameName, tvGameType, tvGameDate;
		// ImageView tvImage;
	}

}
