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
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.myapps.playnation.R;
import com.myapps.playnation.Adapters.HomeMsgesAdapter;
import com.myapps.playnation.Adapters.HomeWallExpListAdapter;
import com.myapps.playnation.Classes.ExpandbleParent;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Fragments.BaseFragment;
import com.myapps.playnation.Operations.DataConnector;
import com.myapps.playnation.main.MessagesActivity;

public class HomeMessagesFragment extends Fragment implements BaseFragment {
	private DataConnector con;
	private ArrayList<ExpandbleParent> listParents = new ArrayList<ExpandbleParent>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.component_mainlist,
				container, false);
		con = DataConnector.getInst();

		ListView mListView = (ListView) view
				.findViewById(R.id.mainList);

		if (!con.getLinker().checkDBTableExits(Keys.HomeMsgTable))
			con.queryPlayerMessages(Keys.TEMPLAYERID);

		listParents.clear();
		ArrayList<Bundle> list = con.getTable(Keys.HomeMsgTable,
				Keys.TEMPLAYERID);
		if (list != null)
			for (Bundle hashMap : list) {
				if (hashMap != null) {
					ExpandbleParent parentItem = new ExpandbleParent(hashMap,Keys.HomeMsgRepliesTable);
					listParents.add(parentItem);
				}
			}
		ArrayList<Bundle> msges = new ArrayList<Bundle>();
		for(ExpandbleParent item:listParents)
		{
			msges.add(item.getFirstChild());
		}
		HomeMsgesAdapter expAdapter = new HomeMsgesAdapter(getActivity(), msges);

		if (expAdapter.isEmpty()) {
			TextView msgText = (TextView) view.findViewById(R.id.noFriendsText);
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
			//mListView.addHeaderView(msgText);

		}
		mListView.setAdapter(expAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {					
					Bundle args = new Bundle();
					ArrayList<Bundle> replies = listParents.get(pos).getArrayChildren();
					for(int i=0;i<replies.size();i++)
					args.putBundle(""+i, replies.get(i));
					startMessageAct(args);					
			}		
			
		});
		return view;
	}

	private void startMessageAct(Bundle args)
	{
		Intent mInt = new Intent(getActivity(),MessagesActivity.class);
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
