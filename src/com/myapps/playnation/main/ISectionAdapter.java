package com.myapps.playnation.main;

import android.os.Bundle;

public interface ISectionAdapter {
	//public SectionAdapter getAdapter();

	public void setPageAndTab(int pageIndex, int tabIndex, Bundle args);
	public void setPage(int pageIndex,Bundle args);
	// public void menuClick();
	public void finishTask(int viewPagerState);

	public void setIndeterminateVisibility(boolean isVisible);

	public void startTask(int viewPagerState);
	
	
}
