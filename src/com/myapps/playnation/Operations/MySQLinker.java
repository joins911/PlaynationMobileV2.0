package com.myapps.playnation.Operations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.myapps.playnation.Classes.Keys;

public class MySQLinker extends SQLinker {

	private static String DATABASE_NAME = "playnation.db";
	private static int DATABASE_VERSION = 1;

	public MySQLinker(Context con) {
		super(con, DATABASE_NAME, DATABASE_VERSION);
	}

	public Bundle getItem(String itemName, String tableName) {
		Bundle bundle = new Bundle();
		String selectQuery = "SELECT * FROM " + tableName + " WHERE GameName="
				+ itemName + ";";

		SQLiteDatabase sql = this.getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);

		if (cursor != null) {
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				do {
					if (tableName.equalsIgnoreCase(Keys.gamesTable)) {
						bundle = BundleBuilder.putGameInBundle(cursor);
					}
					if (tableName.equalsIgnoreCase(Keys.groupsTable)) {
						bundle = BundleBuilder.putGroupInBundle(cursor);
					}
				} while (cursor.moveToNext());
			}
			cursor.close();
		}

		return bundle;
	}

	public ArrayList<Bundle> getSQLiteGame(String tableName) {
		ArrayList<Bundle> list = new ArrayList<Bundle>();
		Keys.tempLimitGame += 10;
		String selectQuery = HelperClass.sqliteQueryStrings(tableName, "", "",
				Keys.tempLimitGame + "");
		SQLiteDatabase sql = this.getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);

		if (cursor != null) {
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				do {
					list.add(BundleBuilder.putGameInBundle(cursor));
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		return list;
	}

	public ArrayList<Bundle> getSQLiteGroups(String tableName) {
		ArrayList<Bundle> list = new ArrayList<Bundle>();
		String selectQuery = HelperClass.sqliteQueryStrings(tableName, "", "",
				0 + "");
		SQLiteDatabase sql = this.getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);

		if (cursor != null) {
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				do {

					list.add(BundleBuilder.putGroupInBundle(cursor));
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		return list;
	}

	public void writeTempNewsTab(String id_game, String gameType, JSONArray json) {
		SQLiteDatabase sql = this.getWritableDatabase();

		if (json != null) {
			for (int i = 0; i < json.length(); i++) {
				try {
					String id = json.getJSONObject(i).getString(
							Keys.NEWSCOLID_NEWS);
					if (gameType.toLowerCase().equals("game")) {
						if (!checkRowExist(Keys.newsTempTable, id, id_game)) {
							ContentValues temp = ContentVBuilder
									.putTempNewsInContentV(
											json.getJSONObject(i), id, id_game);
							sql.insertWithOnConflict(Keys.newsTempTable, null,
									temp, SQLiteDatabase.CONFLICT_REPLACE);

						}
					} else {
						if (!checkRowExist(Keys.companyTempTable, id, id_game)) {
							ContentValues temp = ContentVBuilder
									.putTempNewsInContentV(
											json.getJSONObject(i), id, id_game);
							sql.insertWithOnConflict(Keys.companyTempTable,
									null, temp, SQLiteDatabase.CONFLICT_REPLACE);
						}
					}
				} catch (Exception e) {
					Log.e("Fetching writeTempNewsTab", "Error writeTempNewsTab"
							+ e);
				}
			}
		}
	}

	public void addGames(JSONArray jsonArray) throws JSONException {
		SQLiteDatabase sql = this.getWritableDatabase();
		Set<String> gameTypes = new HashSet<String>();
		for (int i = 0; i < jsonArray.length(); i++) {
			String id_GAME = jsonArray.getJSONObject(i).getString(Keys.ID_GAME);
			if (checkRowExist(Keys.gamesTable, id_GAME, "") == false) {
				ContentValues temp = ContentVBuilder
						.putGameInContentV(jsonArray.getJSONObject(i));
				int id = Integer.parseInt(id_GAME);
				if (id > getLastIDGames())
					setLastIDGames(id);
				temp.put(Keys.ID_GAME, id_GAME);
				sql.insertWithOnConflict(Keys.gamesTable, null, temp,
						SQLiteDatabase.CONFLICT_REPLACE);
			}
		}

	}

	public void addGroups(JSONArray jsonArray) throws JSONException {
		SQLiteDatabase sql = this.getWritableDatabase();
		Set<String> groupTypes = new HashSet<String>();
		for (int i = 0; i < jsonArray.length(); i++) {
			String ID_Groups = jsonArray.getJSONObject(i).getString(
					Keys.ID_GROUP);
			if (!checkRowExist(Keys.groupsTable, ID_Groups, "")) {
				ContentValues temp = ContentVBuilder.putGroupInContentV(
						jsonArray.getJSONObject(i), ID_Groups);
				int id = Integer.parseInt(ID_Groups);
				if (id > getLastIDGroups())
					setLastIDGroups(id);
				sql.insertWithOnConflict(Keys.groupsTable, null, temp,
						SQLiteDatabase.CONFLICT_REPLACE);
			}
		}
	}

	public void addNews(JSONArray json) throws JSONException {
		SQLiteDatabase sql = this.getWritableDatabase();

		for (int i = 0; i < json.length(); i++) {
			String ID_News = json.getJSONObject(i).getString(
					Keys.NEWSCOLID_NEWS);
			if (!checkRowExist(Keys.newsTable, ID_News, "")) {
				ContentValues temp = ContentVBuilder.putTempNewsInContentV(
						json.getJSONObject(i), "", ID_News);

				sql.insertWithOnConflict(Keys.newsTable, null, temp,
						SQLiteDatabase.CONFLICT_REPLACE);
			}
		}
	}

	public void addCompany(JSONArray jsonArray) throws JSONException {
		SQLiteDatabase sql = this.getWritableDatabase();
		// // Print the data to the console
		if (jsonArray != null)
			for (int i = 0; i < jsonArray.length(); i++) {
				try {
					String ID = jsonArray.getJSONObject(i).getInt(
							Keys.EventID_COMPANY)
							+ "";
					if (!checkRowExist(Keys.companyTable, ID, "")) {
						ContentValues map = ContentVBuilder
								.putCompanyInContentV(jsonArray
										.getJSONObject(i));

						map.put(Keys.EventID_COMPANY, ID);
						int id = Integer.parseInt(ID);
						if (id > getLastIDCompanies())
							setLastIDCompanies(id);
						sql.insertWithOnConflict(Keys.companyTable, null, map,
								SQLiteDatabase.CONFLICT_REPLACE);
					}
				} catch (Exception e) {
					Log.e("Fetching Company", "Error Company" + e);
				}
			}
	}

	public ArrayList<Bundle> getSQLiteNews(String tableName) {
		ArrayList<Bundle> list = new ArrayList<Bundle>();
		String selectQuery = HelperClass.sqliteQueryStrings(tableName, "", "",
				"0");
		SQLiteDatabase sql = this.getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);
		if (cursor != null) {
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				do {
					list.add(BundleBuilder.putTempNewsInBundle(cursor));
				} while (cursor.moveToNext());
			}
			cursor.close();
		}

		return list;
	}

	public ArrayList<Bundle> getTempNewsTab(String id, String gameType) {
		SQLiteDatabase sql = null;
		Cursor cursor = null;
		String selectQuery = "";
		if (gameType.equals("game")) {
			selectQuery = "Select * from " + Keys.newsTempTable
					+ " Where ID_GAME=" + id;
		} else {
			selectQuery = "Select * from " + Keys.companyTempTable
					+ " Where ID_GAME=" + id + "";
		}

		sql = getReadableDatabase();
		cursor = sql.rawQuery(selectQuery, null);
		if (cursor != null) {
			ArrayList<Bundle> arrayList = new ArrayList<Bundle>();
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				do {
					Bundle bundle = BundleBuilder.putTempNewsInBundle(cursor);
					bundle.putString(Keys.ID_OWNER, cursor.getString(cursor
							.getColumnIndex(Keys.ID_OWNER)));
					arrayList.add(bundle);
				} while (cursor.moveToNext());
			}
			cursor.close();
			return arrayList;
		}

		return null;
	}

	public ArrayList<Bundle> getSQLitePWall(String tableName, String id) {
		ArrayList<Bundle> list = new ArrayList<Bundle>();
		String selectQuery = HelperClass.sqliteQueryStrings(tableName, id, "",
				0 + "");
		SQLiteDatabase sql = getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);
		if (cursor != null) {
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				do {
					list.add(BundleBuilder.putPWallInBundle(cursor));
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		return list;
	}

	public ArrayList<Bundle> getSQLitePWallReplies(String tableName,
			String sepateID) {
		ArrayList<Bundle> list = new ArrayList<Bundle>();
		String selectQuery = HelperClass.sqliteQueryStrings(tableName,
				sepateID, "", 0 + "");
		SQLiteDatabase sql = getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);
		if (cursor != null) {
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				do {
					list.add(BundleBuilder.putPWallInBundle(cursor));
				} while (cursor.moveToNext());

			}
			cursor.close();
		}
		return list;
	}

	public ArrayList<Bundle> getSQLitePMSG(String tableName, String playerID) {
		ArrayList<Bundle> list = new ArrayList<Bundle>();
		String selectQuery = HelperClass.sqliteQueryStrings(tableName,
				playerID, "", 0 + "");
		SQLiteDatabase sql = getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);
		if (cursor != null) {
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				do {
					list.add(BundleBuilder.putPMsgInBundle(cursor));
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		return list;
	}

	public ArrayList<Bundle> getSQLitePMsgReplies(String tableName,
			String sepateID) {
		ArrayList<Bundle> list = new ArrayList<Bundle>();
		String selectQuery = HelperClass.sqliteQueryStrings(tableName,
				sepateID, "", 0 + "");
		SQLiteDatabase sql = getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);
		if (cursor != null) {
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				do {
					list.add(BundleBuilder.putPMsgInBundle(cursor));
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		return list;
	}

	public ArrayList<String> getMyGames(String playerID) {
		ArrayList<String> myGames = new ArrayList<String>();
		int i = 0;
		String selectQuery = HelperClass.sqliteQueryStrings(
				Keys.HomeGamesTable, "", "", 0 + "");
		SQLiteDatabase sql = getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, new String[] { playerID });
		if (cursor != null) {
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				do {
					myGames.add(cursor.getString(cursor
							.getColumnIndex(Keys.GAMENAME)));
					i++;
				} while (cursor.moveToNext() && i < 3);
			}
			cursor.close();
		}
		return myGames;
	}

	public ArrayList<String> getMyGroups(String playerID) {
		ArrayList<String> myGroups = new ArrayList<String>();
		int i = 0;
		String selectQuery = HelperClass.sqliteQueryStrings(
				Keys.HomeGroupTable, "", "", 0 + "");
		SQLiteDatabase sql = getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, new String[] { playerID });
		if (cursor != null) {
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				do {
					myGroups.add(cursor.getString(cursor
							.getColumnIndex(Keys.GROUPNAME)));
					i++;
				} while (cursor.moveToNext() && i < 3);
			}
			cursor.close();
		}
		return myGroups;
	}

	public ArrayList<Bundle> getSQLiteWhoIsPlaying(String tableName,
			String iD_Game) {
		ArrayList<Bundle> list = new ArrayList<Bundle>();
		String selectQuery = HelperClass.sqliteQueryStrings(tableName, iD_Game,
				"", 0 + "");
		SQLiteDatabase sql = getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);
		if (cursor != null) {
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				do {
					Bundle bundle = BundleBuilder.putPlayerInBundle(cursor);
					bundle.putString(Keys.ID_GAME, cursor.getString(cursor
							.getColumnIndex(Keys.ID_GAME)));
					list.add(bundle);
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		return list;
	}

	public Bundle getPlayer(String playerID) {
		String selectQuery = HelperClass.sqliteQueryStrings(Keys.PlayerTable,
				playerID, "", 0 + "");
		SQLiteDatabase sql = getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);
		Bundle returnBundle = null;
		if (cursor != null) {
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				do {
					returnBundle = BundleBuilder.putPlayerInBundle(cursor);
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		return returnBundle;
	}

	public ArrayList<Bundle> getSQLitePGames(String tableName, String sep) {
		ArrayList<Bundle> list = new ArrayList<Bundle>();
		String selectQuery = HelperClass.sqliteQueryStrings(tableName, sep, "",
				0 + "");
		SQLiteDatabase sql = getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, new String[] { sep });
		if (cursor != null) {
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				do {
					list.add(BundleBuilder.putPGameExtraInBundle(cursor));
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		return list;
	}

	public ArrayList<Bundle> getSQLitePGroup(String tableName, String sepateID) {
		ArrayList<Bundle> list = new ArrayList<Bundle>();
		String selectQuery = HelperClass.sqliteQueryStrings(tableName,
				sepateID, "", 0 + "");
		SQLiteDatabase sql = getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, new String[] { sepateID });
		if (cursor != null) {
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				do {
					Bundle bundle = BundleBuilder.putGroupInBundle(cursor);
					bundle.putString(Keys.ID_GROUP,
							cursor.getInt(cursor.getColumnIndex(Keys.ID_GROUP))
									+ "");
					bundle.putString(
							Keys.ID_PLAYER,
							cursor.getInt(cursor.getColumnIndex(Keys.ID_PLAYER))
									+ "");
					list.add(bundle);
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		return list;
	}

	public ArrayList<Bundle> getSQLiteCompanies(String tableName) {
		ArrayList<Bundle> list = new ArrayList<Bundle>();
		String selectQuery = HelperClass.sqliteQueryStrings(tableName, "", "",
				0 + "");
		SQLiteDatabase sql = getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);
		if (cursor != null) {
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				do {
					list.add(BundleBuilder.putCompanyInBundle(cursor));
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		return list;
	}

	public ArrayList<Bundle> getSQLitePSubscription(String tableName) {
		ArrayList<Bundle> list = new ArrayList<Bundle>();
		String selectQuery = HelperClass.sqliteQueryStrings(tableName, "", "",
				0 + "");
		SQLiteDatabase sql = getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);
		if (cursor != null) {
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				do {
					Bundle bundle = new Bundle();
					bundle.putString(Keys.ID_ITEM,
							cursor.getInt(cursor.getColumnIndex(Keys.ID_ITEM))
									+ "");
					bundle.putString(Keys.ID_OWNER,
							cursor.getInt(cursor.getColumnIndex(Keys.ID_OWNER))
									+ "");
					bundle.putString(Keys.ItemName, cursor.getString(cursor
							.getColumnIndex(Keys.ItemName)));
					bundle.putString(Keys.ItemType, cursor.getString(cursor
							.getColumnIndex(Keys.ItemType)));
					bundle.putString(Keys.SubscriptionTime, cursor
							.getString(cursor
									.getColumnIndex(Keys.SubscriptionTime)));
					list.add(bundle);
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		return list;
	}

	public ArrayList<Bundle> getSQLitePEvent(String tableName) {
		ArrayList<Bundle> list = new ArrayList<Bundle>();
		String selectQuery = HelperClass.sqliteQueryStrings(tableName, "", "",
				0 + "");
		SQLiteDatabase sql = getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);
		if (cursor != null) {
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				do {
					list.add(BundleBuilder.putPEventInBundle(cursor));
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		return list;
	}

	public ArrayList<Bundle> getSQLitePFriends(String tableName, String sepateID) {
		ArrayList<Bundle> list = new ArrayList<Bundle>();
		String selectQuery = HelperClass.sqliteQueryStrings(tableName,
				sepateID, "", 0 + "");
		SQLiteDatabase sql = getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, new String[] { sepateID });
		if (cursor != null) {
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				do {
					Bundle bundle = BundleBuilder.putPlayerInBundle(cursor);
					bundle.putString(Keys.ID_FRIEND, cursor.getString(cursor
							.getColumnIndex(Keys.ID_FRIEND)));
					bundle.putString(Keys.Mutual, cursor.getString(cursor
							.getColumnIndex(Keys.Mutual)));
					list.add(bundle);
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		return list;
	}

	public void insertPEvents(JSONArray json, String playerID) {
		SQLiteDatabase sql = this.getWritableDatabase();
		if (json != null)
			for (int i = 0; i < json.length(); i++) {

				try {
					ContentValues map = ContentVBuilder
							.putPEventsInContentV(json.getJSONObject(i));
					String ID = json.getJSONObject(i).getInt(Keys.ID_EVENT)
							+ "";
					if (!checkRowExist(Keys.HomeEventTable, ID, playerID)) {
						int id = Integer.parseInt(ID);
						if (id > getLastHomeIDEvents())
							setLastHomeIDEvents(id);
						map.put(Keys.ID_EVENT, ID);
						sql.insertWithOnConflict(Keys.HomeEventTable, null,
								map, SQLiteDatabase.CONFLICT_REPLACE);
					}
				} catch (Exception e) {
					Log.e("Fetching Events", "Error Events" + e);
				}
			}
	}

	public void insertPFriends(JSONArray json, String playerID) {
		SQLiteDatabase sql = this.getWritableDatabase();
		if (json != null) {
			for (int i = 0; i < json.length(); i++) {

				try {
					String ID = json.getJSONObject(i).getString(Keys.ID_PLAYER);
					if (!checkRowExist(Keys.HomeFriendsTable, ID, playerID)) {
						ContentValues map = ContentVBuilder
								.putPlayerInContentV(json.getJSONObject(i));
						map.put(Keys.ID_OWNER,
								json.getJSONObject(i).getString(Keys.ID_OWNER));
						map.put(Keys.Mutual,
								json.getJSONObject(i).optString(Keys.Mutual));
						map.put(Keys.ID_FRIEND, json.getJSONObject(i)
								.optString(Keys.ID_FRIEND));
						sql.insertWithOnConflict(Keys.HomeFriendsTable, null,
								map, SQLiteDatabase.CONFLICT_REPLACE);
					}
				} catch (Exception e) {
					Log.e("Fetching Friends", "Error Friends" + e);
				}
			}
		}
	}

	public String insertNewImgURL(JSONArray json, String mainTable, String ids) {
		SQLiteDatabase sql = this.getWritableDatabase();
		String returnUrl = "";
		if (json != null)
			for (int i = 0; i < json.length(); i++) {
				ContentValues map = new ContentValues();
				try {
					returnUrl = json.getJSONObject(i).getString("ImageUrl");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (mainTable.equals(Keys.gamesTable)) {
					map.put(Keys.EventIMAGEURL, returnUrl);
					sql.updateWithOnConflict(mainTable, map, Keys.ID_GAME + "="
							+ ids, null, SQLiteDatabase.CONFLICT_REPLACE);
				} else if (mainTable.equals(Keys.companyTable)) {
					map.put(Keys.CompanyImageURL, returnUrl);
					sql.updateWithOnConflict(mainTable, map,
							Keys.EventID_COMPANY + "=" + ids, null,
							SQLiteDatabase.CONFLICT_REPLACE);
				} else if (mainTable.equals(Keys.groupsTable)) {
					map.put(Keys.EventIMAGEURL, returnUrl);
					sql.updateWithOnConflict(mainTable, map, Keys.ID_GROUP
							+ "=" + ids, null, SQLiteDatabase.CONFLICT_REPLACE);
				} else if (mainTable.equals(Keys.newsTable)) {
					map.put(Keys.NEWSCOLIMAGE, returnUrl);
					sql.updateWithOnConflict(mainTable, map,
							Keys.NEWSCOLID_NEWS + "=" + ids, null,
							SQLiteDatabase.CONFLICT_REPLACE);
				} else if (mainTable.equals(Keys.newsTempTable)) {
					map.put(Keys.NEWSCOLIMAGE, returnUrl);
					sql.updateWithOnConflict(mainTable, map,
							Keys.NEWSCOLID_NEWS + "=" + ids, null,
							SQLiteDatabase.CONFLICT_REPLACE);
				} else if (mainTable.equals(Keys.companyTempTable)) {
					map.put(Keys.NEWSCOLIMAGE, returnUrl);
					sql.updateWithOnConflict(mainTable, map,
							Keys.NEWSCOLID_NEWS + "=" + ids, null,
							SQLiteDatabase.CONFLICT_REPLACE);
				}
			}
		return returnUrl;
	}

	public void insertWhoIsPlaying(JSONArray json, String playerID) {
		SQLiteDatabase sql = this.getWritableDatabase();
		if (json != null) {
			for (int i = 0; i < json.length(); i++) {

				try {
					String ID = json.getJSONObject(i).getString(Keys.ID_PLAYER);
					if (!checkRowExist(Keys.whoIsPlayingTable, playerID, ID)) {
						ContentValues map = ContentVBuilder
								.putPlayerInContentV(json.getJSONObject(i));
						map.put(Keys.ID_GAME,
								json.getJSONObject(i).getString(Keys.ID_GAME));
						sql.insertWithOnConflict(Keys.whoIsPlayingTable, null,
								map, SQLiteDatabase.CONFLICT_REPLACE);
					}
				} catch (Exception e) {
					Log.e("Fetching WhoISPlaying", "Error Friends" + e);
				}
			}
		}
	}

	public void insertPGames(JSONArray json, String playerID) {
		SQLiteDatabase sql = this.getWritableDatabase();
		if (json != null)
			for (int i = 0; i < json.length(); i++) {
				try {
					String ID = json.getJSONObject(i).getInt(Keys.ID_GAME) + "";
					if (!checkRowExist(Keys.HomeGamesTable, ID, playerID)) {
						ContentValues m = ContentVBuilder
								.putExtraGameInContentV(json.getJSONObject(i));
						sql.insertWithOnConflict(Keys.HomeGamesTable, null, m,
								SQLiteDatabase.CONFLICT_REPLACE);
					}
				} catch (Exception e) {
					Log.e("Fetching Games", "Error Games" + e);
				}
			}
	}

	public void insertPGroups(JSONArray json, String playerID) {
		SQLiteDatabase sql = this.getWritableDatabase();
		if (json != null)
			for (int i = 0; i < json.length(); i++) {
				try {
					String ID = json.getJSONObject(i).getInt(Keys.ID_GROUP)
							+ "";
					if (!checkRowExist(Keys.HomeGroupTable, ID, playerID)) {
						ContentValues m = ContentVBuilder.putGroupInContentV(
								json.getJSONObject(i), ID);
						m.put(Keys.ID_PLAYER,
								json.getJSONObject(i).getInt(Keys.ID_PLAYER)
										+ "");
						m.put(Keys.GruopCreatorName, json.getJSONObject(i)
								.getString(Keys.PLAYERNICKNAME));
						sql.insertWithOnConflict(Keys.HomeGroupTable, null, m,
								SQLiteDatabase.CONFLICT_REPLACE);
					}
				} catch (Exception e) {
					Log.e("Fetching Group", "Error Group " + e);
				}
			}
	}

	public void insertPMessages(JSONArray json, String playerID) {
		SQLiteDatabase sql = this.getWritableDatabase();
		if (json != null)
			for (int i = 0; i < json.length(); i++) {
				try {
					String ID = json.getJSONObject(i).getInt(Keys.ID_MESSAGE)
							+ "";
					if (!checkRowExist(Keys.HomeMsgTable, ID, playerID)) {
						ContentValues map = new ContentValues();
						int id = Integer.parseInt(ID);
						if (id > getLastIDHomeMSg())
							setLastIDHomeMSg(id);
						if (map != null)
							sql.insertWithOnConflict(Keys.HomeMsgTable, null,
									map, SQLiteDatabase.CONFLICT_REPLACE);
					}
				} catch (Exception e) {
					Log.e("Fetching Msg", "Error Msg" + e);
				}
			}
	}

	public void insertPMessagesReplies(JSONArray json, String playerID,
			String wallitem) {
		SQLiteDatabase sql = this.getWritableDatabase();
		if (json != null)
			for (int i = 0; i < json.length(); i++) {
				//
				try {
					String ID = json.getJSONObject(i).getInt(Keys.ID_MESSAGE)
							+ "";
					if (!checkRowExist(Keys.HomeMsgRepliesTable, wallitem,
							playerID)) {
						ContentValues map = ContentVBuilder
								.putMessageInContentV(json.getJSONObject(i));
						if (map != null)
							sql.insertWithOnConflict(Keys.HomeMsgRepliesTable,
									null, map, SQLiteDatabase.CONFLICT_REPLACE);
					}
				} catch (Exception e) {
					Log.e("Fetching MSG Replies", "Fetching MSGReplies Error"
							+ e);
				}
			}
	}

	public void insertPSubscription(JSONArray json, String playerID) {
		SQLiteDatabase sql = this.getWritableDatabase();
		if (json != null)
			for (int i = 0; i < json.length(); i++) {

				try {
					String ID = json.getJSONObject(i).getInt(Keys.ID_ITEM) + "";
					if (!checkRowExist(Keys.HomeSubscriptionTable, ID, playerID)) {
						ContentValues m = new ContentValues();
						int id = Integer.parseInt(ID);
						if (id > getLastIDHomeSubs())
							setLastIDHomeSubs(id);
						m.put(Keys.ID_ITEM, ID);
						m.put(Keys.ID_OWNER,
								json.getJSONObject(i).getInt(Keys.ID_OWNER)
										+ "");
						m.put(Keys.ItemName,
								json.getJSONObject(i).getString(Keys.ItemName));
						m.put(Keys.ItemType,
								json.getJSONObject(i).getString(Keys.ItemType));
						m.put(Keys.SubscriptionTime, HelperClass.convertTime(
								Integer.parseInt(json.getJSONObject(i)
										.getString(Keys.SubscriptionTime)),
								Configurations.dataTemplate));

						sql.insertWithOnConflict(Keys.HomeSubscriptionTable,
								null, m, SQLiteDatabase.CONFLICT_REPLACE);
					}
				} catch (Exception e) {
					Log.e("Fetching Subscription", "Error Subscription " + e);
				}
			}
	}

	public void insertPWall(JSONArray json, String playerID) {
		SQLiteDatabase sql = this.getWritableDatabase();
		if (json != null)
			for (int i = 0; i < json.length(); i++) {
				//
				try {
					String ID = json.getJSONObject(i).getInt(Keys.ID_WALLITEM)
							+ "";
					ContentValues map = ContentVBuilder.putPWallInContentV(json
							.getJSONObject(i));
					sql.insertWithOnConflict(Keys.HomeWallTable, null, map,
							SQLiteDatabase.CONFLICT_REPLACE);
				} catch (Exception e) {
					Log.e("DataConnector ", " querryPlayerWall() Error " + e);
				}
			}
	}

	public void insertPWallReplies(JSONArray json, String playerID) {
		SQLiteDatabase sql = this.getWritableDatabase();
		if (json != null) {
			for (int i = 0; i < json.length(); i++) {
				try {
					String ID = json.getJSONObject(i).getInt(Keys.ID_WALLITEM)
							+ "";
					ContentValues map = ContentVBuilder
							.putPWallRepInContentV(json.getJSONObject(i));
					sql.insertWithOnConflict(Keys.HomeWallRepliesTable, null,
							map, SQLiteDatabase.CONFLICT_REPLACE);
				} catch (Exception e) {
					Log.e("Fetching Wall Replies",
							"Fetching WallReplies: Error" + e);
				}
			}
		}
	}

	public void insertPlayerInfo(JSONArray json, String playerID) {
		SQLiteDatabase sql = this.getWritableDatabase();
		if (json != null)
			for (int i = 0; i < json.length(); i++) {
				try {
					String ID = json.getJSONObject(i).getString(Keys.ID_PLAYER);
					ContentValues map = ContentVBuilder
							.putPlayerInContentV(json.getJSONObject(i));
					if (!checkRowExist(Keys.PlayerTable, ID, "")) {
						map.put(Keys.ID_PLAYER, ID);
						sql.insertWithOnConflict(Keys.PlayerTable, null, map,
								SQLiteDatabase.CONFLICT_REPLACE);
					} else {
						sql.updateWithOnConflict(Keys.PlayerTable, map,
								"ID_PLAYER=" + ID, null,
								SQLiteDatabase.CONFLICT_REPLACE);
						DataConnector.getInst().queryNewImageURL(ID, "player",
								Keys.PlayerTable);
					}

				} catch (Exception e) {
					Log.e("Fetching Info", "Error " + e);
				}
			}
	}

	public boolean checkDBTableExits(String tableName) {
		String selectQuery = "SELECT  * FROM " + tableName;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null) {
			if (cursor.getCount() > 0) {
				cursor.close();
				return true;
			}
			cursor.close();
		}
		cursor.close();
		return false;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String cREATE_PWall = "CREATE TABLE " + Keys.HomeWallTable + " ("
				+ Keys.ID_WALLITEM + " INTEGER PRIMARY KEY, "
				+ Keys.WallPosterDisplayName + " TEXT, " + Keys.ID_OWNER
				+ " INTEGER, " + Keys.ItemType + " TEXT, "
				+ Keys.WallLastActivityTime + " TEXT, " + Keys.WallMessage
				+ " TEXT, " + Keys.WallOwnerType + " TEXT, "
				+ Keys.PLAYERAVATAR + " TEXT, " + Keys.WallPostingTime
				+ " TEXT" + ");";
		db.execSQL(cREATE_PWall);

		String cREATE_gamesTable = "CREATE TABLE " + Keys.gamesTable + " ("
				+ Keys.ID_GAME + " INTEGER PRIMARY KEY, " + Keys.GAMENAME
				+ " TEXT, " + Keys.GAMETYPE + " TEXT, " + Keys.GAMEDESC
				+ " TEXT, " + Keys.GAMEDATE + " TEXT, " + Keys.EventIMAGEURL
				+ " TEXT, " + Keys.GameIsLiked + " TEXT, " + Keys.GameisPlaying
				+ " TEXT, " + Keys.RATING + " TEXT, " + Keys.GAMEESRB
				+ " TEXT, " + Keys.GAMEURL + " TEXT, " + Keys.GAMEPLAYERSCOUNT
				+ " TEXT, " + Keys.GAMETYPENAME + " TEXT, " + Keys.GAMEPLATFORM
				+ " TEXT, " + Keys.GAMECompanyDistributor + " TEXT, "
				+ Keys.CompanyFounded + " TEXT, " + Keys.CompanyName + " TEXT"
				+ ");";
		db.execSQL(cREATE_gamesTable);

		String cREATE_HomeGamesTable = "CREATE TABLE " + Keys.HomeGamesTable
				+ " (" + Keys.ID_GAME + " INTEGER PRIMARY KEY," + Keys.RATING
				+ " TEXT," + Keys.GAMEESRB + " TEXT," + Keys.GAMEURL + " TEXT,"
				+ Keys.GAMEPLAYERSCOUNT + " TEXT, " + Keys.GAMEDATE + " TEXT, "
				+ Keys.ID_PLAYER + " INTEGER," + Keys.GameComments + " TEXT, "
				+ Keys.GAMENAME + " TEXT," + Keys.GAMEDESC + " TEXT,"
				+ Keys.GameID_GAMETYPE + " INTEGER," + Keys.GAMETYPE + " TEXT,"
				+ Keys.GAMETYPENAME + " TEXT, " + Keys.EventIMAGEURL + " TEXT,"
				+ Keys.GameisPlaying + " TEXT," + Keys.GameIsLiked
				+ " INTEGER, " + Keys.GamesisSubscribed + " TEXT,"
				+ Keys.GamePostCount + " INTEGER," + Keys.GamesSubscriptionTime
				+ " TEXT," + Keys.GAMEPLATFORM + " TEXT, "
				+ Keys.GAMECompanyDistributor + " TEXT, " + Keys.CompanyFounded
				+ " TEXT, " + Keys.CompanyName + " TEXT" + ");";
		db.execSQL(cREATE_HomeGamesTable);

		String cREATE_groupsTable = "CREATE TABLE " + Keys.groupsTable + " ("
				+ Keys.ID_GROUP + " INTEGER PRIMARY KEY, " + Keys.ID_CREATOR
				+ " INTEGER, " + Keys.GROUPNAME + " TEXT, " + Keys.GROUPTYPE
				+ " TEXT, " + Keys.EventIMAGEURL + " TEXT, " + Keys.GROUPDESC
				+ " TEXT, " + Keys.GROUPTYPE2 + " TEXT, " + Keys.GameIsLiked
				+ " TEXT, " + Keys.isMember + " TEXT, " + Keys.GroupMemberCount
				+ " TEXT, " + Keys.GROUPDATE + " TEXT, "
				+ Keys.GruopCreatorName + " TEXT);";
		db.execSQL(cREATE_groupsTable);

		String cREATE_newsTable = "CREATE TABLE " + Keys.newsTable + " ("
				+ Keys.NEWSCOLID_NEWS + " INTEGER PRIMARY KEY," + Keys.ID_GAME
				+ " TEXT," + Keys.ID_OWNER + " TEXT," + Keys.OWNERTYPE
				+ " TEXT," + Keys.NEWSCOLNEWSTEXT + " TEXT,"
				+ Keys.NEWSCOLINTROTEXT + " TEXT," + Keys.NEWSCOLPOSTINGTIME
				+ " TEXT," + Keys.NEWSCOLHEADLINE + " TEXT,"
				+ Keys.NEWSCOLIMAGE + " TEXT," + Keys.Author + " TEXT);";
		db.execSQL(cREATE_newsTable);

		String cREATE_newsTempTable = "CREATE TABLE " + Keys.newsTempTable
				+ " (" + Keys.NEWSCOLID_NEWS + " INTEGER PRIMARY KEY,"
				+ Keys.ID_GAME + " INTEGER," + Keys.ID_OWNER + " TEXT,"
				+ Keys.NEWSCOLNEWSTEXT + " TEXT," + Keys.NEWSCOLINTROTEXT
				+ " TEXT," + Keys.NEWSCOLPOSTINGTIME + " TEXT,"
				+ Keys.NEWSCOLHEADLINE + " TEXT," + Keys.NEWSCOLIMAGE
				+ " TEXT," + Keys.Author + " TEXT);";
		db.execSQL(cREATE_newsTempTable);

		String cREATE_companyTempTable = "CREATE TABLE "
				+ Keys.companyTempTable + " (" + Keys.NEWSCOLID_NEWS
				+ " INTEGER PRIMARY KEY," + Keys.ID_GAME + " INTEGER,"
				+ Keys.ID_OWNER + " TEXT," + Keys.NEWSCOLNEWSTEXT + " TEXT,"
				+ Keys.NEWSCOLINTROTEXT + " TEXT," + Keys.NEWSCOLPOSTINGTIME
				+ " TEXT," + Keys.NEWSCOLHEADLINE + " TEXT,"
				+ Keys.NEWSCOLIMAGE + " TEXT," + Keys.Author + " TEXT);";
		db.execSQL(cREATE_companyTempTable);

		String cREATE_HomeMsgTable = "CREATE TABLE " + Keys.HomeMsgTable + " ("
				+ Keys.ID_MESSAGE + " INTEGER PRIMARY KEY,"
				+ Keys.MessageID_CONVERSATION + " INTEGER," + Keys.ID_PLAYER
				+ " INTEGER," + Keys.PLAYERNICKNAME + " TEXT,"
				+ Keys.PLAYERAVATAR + " TEXT," + Keys.MessageText + " TEXT,"
				+ Keys.MessageTime + " TEXT);";
		db.execSQL(cREATE_HomeMsgTable);

		String cREATE_HomeEventTable = "CREATE TABLE " + Keys.HomeEventTable
				+ " (" + Keys.ID_EVENT + " INTEGER PRIMARY KEY,"
				+ Keys.EventID_COMPANY + " INTEGER," + Keys.ID_GAME
				+ " INTEGER," + Keys.ID_PLAYER + " INTEGER," + Keys.ID_GROUP
				+ " INTEGER," + Keys.EventID_TEAM + " INTEGER,"
				+ Keys.EventIMAGEURL + " TEXT," + Keys.EventDescription
				+ " TEXT," + Keys.EventDuration + " TEXT," + Keys.EventHeadline
				+ " TEXT," + Keys.EventTime + " TEXT," + Keys.EventLocation
				+ " TEXT," + Keys.EventInviteLevel + " INTEGER,"
				+ Keys.EventIsPublic + " TEXT," + Keys.EventType + " TEXT,"
				+ Keys.EventIsExpired + " TEXT);";
		db.execSQL(cREATE_HomeEventTable);

		String cREATE_HomeFriendsTable = "CREATE TABLE "
				+ Keys.HomeFriendsTable + " (" + Keys.ID_PLAYER
				+ " INTEGER PRIMARY KEY," + Keys.ID_FRIEND + " INTEGER,"
				+ Keys.ID_OWNER + " INTEGER," + Keys.CITY + " TEXT,"
				+ Keys.COUNTRY + " TEXT," + Keys.PLAYERNICKNAME + " TEXT,"
				+ Keys.Email + " TEXT," + Keys.PLAYERAVATAR + " TEXT,"
				+ Keys.FirstName + " TEXT," + Keys.Mutual + " TEXT,"
				+ Keys.LastName + " TEXT," + Keys.Age + " TEXT);";
		db.execSQL(cREATE_HomeFriendsTable);

		String cREATE_whoIsPlayingTable = "CREATE TABLE "
				+ Keys.whoIsPlayingTable + " (" + Keys.ID_PLAYER
				+ " INTEGER PRIMARY KEY," + Keys.ID_GAME + " TEXT," + Keys.CITY
				+ " TEXT," + Keys.COUNTRY + " TEXT," + Keys.PLAYERNICKNAME
				+ " TEXT," + Keys.Email + " TEXT," + Keys.PLAYERAVATAR
				+ " TEXT," + Keys.FirstName + " TEXT," + Keys.LastName
				+ " TEXT," + Keys.Age + " TEXT);";
		db.execSQL(cREATE_whoIsPlayingTable);

		String cREATE_HomeGroupTable = "CREATE TABLE " + Keys.HomeGroupTable
				+ " (" + Keys.ID_GROUP + " INTEGER PRIMARY KEY,"
				+ Keys.ID_PLAYER + " INTEGER," + Keys.ID_CREATOR + " INTEGER, "
				+ Keys.GROUPNAME + " TEXT," + Keys.GROUPDESC + " TEXT,"
				+ Keys.GROUPTYPE + " TEXT," + Keys.GROUPTYPE2 + " TEXT,"
				+ Keys.GAMENAME + " TEXT," + Keys.GameIsLiked + " TEXT, "
				+ Keys.isMember + " TEXT, " + Keys.GroupMemberCount + " TEXT,"
				+ Keys.EventIMAGEURL + " TEXT," + Keys.GROUPDATE + " TEXT,"
				+ Keys.GruopCreatorName + " TEXT," + Keys.PLAYERNICKNAME
				+ " TEXT);";
		db.execSQL(cREATE_HomeGroupTable);

		String cREATE_HomeWallRepliesTable = "CREATE TABLE "
				+ Keys.HomeWallRepliesTable + " (" + Keys.ID_WALLITEM
				+ " INTEGER PRIMARY KEY," + Keys.WallPosterDisplayName
				+ " TEXT," + Keys.ID_ORGOWNER + " INTEGER," + Keys.PLAYERAVATAR
				+ " TEXT," + Keys.WallLastActivityTime + " TEXT,"
				+ Keys.WallMessage + " TEXT," + Keys.WallOwnerType + " TEXT,"
				+ Keys.WallPostingTime + " TEXT);";
		db.execSQL(cREATE_HomeWallRepliesTable);

		String cREATE_HomeMsgRepliesTable = "CREATE TABLE "
				+ Keys.HomeMsgRepliesTable + " (" + Keys.ID_MESSAGE
				+ " INTEGER PRIMARY KEY," + Keys.MessageID_CONVERSATION
				+ " INTEGER," + Keys.ID_PLAYER + " INTEGER,"
				+ Keys.PLAYERNICKNAME + " TEXT," + Keys.PLAYERAVATAR + " TEXT,"
				+ Keys.MessageText + " TEXT," + Keys.MessageTime + " TEXT);";
		db.execSQL(cREATE_HomeMsgRepliesTable);

		String cREATE_HomeSubscriptionTable = "CREATE TABLE "
				+ Keys.HomeSubscriptionTable + " (" + Keys.ID_ITEM
				+ " INTEGER PRIMARY KEY," + Keys.ID_PLAYER + " INTEGER,"
				+ Keys.ID_OWNER + " INTEGER," + Keys.ItemName + " TEXT,"
				+ Keys.ItemType + " TEXT," + Keys.SubscriptionTime + " TEXT);";
		db.execSQL(cREATE_HomeSubscriptionTable);

		String cREATE_companyTable = "CREATE TABLE " + Keys.companyTable + " ("
				+ Keys.EventID_COMPANY + " INTEGER PRIMARY KEY,"
				+ Keys.CompanyName + " TEXT," + Keys.CompanyEmployees
				+ " INTEGER," + Keys.CompanyImageURL + " TEXT,"
				+ Keys.CompanyAddress + " TEXT," + Keys.CompanyDesc + " TEXT,"
				+ Keys.CompanyFounded + " TEXT," + Keys.CompanyURL + " TEXT,"
				+ Keys.CompanyCreatedTime + " TEXT," + Keys.GameIsLiked
				+ " TEXT," + Keys.CompanyOwnership + " TEXT,"
				+ Keys.CompanyType + " TEXT," + Keys.CompanyNewsCount
				+ " INTEGER," + Keys.CompanyEventCount + " INTEGER,"
				+ Keys.CompanyGameCount + " INTEGER,"
				+ Keys.CompanySocialRating + " TEXT);";
		db.execSQL(cREATE_companyTable);

		String cREATE_PlayerTable = "CREATE TABLE " + Keys.PlayerTable + " ("
				+ Keys.ID_PLAYER + " INTEGER PRIMARY KEY," + Keys.CITY
				+ " TEXT," + Keys.COUNTRY + " TEXT," + Keys.PLAYERNICKNAME
				+ " TEXT," + Keys.PLAYERAVATAR + " TEXT," + Keys.FirstName
				+ " TEXT," + Keys.LastName + " TEXT," + Keys.Age + " TEXT,"
				+ Keys.Email + " TEXT);";
		db.execSQL(cREATE_PlayerTable);

		String cREATE_LASTIDTABLE = "CREATE TABLE " + Keys.LASTIDTABLE + " ("
				+ "_id INTEGER PRIMARY KEY autoincrement," + Keys.POSTLASTID
				+ " INTEGER, COLNAME  TEXT);";
		db.execSQL(cREATE_LASTIDTABLE);

		db.execSQL("Insert into " + Keys.LASTIDTABLE + " (" + Keys.POSTLASTID
				+ ", COLNAME) values(0,'lastIDNews');");
		db.execSQL("Insert into " + Keys.LASTIDTABLE + " (" + Keys.POSTLASTID
				+ ", COLNAME) values(0,'lastIDHomeEvents');");
		db.execSQL("Insert into " + Keys.LASTIDTABLE + " (" + Keys.POSTLASTID
				+ ", COLNAME) values(0,'lastIDHomeSubs');");
		db.execSQL("Insert into " + Keys.LASTIDTABLE + " (" + Keys.POSTLASTID
				+ ", COLNAME) values(0,'lastIDGames');");
		db.execSQL("Insert into " + Keys.LASTIDTABLE + " (" + Keys.POSTLASTID
				+ ", COLNAME) values(0,'lastIDCompanies');");
		db.execSQL("Insert into " + Keys.LASTIDTABLE + " (" + Keys.POSTLASTID
				+ ", COLNAME) values(0,'lastIDFriends');");
		db.execSQL("Insert into " + Keys.LASTIDTABLE + " (" + Keys.POSTLASTID
				+ ", COLNAME) values(0,'lastIDGroups');");
		db.execSQL("Insert into " + Keys.LASTIDTABLE + " (" + Keys.POSTLASTID
				+ ", COLNAME) values(0,'lastIDHomeMSg');");
		db.execSQL("Insert into " + Keys.LASTIDTABLE + " (" + Keys.POSTLASTID
				+ ", COLNAME) values(0,'lastIDHomeWall');");
		db.execSQL("Insert into " + Keys.LASTIDTABLE + " (" + Keys.POSTLASTID
				+ ", COLNAME) values(0,'lastIDHomeMSgRep');");
		db.execSQL("Insert into " + Keys.LASTIDTABLE + " (" + Keys.POSTLASTID
				+ ", COLNAME) values(0,'lastIDHomeWallRep');");
		db.execSQL("Insert into " + Keys.LASTIDTABLE + " (" + Keys.POSTLASTID
				+ ", COLNAME) values(0,'lastIDHomeGroups');");
		db.execSQL("Insert into " + Keys.LASTIDTABLE + " (" + Keys.POSTLASTID
				+ ", COLNAME) values(0,'lastIDHomeGames');");
		db.execSQL("Insert into " + Keys.LASTIDTABLE + " (" + Keys.POSTLASTID
				+ ", COLNAME) values(0,'lastIDHomeFriends');");
		db.execSQL("Insert into " + Keys.LASTIDTABLE + " (" + Keys.POSTLASTID
				+ ", COLNAME) values(0,'MinNewsID');");
		db.execSQL("Insert into " + Keys.LASTIDTABLE + " (" + Keys.POSTLASTID
				+ ", COLNAME) values(0,'MinGameID');");
		db.execSQL("Insert into " + Keys.LASTIDTABLE + " (" + Keys.POSTLASTID
				+ ", COLNAME) values(0,'MinGroupID');");
		db.execSQL("Insert into " + Keys.LASTIDTABLE + " (" + Keys.POSTLASTID
				+ ", COLNAME) values(0,'MinCompaniesID');");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS " + Keys.newsTable);
		db.execSQL("DROP TABLE IF EXISTS " + Keys.groupsTable);
		db.execSQL("DROP TABLE IF EXISTS " + Keys.gamesTable);
		db.execSQL("DROP TABLE IF EXISTS " + Keys.companyTable);
		db.execSQL("DROP TABLE IF EXISTS " + Keys.HomeWallTable);
		db.execSQL("DROP TABLE IF EXISTS " + Keys.newsTempTable);
		db.execSQL("DROP TABLE IF EXISTS " + Keys.companyTempTable);
		db.execSQL("DROP TABLE IF EXISTS " + Keys.PlayerTable);
		db.execSQL("DROP TABLE IF EXISTS " + Keys.LASTIDTABLE);
		db.execSQL("DROP TABLE IF EXISTS " + Keys.HomeMsgRepliesTable);
		db.execSQL("DROP TABLE IF EXISTS " + Keys.HomeWallRepliesTable);
		db.execSQL("DROP TABLE IF EXISTS " + Keys.HomeGroupTable);
		db.execSQL("DROP TABLE IF EXISTS " + Keys.whoIsPlayingTable);
		db.execSQL("DROP TABLE IF EXISTS " + Keys.HomeFriendsTable);
		db.execSQL("DROP TABLE IF EXISTS " + Keys.HomeEventTable);
		db.execSQL("DROP TABLE IF EXISTS " + Keys.HomeMsgTable);
		db.execSQL("DROP TABLE IF EXISTS " + Keys.HomeGamesTable);
		db.execSQL("DROP TABLE IF EXISTS " + Keys.HomeSubscriptionTable);
		onCreate(db);
	}
}
