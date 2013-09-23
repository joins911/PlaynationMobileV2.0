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
import com.myapps.playnation.Fragments.Tabs.Home.HomeWallFragment;
import com.myapps.playnation.Fragments.Tabs.Home.XHomeMessagesFragment;
import com.myapps.playnation.Fragments.Tabs.Home.XHomeNotificationFragment;
import com.myapps.playnation.Operations.BackTimer;
import com.myapps.playnation.Operations.Configurations;
import com.myapps.playnation.Operations.DataConnector;

public class MainActivity extends ActionBarActivity implements ISectionAdapter {
	private DrawerLayout mDrawerLayout;
	private ExpandableListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mPagesTitles;
	private ArrayList<String> mGamesTitles = new ArrayList<String>();
	private ArrayList<String> mGroupsTitles = new ArrayList<String>();

	DataConnector con;
	private int total;
	private boolean finished = false;
	private boolean isTablet;
	private Fragment currFragment;
	private BrowserFragment mBrowserFragment;
	private BackTimer mBackTimer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		miniSetup();
		setContentView(R.layout.activity_main);
		mTitle = mDrawerTitle = getTitle();
		mPagesTitles = getResources().getStringArray(R.array.main_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ExpandableListView) findViewById(R.id.left_drawer);
		mBackTimer = new BackTimer();
		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		MainMenuAdapter mMenuAdapter = new MainMenuAdapter(
				getApplicationContext(), buildMenu());
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
		con.queryPlayerGames("12");
		con.queryPlayerGroup("12");
		mGamesTitles = con.getLinker().getMyGames("12");
		mGroupsTitles = con.getLinker().getMyGroups("12");
		Toast.makeText(getApplicationContext(), mGamesTitles.toString(), Toast.LENGTH_SHORT).show();
		String showMore = getApplicationContext().getResources().getString(
				R.string.showMore);
		mGamesTitles.add(showMore);
		mGroupsTitles.add(showMore);

		// String[] gamesArr =
		// {"Crysis","World of Warcraft","League of Legends", "Show More..."};
		// String[] groupsArr = {"Crysis Legion","Playnation","Sons Of Durotar",
		// "Show More..."};
		// DataConnector con = DataConnector.getInst();
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
			if (!((BaseFragment) currFragment).onBackButtonPressed())
				finishActivity();
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
		java.lang.System.gc();
		super.onDestroy();
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
		switch (groupPos) {
		case mMainMenu:
			selectChildHome(childPos);
			break;
		case mGamesMenu:
			selectChildGames(childPos);
			break;
		case mGroupsMenu:
			selectChildGroups(childPos);
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
		if (!(currFragment instanceof BrowserFragment))
			if (currFragment == null) {
				currFragment = mBrowserFragment = new BrowserFragment();
				getSupportFragmentManager().beginTransaction()
						.add(R.id.content_frame, currFragment).commit();
			} else {
				FragmentManager fragmentManager = getSupportFragmentManager();
				fragmentManager.beginTransaction().remove(currFragment)
						.commit();
				mBrowserFragment.setVisible();
				currFragment = mBrowserFragment;
			}
	}

	private void initWall() {
		HomeWallFragment temp = new HomeWallFragment();
		putOnTop(temp);

	}

	private void initMessages() {
		// MessagesFragment temp = new MessagesFragment();
		XHomeMessagesFragment temp = new XHomeMessagesFragment();
		Bundle args = new Bundle();
		args.putInt(Keys.ARG_POSITION, 2);
		temp.setArguments(args);
		putOnTop(temp);
	}

	private void initNotification() {
		XHomeNotificationFragment temp = new XHomeNotificationFragment();
		Bundle args = new Bundle();
		args.putInt(Keys.ARG_POSITION, 3);
		temp.setArguments(args);
		putOnTop(temp);
	}

	private void initGamesPage() {
		WrapperFragment frag = new WrapperFragment();
		Bundle args = new Bundle();
		args.putInt(Keys.ARG_POSITION, Configurations.GamesSTATE);
		frag.setArguments(args);
		putOnTop(frag);
	}

	private void initGamePage(int childPos) {
		// ToDo Show SelectedGame in a HeaderFragment
		HeaderFragment temp = new HeaderFragment();
		Bundle args = new Bundle();
		args.putInt(Keys.ARG_POSITION, Configurations.GamesSTATE);
		args.putAll(con.getLinker().getItem(
				"'" + mGamesTitles.get(childPos) + "'", Keys.gamesTable));
		// args.putAll();
		temp.setArguments(args);
		putOnTop(temp);
	}

	private void initGroupsPage() {
		WrapperFragment frag = new WrapperFragment();
		Bundle args = new Bundle();
		args.putInt(Keys.ARG_POSITION, Configurations.GroupsSTATE);
		frag.setArguments(args);
		putOnTop(frag);
	}

	private void initGroupPage(int childPos) {
		// ToDo Show SelectedGroup in a HeaderFragment
		HeaderFragment temp = new HeaderFragment();
		Bundle args = new Bundle();
		args.putInt(Keys.ARG_POSITION, Configurations.GroupsSTATE);
		// args.putAll();
		temp.setArguments(args);
		putOnTop(temp);
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
	// private final int mMediaPage =4;
	// private final int mHeaderMenu = 0;
	private final int mMainMenu = 1;
	private final int mGamesMenu = 2;
	private final int mGroupsMenu = 3;

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