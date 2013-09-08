package com.myapps.playnation.Classes.Menu;

import java.util.ArrayList;

import com.myapps.playnation.R;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Operations.DataConnector;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.QuickContactBadge;
import android.widget.TextView;

public class MainMenuAdapter extends BaseExpandableListAdapter {

	private ArrayList<MyMenuItem> mMenuItems;
	private LayoutInflater inflater;
	private Context context;
	private DataConnector con;

	public MainMenuAdapter(Context context, ArrayList<MyMenuItem> items) {
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuItems = items;
		con = DataConnector.getInst();
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View view = inflater.inflate(R.layout.component_newslist_dateselected, null);
		TextView text = (TextView) view.findViewById(R.id.txtNewsDate);
		text.setText(mMenuItems.get(groupPosition).getTitle());
		return view;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View view = null;
		TextView text=null;
		if(groupPosition==0 && childPosition == 0)
		{
			view = inflater.inflate(R.layout.component_homeheader_layout, null);
			con.queryPlayerInfo(Keys.TEMPLAYERID);
			QuickContactBadge edit = (QuickContactBadge) view
					.findViewById(R.id.quickContactBadge1);
			view = con.populatePlayerGeneralInfo(view, "Wall",
					Keys.TEMPLAYERID);
		}else {
			view = inflater.inflate(R.layout.component_menu_subitem_layout, null);
			text = (TextView) view.findViewById(R.id.subitem_title);
			text.setText(mMenuItems.get(groupPosition).getSubItems().get(childPosition).getTitle());
		}
		if((groupPosition==2 || groupPosition==3)&&(childPosition==3))
		{			
			text.setTextColor(context.getResources().getColor(R.color.main_fontColor));
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.LEFT;
			text.setLayoutParams(params);
		}
		
		return view;
	}

	@Override
	public int getGroupCount() {
		return mMenuItems.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mMenuItems.get(groupPosition).getSubItems().size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mMenuItems.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mMenuItems.get(groupPosition).getSubItems().get(childPosition);
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
