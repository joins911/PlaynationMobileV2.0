package com.myapps.playnation.Fragments.TabHosts.Players;

import java.sql.ResultSet;
import java.util.ArrayList;

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
import com.myapps.playnation.Adapters.GamesListAdapter;
import com.myapps.playnation.Adapters.HomeListViewAdapter;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Operations.Configurations;
import com.myapps.playnation.Operations.DataConnector;
import com.myapps.playnation.Workers.QueryTask;
import com.myapps.playnation.main.IQueryTask;
import com.myapps.playnation.main.ISectionAdapter;
import com.myapps.playnation.main.MainActivity;

public class PlayerGamesFragment extends Fragment implements IQueryTask{
	private DataConnector con;
	private ISectionAdapter mCallback;
	private ListView mListView;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
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
		mListView = (ListView) view
				.findViewById(R.id.generalPlayerListView);

		Bundle args = getArguments();

		final String playerID = args.getString(Keys.ID_PLAYER);
		con.queryPlayerGames(playerID);
		ArrayList<Bundle> temp = con.getTable(Keys.HomeGamesTable,playerID);
		GamesListAdapter expAdapter = new GamesListAdapter(getActivity(),temp);
		if (expAdapter.isEmpty()) {

			TextView msgText = new TextView(getActivity());
			msgText.setText(R.string.emptyGameListString);
			msgText.setTextColor(Color.parseColor("#CFCFCF"));
			msgText.setTextSize(TypedValue.COMPLEX_UNIT_SP, Keys.testSize);
			msgText.setGravity(Gravity.CENTER_HORIZONTAL);
			mListView.addHeaderView(msgText);
		}	
		mListView.setAdapter(expAdapter);

		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Bundle args = (Bundle) arg0.getItemAtPosition(arg2);
				mCallback.setPageAndTab(Configurations.GamesSTATE, 3,
						args);
			}
		});

		return view;
	}

	@Override
	public void setQueryResult(ArrayList<Bundle> args) {
		
	}
}
