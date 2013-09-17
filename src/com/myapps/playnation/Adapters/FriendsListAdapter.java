package com.myapps.playnation.Adapters;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.myapps.playnation.R;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Operations.Configurations;
import com.myapps.playnation.Operations.DataConnector;
import com.myapps.playnation.Operations.LoadImage;

public class FriendsListAdapter extends BaseAdapter implements MyBaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<Bundle> generalList;
	private int count;
	private boolean showMore = true;
	String addText = "";
	int color;
	private Context context;
	private DataConnector con;

	public FriendsListAdapter(Context context, ArrayList<Bundle> list) {
		this.generalList = list;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		addText = context.getResources().getString(
				R.string.btnSendFriendRequest);
		color = context.getResources().getColor(R.color.btnLikeColor);
		this.context = context;
		con = DataConnector.getInst();
		count = 10;
	}

	@Override
	public int getCount() {
		if (generalList.size() >= count)
			return count;
		else
			return generalList.size();
	}

	@Override
	public Object getItem(int position) {
		return generalList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		view = inflater.inflate(R.layout.component_homeheader_layout, parent,
				false);
		QuickContactBadge playerIcon = (QuickContactBadge) view
				.findViewById(R.id.quickContactBadge1);

		TextView txPlName = (TextView) view.findViewById(R.id.txPlName);
		TextView txPlNick = (TextView) view.findViewById(R.id.txPlNick);
		TextView txPlAge = (TextView) view.findViewById(R.id.txPlAge);
		TextView txPlCountry = (TextView) view.findViewById(R.id.txPlCountry);
		TextView txEdit = (TextView) view.findViewById(R.id.txtEdit);
		txEdit.setText(addText);
		txEdit.setTextColor(color);

		final Bundle mapEntry = generalList.get(position);
		if (mapEntry != null) {

			if (mapEntry.getString(Keys.Mutual,"").equals("1"))
				txEdit.setVisibility(View.GONE);
			else {
				txEdit.setText(addText);
				txEdit.setTextColor(color);
			}
			txEdit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					con.functionQuery(Configurations.CurrentPlayerID, mapEntry
							.get(Keys.ID_PLAYER).toString(),
							"friendFuncions.php", Keys.POSTFUNCOMMANTSend, "");
				}
			});
			if (Configurations.getConfigs().getApplicationState() != 0) {
				txEdit.setVisibility(View.GONE);
			}

			txPlName.setText("" + mapEntry.getString(Keys.FirstName) + " , "
					+ mapEntry.getString(Keys.LastName));
			txPlNick.setText(view.getResources().getString(R.string.Nick)
					+ mapEntry.getString(Keys.PLAYERNICKNAME));
			txPlCountry.setText(view.getResources().getString(R.string.Country)
					+ mapEntry.getString(Keys.COUNTRY));
			String[] dates = mapEntry.getString(Keys.Age).split("-");
			int year = Integer.parseInt(dates[0]);
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			txPlAge.setText(view.getResources().getString(R.string.Age)
					+ (currentYear - year));

			String imageUrl = mapEntry.getString(Keys.PLAYERAVATAR);
			playerIcon.setTag(imageUrl);
			new LoadImage(imageUrl, playerIcon, "players").execute(playerIcon);
			playerIcon.setMaxWidth(Keys.globalMaxandMinImageSize);
			playerIcon.setMinimumWidth(Keys.globalMaxandMinImageSize);
			playerIcon.setMaxHeight(Keys.globalMaxandMinImageSize);
			playerIcon.setMinimumHeight(Keys.globalMaxandMinImageSize);
		}
		return view;
	}

	@Override
	public void showMore() {
		if (showMore)
			if (count + 5 <= generalList.size())
				count = count + 5;
			else {
				count = generalList.size();
				showMore = false;
			}

	}

	@Override
	public boolean canShowMore() {
		return showMore;
	}

	@Override
	public ArrayList<Bundle> getList() {
		return null;
	}

}
