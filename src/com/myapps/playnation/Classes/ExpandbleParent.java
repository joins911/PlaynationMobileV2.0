package com.myapps.playnation.Classes;

import java.util.ArrayList;

import android.os.Bundle;

import com.myapps.playnation.Operations.DataConnector;

public class ExpandbleParent {
	private ArrayList<Bundle> mItemReplies;
	private Bundle mFirstItem;

	public ExpandbleParent(Bundle args, String tableName) {
		mFirstItem = args;
		if (tableName.equals(Keys.HomeMsgRepliesTable)) {
			mItemReplies = DataConnector.getInst().getTable(tableName,
					args.getString(Keys.MessageID_CONVERSATION));
		} else {
			mItemReplies = DataConnector.getInst().getTable(tableName,
					args.getString(Keys.ID_WALLITEM));
		}
	}

	public ArrayList<Bundle> getArrayChildren() {
		return mItemReplies;
	}

	public Bundle getFirstChild() {
		return mFirstItem;
	}

}