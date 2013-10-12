package com.myapps.playnation.Fragments.TabHosts.Home;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.myapps.playnation.R;
import com.myapps.playnation.Adapters.HomeWallExpListAdapter;
import com.myapps.playnation.Classes.ExpandbleParent;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Fragments.BaseFragment;
import com.myapps.playnation.Operations.Configurations;
import com.myapps.playnation.Operations.DataConnector;

public class HomeWallFragment extends Fragment implements BaseFragment {
	private DataConnector con;
	private ArrayList<ExpandbleParent> listParents = new ArrayList<ExpandbleParent>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.component_homewall_layout,
				container, false);
		con = DataConnector.getInst();
		if (!con.getLinker().checkDBTableExits(Keys.HomeWallTable))
			con.queryPlayerWall(Configurations.CurrentPlayerID, "player");

		listParents.clear();
		ArrayList<Bundle> list = con.getTable(Keys.HomeWallTable,
				Keys.TEMPLAYERID);
		if (list != null)
			for (Bundle hashMap : list) {
				if (hashMap != null) {
					// con.queryPlayerWallReplices(
					// hashMap.getString(Keys.ID_WALLITEM),
					// Configurations.CurrentPlayerID);
					ExpandbleParent parentItem = new ExpandbleParent(hashMap,
							Keys.HomeWallRepliesTable);
					listParents.add(parentItem);
				}
			}

		ExpandableListView eListView = (ExpandableListView) view
				.findViewById(R.id.listView);
		HomeWallExpListAdapter expAdapter = new HomeWallExpListAdapter(
	getActivity(), listParents, getFragmentManager());
		if (expAdapter.isEmpty()) {
			TextView msgText = new TextView(getActivity());
			msgText.setText(R.string.emptyListString);
			msgText.setTextColor(Color.parseColor("#CFCFCF"));
			msgText.setTextSize(TypedValue.COMPLEX_UNIT_SP, Keys.testSize);
			msgText.setGravity(Gravity.CENTER_HORIZONTAL);
			eListView.addHeaderView(msgText);
		}
		eListView.setAdapter(expAdapter);
		return view;
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
