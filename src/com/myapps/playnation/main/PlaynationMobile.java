package com.myapps.playnation.main;

import android.app.Application;
import android.content.Context;

public class PlaynationMobile extends Application{
	private static PlaynationMobile instance;
	
	public PlaynationMobile()
	{
		instance = this;
	}
	public static Context getContext()
	{
		return instance;
	}

}
