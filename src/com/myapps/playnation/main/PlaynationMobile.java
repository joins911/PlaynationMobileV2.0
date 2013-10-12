package com.myapps.playnation.main;

import com.myapps.playnation.Operations.DataConnector;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

public class PlaynationMobile extends Application{
	private static PlaynationMobile instance;
	
	public PlaynationMobile()
	{
		instance = this;
		DataConnector.getInst().setSQLLinker(instance);
	}
	public static Context getContext()
	{
		return instance;
	}
	public static Resources getPlaynationResources()
	{
		return instance.getResources();
	}

}
