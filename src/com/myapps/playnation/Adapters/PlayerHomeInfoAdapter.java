package com.myapps.playnation.Adapters;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.myapps.playnation.R;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Fragments.DialogSendCommentFragment;
import com.myapps.playnation.Operations.Configurations;
import com.myapps.playnation.Operations.DataConnector;
import com.myapps.playnation.Operations.LoadImage;

public class PlayerHomeInfoAdapter extends BaseAdapter implements IShowMore {
	private LayoutInflater inflater;
	private ArrayList<Bundle> tempList;
	private int count;
	private boolean showMore = true;
	private DataConnector con;
	private FragmentManager fragmentManager;
	String addText = "";
	int color;

	public PlayerHomeInfoAdapter(Context context, ArrayList<Bundle> list,
			FragmentManager frag) {
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.tempList = list;
		count = 10;
		addText = context.getResources().getString(
				R.string.btnSendFriendRequest);
		color = context.getResources().getColor(R.color.btnLikeColor);
		fragmentManager = frag;
		con = DataConnector.getInst();
	}

	@Override
	public int getCount() {
		if (tempList != null) {
			if (tempList.size() <= count) {
				return tempList.size();
			} else {
				return count;
			}
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int arg0) {
		return tempList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View view = arg1;
		view = inflater.inflate(R.layout.component_homeheader_layout, arg2,
				false);

		QuickContactBadge playerIcon = (QuickContactBadge) view
				.findViewById(R.id.quickContactBadge1);

		TextView txPlName = (TextView) view.findViewById(R.id.txPlName);
		TextView txPlNick = (TextView) view.findViewById(R.id.txPlNick);
		TextView txPlAge = (TextView) view.findViewById(R.id.txPlAge);
		TextView txPlCountry = (TextView) view.findViewById(R.id.txPlCountry);

		TextView txEdit = (TextView) view.findViewById(R.id.txtEdit);
		TextView txSendMsg = (TextView) view.findViewById(R.id.lblSendMessage);

		txEdit.setText(addText);
		txEdit.setTextColor(color);
		final Bundle mapEntry = tempList.get(arg0);

		if (mapEntry != null) {
			String mutual = mapEntry.getString(Keys.Mutual);
			if (mutual.equals("1") || mutual.equals("null")) {
				txEdit.setVisibility(View.GONE);

			} else {
				txSendMsg.setVisibility(View.GONE);
				txEdit.setText(addText);
				txEdit.setTextColor(color);
			}
			txEdit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					con.functionQuery(Configurations.CurrentPlayerID,
							mapEntry.get(Keys.ID_PLAYER) + "",
							"friendFunctions.php", Keys.POSTFUNCOMMANTSend, "");
					v.setVisibility(View.GONE);
				}
			});
			if (Configurations.getConfigs().getApplicationState() != 0) {
				txEdit.setVisibility(View.GONE);
				txSendMsg.setVisibility(View.GONE);
			}
			txSendMsg.setTextColor(color);
			txSendMsg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					DialogFragment dialog = new DialogSendCommentFragment();
					dialog.show(fragmentManager, "SendMessage");
					Bundle argss = new Bundle();
					argss.putString(Keys.ID_PLAYER,
							Configurations.CurrentPlayerID);
					argss.putString(Keys.functionAnotherID,
							mapEntry.getString(Keys.ID_PLAYER));
					argss.putString(Keys.PLAYERNICKNAME,
							mapEntry.getString(Keys.PLAYERNICKNAME));
					dialog.setArguments(argss);
				}
			});
			playerIcon.setContentDescription(mapEntry
					.getString(Keys.PLAYERNICKNAME));
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
		}

		return view;
	}

	@Override
	public void showMore() {
		if (showMore)
			if (count + 5 <= tempList.size())
				count = count + 5;
			else {
				count = tempList.size();
				showMore = false;
			}
	}

	@Override
	public boolean canShowMore() {
		return showMore;
	}

}
