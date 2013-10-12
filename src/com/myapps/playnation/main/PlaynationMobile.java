package com.myapps.playnation.main;

import com.myapps.playnation.Operations.DataConnector;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

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
	public void makeLilToast(String message)
	{
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}
}
