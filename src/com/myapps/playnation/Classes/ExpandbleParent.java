package com.myapps.playnation.Classes;

import java.util.ArrayList;

import com.myapps.playnation.Operations.DataConnector;

import android.os.Bundle;

public class ExpandbleParent {
	private ArrayList<Bundle> mItemReplies;
	private Bundle mFirstItem;
	
	public ExpandbleParent(Bundle args,String tableName)
	{
		mFirstItem = args;
		if(tableName!="")
		mItemReplies = DataConnector.getInst()
				.getTable(tableName, args.getString(Keys.ID_WALLITEM));
	}

	public ArrayList<Bundle> getArrayChildren() {
		return mItemReplies;
	}

	public Bundle getFirstChild() {
		return mFirstItem;
	}

}