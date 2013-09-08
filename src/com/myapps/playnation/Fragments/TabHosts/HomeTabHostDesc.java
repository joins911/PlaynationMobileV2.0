package com.myapps.playnation.Fragments.TabHosts;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.myapps.playnation.R;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Fragments.Tabs.Home.XHomeEditProfileFragment;
import com.myapps.playnation.Fragments.Tabs.Home.XHomeEventsFragment;
import com.myapps.playnation.Fragments.Tabs.Home.XHomeFriendsFragment;
import com.myapps.playnation.Fragments.Tabs.Home.XHomeGamesFragment;
import com.myapps.playnation.Fragments.Tabs.Home.XHomeGroupsFragment;
import com.myapps.playnation.Fragments.Tabs.Home.XHomeMessagesFragment;
import com.myapps.playnation.Fragments.Tabs.Home.HomeWallFragment;

public class HomeTabHostDesc extends TabHostDesc implements OnTabChangeListener {

	private FragmentTabHost mTabHost;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return initTabHost();
	}

	private FragmentTabHost initTabHost() {
		mTabHost = new FragmentTabHost(getActivity());
		mTabHost.setup(getActivity(), getChildFragmentManager(),
				android.R.id.tabhost);
		setupTab(HomeWallFragment.class, new TextView(getActivity()),
				Keys.TAB_WALL, getArguments());
		setupTab(XHomeMessagesFragment.class, new TextView(getActivity()),
				Keys.TAB_MESSAGES, getArguments());
		setupTab(XHomeGroupsFragment.class, new TextView(getActivity()),
				Keys.TAB_GROUPS, getArguments());
		setupTab(XHomeGamesFragment.class, new TextView(getActivity()),
				Keys.TAB_GAMES, getArguments());
		// setupTab(HomeSubscriptionFragment.class,new
		// TextView(getActivity()),Keys.TAB_SUBSCRIPTION);
		setupTab(XHomeFriendsFragment.class, new TextView(getActivity()),
				Keys.TAB_FRIENDS, getArguments());
		// setupTab(HomeEventsFragment.class, new TextView(getActivity()),
		// Keys.TAB_EVENTS, getArguments());
		// setupTab(GameMediaFragment.class,new
		// TextView(getActivity()),Keys.TAB_EDITPROFILE);
		mTabHost.setOnTabChangedListener(this);
		return mTabHost;

	}

	private void setupTab(Class<?> fragClass, final View view,
			final String tag, Bundle args) {
		View tabview = createTabView(mTabHost.getContext(), tag);

		TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview)
				.setContent(new TabContentFactory() {
					@Override
					public View createTabContent(String tag) {
						return view;
					}
				});
		mTabHost.addTab(setContent, fragClass, args);
	}

	private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.component_tabs_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}

	@Override
	public void onDestroyView() {
		mTabHost = null;
		super.onDestroyView();
	}

	@Override
	public void onTabChanged(String tabId) {
		if (Keys.TAB_WALL.equals(tabId)) {
			HomeWallFragment frag = new HomeWallFragment();
			updateTab(tabId, android.R.id.tabcontent, frag);
			// mCurrentTab = 0;
			// HomeMessagesFragment frag2 = new HomeMessagesFragment();
			// updateTab(tabId, R.id.tabcontent2, frag2);
			return;
		}
		if (Keys.TAB_MESSAGES.equals(tabId)) {
			XHomeMessagesFragment frag = new XHomeMessagesFragment();
			updateTab(tabId, android.R.id.tabcontent, frag);
			// mCurrentTab = 1;
			return;
		}
		if (Keys.TAB_GROUPS.equals(tabId)) {
			XHomeGroupsFragment frag = new XHomeGroupsFragment();
			updateTab(tabId, android.R.id.tabcontent, frag);
			// mCurrentTab = 2;
			return;
		}
		if (Keys.TAB_GAMES.equals(tabId)) {
			XHomeGamesFragment frag = new XHomeGamesFragment();
			updateTab(tabId, android.R.id.tabcontent, frag);
			// mCurrentTab = 3;
			return;
		}
		/*
		 * if (Keys.TAB_SUBSCRIPTION.equals(tabId)) { HomeSubscriptionFragment
		 * frag = new HomeSubscriptionFragment(); updateTab(tabId,
		 * android.R.id.tabcontent, frag); // mCurrentTab = 4; return; }
		 */
		if (Keys.TAB_FRIENDS.equals(tabId)) {
			XHomeFriendsFragment frag = new XHomeFriendsFragment();
			updateTab(tabId, android.R.id.tabcontent, frag);
			// mCurrentTab = 5;
			return;
		}
		if (Keys.TAB_EVENTS.equals(tabId)) {
			XHomeEventsFragment frag = new XHomeEventsFragment();
			updateTab(tabId, android.R.id.tabcontent, frag);
			// mCurrentTab = 6;
			return;
		}
	}

	private void updateTab(String tabId, int placeholder, Fragment frag) {
		FragmentManager fm = getFragmentManager();
		Bundle args = new Bundle();
		args.putAll(getArguments());
		frag.setArguments(args);
		if (fm.findFragmentByTag(tabId) == null) {
			fm.beginTransaction().replace(placeholder, frag, tabId).commit();
		}
	}

	public void switchToEdit() {
		getFragmentManager()
				.beginTransaction()
				.replace(android.R.id.tabcontent,
						new XHomeEditProfileFragment(), Keys.TAB_EDITPROFILE)
				.commit();
	}

	@Override
	public void switchToTab(int tabIndex) {
		this.mTabHost.setCurrentTab(tabIndex);

	}
}
