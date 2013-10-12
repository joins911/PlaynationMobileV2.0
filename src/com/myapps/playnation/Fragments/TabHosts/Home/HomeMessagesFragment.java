package com.myapps.playnation.Fragments.TabHosts.Home;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.myapps.playnation.R;
import com.myapps.playnation.Adapters.HomeMsgesAdapter;
import com.myapps.playnation.Classes.ExpandbleParent;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Fragments.BaseFragment;
import com.myapps.playnation.Operations.Configurations;
import com.myapps.playnation.Operations.DataConnector;
import com.myapps.playnation.main.MessagesActivity;

public class HomeMessagesFragment extends Fragment implements BaseFragment {
	private DataConnector con;
	private ArrayList<ExpandbleParent> listParents = new ArrayList<ExpandbleParent>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.component_mainlist, container,
				false);
		con = DataConnector.getInst();

		ListView mListView = (ListView) view.findViewById(R.id.mainList);

		if (!con.getLinker().checkDBTableExits(Keys.HomeMsgTable))
			con.queryPlayerMessages(Keys.TEMPLAYERID);

		listParents.clear();
		ArrayList<Bundle> list = con.getTable(Keys.HomeMsgTable,
				Keys.TEMPLAYERID);
		if (list != null)
			for (Bundle hashMap : list) {
				if (hashMap != null) {
					con.queryPlayerMSGReplices(
							hashMap.getString(Keys.MessageID_CONVERSATION),
							Configurations.CurrentPlayerID);
					ExpandbleParent parentItem = new ExpandbleParent(hashMap,
							Keys.HomeMsgRepliesTable);
					listParents.add(parentItem);
				}
			}
		ArrayList<Bundle> msges = new ArrayList<Bundle>();
		if (listParents.size() != 0)
			for (ExpandbleParent item : listParents) {
				msges.add(item.getFirstChild());
			}
		else
			msges = makeFakeList();
		HomeMsgesAdapter expAdapter = new HomeMsgesAdapter(getActivity(), msges);
		TextView msgText = (TextView) view.findViewById(R.id.noFriendsText);
		if (expAdapter.isEmpty()) {			
			msgText.setVisibility(View.VISIBLE);
			msgText.setText(R.string.emptyMsgListString);
			msgText.setTextColor(Color.parseColor("#CFCFCF"));
			msgText.setTextSize(TypedValue.COMPLEX_UNIT_SP, Keys.testSize);
			msgText.setGravity(Gravity.CENTER_HORIZONTAL);
			msgText.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					startMessageAct(new Bundle());
				}
			});
			// mListView.addHeaderView(msgText);
		} else {
			msgText.setVisibility(View.GONE);
		}

		mListView.setAdapter(expAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				Bundle args = new Bundle();
				if (listParents.size() != 0) {
					ArrayList<Bundle> replies = listParents.get(pos)
							.getArrayChildren();
					for (int i = 0; i < replies.size(); i++)
						args.putBundle("" + i, replies.get(i));
				}
				else {
				ArrayList<Bundle> replies = makeFakeList();
				for (int i = 0; i < replies.size(); i++)
					args.putBundle("" + i, replies.get(i));
				}
				startMessageAct(args);
				}
		});
		return view;
	}

	private ArrayList<Bundle> makeFakeList() {
		ArrayList<Bundle> bunds = new ArrayList<Bundle>();
		Bundle args = new Bundle();
		args.putString(Keys.PLAYERNICKNAME, "justme");
		args.putString(Keys.MessageText, "07/12/2012 18:46");
		args.putString(Keys.MessageText, "dsadsadmaennnisa ddsa in measdas");
		bunds.add(args);
		Bundle args2 = new Bundle();
		args.putString(Keys.PLAYERNICKNAME, "heyYou");
		args.putString(Keys.MessageText, "07/12/2012 18:46");
		args.putString(Keys.MessageText, "dsadsadmaennnisa ddsa in measdas");
		bunds.add(args2);
		bunds.add(args);
		return bunds;
	}

	private void startMessageAct(Bundle args) {
		Intent mInt = new Intent(getActivity(), MessagesActivity.class);
		mInt.putExtras(args);
		startActivity(mInt);
	}

	@Override
	public void searchFunction(String args) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onBackButtonPressed() {
		// TODO Auto-generated method stub
		return false;
	}

}
