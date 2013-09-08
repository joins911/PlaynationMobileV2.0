package com.myapps.playnation.Operations;

public class Configurations {

	public final static int appStateOffUser = 2;
	public final static int appStateOnGuest = 1;
	public final static int appStateOnUser = 0;
	public final static boolean isLoginEnabled = true;

	public static int GamesSTATE = 0;
	public static int GroupsSTATE = 1;
	public static int NewsSTATE = 2;
	public static int PlayersSTATE = 3;
	public static int CompaniesSTATE = 4;
	public static int Total;
	private int appState;

	public Configurations(int appState) {
		this.appState = appState;
	}

	public int getApplicationState() {
		return appState;
	}
}