package com.myapps.playnation.Operations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.myapps.playnation.Classes.Keys;

public abstract class SQLinker extends SQLiteOpenHelper{
	
	public SQLinker(Context context, String db_name, int db_vers)
	{
		super(context, db_name, null, db_vers);
	}
	
	public boolean checkRowExist(String tableName, String separeteID,
			String anotherID) {
		SQLiteDatabase sql = getReadableDatabase();
		Cursor cursor = sql.rawQuery(HelperClass.sqliteQueryStringsChecker(
				tableName, separeteID, anotherID), new String[] { separeteID });
		boolean move = cursor.moveToFirst();
		if (move) {
			return true;
		} else {
			return false;
		}
	}

	public int getLastIDNews() {
		String selectQuery = "Select * from " + Keys.LASTIDTABLE
				+ " Where COLNAME='lastIDNews';";

		SQLiteDatabase sql = this.getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);
		int returns = 0;
		if (cursor != null && cursor.getCount() != 0) {
			cursor.moveToFirst();
			returns = Integer.parseInt(cursor.getString(cursor
					.getColumnIndex(Keys.POSTLASTID)));
		}
		return returns;
	}

	public int getMinNewsID() {
		String selectQuery = "Select * from " + Keys.LASTIDTABLE
				+ " Where COLNAME='MinNewsID';";

		SQLiteDatabase sql = this.getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);
		int returns = 0;
		if (cursor != null && cursor.getCount() != 0) {
			cursor.moveToFirst();
			returns = Integer.parseInt(cursor.getString(cursor
					.getColumnIndex(Keys.POSTLASTID)));
		}
		return returns;
	}

	public void setMinGameID(int minGameID) {
		SQLiteDatabase sql = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(Keys.POSTLASTID, minGameID);
		sql.updateWithOnConflict(Keys.LASTIDTABLE, value,
				"COLNAME='MinGameID'", null, SQLiteDatabase.CONFLICT_REPLACE);
	}

	public int getMinGameID() {
		String selectQuery = "Select * from " + Keys.LASTIDTABLE
				+ " Where COLNAME='MinGameID';";

		SQLiteDatabase sql = this.getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);
		int returns = 0;
		if (cursor != null && cursor.getCount() != 0) {
			cursor.moveToFirst();
			returns = Integer.parseInt(cursor.getString(cursor
					.getColumnIndex(Keys.POSTLASTID)));
		}

		return returns;
	}

	public void setMinGroupID(int MinGroupID) {
		SQLiteDatabase sql = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(Keys.POSTLASTID, MinGroupID);
		sql.updateWithOnConflict(Keys.LASTIDTABLE, value,
				"COLNAME='MinGroupID'", null, SQLiteDatabase.CONFLICT_REPLACE);
	}

	public int getMinGroupID() {
		String selectQuery = "Select * from " + Keys.LASTIDTABLE
				+ " Where COLNAME='MinGroupID';";

		SQLiteDatabase sql = this.getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);
		int returns = 0;
		if (cursor != null && cursor.getCount() != 0) {
			cursor.moveToFirst();
			returns = Integer.parseInt(cursor.getString(cursor
					.getColumnIndex(Keys.POSTLASTID)));
		}

		return returns;
	}

	public void setMinCompanyID(int MinCompanyID) {
		SQLiteDatabase sql = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(Keys.POSTLASTID, MinCompanyID);
		sql.updateWithOnConflict(Keys.LASTIDTABLE, value,
				"COLNAME='MinGroupID'", null, SQLiteDatabase.CONFLICT_REPLACE);
	}

	public int getMinCompanyID() {
		String selectQuery = "Select * from " + Keys.LASTIDTABLE
				+ " Where COLNAME='MinCompanyID';";

		SQLiteDatabase sql = this.getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);
		int returns = 0;
		if (cursor != null && cursor.getCount() != 0) {
			cursor.moveToFirst();
			returns = Integer.parseInt(cursor.getString(cursor
					.getColumnIndex(Keys.POSTLASTID)));
		}

		return returns;
	}

	public void setMinNewsID(int minNewsID) {
		SQLiteDatabase sql = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(Keys.POSTLASTID, minNewsID);
		sql.updateWithOnConflict(Keys.LASTIDTABLE, value,
				"COLNAME='MinNewsID'", null, SQLiteDatabase.CONFLICT_REPLACE);
	}

	public void setLastIDNews(int lastIDNews) {
		SQLiteDatabase sql = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(Keys.POSTLASTID, lastIDNews);
		sql.updateWithOnConflict(Keys.LASTIDTABLE, value,
				"COLNAME='lastIDNews'", null, SQLiteDatabase.CONFLICT_REPLACE);
	}

	public int getLastIDGames() {
		String selectQuery = "Select * from " + Keys.LASTIDTABLE
				+ " Where COLNAME='lastIDGames';";

		SQLiteDatabase sql = this.getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);
		int returns = 0;
		if (cursor != null && cursor.getCount() != 0) {
			cursor.moveToFirst();
			returns = Integer.parseInt(cursor.getString(cursor
					.getColumnIndex(Keys.POSTLASTID)));
		}
		return returns;
	}

	public void setLastIDGames(int lastIDGames) {
		SQLiteDatabase sql = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(Keys.POSTLASTID, lastIDGames);
		sql.updateWithOnConflict(Keys.LASTIDTABLE, value,
				"COLNAME='lastIDGames'", null, SQLiteDatabase.CONFLICT_REPLACE);
	}

	public int getLastIDCompanies() {
		String selectQuery = "Select * from " + Keys.LASTIDTABLE
				+ " Where COLNAME='lastIDCompanies';";

		SQLiteDatabase sql = this.getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);
		int returns = 0;
		if (cursor != null && cursor.getCount() != 0) {
			cursor.moveToFirst();
			returns = Integer.parseInt(cursor.getString(cursor
					.getColumnIndex(Keys.POSTLASTID)));
		}
		return returns;
	}

	public void setLastIDCompanies(int lastIDCompanies) {
		// LastIDs.lastIDCompanies = ;
		SQLiteDatabase sql = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(Keys.POSTLASTID, lastIDCompanies);

		sql.updateWithOnConflict(Keys.LASTIDTABLE, value,
				"COLNAME='lastIDCompanies'", null,
				SQLiteDatabase.CONFLICT_REPLACE);

	}

	public int getLastIDFriends() {
		String selectQuery = "Select * from " + Keys.LASTIDTABLE
				+ " Where COLNAME='lastIDFriends';";

		SQLiteDatabase sql = this.getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);
		int returns = 0;
		if (cursor != null && cursor.getCount() != 0) {
			cursor.moveToFirst();
			returns = Integer.parseInt(cursor.getString(cursor
					.getColumnIndex(Keys.POSTLASTID)));
		}
		return returns;
	}

	public void setLastIDFriends(int lastIDFriends) {
		SQLiteDatabase sql = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(Keys.POSTLASTID, lastIDFriends);

		sql.updateWithOnConflict(Keys.LASTIDTABLE, value,
				"COLNAME='lastIDFriends'", null,
				SQLiteDatabase.CONFLICT_REPLACE);
	}

	public int getLastIDGroups() {
		String selectQuery = "Select * from " + Keys.LASTIDTABLE
				+ " Where COLNAME='lastIDGroups';";

		SQLiteDatabase sql = this.getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);
		int returns = 0;
		if (cursor != null && cursor.getCount() != 0) {
			cursor.moveToFirst();
			returns = Integer.parseInt(cursor.getString(cursor
					.getColumnIndex(Keys.POSTLASTID)));
		}
		return returns;
	}

	public void setLastIDGroups(int lastIDGroups) {
		SQLiteDatabase sql = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(Keys.POSTLASTID, lastIDGroups);
		sql.updateWithOnConflict(Keys.LASTIDTABLE, value,
				"COLNAME='lastIDGroups'", null, SQLiteDatabase.CONFLICT_REPLACE);

	}

	public int getLastIDHomeMSg() {
		String selectQuery = "Select * from " + Keys.LASTIDTABLE
				+ " Where COLNAME='lastIDHomeMSg';";
		SQLiteDatabase sql = this.getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);
		int returns = 0;
		if (cursor != null && cursor.getCount() != 0) {
			cursor.moveToFirst();
			returns = Integer.parseInt(cursor.getString(cursor
					.getColumnIndex(Keys.POSTLASTID)));
		}
		return returns;
	}

	public void setLastIDHomeMSg(int lastIDHomeMSg) {
		SQLiteDatabase sql = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(Keys.POSTLASTID, lastIDHomeMSg);
		sql.updateWithOnConflict(Keys.LASTIDTABLE, value,
				"COLNAME='lastIDHomeMSg'", null,
				SQLiteDatabase.CONFLICT_REPLACE);
	}

	public int getLastIDHomeWall() {
		String selectQuery = "Select * from " + Keys.LASTIDTABLE
				+ " Where COLNAME='lastIDHomeWall';";
		SQLiteDatabase sql = this.getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);
		int returns = 0;
		if (cursor != null && cursor.getCount() != 0) {
			cursor.moveToFirst();
			returns = Integer.parseInt(cursor.getString(cursor
					.getColumnIndex(Keys.POSTLASTID)));
		}
		return returns;
	}

	public void setLastIDHomeWall(int lastIDHomeWall) {
		SQLiteDatabase sql = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(Keys.POSTLASTID, lastIDHomeWall);

		sql.updateWithOnConflict(Keys.LASTIDTABLE, value,
				"COLNAME='lastIDHomeWall'", null,
				SQLiteDatabase.CONFLICT_REPLACE);

	}

	public int getLastIDHomeGroups() {
		String selectQuery = "Select * from " + Keys.LASTIDTABLE
				+ " Where COLNAME='lastIDHomeGroups';";

		SQLiteDatabase sql = this.getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);
		int returns = 0;
		if (cursor != null && cursor.getCount() != 0) {
			cursor.moveToFirst();
			returns = Integer.parseInt(cursor.getString(cursor
					.getColumnIndex(Keys.POSTLASTID)));
		}
		return returns;
	}

	public void setLastIDHomeGroups(int lastIDHomeGroups) {
		SQLiteDatabase sql = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(Keys.POSTLASTID, lastIDHomeGroups);

		sql.updateWithOnConflict(Keys.LASTIDTABLE, value,
				"COLNAME='lastIDHomeGroups'", null,
				SQLiteDatabase.CONFLICT_REPLACE);

	}

	public int getLastIDHomeGames() {
		String selectQuery = "Select * from " + Keys.LASTIDTABLE
				+ " Where COLNAME='lastIDHomeGames';";

		SQLiteDatabase sql = this.getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);
		int returns = 0;
		if (cursor != null && cursor.getCount() != 0) {
			cursor.moveToFirst();
			returns = Integer.parseInt(cursor.getString(cursor
					.getColumnIndex(Keys.POSTLASTID)));
		}
		return returns;
	}

	public void setLastIDHomeGames(int lastIDHomeGames) {
		SQLiteDatabase sql = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(Keys.POSTLASTID, lastIDHomeGames);

		sql.updateWithOnConflict(Keys.LASTIDTABLE, value,
				"COLNAME='lastIDHomeGames'", null,
				SQLiteDatabase.CONFLICT_REPLACE);

	}

	public int getLastIDHomeFriends() {
		String selectQuery = "Select * from " + Keys.LASTIDTABLE
				+ " Where COLNAME='lastIDHomeFriends';";

		SQLiteDatabase sql = this.getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);
		int returns = 0;
		if (cursor != null && cursor.getCount() != 0) {
			cursor.moveToFirst();
			returns = Integer.parseInt(cursor.getString(cursor
					.getColumnIndex(Keys.POSTLASTID)));
		}
		return returns;
	}

	public void setLastIDHomeFriends(int lastIDHomeFriends) {
		SQLiteDatabase sql = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(Keys.POSTLASTID, lastIDHomeFriends);

		sql.updateWithOnConflict(Keys.LASTIDTABLE, value,
				"COLNAME='lastIDHomeFriends'", null,
				SQLiteDatabase.CONFLICT_REPLACE);

	}

	public int getLastHomeIDEvents() {
		String selectQuery = "Select * from " + Keys.LASTIDTABLE
				+ " Where COLNAME='lastHomeIDEvents';";

		SQLiteDatabase sql = this.getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);
		int returns = 0;
		if (cursor != null && cursor.getCount() != 0) {
			cursor.moveToFirst();
			returns = Integer.parseInt(cursor.getString(cursor
					.getColumnIndex(Keys.POSTLASTID)));
		}
		return returns;
	}

	public void setLastHomeIDEvents(int lastIDEvents) {
		SQLiteDatabase sql = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(Keys.POSTLASTID, lastIDEvents);

		sql.updateWithOnConflict(Keys.LASTIDTABLE, value,
				"COLNAME='lastIDEvents'", null, SQLiteDatabase.CONFLICT_REPLACE);

	}

	public int getLastIDHomeSubs() {
		String selectQuery = "Select * from " + Keys.LASTIDTABLE
				+ " Where COLNAME='lastIDHomeSubs';";

		SQLiteDatabase sql = this.getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);
		int returns = 0;
		if (cursor != null && cursor.getCount() != 0) {
			cursor.moveToFirst();
			returns = Integer.parseInt(cursor.getString(cursor
					.getColumnIndex(Keys.POSTLASTID)));
		}
		return returns;
	}

	public void setLastIDHomeSubs(int lastIDHomeSubs) {
		SQLiteDatabase sql = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(Keys.POSTLASTID, lastIDHomeSubs);

		sql.updateWithOnConflict(Keys.LASTIDTABLE, value,
				"COLNAME='lastIDHomeSubs'", null,
				SQLiteDatabase.CONFLICT_REPLACE);

	}

	public int getLastIDHomeMSgRep() {
		String selectQuery = "Select * from " + Keys.LASTIDTABLE
				+ " Where COLNAME='lastIDHomeMSgRep';";

		SQLiteDatabase sql = this.getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);
		int returns = 0;
		if (cursor != null && cursor.getCount() != 0) {
			cursor.moveToFirst();
			returns = Integer.parseInt(cursor.getString(cursor
					.getColumnIndex(Keys.POSTLASTID)));
		}
		return returns;
	}

	public void setLastIDHomeMSgRep(int lastIDHomeMSgRep) {
		SQLiteDatabase sql = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(Keys.POSTLASTID, lastIDHomeMSgRep);

		sql.updateWithOnConflict(Keys.LASTIDTABLE, value,
				"COLNAME='lastIDHomeMSgRep'", null,
				SQLiteDatabase.CONFLICT_REPLACE);

	}

	public int getLastIDHomeWallRep() {
		String selectQuery = "Select * from " + Keys.LASTIDTABLE
				+ " Where COLNAME='lastIDHomeWallRep';";

		SQLiteDatabase sql = this.getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);
		int returns = 0;
		if (cursor != null && cursor.getCount() != 0) {
			cursor.moveToFirst();
			returns = Integer.parseInt(cursor.getString(cursor
					.getColumnIndex(Keys.POSTLASTID)));
		}
		return returns;
	}

	public void setLastIDHomeWallRep(int lastIDHomeWallRep) {
		SQLiteDatabase sql = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(Keys.POSTLASTID, lastIDHomeWallRep);

		sql.updateWithOnConflict(Keys.LASTIDTABLE, value,
				"COLNAME='lastIDHomeWallRep'", null,
				SQLiteDatabase.CONFLICT_REPLACE);

	}

}
