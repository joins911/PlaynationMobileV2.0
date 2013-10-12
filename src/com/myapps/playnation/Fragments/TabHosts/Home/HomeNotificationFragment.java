package com.myapps.playnation.Fragments.TabHosts.Home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.myapps.playnation.R;
import com.myapps.playnation.Adapters.NotificationAdapter;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Fragments.BaseFragment;
import com.myapps.playnation.Operations.Configurations;
import com.myapps.playnation.Operations.DataConnector;

public class HomeNotificationFragment extends Fragment implements BaseFragment {
	private DataConnector con;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_template_tabslistview,
				container, false);
		con = DataConnector.getInst();

		ListView mListView = (ListView) view
				.findViewById(R.id.generalPlayerListView);

		if (!con.getLinker().checkDBTableExits(Keys.HomeNotificationTable))
			con.queryNotification(Configurations.CurrentPlayerID,
					"PNotificationF");

		NotificationAdapter expAdapter = new NotificationAdapter(getActivity(),
				con.getLinker().getSQLiteNotification(
						Keys.HomeNotificationTable,
						Configurations.CurrentPlayerID));

		if (expAdapter.isEmpty()) {
			TextView msgText = new TextView(getActivity());
			msgText.setText(R.string.emptyNotifListString);
			msgText.setTextColor(Color.parseColor("#CFCFCF"));
			msgText.setTextSize(TypedValue.COMPLEX_UNIT_SP, Keys.testSize);
			msgText.setGravity(Gravity.CENTER_HORIZONTAL);
			mListView.addHeaderView(msgText);
		}
		mListView.setAdapter(expAdapter);

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
