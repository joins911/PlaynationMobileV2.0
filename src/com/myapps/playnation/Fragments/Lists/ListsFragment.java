package com.myapps.playnation.Fragments.Lists;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.myapps.playnation.R;
import com.myapps.playnation.Adapters.CompanyListAdapter;
import com.myapps.playnation.Adapters.FriendsListAdapter;
import com.myapps.playnation.Adapters.GamesListAdapter;
import com.myapps.playnation.Adapters.GroupsListAdapter;
import com.myapps.playnation.Adapters.MyBaseAdapter;
import com.myapps.playnation.Adapters.NewsListAdapter;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Classes.NewsFeed;
import com.myapps.playnation.Fragments.BaseFragment;
import com.myapps.playnation.Operations.Configurations;
import com.myapps.playnation.Operations.DataConnector;
import com.myapps.playnation.Operations.HelperClass;
import com.myapps.playnation.main.ISectionAdapter;

public class ListsFragment extends Fragment implements BaseFragment {

	private DataConnector con;
	private View rootView;
	private int mViewPagerState;
	private ISectionAdapter mCallback;
	private ViewFlipper flipper = null;
	private ListView mList;
	private ArrayList<Bundle> mListBundle;
	TextView friendsString;
	private AsyncTask mListTask;
	public Configurations configs;

