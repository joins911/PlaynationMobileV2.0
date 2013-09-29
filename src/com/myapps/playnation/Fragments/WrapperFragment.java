package com.myapps.playnation.Fragments;

import java.io.FileDescriptor;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myapps.playnation.R;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Fragments.TabHosts.News.SelectedNewsFeed;
import com.myapps.playnation.Operations.Configurations;
import com.myapps.playnation.Operations.HelperClass;
import com.myapps.playnation.main.MainActivity;

public class WrapperFragment extends Fragment implements BaseFragment{
	private Fragment mFragments;
	private Fragment mHeaderFragment;
	private boolean canBack = false;
	private int mViewPagerState;
	private int clickedIndex = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createListsFragment();
		
		setRetainInstance(true);
		mFragments.setRetainInstance(true);
		getChildFragmentManager().beginTransaction()
				.replace(R.id.container_wrapper, mFragments).commit();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// There has to be a view with id `container` inside `wrapper.xml`

		return inflater.inflate(R.layout.wrapper, container, false);
	}

	public boolean canBack() {
		return canBack;
	}
	
	public void createListsFragment()
	{
		mViewPagerState = getArguments().getInt(Keys.ARG_POSITION);
		Bundle args = new Bundle();
		args.putInt(Keys.ARG_POSITION, mViewPagerState);
		args.putInt(Keys.clickedIndex, clickedIndex);
		mFragments = new ListsFragment();
		((ListsFragment) mFragments).setParent(this);
		mFragments.setArguments(args);
	}
	
	public void switchToHeader(Bundle args,int index)
	{
		clickedIndex = index;
		switchToHeader(args);
	}

	public void switchToHeader(Bundle args) {
		if (mViewPagerState == Configurations.NewsSTATE) {
			mHeaderFragment = new SelectedNewsFeed();
			mHeaderFragment.setArguments(args);
			canBack = true;
		} else {
			args.putAll(getArguments());
			mHeaderFragment = new HeaderFragment();
			mHeaderFragment.setArguments(args);
			canBack = true;
		}
		if (Configurations.getConfigs().isTablet()) {
			getChildFragmentManager().beginTransaction()
					.replace(R.id.fragment1, mHeaderFragment)
					.addToBackStack(null).commit();
		} else {
			getChildFragmentManager().beginTransaction()
					.replace(R.id.container_wrapper, mHeaderFragment)
					.addToBackStack(null).commit();

		}

	}

	public ListsFragment getListFragment() {
		if (mFragments instanceof ListsFragment)
			return (ListsFragment) mFragments;
		return null;
	}

	public void switchBack() {
		if (mFragments == null) {
			createListsFragment();
		}
		canBack = false;
		getChildFragmentManager().beginTransaction()
				.replace(R.id.container_wrapper, mFragments).commit();

	}

	/*
	 * public void stopLists() { if(mFragments instanceof ListsFragment)
	 * ((ListsFragment)mFragments).stopList(); }
	 */
	public void switchToTab(int tabIndex, Bundle args) {
		if (mFragments instanceof ListsFragment)
			if (mViewPagerState == Configurations.NewsSTATE) {
				this.switchToHeader(args);
			} else {
				this.switchToHeader(args);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				((HeaderFragment) mHeaderFragment).switchToTab(tabIndex);
			}
		else {
			((HeaderFragment) mHeaderFragment).switchToTab(tabIndex);
		}

	}

	@Override
	public void searchFunction(String args) {		
	}

	@Override
	public boolean onBackButtonPressed() {
		if(canBack) switchBack();
		return canBack;
	}


}
