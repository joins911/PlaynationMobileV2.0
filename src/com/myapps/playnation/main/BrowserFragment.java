package com.myapps.playnation.main;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myapps.playnation.R;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Fragments.BaseFragment;
import com.myapps.playnation.Fragments.ListsFragment;
import com.myapps.playnation.Operations.Configurations;
import com.myapps.playnation.Operations.DataConnector;

public class BrowserFragment extends Fragment implements BaseFragment {
	public SectionAdapter mSectionAdapter;
	DataConnector con;
	View root;
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		root = inflater.inflate(R.layout.fragment_browser, null);
		initializePager(root);
		con = DataConnector.getInst();
		return root;
	}

	@Override
	public void onDestroyView() {
		// mSectionAdapter.stopLists();
		super.onDestroyView();
	}

	private void initializePager(View root) {
		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) root.findViewById(R.id.pager);
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		this.mSectionAdapter = new SectionAdapter(getFragmentManager(),
				getActivity(), mViewPager);
		mViewPager.setAdapter(mSectionAdapter);
		mViewPager.setOffscreenPageLimit(6);
	}

	public void searchFunction(String args) {
		ListsFragment frag = mSectionAdapter.getFragments()
				.get(mViewPager.getCurrentItem()).getListFragment();
		ArrayList<Bundle> temp = null;
		if (frag != null && args.length() > 2) {
			if (mViewPager.getCurrentItem() == Configurations.GamesSTATE)
				temp = searchListGames(args);
			else {
				Configurations.getConfigs();
				if (mViewPager.getCurrentItem() == Configurations.GroupsSTATE)
					temp = searchListGroups(args);
				else {
					Configurations.getConfigs();
					if (mViewPager.getCurrentItem() == Configurations.NewsSTATE)
						Log.i("NewsSearch", "To Do");
					else {
						Configurations.getConfigs();
						if (mViewPager.getCurrentItem() == Configurations.CompaniesSTATE)
							temp = searchListCompanies(args);
						else {
							Configurations.getConfigs();
							if (mViewPager.getCurrentItem() == Configurations.PlayersSTATE)
								temp = searchListPlayers(args);
						}
					}
				}
			}
			if (!temp.isEmpty() || temp != null)
				frag.setListBundle(temp);
		}
	}

	public ArrayList<Bundle> searchListGames(String args) {
		ArrayList<Bundle> list = con.getTable(Keys.gamesTable, "");
		ArrayList<Bundle> results = new ArrayList<Bundle>();
		for (int i = 0; i < list.size(); i++)
			if (list.get(i).getString(Keys.GAMENAME).contains(args))
				results.add(list.get(i));
		return results;
	}

	public ArrayList<Bundle> searchListGroups(String args) {
		ArrayList<Bundle> list = con.getTable(Keys.groupsTable, "");
		ArrayList<Bundle> results = new ArrayList<Bundle>();
		for (int i = 0; i < list.size(); i++)
			if (list.get(i).getString(Keys.GROUPNAME).contains(args))
				results.add(list.get(i));
		return results;
	}

	public ArrayList<Bundle> searchListCompanies(String args) {
		ArrayList<Bundle> list = con.getTable(Keys.companyTable, "");
		ArrayList<Bundle> results = new ArrayList<Bundle>();
		for (int i = 0; i < list.size(); i++)
			if (list.get(i).getString(Keys.CompanyName).contains(args))
				results.add(list.get(i));
		return results;
	}

	public ArrayList<Bundle> searchListPlayers(String args) {
		ArrayList<Bundle> list = con.queryPlayerFriendsSearch(args);
		ArrayList<Bundle> results = new ArrayList<Bundle>();
		if (list != null) {
			for (int i = 0; i < list.size(); i++)
				results.add(list.get(i));
		}
		return results;
	}

	public void setPageAndTab(int pageIndex, int tabIndex, Bundle args) {
		mViewPager.setCurrentItem(pageIndex);
		mSectionAdapter.setPageAndTab(pageIndex, tabIndex, args);
	}

	public boolean onBackButtonPressed() {
		if (mSectionAdapter.canBack(mViewPager.getCurrentItem())) {
			mSectionAdapter.onBackBtnPressed();
			return true;
		}
		return false;
	}

	public void setVisible() {
		root.setVisibility(View.VISIBLE);
		mViewPager.invalidate();
	}

	public void setInvisible() {
		root.setVisibility(View.GONE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
