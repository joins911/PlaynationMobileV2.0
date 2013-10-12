package com.myapps.playnation.Operations;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.res.Resources;

import com.myapps.playnation.R;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.main.PlaynationMobile;

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
	public static int HomeEditSTATE = 5;

	public static int Total;
	public static float screenDencity;
	public static int screenDpi;

	public final static SimpleDateFormat dataTemplate = new SimpleDateFormat(
			"MMM dd,yyyy HH:mm", Locale.getDefault());

	public static String CurrentPlayerID = Keys.TEMPLAYERID;

	private static Configurations configs;

	private int appState;
	private int listsIncrement;
	private int backTimer;
	private int initialListCount;
	public static boolean isReachable;

	// public static boolean connectionStatus = isReachable;

	private Configurations() {
	}

	public static Configurations getConfigs() {
		if (configs == null)
			configs = new Configurations();
		return configs;
	}

	public void loadDefault(int appState) {
		this.appState = appState;
		listsIncrement = 7;
		backTimer = 2; // Wait x seconds for 2nd back button pressed to
						// finishActivity
		initialListCount = 14; //
	}

	public boolean isTablet() {
		return PlaynationMobile.getContext().getResources()
				.getBoolean(R.bool.isTablet);
	}

	public int getApplicationState() {
		return appState;
	}

	public void setBackTimer(int time) {
		backTimer = time;
	}

	public int getBackTimer() {
		return backTimer;
	}

	public void setListsIncrement(int increment) {
		listsIncrement = increment;
	}

	public int getListsIncrement() {
		return listsIncrement;
	}

	public void setInitialListCount(int count) {
		initialListCount = count;
	}

	public int getInitialListCount() {
		return initialListCount;
	}
}