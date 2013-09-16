package com.myapps.playnation.Operations;

import com.myapps.playnation.Classes.Keys;

public class Configurations {

	public final static int appStateOffUser = 2;
	public final static int appStateOnGuest = 1;
	public final static int appStateOnUser = 0;
	public final static boolean isLoginEnabled = true;

	public static int NewsSTATE = 0;
	public static int PlayersSTATE = 2;
	public static int GamesSTATE = 1;
	public static int GroupsSTATE = 3;
	public static int CompaniesSTATE = 4;
	public static int Total;
	
	public static String CurrentPlayerID = Keys.TEMPLAYERID;
	
	private static Configurations configs;
	
	private int appState;
	private int listsIncrement;
	private int backTimer;
	private int initialListCount;
	private boolean internetStatus;
	
	private Configurations() {		
	}
	
	public static Configurations getConfigs()	
	{
		if(configs==null) configs = new Configurations();
		return configs;
	}
	
	public void loadDefault(int appState)
	{
		this.appState = appState;
		listsIncrement = 7;
		backTimer = 2; //Wait x seconds for 2nd back button pressed to finishActivity
		initialListCount=14; //
	}
	
	public boolean getInternetStatus()
	{
		return internetStatus;
	}
	
	public void setInternetStatus(boolean connected)
	{
		internetStatus = connected;
	}
	
	public int getApplicationState() {
		return appState;
	}
	
	public void setBackTimer(int time)
	{
		backTimer = time;
	}
	
	public int getBackTimer()
	{
		return backTimer;
	}
	
	public void setListsIncrement(int increment)
	{
		listsIncrement = increment;
	}

	public int getListsIncrement() {
		return listsIncrement;
	}

	public void setInitialListCount(int count)
	{
		initialListCount = count;
	}
	
	public int getInitialListCount() {		
		return initialListCount;
	}
}