	public ListsFragment() {
		con = DataConnector.getInst();
		// setRetainInstance(true);
	}

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
		configs = Configurations.getConfigs();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.component_mainlist, container,
				false);
		mViewPagerState = this.getArguments().getInt(Keys.ARG_POSITION);
		ListView list = (ListView) rootView.findViewById(R.id.mainList);
		mList = list;

		friendsString = (TextView) rootView.findViewById(R.id.noFriendsText);
		if (friendsString != null)
			friendsString.setVisibility(View.GONE);

		mList.setOnScrollListener(new OnScrollListener() {

			// private int currentFirstVisibleItem;
			private int visibleItemCount;
			private int currentScrollState;
			private int totalItemCount;
			private int firstVisible;

			// private boolean isLoading;

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// currentFirstVisibleItem = firstVisibleItem;
				this.visibleItemCount = visibleItemCount;
				firstVisible = firstVisibleItem;
				this.totalItemCount = totalItemCount;

			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				this.currentScrollState = scrollState;
				this.isScrollCompleted();

			}

			private void isScrollCompleted() {
				if (firstVisible + visibleItemCount == totalItemCount
						&& totalItemCount != 0
						&& this.currentScrollState == SCROLL_STATE_IDLE) {
					/***
					 * In this way I detect if there's been a scroll which has
					 * completed
					 ***/
					/*** do the work for load more date! ***/
					if (((MyBaseAdapter) mList.getAdapter()).canShowMore()) {
						((MyBaseAdapter) mList.getAdapter()).showMore();
						((BaseAdapter) mList.getAdapter())
								.notifyDataSetChanged();

					}
				}
			}

		});
		if (HelperClass.isTablet(getActivity())) {
			flipper = (ViewFlipper) rootView.findViewById(R.id.viewFlipper1);
		}

		mListTask = new LoadListTask().execute();

		return rootView;
	}

	@Override
	public void onDestroy() {
		mListTask.cancel(true);
		super.onDestroy();
	}

	@Override
	public void onStop() {
		mListTask.cancel(true);
		super.onStop();
	}

	private void initializeGames(final ArrayList<Bundle> results) {
		mListBundle = results;
		if (mListBundle != null) {
			GamesListAdapter bindingData = new GamesListAdapter(getActivity(),
					mListBundle);
			mList.setAdapter(bindingData);
		}
		mList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				tabletOrPhoneControll(configs.GamesSTATE, results.get(position));
			}
		});
	}

	private void initializeGroups(final ArrayList<Bundle> results) {
		mListBundle = results;
		if (mListBundle != null) {
			GroupsListAdapter bindingData = new GroupsListAdapter(
					getActivity(), mListBundle);
			mList.setAdapter(bindingData);
		}
		mList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				tabletOrPhoneControll(configs.GroupsSTATE,
						mListBundle.get(position));
			}
		});
	}

	private void initializeNews(final ArrayList<Bundle> results) {
		mListBundle = results;
		if (mListBundle != null) {
			NewsListAdapter bindingData = new NewsListAdapter(getActivity(),
					HelperClass.createHeaderListView(HelperClass
							.queryNewsList(mListBundle)));
			mList.setAdapter(bindingData);
		}
		mList.setOnItemClickListener(new OnItemClickListener() {

			@SuppressLint("SimpleDateFormat")
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (parent.getItemAtPosition(position) instanceof NewsFeed) {
					NewsFeed feed = (NewsFeed) parent
							.getItemAtPosition(position);
					Bundle edit = new Bundle();
					SimpleDateFormat format = Configurations.dataTemplate;
					edit.putInt(Keys.NEWSCOLID_NEWS, feed.getKey_NewsFeedID());
					edit.putString(Keys.NEWSCOLIMAGE, feed.getKey_NewsImage());
					edit.putString(Keys.NEWSCOLHEADLINE,
							feed.getKey_NewsTitle());
					edit.putString(Keys.NEWSCOLINTROTEXT,
							feed.getKey_NewsIntroText());
					edit.putString(Keys.NEWSCOLNEWSTEXT, feed.getKey_NewsText());
					edit.putString(Keys.Author, feed.getKey_Author());
					edit.putString(Keys.NEWSCOLPOSTINGTIME,
							format.format(feed.getKey_NewsDate().getTime()));

					tabletOrPhoneControll(configs.NewsSTATE, edit);
					view.setSelected(true);
				}
			}
		});
	}

	private void initializePlayers(final ArrayList<Bundle> results) {
		mListBundle = results;
		if (mListBundle != null) {
			FriendsListAdapter bindingData = new FriendsListAdapter(
					getActivity(), mListBundle);
			mList.setAdapter(bindingData);

			mList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					tabletOrPhoneControll(configs.PlayersSTATE,
							mListBundle.get(position));
				}
			});
		} else {
			friendsString.setVisibility(View.VISIBLE);
			if (configs.getApplicationState() == Configurations.appStateOnGuest)
				friendsString.setText(getResources().getString(
						R.string.guestFriendListString));
		}
	}

	public ListView getList() {
		return mList;
	}

	public void setListBundle(ArrayList<Bundle> bund) {
		mListBundle.clear();
		mListBundle.addAll(bund);
		((BaseAdapter) mList.getAdapter()).notifyDataSetChanged();
	}

	public ArrayList<Bundle> getListBundle() {
		return mListBundle;
	}

	private void initializeCompanies(final ArrayList<Bundle> results) {
		mListBundle = results;
		if (mListBundle != null) {
			CompanyListAdapter bindingData = new CompanyListAdapter(
					getActivity(), mListBundle);
			mList.setAdapter(bindingData);
		}
		mList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				/*
				 * con.writeTempNewsTab(
				 * results.get(position).getString(Keys.EventID_COMPANY),
				 * Keys.companysubNewsTAB);
				 */

				tabletOrPhoneControll(configs.CompaniesSTATE,
						mListBundle.get(position));
			}
		});
	}

	private void tabletOrPhoneControll(int state, Bundle edit) {
		if (flipper != null) {
			flipper.setDisplayedChild(2);
			flipper.showNext();
			mCallback.setPage(state, edit);
		} else {
			mCallback.setPage(state, edit);
		}
	}

	public void finishTask() {
		mCallback.finishTask(mViewPagerState);
	}

	public void startTask() {
		mCallback.startTask(mViewPagerState);
	}

	class LoadListTask extends AsyncTask<Void, Void, Void> {

		String tableName;
		int appState;
		Intent mInt;
		Bundle mBundle;
		ArrayList<Bundle> mResults;

		private LoadListTask() {
			con = DataConnector.getInst();
		}

		@Override
		protected void onPreExecute() {
			startTask();
		}

		@Override
		protected Void doInBackground(Void... params) {
			if (configs.GamesSTATE == mViewPagerState) {
				mResults = con.getTable(Keys.gamesTable, "");
			} else if (configs.GroupsSTATE == mViewPagerState) {
				mResults = con.getTable(Keys.groupsTable, "");
			} else if (configs.NewsSTATE == mViewPagerState) {
				mResults = con.getTable(Keys.newsTable, "");
			} else if (configs.PlayersSTATE == mViewPagerState) {
				mResults = con.queryPlayerFriendsSearch("");
			} else if (configs.CompaniesSTATE == mViewPagerState) {
				mResults = con.getTable(Keys.companyTable, "");
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (configs.GamesSTATE == mViewPagerState) {
				initializeGames(mResults);
			} else if (configs.GroupsSTATE == mViewPagerState) {
				initializeGroups(mResults);
			} else if (configs.NewsSTATE == mViewPagerState) {
				initializeNews(mResults);
			} else if (configs.PlayersSTATE == mViewPagerState) {
				initializePlayers(mResults);
			} else if (configs.CompaniesSTATE == mViewPagerState) {
				initializeCompanies(mResults);
			}
			finishTask();
		}
	}

	public BaseFragment getThis() {
		return this;
	}

	public void stopList() {
		// TODO Auto-generated method stub

	}

	@Override
	public void searchFunction(String args) {
		// TODO Auto-generated method stub

	}
}