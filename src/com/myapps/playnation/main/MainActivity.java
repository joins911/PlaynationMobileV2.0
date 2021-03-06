/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.myapps.playnation.main;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.myapps.playnation.R;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Classes.Menu.MainMenuAdapter;
import com.myapps.playnation.Classes.Menu.MyMenuItem;
import com.myapps.playnation.Fragments.BaseFragment;
import com.myapps.playnation.Fragments.HeaderFragment;
import com.myapps.playnation.Fragments.WrapperFragment;
import com.myapps.playnation.Fragments.TabHosts.Home.HomeMessagesFragment;
import com.myapps.playnation.Fragments.TabHosts.Home.HomeNotificationFragment;
import com.myapps.playnation.Fragments.TabHosts.Home.HomeWallFragment;
import com.myapps.playnation.Fragments.TabHosts.Home.XHomeEditProfileFragment;
import com.myapps.playnation.Operations.BackTimer;
import com.myapps.playnation.Operations.Configurations;
import com.myapps.playnation.Operations.DataConnector;
import com.myapps.playnation.Operations.ServiceClass;

public class MainActivity extends ActionBarActivity implements ISectionAdapter {
	private DrawerLayout mDrawerLayout;
	private ExpandableListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mPagesTitles;
	private String[] mMenuTitles;
	private ArrayList<String> mGamesTitles = new ArrayList<String>();
	private ArrayList<String> mGroupsTitles = new ArrayList<String>();

