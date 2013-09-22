package com.myapps.playnation.Fragments.Tabs.Home;

import android.app.Activity;
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
import com.myapps.playnation.Operations.Configurations;
import com.myapps.playnation.Operations.DataConnector;
import com.myapps.playnation.main.ISectionAdapter;

public class XHomeNotificationFragment extends Fragment {
	private DataConnector con;
	private ISectionAdapter mCallback;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (ISectionAdapter) getActivity();
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_template_tabslistview,
				container, false);
		con = DataConnector.getInst();

		ListView mListView = (ListView) view
				.findViewById(R.id.generalPlayerListView);

		if (!con.getLinker().checkDBTableExits(Keys.HomeNotificationTable))
			con.queryNotification(Configurations.CurrentPlayerID);

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
}
