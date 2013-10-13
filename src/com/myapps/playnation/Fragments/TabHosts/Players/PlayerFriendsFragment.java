package com.myapps.playnation.Fragments.TabHosts.Players;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.myapps.playnation.R;
import com.myapps.playnation.Adapters.PlayerHomeInfoAdapter;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Operations.Configurations;
import com.myapps.playnation.Operations.DataConnector;
import com.myapps.playnation.main.ISectionAdapter;

public class PlayerFriendsFragment extends Fragment {
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

		Bundle args = getArguments();

		con.queryPlayerFriends(args.getString(Keys.ID_PLAYER));
		PlayerHomeInfoAdapter expAdapter = new PlayerHomeInfoAdapter(
				getActivity(), con.getTable(Keys.HomeFriendsTable,
						args.getString(Keys.ID_PLAYER)), getFragmentManager());

		if (expAdapter.isEmpty()) {

			TextView msgText = new TextView(getActivity());
			msgText.setText(R.string.emptyFriendsListString);
			msgText.setTextColor(Color.parseColor("#CFCFCF"));
			msgText.setTextSize(TypedValue.COMPLEX_UNIT_SP, Keys.testSize);
			msgText.setGravity(Gravity.CENTER_HORIZONTAL);
			mListView.addHeaderView(msgText);
		}
		mListView.setAdapter(expAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle args = (Bundle) parent.getItemAtPosition(position);

				mCallback.setPageAndTab(Configurations.PlayersSTATE, 5, args);
			}
		});
		return view;
	}

}
