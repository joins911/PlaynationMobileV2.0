package com.myapps.playnation.Fragments;

import java.util.ArrayList;

import android.content.Context;

import com.myapps.playnation.Classes.UserComment;

public interface IWallFragment {

	public void initComments(ArrayList<UserComment> comments);

	public Context getContext();
}
