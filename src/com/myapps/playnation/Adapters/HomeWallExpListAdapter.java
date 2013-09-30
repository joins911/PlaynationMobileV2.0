package com.myapps.playnation.Adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.myapps.playnation.R;
import com.myapps.playnation.Classes.ExpandbleParent;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Fragments.TabHosts.Home.HomeWallFragment;
import com.myapps.playnation.Fragments.TabHosts.Home.HomeMessagesFragment;
import com.myapps.playnation.Operations.Configurations;
import com.myapps.playnation.Operations.DataConnector;
import com.myapps.playnation.Operations.HelperClass;

public class HomeWallExpListAdapter extends BaseExpandableListAdapter {
	private LayoutInflater inflater;
	private ArrayList<ExpandbleParent> mParent;

	// Only used as mark which class is currently present.
	// private Object currentFragment;

	public HomeWallExpListAdapter(Context context,
			ArrayList<ExpandbleParent> parent) {
		mParent = parent;
		inflater = LayoutInflater.from(context);
		//con = DataConnector.getInst();
	}

	// ---------------------------------------------------------------

	// ---------------------------------------------------------------

	@Override
	// counts the number of group/parent items so the list knows how many
	// times calls getGroupView() method
	public int getGroupCount() {
		return mParent.size();
	}

	@Override
	// counts the number of children items so the list knows how many times
	// calls getChildView() method
	public int getChildrenCount(int i) {
		if (mParent.get(i).getArrayChildren() != null)
			return mParent.get(i).getArrayChildren().size();
		else
			return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mParent.get(groupPosition).getFirstChild();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mParent.get(groupPosition).getArrayChildren().get(childPosition);
	}

	@Override
	public long getGroupId(int i) {
		return i;
	}

	@Override
	public long getChildId(int i, int i1) {
		return i1;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	// in this method you must set the text to see the parent/group on the
	// list
	public View getGroupView(int groupPosition, boolean b, View view,
			ViewGroup viewGroup) {

		TextView txEHeadline;
		TextView txELocation;
		TextView txEDate;
		TextView txText;

		Bundle mapEntry = mParent.get(groupPosition).getFirstChild();
		view = inflater.inflate(R.layout.component_homewall_eitem_layout,
				viewGroup, false);
		txEHeadline = (TextView) view.findViewById(R.id.name_TV);
		txText = (TextView) view.findViewById(R.id.content_TV);
		txEDate = (TextView) view.findViewById(R.id.time_TV);
		// ImageView img = (ImageView) view.findViewById(R.id.imgEvent);
		// img.setImageResource(R.drawable.no_forum_100x100);

		txEHeadline
				.setText("" + mapEntry.getString(Keys.WallPosterDisplayName));
		txText.setText("" + Html.fromHtml(mapEntry.getString(Keys.WallMessage)));
		txEDate.setText("Date: "
				+ HelperClass.convertTime(Integer.parseInt(mapEntry
						.getString(Keys.WallPostingTime)),
						Configurations.dataTemplate));
		// return the entire view
		return view;
	}

	int index = 0;

	@SuppressLint("SimpleDateFormat")
	@Override
	// in this method you must set the text to see the children on the list
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View view, ViewGroup viewGroup) {
		view = null;		
		// return the entire view

		TextView txEHeadline;
		TextView txELocation;
		TextView txEDate;
		TextView txText;

		if (!mParent.get(groupPosition).getArrayChildren().isEmpty()) {
			Bundle mapEntry = mParent.get(groupPosition).getArrayChildren().get(childPosition);
			if (mapEntry != null) {
				view = inflater.inflate(
						R.layout.component_homewall_eitem_layout, viewGroup,
						false);
				txEHeadline = (TextView) view.findViewById(R.id.name_TV);
				txText = (TextView) view.findViewById(R.id.content_TV);
				txEDate = (TextView) view.findViewById(R.id.time_TV);
				ImageView img = (ImageView) view.findViewById(R.id.imgEvent);
				img.setImageResource(R.drawable.event);

				txEHeadline.setText(""
						+ mapEntry.getString(Keys.WallPosterDisplayName));
				txText.setText(""
						+ Html.fromHtml(mapEntry.getString(Keys.WallMessage)));
				txEDate.setText("Date: "
						+ mapEntry.getString(Keys.WallPostingTime));
			}
		}
		return view;
	}

	@Override
	public boolean isChildSelectable(int i, int i1) {
		return true;
	}

	// @Override
	// public void registerDataSetObserver(DataSetObserver observer) {
	// /* used to make the notifyDataSetChanged() method work */
	// super.registerDataSetObserver(observer);
	// }

	/*
	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
		//mExpandableList.expandGroup(groupPosition);
		if (groupPosition != lastParent) {
			mExpandableList.collapseGroup(lastParent);
		}
		array = new ArrayList<Bundle>();
		Bundle mapEntry = mParent.get(groupPosition).getFirstChild();
		if (lastParent == groupPosition) {
			con.queryPlayerWallReplices(mapEntry.getString(Keys.ID_WALLITEM),
					Configurations.CurrentPlayerID);
			array = con.getTable(Keys.HomeWallRepliesTable,
					mapEntry.getString(Keys.ID_WALLITEM));
			mParent.get(groupPosition).setArrayChildren(array);
		}
		lastParent = groupPosition;
		notifyDataSetChanged();
	}*/

}