	DataConnector con;
	private int total;
	private boolean finished = false;
	private boolean isTablet;
	private Fragment currFragment;
	private BrowserFragment mBrowserFragment;
	private BackTimer mBackTimer;
	// Used for setting the different fragments outside of the MainActivity
	public static Fragment passCurrFragment;
	public static BrowserFragment passmBrowserFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		miniSetup();
		setContentView(R.layout.activity_main);
		mTitle = mDrawerTitle = getTitle();
		mPagesTitles = getResources().getStringArray(R.array.main_array);
		mMenuTitles = getResources().getStringArray(R.array.menu_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ExpandableListView) findViewById(R.id.left_drawer);
		mBackTimer = new BackTimer();
		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		MainMenuAdapter mMenuAdapter = new MainMenuAdapter(
				getApplicationContext(), buildMenu(),
				getSupportFragmentManager());
		mDrawerList.setAdapter(mMenuAdapter);
		/*
		 * DrawerItemClickListener mListener = new DrawerItemClickListener();
		 * mDrawerList.setOnGroupClickListener(mListener);
		 * mDrawerList.setOnChildClickListener(mListener);
		 */
		mDrawerList.setOnChildClickListener(new DrawerItemClickListener());
		// enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				supportInvalidateOptionsMenu(); // creates call to
				// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mDrawerTitle);
				if (!mDrawerList.isGroupExpanded(0)) {
					mDrawerList.expandGroup(0);
				}
				if (!mDrawerList.isGroupExpanded(1)) {
					mDrawerList.expandGroup(1);
				}
				supportInvalidateOptionsMenu(); // creates call to
				// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectGroup(1, 0);
		}
	}

	private ArrayList<MyMenuItem> buildMenu() {
		ArrayList<MyMenuItem> menu = new ArrayList<MyMenuItem>();
		String[] mainArr = getResources().getStringArray(R.array.main_array);
		String[] menuArr = getResources().getStringArray(R.array.menu_array);
		String[] topArr = { "header" };
		con.queryPlayerGames(Configurations.CurrentPlayerID);
		con.queryPlayerGroup(Configurations.CurrentPlayerID);
		mGamesTitles = con.getLinker().getMyGames(
				Configurations.CurrentPlayerID);
		mGroupsTitles = con.getLinker().getMyGroups(
				Configurations.CurrentPlayerID);
		String showMore = getApplicationContext().getResources().getString(
				R.string.showMore);
		mGamesTitles.add(showMore);
		mGroupsTitles.add(showMore);

		for (int i = 0; i < menuArr.length; i++) {
			MyMenuItem temp = new MyMenuItem(menuArr[i]);
			switch (i) {
			case (0): {
				temp.buildSubItems(topArr);
				break;
			}
			case (1): {
				temp.buildSubItems(mainArr);
				break;
			}
			case (2): {
				temp.buildSubItems(mGamesTitles);
				break;
			}
			case (3): {
				temp.buildSubItems(mGroupsTitles);
				break;
			}
			}
			menu.add(temp);
		}
		return menu;
	}

	@SuppressLint("NewApi")
	private void miniSetup() {
		if (android.os.Build.VERSION.SDK_INT > 10) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		// requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		getSupportActionBar().setTitle("Playnation Mobile");
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.color.background_gradient));
		con = DataConnector.getInst();
		Log.i("MainActiv", "intent.getInt() = "
				+ getIntent().getExtras().getInt(Keys.AppState));
		isTablet = Configurations.getConfigs().isTablet();
		if (!isTablet)
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setSupportProgressBarIndeterminateVisibility(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.barmenu, menu);

		MenuItem searchMenuItem = menu.findItem(R.id.menu_search);
		SearchView searchView = (SearchView) MenuItemCompat
				.getActionView(searchMenuItem);

		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextChange(String arg0) {
				return false;
			}

			@Override
			public boolean onQueryTextSubmit(String arg0) {
				((BaseFragment) currFragment).searchFunction(arg0);
				return false;

			}
		});

		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		// menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onBackPressed() {
		if (currFragment instanceof BaseFragment) {
			if (currFragment instanceof BrowserFragment) {
				if (!((BaseFragment) currFragment).onBackButtonPressed())
					finishActivity();
			} else
				initHome();
		} else
			finishActivity();
	}

	private void finishActivity() {
		if (!mBackTimer.canBack()) {
			mBackTimer.setTimer(2);
			Toast.makeText(getApplicationContext(), "Press back again to exit",
					Toast.LENGTH_SHORT).show();
		} else
			super.onBackPressed();
	}

	@Override
	public void onDestroy() {
		stopService(new Intent(this, ServiceClass.class));
		super.onDestroy();
		java.lang.System.gc();
	}

	public void setPageAndTab(int pageIndex, int tabIndex, Bundle args) {
		if (currFragment instanceof BrowserFragment)
			((BrowserFragment) currFragment).setPageAndTab(pageIndex, tabIndex,
					args);
	}

	public void setPage(int pageIndex, Bundle args) {
		mBrowserFragment.mSectionAdapter.switchTo(pageIndex, args);
	}

	@Override
	public void setIndeterminateVisibility(boolean isVisible) {
		setSupportProgressBarIndeterminateVisibility(isVisible);
	}

	@Override
	public void finishTask(int viewPagerState) {
		Log.i("total:=" + total + " ", "state:=" + viewPagerState + "; "
				+ finished);
		total = total + viewPagerState;
		if (total == 0)
			setSupportProgressBarIndeterminateVisibility(false);
	}

	@Override
	public void startTask(int viewPagerState) {
		total = total - viewPagerState;
		setSupportProgressBarIndeterminateVisibility(true);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	private void selectGroup(int groupPos, int childPos) {
		setTitle(mMenuTitles[groupPos]);
		switch (groupPos) {
		case MainMenu:
			selectChildHome(childPos);
			break;
		case GamesMenu:
			selectChildGames(childPos);
			break;
		case GroupsMenu:
			selectChildGroups(childPos);
			break;
		case LogOutMenu:
			Toast.makeText(getApplicationContext(), "Logging out...",
					Toast.LENGTH_SHORT).show();
			this.finish();
			break;
		}
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	private void selectChildHome(int childPos) {
		switch (childPos) {
		case HomePage:
			initHome();
			break;
		case WallPage:
			initWall();
			break;
		case MessagesPage:
			initMessages();
			break;
		case NotificationPage:
			initNotification();
			break;
		case FriendsPage:
			initFriends();
		}
		setTitle(mPagesTitles[childPos]);
	}

	private void selectChildGames(int childPos) {
		if ((mGamesTitles.size() == 1 && childPos == 0) || childPos == 3)
			initGamesPage();
		else
			initGamePage(childPos);
	}

	private void selectChildGroups(int childPos) {
		if (mGroupsTitles.size() == 1 && childPos == 0)
			initGroupsPage();
		else if (childPos == 3)
			initGroupsPage();
		else
			initGroupPage(childPos);
	}

	private void initHome() {
		System.out.println(passCurrFragment);
		if (!(currFragment instanceof BrowserFragment)
				|| !(passCurrFragment instanceof BrowserFragment)) {
			if (currFragment == null) {
				currFragment = mBrowserFragment = new BrowserFragment();
				getSupportFragmentManager().beginTransaction()
						.add(R.id.content_frame, currFragment).commit();
				passmBrowserFragment = mBrowserFragment;
				passCurrFragment = currFragment;
			} else {
				if (passCurrFragment instanceof XHomeEditProfileFragment
						|| passCurrFragment instanceof HomeNotificationFragment
						|| passCurrFragment instanceof HomeMessagesFragment) {
					FragmentManager fragmentManager = getSupportFragmentManager();
					fragmentManager.beginTransaction().remove(passCurrFragment)
							.commit();
				} else {
					FragmentManager fragmentManager = getSupportFragmentManager();
					fragmentManager.beginTransaction().remove(currFragment)
							.commit();
				}
				mBrowserFragment.setVisible();
				currFragment = mBrowserFragment;
				passCurrFragment = currFragment;
			}
		} else {

		}

		setTitle(mPagesTitles[HomePage]);
	}

	private void initWall() {
		HomeWallFragment temp = new HomeWallFragment();
		putOnTop(temp);
	}

	private void initMessages() {
		// MessagesFragment temp = new MessagesFragment();
		HomeMessagesFragment temp = new HomeMessagesFragment();
		temp.setArguments(putPositionInBundle(MessagesPage));
		putOnTop(temp);
	}

	private void initNotification() {
		HomeNotificationFragment temp = new HomeNotificationFragment();
		temp.setArguments(putPositionInBundle(NotificationPage));
		putOnTop(temp);
	}

	private void initFriends() {
		WrapperFragment frag = new WrapperFragment();
		frag.setArguments(putPositionInBundle(Configurations.PlayersSTATE));
		putOnTop(frag);

	}

	private void initGamesPage() {
		WrapperFragment frag = new WrapperFragment();
		frag.setArguments(putPositionInBundle(Configurations.GamesSTATE));
		putOnTop(frag);
	}

	private void initGamePage(int childPos) {
		// ToDo Show SelectedGame in a HeaderFragment
		HeaderFragment temp = new HeaderFragment();
		Bundle args = putPositionInBundle(Configurations.GamesSTATE);
		args.putAll(con.getLinker().getItem(
				"'" + mGamesTitles.get(childPos) + "'", Keys.gamesTable));
		// args.putAll();
		temp.setArguments(args);
		putOnTop(temp);
	}

	private void initGroupsPage() {
		WrapperFragment frag = new WrapperFragment();
		frag.setArguments(putPositionInBundle(Configurations.GroupsSTATE));
		putOnTop(frag);
	}

	private void initGroupPage(int childPos) {
		// ToDo Show SelectedGroup in a HeaderFragment
		HeaderFragment temp = new HeaderFragment();
		temp.setArguments(putPositionInBundle(Configurations.GroupsSTATE));
		putOnTop(temp);
	}

	private Bundle putPositionInBundle(int position) {
		Bundle args = new Bundle();
		args.putInt(Keys.ARG_POSITION, position);
		return args;
	}

	private void putOnTop(Fragment frag) {
		if (!(currFragment instanceof BrowserFragment)) {
			getSupportFragmentManager().beginTransaction().remove(currFragment)
					.add(R.id.content_frame, frag).commit();
		} else {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.content_frame, frag).commit();
		}
		currFragment = frag;
		mBrowserFragment.setInvisible();
	}

	private final int HomePage = 0;
	private final int WallPage = 1;
	private final int MessagesPage = 2;
	private final int NotificationPage = 3;
	// private final int mEventsPage = 3;
	private final int FriendsPage = 4;

	// private final int mHeaderMenu = 0;
	private final int MainMenu = 1;
	private final int GamesMenu = 2;
	private final int GroupsMenu = 3;
	private final int LogOutMenu = 4;

	// private
	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ExpandableListView.OnChildClickListener {

		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			selectGroup(groupPosition, childPosition);
			return false;
		}
	}

}