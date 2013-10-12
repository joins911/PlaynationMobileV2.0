package com.myapps.playnation.Workers;

import java.util.ArrayList;

import android.os.AsyncTask;

import com.myapps.playnation.Classes.UserComment;
import com.myapps.playnation.Fragments.IWallFragment;
import com.myapps.playnation.Operations.DataConnector;
import com.myapps.playnation.main.ISectionAdapter;

public class LoadCommentsTask extends PlaynationTask {

	private ArrayList<UserComment> comments;
	private ISectionAdapter mActConn;
	private String playerId;
	private String wallType;
	private IWallFragment mFrag;

	public LoadCommentsTask(IWallFragment mFrag, String playerID, String section) {
		super(mFrag.getContext());
		this.playerId = playerID;
		wallType = section;
		this.mFrag = mFrag;
	}

	@Override
	protected Void doInBackground(Void... params) {
		comments = DataConnector.getInst().getComments(playerId, wallType);
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		mFrag.initComments(comments);

	}
}
