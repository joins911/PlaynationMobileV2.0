package com.myapps.playnation.Operations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import com.myapps.playnation.R;
import com.myapps.playnation.Classes.CommentInfo;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Classes.UserComment;

/**
 * 
 * @author viperime
 * @category Operations Class in charge of data pulling from the database and
 *           holding it;
 */
public class DataConnector extends SQLiteOpenHelper {

	static DataConnector inst;
	InputStream is = null;
	HttpClient httpclient;
	String url;
	HashMap<String, ArrayList<Bundle>> lilDb;
	String[] gameTypes;
	String[] groupTypes;
	public final SimpleDateFormat dataTemplate = new SimpleDateFormat(
			"MMM dd,yyyy HH:mm", Locale.getDefault());
	private static JSONArray json;
	private boolean connStatus = false;
	private static ArrayList<Bundle> searchArray;
	private static ArrayList<Bundle> arrayChildren = new ArrayList<Bundle>();

	private static String DATABASE_NAME = "cdcol.db";
	private static int DATABASE_VERSION = 2;
	private Bundle currentPlayer;

	public Bundle getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Bundle currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	private DataConnector(Context con) {
		super(con, DATABASE_NAME, null, DATABASE_VERSION);

		url = "http://playnation.eu/beta/hacks/";
		// url = "http://" + ServerIp + "/test/";
		lilDb = new HashMap<String, ArrayList<Bundle>>();
		// new CheckConnectionTask().execute();
	}

	class CheckConnectionTask extends AsyncTask<Void, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				String temp = url + "getGames.php";
				URL serverURL = new URL(temp);

				URLConnection urlconn = serverURL.openConnection();
				urlconn.setConnectTimeout(5000);
				urlconn.connect();
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			connStatus = result;
		}
	}

	/**
	 * Single ton pattern
	 * 
	 * @return the static refrence of the class
	 */
	public static DataConnector getInst(Context con) {
		if (inst == null)
			return inst = new DataConnector(con);
		return inst;
	}

	public static DataConnector getInst() {
		return inst;
	}

	public String[] getGroupTypes() {
		return groupTypes;
	}

	public String[] getGameTypes() {
		return gameTypes;
	}

	@SuppressLint({ "DefaultLocale", "NewApi" })
	public void writeTempNewsTab(String id_game, String gameType) {
		SQLiteDatabase sql = this.getWritableDatabase();
		JSONArray json = getArrayFromQuerryWithPostVariable(id_game, gameType,
				"0", 0);

		if (json != null) {
			for (int i = 0; i < json.length(); i++) {
				try {
					String id = json.getJSONObject(i).getString(
							Keys.NEWSCOLID_NEWS);
					if (gameType.toLowerCase().equals("game")) {
						if (!checkRowExist(Keys.newsTempTable, id, id_game)) {

							ContentValues temp = new ContentValues();
							temp.put(Keys.NEWSCOLID_NEWS, id);
							temp.put(Keys.ID_OWNER, json.getJSONObject(i)
									.getString(Keys.ID_OWNER));
							temp.put(Keys.ID_GAME, id_game);
							temp.put(
									Keys.NEWSCOLNEWSTEXT,
									json.getJSONObject(i).getString(
											Keys.NEWSCOLNEWSTEXT));
							temp.put(
									Keys.NEWSCOLINTROTEXT,
									json.getJSONObject(i).getString(
											Keys.NEWSCOLINTROTEXT));
							temp.put(
									Keys.NEWSCOLPOSTINGTIME,
									json.getJSONObject(i).getString(
											Keys.NEWSCOLPOSTINGTIME));
							temp.put(
									Keys.NEWSCOLHEADLINE,
									json.getJSONObject(i).getString(
											Keys.NEWSCOLHEADLINE));
							temp.put(
									Keys.Author,
									json.getJSONObject(i).getString(
											Keys.FirstName)
											+ " "
											+ json.getJSONObject(i).getString(
													Keys.LastName));
							String imageUrl = json.getJSONObject(i).getString(
									Keys.NEWSCOLIMAGE);

							temp.put(Keys.NEWSCOLIMAGE, imageUrl);

							sql.insertWithOnConflict(Keys.newsTempTable, null,
									temp, SQLiteDatabase.CONFLICT_REPLACE);

						}
					} else {
						if (!checkRowExist(Keys.companyTempTable, id, id_game)) {
							ContentValues temp = new ContentValues();
							temp.put(Keys.NEWSCOLID_NEWS, id);
							temp.put(Keys.ID_OWNER, json.getJSONObject(i)
									.getString(Keys.ID_OWNER));
							temp.put(Keys.ID_GAME, id_game);
							temp.put(
									Keys.NEWSCOLNEWSTEXT,
									json.getJSONObject(i).getString(
											Keys.NEWSCOLNEWSTEXT));
							temp.put(
									Keys.NEWSCOLINTROTEXT,
									json.getJSONObject(i).getString(
											Keys.NEWSCOLINTROTEXT));
							temp.put(
									Keys.NEWSCOLPOSTINGTIME,
									json.getJSONObject(i).getString(
											Keys.NEWSCOLPOSTINGTIME));
							temp.put(
									Keys.NEWSCOLHEADLINE,
									json.getJSONObject(i).getString(
											Keys.NEWSCOLHEADLINE));
							temp.put(
									Keys.Author,
									json.getJSONObject(i).getString(
											Keys.FirstName)
											+ " "
											+ json.getJSONObject(i).getString(
													Keys.LastName));
							String imageUrl = json.getJSONObject(i).getString(
									Keys.NEWSCOLIMAGE);

							temp.put(Keys.NEWSCOLIMAGE, imageUrl);

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
					Bundle temp = new Bundle();
					temp.putString(Keys.NEWSCOLID_NEWS, cursor.getString(cursor
							.getColumnIndex(Keys.NEWSCOLID_NEWS)));
					temp.putString(Keys.ID_OWNER, cursor.getString(cursor
							.getColumnIndex(Keys.ID_OWNER)));
					temp.putString(Keys.NEWSCOLNEWSTEXT, cursor
							.getString(cursor
									.getColumnIndex(Keys.NEWSCOLNEWSTEXT)));
					temp.putString(Keys.NEWSCOLINTROTEXT, cursor
							.getString(cursor
									.getColumnIndex(Keys.NEWSCOLINTROTEXT)));
					temp.putString(Keys.NEWSCOLPOSTINGTIME, cursor
							.getString(cursor
									.getColumnIndex(Keys.NEWSCOLPOSTINGTIME)));
					temp.putString(Keys.NEWSCOLHEADLINE, cursor
							.getString(cursor
									.getColumnIndex(Keys.NEWSCOLHEADLINE)));
					temp.putString(Keys.Author, cursor.getString(cursor
							.getColumnIndex(Keys.Author)));
					temp.putString(Keys.NEWSCOLIMAGE, cursor.getString(cursor
							.getColumnIndex(Keys.NEWSCOLIMAGE)));
					arrayList.add(temp);
				} while (cursor.moveToNext());
			}
			cursor.close();
			return arrayList;
		}

		return null;
	}

	/**
	 * @param groupTypes
	 *            The Set containing all the group Types to be changed into a
	 *            normal array
	 */
	private void convertGroupTypes(Set<String> groupTypes) {
		String[] items = new String[groupTypes.size() + 1];
		items[0] = "All";
		Iterator<String> itr = groupTypes.iterator();
		int i = 1;
		while (itr.hasNext()) {
			items[i] = itr.next();
			i++;
		}
		this.groupTypes = items;
	}

	/**
	 * @param gameTypes
	 *            The Set containing all the games Types to be changed into a
	 *            normal array
	 */
	public void convertGameTypes(Set<String> gameTypes) {
		String[] items = new String[gameTypes.size() + 1];
		items[0] = "All";
		Iterator<String> itr = gameTypes.iterator();
		int i = 1;
		while (itr.hasNext()) {
			items[i] = itr.next();
			i++;
		}
		this.gameTypes = items;
	}

	/**
	 * Takes the data out of the JSON response and adds it to a Map for holding
	 * until destroyed
	 * 
	 * @param jsonArray
	 *            the JSON response from the Database
	 * @throws JSONException
	 */
	@TargetApi(Build.VERSION_CODES.FROYO)
	@SuppressLint("NewApi")
	public void addGames(JSONArray jsonArray) throws JSONException {

		SQLiteDatabase sql = this.getWritableDatabase();
		Set<String> gameTypes = new HashSet<String>();
		for (int i = 0; i < jsonArray.length(); i++) {
			String id_GAME = jsonArray.getJSONObject(i).getString(Keys.ID_GAME);
			if (checkRowExist(Keys.gamesTable, id_GAME, "") == false) {
				ContentValues temp = new ContentValues();
				temp.put(Keys.GAMENAME,
						jsonArray.getJSONObject(i).getString(Keys.GAMENAME));
				String gameType = jsonArray.getJSONObject(i).getString(
						Keys.GAMETYPE);
				temp.put(Keys.GAMETYPE, gameType);
				temp.put(Keys.GAMEDESC,
						jsonArray.getJSONObject(i).getString(Keys.GAMEDESC));
				temp.put(Keys.GAMEDATE,
						jsonArray.getJSONObject(i).getString(Keys.GAMEDATE));
				temp.put(Keys.RATING,
						jsonArray.getJSONObject(i).getString(Keys.RATING));
				temp.put(Keys.GAMEESRB,
						jsonArray.getJSONObject(i).getString(Keys.GAMEESRB));
				String imageUrl = jsonArray.getJSONObject(i).getString(
						Keys.EventIMAGEURL);
				temp.put(Keys.EventIMAGEURL, imageUrl);
				temp.put(Keys.GAMEURL,
						jsonArray.getJSONObject(i).getString(Keys.GAMEURL));
				temp.put(Keys.GAMEPLAYERSCOUNT, jsonArray.getJSONObject(i)
						.getString(Keys.GAMEPLAYERSCOUNT));
				int id = Integer.parseInt(id_GAME);
				if (id > getLastIDGames())
					setLastIDGames(id);
				temp.put(Keys.ID_GAME, id_GAME);
				temp.put(Keys.GAMETYPENAME, jsonArray.getJSONObject(i)
						.getString(Keys.GAMETYPENAME));
				temp.put(Keys.GAMEPLATFORM, jsonArray.getJSONObject(i)
						.getString(Keys.GAMEPLATFORM));
				temp.put(Keys.GAMECompanyDistributor, jsonArray
						.getJSONObject(i)
						.getString(Keys.GAMECompanyDistributor));
				temp.put(Keys.CompanyFounded, jsonArray.getJSONObject(i)
						.getString(Keys.CompanyFounded));
				temp.put(Keys.CompanyName, jsonArray.getJSONObject(i)
						.getString(Keys.GAMECompanyDeveloper));
				gameTypes.add(jsonArray.getJSONObject(i).getString(
						Keys.GAMETYPE));
				sql.insertWithOnConflict(Keys.gamesTable, null, temp,
						SQLiteDatabase.CONFLICT_REPLACE);
			}
		}

		convertGameTypes(gameTypes);
	}

	/**
	 * Takes the data out of the JSON response and adds it to a DatabaseMAP for
	 * holding until destroyed
	 * 
	 * @param jsonArray
	 *            the JSON response from the Database
	 * @throws JSONException
	 */
	public void addComments(JSONArray jsonArray) throws JSONException {
		ArrayList<Bundle> arrayList = new ArrayList<Bundle>();
		for (int i = 0; i < jsonArray.length(); i++) {
			Bundle temp = new Bundle();
			temp.putString(Keys.ID_OWNER,
					jsonArray.getJSONObject(i).getString(Keys.ID_OWNER));
			temp.putString(Keys.COMMENT,
					jsonArray.getJSONObject(i).getString(Keys.COMMENT));
			temp.putString(Keys.WallMessage, jsonArray.getJSONObject(i)
					.getString(Keys.WallMessage));
			temp.putString(Keys.CommentTime, jsonArray.getJSONObject(i)
					.getString(Keys.CommentTime));
			arrayList.add(temp);
		}
		if (!lilDb.containsKey(Keys.commentsTable))
			lilDb.put(Keys.commentsTable, arrayList);
		else
			lilDb.get(Keys.commentsTable).addAll(arrayList);
	}

	@SuppressLint("NewApi")
	public void addGroups(JSONArray jsonArray) throws JSONException {
		SQLiteDatabase sql = this.getWritableDatabase();
		Set<String> groupTypes = new HashSet<String>();
		for (int i = 0; i < jsonArray.length(); i++) {
			String ID_Groups = jsonArray.getJSONObject(i).getString(
					Keys.ID_GROUP);
			if (!checkRowExist(Keys.groupsTable, ID_Groups, "")) {
				ContentValues temp = new ContentValues();
				temp.put(Keys.GROUPNAME,
						jsonArray.getJSONObject(i).getString(Keys.GROUPNAME));
				temp.put(Keys.GROUPTYPE,
						jsonArray.getJSONObject(i).getString(Keys.GROUPTYPE));
				temp.put(Keys.GROUPDESC,
						jsonArray.getJSONObject(i).getString(Keys.GROUPDESC));
				temp.put(Keys.GROUPTYPE2,
						jsonArray.getJSONObject(i).getString(Keys.GROUPTYPE2));

				// Changed so date and members should be
				temp.put(Keys.GroupMemberCount, jsonArray.getJSONObject(i)
						.getString(Keys.GroupMemberCount));
				String imageUrl = jsonArray.getJSONObject(i).getString(
						Keys.EventIMAGEURL);

				temp.put(Keys.EventIMAGEURL, imageUrl);

				temp.put(Keys.GROUPDATE, HelperClass.convertTime(Integer
						.parseInt(jsonArray.getJSONObject(i).getString(
								Keys.GROUPDATE)), new SimpleDateFormat(
						"EEEE,MMMM d,yyyy h:mm,a", Locale.getDefault())));

				temp.put(Keys.ID_GROUP, ID_Groups);
				int id = Integer.parseInt(ID_Groups);
				if (id > getLastIDGroups())
					setLastIDGroups(id);
				String creator = jsonArray.getJSONObject(i).getString(
						Keys.PLAYERNICKNAME);
				temp.put(Keys.GruopCreatorName, creator);

				sql.insertWithOnConflict(Keys.groupsTable, null, temp,
						SQLiteDatabase.CONFLICT_REPLACE);
				// groupTypes.add(jsonArray.getJSONObject(i).getString(
				// Keys.GROUPTYPE));
			}
		}
		// //sql.close();
		// convertGroupTypes(groupTypes);
	}

	@SuppressLint("NewApi")
	public void addNews(JSONArray jsonArray) throws JSONException {
		SQLiteDatabase sql = this.getWritableDatabase();

		for (int i = 0; i < jsonArray.length(); i++) {
			String ID_News = jsonArray.getJSONObject(i).getString(
					Keys.NEWSCOLID_NEWS);
			if (!checkRowExist(Keys.newsTable, ID_News, "")) {
				ContentValues temp = new ContentValues();
				temp.put(Keys.NEWSCOLID_NEWS, ID_News);
				temp.put(Keys.NEWSCOLNEWSTEXT, jsonArray.getJSONObject(i)
						.getString(Keys.NEWSCOLNEWSTEXT));
				temp.put(Keys.NEWSCOLINTROTEXT, jsonArray.getJSONObject(i)
						.getString(Keys.NEWSCOLINTROTEXT));
				temp.put(Keys.NEWSCOLPOSTINGTIME, jsonArray.getJSONObject(i)
						.getString(Keys.NEWSCOLPOSTINGTIME));

				int id = jsonArray.getJSONObject(i).getInt(Keys.NEWSCOLID_NEWS);
				setLastIDNews(id);

				temp.put(Keys.NEWSCOLHEADLINE, jsonArray.getJSONObject(i)
						.getString(Keys.NEWSCOLHEADLINE));
				temp.put(
						Keys.Author,
						jsonArray.getJSONObject(i).getString(Keys.FirstName)
								+ " "
								+ jsonArray.getJSONObject(i).getString(
										Keys.LastName));
				String imageUrl = jsonArray.getJSONObject(i).getString(
						Keys.NEWSCOLIMAGE);

				temp.put(Keys.NEWSCOLIMAGE, imageUrl);

				sql.insertWithOnConflict(Keys.newsTable, null, temp,
						SQLiteDatabase.CONFLICT_REPLACE);
			}
		}
	}

	public void jsonToArray(JSONArray jsonArray, String table)
			throws JSONException {
		if (jsonArray != null && table.equals(Keys.gamesTable)) {
			addGames(jsonArray);
		} else if (jsonArray != null && table.equals(Keys.commentsTable)) {
			addComments(jsonArray);
		} else if (jsonArray != null && table.equals(Keys.groupsTable)) {
			addGroups(jsonArray);
		} else if (jsonArray != null && table.equals(Keys.newsTable)) {
			addNews(jsonArray);
		} else if (jsonArray != null && table.equals(Keys.newsServiceTable)) {
			addNews(jsonArray);
		} else if (jsonArray != null && table.equals(Keys.companyTable)) {
			addCompany(jsonArray);
		}
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
					Bundle bundle = new Bundle();
					bundle.putString(
							Keys.WallPosterDisplayName,
							cursor.getString(cursor
									.getColumnIndex(Keys.WallPosterDisplayName))
									+ "");
					bundle.putString(
							Keys.ID_WALLITEM,
							cursor.getInt(cursor
									.getColumnIndex(Keys.ID_WALLITEM)) + "");
					bundle.putString(Keys.ID_OWNER,
							cursor.getInt(cursor.getColumnIndex(Keys.ID_OWNER))
									+ "");
					bundle.putString(Keys.ItemType, cursor.getString(cursor
							.getColumnIndex(Keys.ItemType)));
					bundle.putString(Keys.WallLastActivityTime, cursor
							.getString(cursor
									.getColumnIndex(Keys.WallLastActivityTime)));
					bundle.putString(Keys.WallMessage, cursor.getString(cursor
							.getColumnIndex(Keys.WallMessage)));
					bundle.putString(Keys.WallOwnerType, cursor
							.getString(cursor
									.getColumnIndex(Keys.WallOwnerType)));
					bundle.putString(Keys.WallPostingTime, cursor
							.getString(cursor
									.getColumnIndex(Keys.WallPostingTime)));
					bundle.putString(Keys.PLAYERAVATAR, cursor.getString(cursor
							.getColumnIndex(Keys.PLAYERAVATAR)));
					list.add(bundle);
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		return list;
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
					Bundle bundle = new Bundle();
					bundle.putString(Keys.NEWSCOLID_NEWS, cursor
							.getString(cursor
									.getColumnIndex(Keys.NEWSCOLID_NEWS)));
					bundle.putString(Keys.NEWSCOLNEWSTEXT, cursor
							.getString(cursor
									.getColumnIndex(Keys.NEWSCOLNEWSTEXT)));
					bundle.putString(Keys.NEWSCOLINTROTEXT, cursor
							.getString(cursor
									.getColumnIndex(Keys.NEWSCOLINTROTEXT)));
					bundle.putString(Keys.NEWSCOLPOSTINGTIME, cursor
							.getString(cursor
									.getColumnIndex(Keys.NEWSCOLPOSTINGTIME)));
					bundle.putString(Keys.NEWSCOLHEADLINE, cursor
							.getString(cursor
									.getColumnIndex(Keys.NEWSCOLHEADLINE)));
					bundle.putString(Keys.Author, cursor.getString(cursor
							.getColumnIndex(Keys.Author)));
					bundle.putString(Keys.NEWSCOLIMAGE, cursor.getString(cursor
							.getColumnIndex(Keys.NEWSCOLIMAGE)));
					list.add(bundle);
				} while (cursor.moveToNext());
			}
			cursor.close();
		}

		return list;
	}
	
	
	public ArrayList<String> getMyGames(String playerID)
	{
		ArrayList<String> myGames = new ArrayList<String>();
		int i=0;
		String selectQuery = HelperClass.sqliteQueryStrings(Keys.HomeGamesTable, "", "",
				0 + "");
		SQLiteDatabase sql = getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, new String[] { playerID });
		if (cursor != null) {
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				do {
					myGames.add(cursor.getString(cursor
							.getColumnIndex(Keys.GAMENAME)));
					i++;
				} while (cursor.moveToNext() && i<3);
			}
			cursor.close();
		}
		return myGames;
	}
	
	public ArrayList<String> getMyGroups(String playerID)
	{
		ArrayList<String> myGroups = new ArrayList<String>();
		int i=0;
		String selectQuery = HelperClass.sqliteQueryStrings(Keys.HomeGroupTable, "", "",
				0 + "");
		SQLiteDatabase sql = getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, new String[] { playerID });
		if (cursor != null) {
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				do {
					myGroups.add(cursor.getString(cursor
							.getColumnIndex(Keys.GROUPNAME)));
					i++;
				} while (cursor.moveToNext() && i<3);
			}
			cursor.close();
		}
		return myGroups;
	}	 
	
	public Bundle getItem(String itemName, String tableName)
	{		
		Bundle bundle = new Bundle();
		String selectQuery = "Select * from "+ tableName+" where GameName="+itemName+";";
		
		SQLiteDatabase sql = this.getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);

		if (cursor != null) {
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				do {
					if(tableName.equalsIgnoreCase(Keys.gamesTable))
					{
						bundle =putGameInBundle(cursor);
					}	
					if(tableName.equalsIgnoreCase(Keys.gamesTable))
					{
						bundle = putGroupInBundle(cursor);
					}
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		
		return null;
	}
	
	public Bundle putGameInBundle(Cursor cursor)
	{
		Bundle bundle = new Bundle();
		bundle.putString(Keys.GAMENAME, cursor.getString(cursor
				.getColumnIndex(Keys.GAMENAME)));
		String gameType = cursor.getString(cursor
				.getColumnIndex(Keys.GAMETYPE));
		bundle.putString(Keys.GAMETYPE, gameType);
		bundle.putString(Keys.GAMEDESC, cursor.getString(cursor
				.getColumnIndex(Keys.GAMEDESC)));
		bundle.putString(Keys.GAMEDATE, cursor.getString(cursor
				.getColumnIndex(Keys.GAMEDATE)));
		bundle.putString(Keys.RATING, cursor.getString(cursor
				.getColumnIndex(Keys.RATING)));
		bundle.putString(Keys.GAMEESRB, cursor.getString(cursor
				.getColumnIndex(Keys.GAMEESRB)));
		bundle.putString(Keys.GAMEURL, cursor.getString(cursor
				.getColumnIndex(Keys.GAMEURL)));
		bundle.putString(Keys.GAMEPLAYERSCOUNT, cursor
				.getString(cursor
						.getColumnIndex(Keys.GAMEPLAYERSCOUNT)));
		String id_GAME = cursor.getString(cursor
				.getColumnIndex(Keys.ID_GAME));
		bundle.putString(Keys.ID_GAME, id_GAME);
		bundle.putString(Keys.GAMETYPENAME, cursor.getString(cursor
				.getColumnIndex(Keys.GAMETYPENAME)));
		bundle.putString(Keys.GAMETYPE, cursor.getString(cursor
				.getColumnIndex(Keys.GAMETYPE)));
		bundle.putString(Keys.GAMEPLATFORM, cursor.getString(cursor
				.getColumnIndex(Keys.GAMEPLATFORM)));
		bundle.putString(
				Keys.GAMECompanyDistributor,
				cursor.getString(cursor
						.getColumnIndex(Keys.GAMECompanyDistributor)));
		bundle.putString(Keys.CompanyFounded, cursor
				.getString(cursor
						.getColumnIndex(Keys.CompanyFounded)));
		bundle.putString(Keys.CompanyName, cursor.getString(cursor
				.getColumnIndex(Keys.CompanyName)));
		bundle.putString(Keys.EventIMAGEURL, cursor
				.getString(cursor
						.getColumnIndex(Keys.EventIMAGEURL)));
		return bundle;
	}
	
	public Bundle putGroupInBundle(Cursor cursor)
	{
		Bundle bundle = new Bundle();
		bundle.putString(Keys.GROUPNAME, cursor.getString(cursor
				.getColumnIndex(Keys.GROUPNAME)));
		bundle.putString(Keys.GROUPTYPE, cursor.getString(cursor
				.getColumnIndex(Keys.GROUPTYPE)));
		bundle.putString(Keys.GROUPDESC, cursor.getString(cursor
				.getColumnIndex(Keys.GROUPDESC)));
		bundle.putString(Keys.GROUPTYPE2, cursor.getString(cursor
				.getColumnIndex(Keys.GROUPTYPE2)));
		// Changed so date and members should be
		bundle.putString(Keys.GroupMemberCount, cursor
				.getString(cursor
						.getColumnIndex(Keys.GroupMemberCount)));
		bundle.putString(Keys.GROUPDATE, cursor.getString(cursor
				.getColumnIndex(Keys.GROUPDATE)));
		bundle.putString(Keys.ID_GROUP, cursor.getString(cursor
				.getColumnIndex(Keys.ID_GROUP)));
		bundle.putString(Keys.EventIMAGEURL, cursor
				.getString(cursor
						.getColumnIndex(Keys.EventIMAGEURL)));
		bundle.putString(Keys.GruopCreatorName, cursor
				.getString(cursor
						.getColumnIndex(Keys.GruopCreatorName)));
		return bundle;
	}
	
	public ArrayList<Bundle> getSQLiteGame(String tableName) {
		ArrayList<Bundle> list = new ArrayList<Bundle>();
		String selectQuery = HelperClass.sqliteQueryStrings(tableName, "", "",
				0 + "");
		SQLiteDatabase sql = this.getReadableDatabase();
		Cursor cursor = sql.rawQuery(selectQuery, null);

		if (cursor != null) {
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				do {
					list.add(putGameInBundle(cursor));
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
					
					list.add(putGroupInBundle(cursor));
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
					Bundle bundle = new Bundle();
					bundle.putString(Keys.EventID_COMPANY, cursor
							.getString(cursor
									.getColumnIndex(Keys.EventID_COMPANY)));
					bundle.putString(Keys.CompanyName, cursor.getString(cursor
							.getColumnIndex(Keys.CompanyName)));
					bundle.putString(Keys.CompanyEmployees, cursor
							.getString(cursor
									.getColumnIndex(Keys.CompanyEmployees)));
					bundle.putString(Keys.CompanyImageURL, cursor
							.getString(cursor
									.getColumnIndex(Keys.CompanyImageURL)));
					bundle.putString(Keys.CompanyAddress, cursor
							.getString(cursor
									.getColumnIndex(Keys.CompanyAddress)));
					bundle.putString(Keys.CompanyDesc, cursor.getString(cursor
							.getColumnIndex(Keys.CompanyDesc)));
					bundle.putString(Keys.CompanyFounded, cursor
							.getString(cursor
									.getColumnIndex(Keys.CompanyFounded)));
					bundle.putString(Keys.CompanyURL, cursor.getString(cursor
							.getColumnIndex(Keys.CompanyURL)));
					bundle.putString(Keys.CompanyCreatedTime, cursor
							.getString(cursor
									.getColumnIndex(Keys.CompanyCreatedTime)));
					bundle.putString(Keys.CompanyOwnership, cursor
							.getString(cursor
									.getColumnIndex(Keys.CompanyOwnership)));
					bundle.putString(Keys.CompanyType, cursor.getString(cursor
							.getColumnIndex(Keys.CompanyType)));
					bundle.putString(Keys.CompanyNewsCount, cursor
							.getString(cursor
									.getColumnIndex(Keys.CompanyNewsCount)));
					bundle.putString(Keys.CompanyEventCount, cursor
							.getString(cursor
									.getColumnIndex(Keys.CompanyEventCount)));
					bundle.putString(Keys.CompanyGameCount, cursor
							.getString(cursor
									.getColumnIndex(Keys.CompanyGameCount)));
					bundle.putString(Keys.CompanySocialRating, cursor
							.getString(cursor
									.getColumnIndex(Keys.CompanySocialRating)));
					list.add(bundle);
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
					Bundle bundle = new Bundle();
					bundle.putString(
							Keys.ID_MESSAGE,
							cursor.getInt(cursor
									.getColumnIndex(Keys.ID_MESSAGE)) + "");
					bundle.putString(
							Keys.MessageID_CONVERSATION,
							cursor.getInt(cursor
									.getColumnIndex(Keys.MessageID_CONVERSATION))
									+ "");
					bundle.putString(Keys.PLAYERNICKNAME, cursor
							.getString(cursor
									.getColumnIndex(Keys.PLAYERNICKNAME)));
					bundle.putString(Keys.PLAYERAVATAR, cursor.getString(cursor
							.getColumnIndex(Keys.PLAYERAVATAR)));

					bundle.putString(Keys.MessageText, cursor.getString(cursor
							.getColumnIndex(Keys.MessageText)));
					bundle.putString(Keys.MessageTime, cursor.getString(cursor
							.getColumnIndex(Keys.MessageTime)));
					list.add(bundle);
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
					Bundle bundle = new Bundle();
					bundle.putString(Keys.ID_EVENT,
							cursor.getInt(cursor.getColumnIndex(Keys.ID_EVENT))
									+ "");
					bundle.putString(
							Keys.EventID_COMPANY,
							cursor.getInt(cursor
									.getColumnIndex(Keys.EventID_COMPANY)) + "");
					bundle.putString(Keys.ID_GAME,
							cursor.getInt(cursor.getColumnIndex(Keys.ID_GAME))
									+ "");
					bundle.putString(Keys.ID_GROUP,
							cursor.getInt(cursor.getColumnIndex(Keys.ID_GROUP))
									+ "");
					bundle.putString(
							Keys.EventID_TEAM,
							cursor.getInt(cursor
									.getColumnIndex(Keys.EventID_TEAM)) + "");
					bundle.putString(Keys.EventIMAGEURL, cursor
							.getString(cursor
									.getColumnIndex(Keys.EventIMAGEURL)));
					bundle.putString(Keys.EventDescription, cursor
							.getString(cursor
									.getColumnIndex(Keys.EventDescription)));
					bundle.putString(Keys.EventDuration, cursor
							.getString(cursor
									.getColumnIndex(Keys.EventDuration)));
					bundle.putString(Keys.EventHeadline, cursor
							.getString(cursor
									.getColumnIndex(Keys.EventHeadline)));
					bundle.putString(Keys.EventTime, cursor.getString(cursor
							.getColumnIndex(Keys.EventTime)));
					bundle.putString(Keys.EventLocation, cursor
							.getString(cursor
									.getColumnIndex(Keys.EventLocation)));
					bundle.putString(Keys.EventInviteLevel, cursor
							.getString(cursor
									.getColumnIndex(Keys.EventInviteLevel)));
					bundle.putString(
							Keys.EventIsPublic,
							cursor.getInt(cursor
									.getColumnIndex(Keys.EventIsPublic)) + "");
					bundle.putString(Keys.EventType, cursor.getString(cursor
							.getColumnIndex(Keys.EventType)));
					bundle.putString(
							Keys.EventIsExpired,
							cursor.getInt(cursor
									.getColumnIndex(Keys.EventIsExpired)) + "");
					list.add(bundle);
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
					Bundle bundle = new Bundle();
					bundle.putString(Keys.ID_PLAYER, cursor.getString(cursor
							.getColumnIndex(Keys.ID_PLAYER)));
					bundle.putString(Keys.ID_OWNER, cursor.getString(cursor
							.getColumnIndex(Keys.ID_OWNER)));
					bundle.putString(Keys.CITY,
							cursor.getString(cursor.getColumnIndex(Keys.CITY)));
					bundle.putString(Keys.COUNTRY, cursor.getString(cursor
							.getColumnIndex(Keys.COUNTRY)));
					bundle.putString(Keys.PLAYERNICKNAME, cursor
							.getString(cursor
									.getColumnIndex(Keys.PLAYERNICKNAME)));
					bundle.putString(Keys.Email,
							cursor.getString(cursor.getColumnIndex(Keys.Email)));
					bundle.putString(Keys.PLAYERAVATAR, cursor.getString(cursor
							.getColumnIndex(Keys.PLAYERAVATAR)));
					bundle.putString(Keys.FirstName, cursor.getString(cursor
							.getColumnIndex(Keys.FirstName)));
					bundle.putString(Keys.LastName, cursor.getString(cursor
							.getColumnIndex(Keys.LastName)));

					bundle.putString(Keys.Age,
							cursor.getString(cursor.getColumnIndex(Keys.Age)));

					list.add(bundle);
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		return list;
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
					Bundle bundle = new Bundle();
					bundle.putString(Keys.ID_PLAYER, cursor.getString(cursor
							.getColumnIndex(Keys.ID_PLAYER)));
					bundle.putString(Keys.ID_GAME, cursor.getString(cursor
							.getColumnIndex(Keys.ID_GAME)));
					bundle.putString(Keys.CITY,
							cursor.getString(cursor.getColumnIndex(Keys.CITY)));
					bundle.putString(Keys.COUNTRY, cursor.getString(cursor
							.getColumnIndex(Keys.COUNTRY)));
					bundle.putString(Keys.PLAYERNICKNAME, cursor
							.getString(cursor
									.getColumnIndex(Keys.PLAYERNICKNAME)));
					bundle.putString(Keys.Email,
							cursor.getString(cursor.getColumnIndex(Keys.Email)));
					bundle.putString(Keys.PLAYERAVATAR, cursor.getString(cursor
							.getColumnIndex(Keys.PLAYERAVATAR)));
					bundle.putString(Keys.FirstName, cursor.getString(cursor
							.getColumnIndex(Keys.FirstName)));
					bundle.putString(Keys.LastName, cursor.getString(cursor
							.getColumnIndex(Keys.LastName)));
					bundle.putString(Keys.Age,
							cursor.getString(cursor.getColumnIndex(Keys.Age)));

					list.add(bundle);
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		return list;
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
					Bundle bundle = new Bundle();
					bundle.putString(Keys.ID_GAME,
							cursor.getInt(cursor.getColumnIndex(Keys.ID_GAME))
									+ "");
					bundle.putString(Keys.GameComments, cursor.getString(cursor
							.getColumnIndex(Keys.GameComments)));
					bundle.putString(Keys.GAMENAME, cursor.getString(cursor
							.getColumnIndex(Keys.GAMENAME)));
					bundle.putString(
							Keys.GAMEDESC,
							cursor.getString(cursor
									.getColumnIndex(Keys.GAMEDESC)) + "");
					bundle.putString(
							Keys.GameID_GAMETYPE,
							cursor.getInt(cursor
									.getColumnIndex(Keys.GameID_GAMETYPE)) + "");
					bundle.putString(Keys.GAMEDATE, cursor.getString(cursor
							.getColumnIndex(Keys.GAMEDATE)));
					bundle.putString(Keys.RATING, cursor.getString(cursor
							.getColumnIndex(Keys.RATING)));
					bundle.putString(Keys.GAMEESRB, cursor.getString(cursor
							.getColumnIndex(Keys.GAMEESRB)));
					bundle.putString(Keys.GAMEURL, cursor.getString(cursor
							.getColumnIndex(Keys.GAMEURL)));
					bundle.putString(Keys.GAMEPLAYERSCOUNT, cursor
							.getString(cursor
									.getColumnIndex(Keys.GAMEPLAYERSCOUNT)));
					bundle.putString(
							Keys.GAMETYPE,
							cursor.getString(cursor
									.getColumnIndex(Keys.GAMETYPE)) + "");
					bundle.putString(
							Keys.GameisPlaying,
							cursor.getInt(cursor
									.getColumnIndex(Keys.GameisPlaying)) + "");
					bundle.putString(
							Keys.GamesisSubscribed,
							cursor.getInt(cursor
									.getColumnIndex(Keys.GamesisSubscribed))
									+ "");
					bundle.putString(
							Keys.GamePostCount,
							cursor.getInt(cursor
									.getColumnIndex(Keys.GamePostCount)) + "");
					bundle.putString(
							Keys.GamesSubscriptionTime,
							cursor.getString(cursor
									.getColumnIndex(Keys.GamesSubscriptionTime)));
					bundle.putString(Keys.GAMETYPENAME, cursor.getString(cursor
							.getColumnIndex(Keys.GAMETYPENAME)));
					bundle.putString(Keys.GAMETYPE, cursor.getString(cursor
							.getColumnIndex(Keys.GAMETYPE)));
					bundle.putString(Keys.GAMEPLATFORM, cursor.getString(cursor
							.getColumnIndex(Keys.GAMEPLATFORM)));
					bundle.putString(
							Keys.GAMECompanyDistributor,
							cursor.getString(cursor
									.getColumnIndex(Keys.GAMECompanyDistributor)));
					bundle.putString(Keys.CompanyFounded, cursor
							.getString(cursor
									.getColumnIndex(Keys.CompanyFounded)));
					bundle.putString(Keys.CompanyName, cursor.getString(cursor
							.getColumnIndex(Keys.CompanyName)));
					bundle.putString(Keys.EventIMAGEURL, cursor
							.getString(cursor
									.getColumnIndex(Keys.EventIMAGEURL)));

					list.add(bundle);
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
					Bundle bundle = new Bundle();
					bundle.putString(Keys.ID_GROUP,
							cursor.getInt(cursor.getColumnIndex(Keys.ID_GROUP))
									+ "");
					bundle.putString(
							Keys.ID_PLAYER,
							cursor.getInt(cursor.getColumnIndex(Keys.ID_PLAYER))
									+ "");
					bundle.putString(Keys.GROUPNAME, cursor.getString(cursor
							.getColumnIndex(Keys.GROUPNAME)));
					bundle.putString(Keys.GROUPDESC, cursor.getString(cursor
							.getColumnIndex(Keys.GROUPDESC)));
					bundle.putString(Keys.GROUPTYPE, cursor.getString(cursor
							.getColumnIndex(Keys.GROUPTYPE)));
					bundle.putString(Keys.GROUPTYPE2, cursor.getString(cursor
							.getColumnIndex(Keys.GROUPTYPE2)));
					bundle.putString(Keys.GAMENAME, cursor.getString(cursor
							.getColumnIndex(Keys.GAMENAME)));
					bundle.putString(Keys.GroupMemberCount, cursor
							.getString(cursor
									.getColumnIndex(Keys.GroupMemberCount)));
					bundle.putString(Keys.EventIMAGEURL, cursor
							.getString(cursor
									.getColumnIndex(Keys.EventIMAGEURL)));
					bundle.putString(Keys.GROUPDATE, cursor.getString(cursor
							.getColumnIndex(Keys.GROUPDATE)));
					bundle.putString(Keys.GruopCreatorName, cursor
							.getString(cursor
									.getColumnIndex(Keys.PLAYERNICKNAME)));

					list.add(bundle);
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
					Bundle bundle = new Bundle();
					bundle.putString(
							Keys.ID_MESSAGE,
							cursor.getInt(cursor
									.getColumnIndex(Keys.ID_MESSAGE)) + "");
					bundle.putString(
							Keys.MessageID_CONVERSATION,
							cursor.getInt(cursor
									.getColumnIndex(Keys.MessageID_CONVERSATION))
									+ "");
					bundle.putString(Keys.PLAYERNICKNAME, cursor
							.getString(cursor
									.getColumnIndex(Keys.PLAYERNICKNAME)));
					bundle.putString(Keys.PLAYERAVATAR, cursor.getString(cursor
							.getColumnIndex(Keys.PLAYERAVATAR)));

					bundle.putString(Keys.MessageText, cursor.getString(cursor
							.getColumnIndex(Keys.MessageText)));
					bundle.putString(Keys.MessageTime, cursor.getString(cursor
							.getColumnIndex(Keys.MessageTime)));

					list.add(bundle);
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
		System.out.println("Cursor " + cursor.getCount());
		if (cursor != null) {
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				do {
					Bundle bundle = new Bundle();
					bundle.putString(
							Keys.WallPosterDisplayName,
							cursor.getString(cursor
									.getColumnIndex(Keys.WallPosterDisplayName))
									+ "");
					bundle.putString(
							Keys.ID_ORGOWNER,
							cursor.getInt(cursor
									.getColumnIndex(Keys.ID_ORGOWNER)) + "");
					bundle.putString(
							Keys.ID_WALLITEM,
							cursor.getInt(cursor
									.getColumnIndex(Keys.ID_WALLITEM)) + "");
					bundle.putString(
							Keys.PLAYERAVATAR,
							cursor.getString(cursor
									.getColumnIndex(Keys.PLAYERAVATAR)) + "");
					bundle.putString(Keys.WallLastActivityTime, cursor
							.getString(cursor
									.getColumnIndex(Keys.WallLastActivityTime)));
					bundle.putString(Keys.WallMessage, cursor.getString(cursor
							.getColumnIndex(Keys.WallMessage)));
					bundle.putString(Keys.WallOwnerType, cursor
							.getString(cursor
									.getColumnIndex(Keys.WallOwnerType)));
					bundle.putString(Keys.WallPostingTime, cursor
							.getString(cursor
									.getColumnIndex(Keys.WallPostingTime)));

					list.add(bundle);

				} while (cursor.moveToNext());

			}
			cursor.close();
		}
		return list;
	}

	/**
	 * Check if row exists in SqlLite DB.
	 * 
	 * @param tableName
	 * @param separeteID
	 * @return boolean
	 */
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

	/**
	 * Gets the required Table from the DatabaseMAP
	 * 
	 * @param tableName
	 *            Table name we want to get from the DatabaseMAP
	 * @return Database table
	 */
	public ArrayList<Bundle> getTable(String tableName, String sepateID) {

		if (lilDb.get(tableName) == null) {
			if (!checkDBTableExits(tableName)) {
				getQuerry(tableName);
			} else {
				if (tableName.equals(Keys.HomeWallTable)) {
					return getSQLitePWall(tableName, sepateID);
				} else if (tableName.equals(Keys.newsTable)) {
					return getSQLiteNews(tableName);
				} else if (tableName.equals(Keys.groupsTable)) {
					return getSQLiteGroups(tableName);
				} else if (tableName.equals(Keys.gamesTable)) {
					return getSQLiteGame(tableName);
				} else if (tableName.equals(Keys.companyTable)) {
					return getSQLiteCompanies(tableName);
				} else if (tableName.equals(Keys.HomeMsgTable)) {
					return getSQLitePMSG(tableName, sepateID);
				} else if (tableName.equals(Keys.HomeSubscriptionTable)) {
					return getSQLitePSubscription(tableName);
				} else if (tableName.equals(Keys.HomeEventTable)) {
					return getSQLitePEvent(tableName);
				} else if (tableName.equals(Keys.HomeFriendsTable)) {
					return getSQLitePFriends(tableName, sepateID);
				} else if (tableName.equals(Keys.HomeGamesTable)) {
					return getSQLitePGames(tableName, sepateID);
				} else if (tableName.equals(Keys.HomeGroupTable)) {
					return getSQLitePGroup(tableName, sepateID);
				} else if (tableName.equals(Keys.HomeWallRepliesTable)) {
					return getSQLitePWallReplies(tableName, sepateID);
				} else if (tableName.equals(Keys.HomeMsgRepliesTable)) {
					return getSQLitePMsgReplies(tableName, sepateID);
				} else if (tableName.equals(Keys.whoIsPlayingTable)) {
					return getSQLiteWhoIsPlaying(tableName, sepateID);
				}
			}
		}
		return lilDb.get(tableName);
	}

	/**
	 * @param tableName
	 *            the tableName to check
	 * @return if the databaseMAP contains the selected table
	 */
	public boolean checkTable(String tableName) {
		return lilDb.containsKey(tableName);
	}

	public String getScriptString(String tableName) {

		if (tableName.equals(Keys.commentsTable))
			return "getComments.php";
		if (tableName.equals(Keys.SearchGroupTable)
				|| tableName.equals(Keys.SearchEventTable)
				|| tableName.equals(Keys.SearchGameTable)
				|| tableName.equals(Keys.SearchFriendsTable)
				|| tableName.equals(Keys.SearchSubscriptionTable))
			return "getSearching.php";
		if (tableName.equals(Keys.gamesubNewsTAB)
				|| tableName.equals(Keys.companysubNewsTAB))
			return "getTabNews.php";
		else {
			return "getItem.php";
		}
	}

	public void getQuerry(String tableName) {
		String result = "";
		if (!lilDb.containsKey(tableName)) {
			String temp = url;
			url += getScriptString(tableName);
			JSONArray jArray = null;
			// http post
			try {
				httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(url);
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();

			} catch (Exception e) {
				Log.e("DataConnector ",
						" getQuerry() Error in http connection " + e.toString());
			}
			// convert response to string
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				result = sb.toString();

			} catch (Exception e) {
				Log.e("DataConnector ", "getQuerry() Error converting result "
						+ e.toString());
			}

			try {
				jArray = new JSONArray(result);
				jsonToArray(jArray, tableName);
			} catch (JSONException e) {
				Log.e("DataConnector " + tableName + " ", "Error parsing data "
						+ e.toString());
			}
			url = temp;
		}
	}

	public JSONArray getArrayFromQuerryWithPostVariable(String id,
			String tableName, String wallItem, int lastID) {
		String result = "";
		String temp = url;
		url += getScriptString(tableName);
		HttpEntity entity = null;
		JSONArray jArray = null;
		// http post
		try {
			httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			url = temp;
			pairs.add(new BasicNameValuePair(Keys.POSTID_PLAYER, id));
			pairs.add(new BasicNameValuePair(Keys.POSTTableName, tableName));
			pairs.add(new BasicNameValuePair(Keys.POSTWallItem, wallItem));
			pairs.add(new BasicNameValuePair(Keys.POSTLASTID, lastID + ""));

			httppost.setEntity(new UrlEncodedFormEntity(pairs));
			HttpResponse response = httpclient.execute(httppost);
			entity = response.getEntity();
			// is = entity.get;
		} catch (Exception e) {
			Log.e("log_tag HTML Conn",
					"Error in http connection " + e.toString());
		}

		try {
			// BufferedReader reader = new BufferedReader(new InputStreamReader(
			// is, "iso-8859-1"), 8);
			// StringBuilder sb = new StringBuilder();
			// String line = null;
			// while ((line = reader.readLine()) != null) {
			// sb.append(line + "\n");
			// }
			// is.close();

			// result = sb.toString();
			if (entity != null)
				result = EntityUtils.toString(entity);
		} catch (Exception e) {
			Log.e("DataConnector getWithPost2() ", "Error converting result "
					+ e.toString());
		}

		// parse json data
		try {
			if (result != null) {
				jArray = new JSONArray(result);
				jsonToArray(jArray, tableName);
				return jArray;
			}

		} catch (JSONException e) {
			Log.e("log_tag DB Parsing", "Error parsing data " + e.toString());
		}

		return null;
	}

	public ArrayList<NameValuePair> initializeData(int tableID, Bundle data) {
		switch (tableID) {
		case Keys.commentsID:
			ArrayList<NameValuePair> comment = new ArrayList<NameValuePair>();
			comment.add(new BasicNameValuePair(Keys.USERNAME, data
					.getString(Keys.USERNAME)));
			comment.add(new BasicNameValuePair(Keys.COMMENT, data
					.getString(Keys.COMMENT)));
			lilDb.get(Keys.commentsTable).add(data);
			return comment;
		case Keys.replysID:
			ArrayList<NameValuePair> reply = new ArrayList<NameValuePair>();
			reply.add(new BasicNameValuePair(Keys.USERNAME, data
					.getString(Keys.USERNAME)));
			reply.add(new BasicNameValuePair(Keys.COMMENT, data
					.getString(Keys.COMMENT)));
			return reply;
		case Keys.gamesID:
			ArrayList<NameValuePair> post = new ArrayList<NameValuePair>();
			post.add(new BasicNameValuePair(Keys.ID_OWNER, data
					.getString(Keys.ID_OWNER)));
			post.add(new BasicNameValuePair(Keys.OWNERTYPE, data
					.getString(Keys.OWNERTYPE)));
			return post;

		}
		return null;
	}

	public String returnUnserializedText(String text) {
		String result = "";
		SerializedPhpParser spp = new SerializedPhpParser(text);
		Object obj = spp.parse();

		if (!obj.toString().contains("content=")) {
			return obj.toString();
		} else {
			@SuppressWarnings("unchecked")
			Map<Object, Object> map = (Map<Object, Object>) obj;
			Set<Entry<Object, Object>> set = map.entrySet();
			Iterator<Entry<Object, Object>> itr = set.iterator();
			while (itr.hasNext()) {
				Map.Entry<Object, Object> ent = itr.next();
				if (ent.getKey().toString().equals("content")) {
					result = ent.getValue().toString();
				}

			}
		}
		return result;
	}

	@SuppressLint({ "SimpleDateFormat", "NewApi", "InlinedApi" })
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
						ContentValues map = new ContentValues();

						map.put(Keys.EventID_COMPANY, ID);
						int id = Integer.parseInt(ID);
						if (id > getLastIDCompanies())
							setLastIDCompanies(id);

						map.put(Keys.CompanyName, jsonArray.getJSONObject(i)
								.getString(Keys.CompanyName));
						map.put(Keys.CompanyEmployees,
								jsonArray.getJSONObject(i).getInt(
										Keys.CompanyEmployees)
										+ "");
						String imageUrl = jsonArray.getJSONObject(i).getString(
								Keys.CompanyImageURL);

						map.put(Keys.CompanyImageURL, imageUrl);
						map.put(Keys.CompanyAddress, jsonArray.getJSONObject(i)
								.getString(Keys.CompanyAddress));
						map.put(Keys.CompanyDesc, jsonArray.getJSONObject(i)
								.getString(Keys.CompanyDesc));
						String[] foundYear = jsonArray.getJSONObject(i)
								.getString(Keys.CompanyFounded).split("-");
						map.put(Keys.CompanyFounded, foundYear[0]);
						map.put(Keys.CompanyURL, jsonArray.getJSONObject(i)
								.getString(Keys.CompanyURL));
						map.put(Keys.CompanyCreatedTime, HelperClass
								.convertTime(Integer.parseInt(jsonArray
										.getJSONObject(i).getString(
												Keys.CompanyCreatedTime)),
										new SimpleDateFormat("dd/MM/yyyy")));
						map.put(Keys.CompanyOwnership,
								jsonArray.getJSONObject(i).getString(
										Keys.CompanyOwnership));
						map.put(Keys.CompanyType, jsonArray.getJSONObject(i)
								.getString(Keys.CompanyType));
						map.put(Keys.CompanyNewsCount,
								jsonArray.getJSONObject(i).getInt(
										Keys.CompanyNewsCount)
										+ "");
						map.put(Keys.CompanyEventCount, jsonArray
								.getJSONObject(i)
								.getInt(Keys.CompanyEventCount)
								+ "");
						map.put(Keys.CompanyGameCount,
								jsonArray.getJSONObject(i).getInt(
										Keys.CompanyGameCount)
										+ "");
						map.put(Keys.CompanySocialRating,
								jsonArray.getJSONObject(i).getString(
										Keys.CompanySocialRating));

						sql.insertWithOnConflict(Keys.companyTable, null, map,
								SQLiteDatabase.CONFLICT_REPLACE);
					}
				} catch (Exception e) {
					Log.e("Fetching Company", "Error Company" + e);
				}
			}
	}

	@SuppressLint({ "NewApi", "InlinedApi" })
	public void queryPlayerEvents(String playerID, Context v) {
		SQLiteDatabase sql = this.getWritableDatabase();
		JSONArray json = getArrayFromQuerryWithPostVariable(playerID,
				Keys.HomeEventTable, "0", getLastHomeIDEvents());

		// // Print the data to the console
		if (json != null)
			for (int i = 0; i < json.length(); i++) {

				try {
					ContentValues map = new ContentValues();
					String ID = json.getJSONObject(i).getInt(Keys.ID_EVENT)
							+ "";
					if (!checkRowExist(Keys.HomeEventTable, ID, playerID)) {
						int id = Integer.parseInt(ID);
						if (id > getLastHomeIDEvents())
							setLastHomeIDEvents(id);
						map.put(Keys.ID_EVENT, ID);
						map.put(Keys.EventID_COMPANY, json.getJSONObject(i)
								.getInt(Keys.EventID_COMPANY) + "");
						map.put(Keys.ID_GAME,
								json.getJSONObject(i).getInt(Keys.ID_GAME) + "");
						map.put(Keys.ID_PLAYER,
								json.getJSONObject(i).getInt(Keys.ID_PLAYER)
										+ "");
						map.put(Keys.ID_GROUP,
								json.getJSONObject(i).getInt(Keys.ID_GROUP)
										+ "");
						map.put(Keys.EventID_TEAM, json.getJSONObject(i)
								.getInt(Keys.EventID_TEAM) + "");
						map.put(Keys.EventIMAGEURL, json.getJSONObject(i)
								.getString(Keys.EventIMAGEURL));
						map.put(Keys.EventDescription, json.getJSONObject(i)
								.getString(Keys.EventDescription));
						map.put(Keys.EventDuration,
								HelperClass.durationConverter(
										json.getJSONObject(i).getString(
												Keys.EventDuration)
												+ "", v));
						map.put(Keys.EventHeadline, json.getJSONObject(i)
								.getString(Keys.EventHeadline));
						map.put(Keys.EventTime, HelperClass.convertTime(Integer
								.parseInt(json.getJSONObject(i).getString(
										Keys.EventTime)), dataTemplate));
						map.put(Keys.EventLocation, json.getJSONObject(i)
								.getString(Keys.EventLocation));
						map.put(Keys.EventInviteLevel, json.getJSONObject(i)
								.getString(Keys.EventInviteLevel));
						map.put(Keys.EventIsPublic, returnEventPrivacy(json
								.getJSONObject(i).getInt(Keys.EventIsPublic)));
						map.put(Keys.EventType, json.getJSONObject(i)
								.getString(Keys.EventType));
						map.put(Keys.EventIsExpired, json.getJSONObject(i)
								.getInt(Keys.EventIsExpired) + "");

						sql.insertWithOnConflict(Keys.HomeEventTable, null,
								map, SQLiteDatabase.CONFLICT_REPLACE);
					}
				} catch (Exception e) {
					Log.e("Fetching Events", "Error Events" + e);
				}
			}
	}

	@SuppressLint({ "SimpleDateFormat", "NewApi", "InlinedApi" })
	public void queryPlayerFriends(String playerID) {
		SQLiteDatabase sql = this.getWritableDatabase();
		JSONArray json = getArrayFromQuerryWithPostVariable(playerID,
				Keys.HomeFriendsTable, "0", 0);
		if (json != null) {
			for (int i = 0; i < json.length(); i++) {

				try {
					String ID = json.getJSONObject(i).getString(Keys.ID_PLAYER);
					if (!checkRowExist(Keys.HomeFriendsTable, ID, playerID)) {
						ContentValues map = new ContentValues();

						map.put(Keys.ID_PLAYER, ID);
						map.put(Keys.ID_OWNER,
								json.getJSONObject(i).getString(Keys.ID_OWNER));
						map.put(Keys.CITY,
								json.getJSONObject(i).getString(Keys.CITY));
						map.put(Keys.COUNTRY,
								json.getJSONObject(i).getString(Keys.COUNTRY));
						map.put(Keys.PLAYERNICKNAME, json.getJSONObject(i)
								.getString(Keys.PLAYERNICKNAME));
						map.put(Keys.Email,
								json.getJSONObject(i).getString(Keys.Email));
						String imageUrl = json.getJSONObject(i).getString(
								Keys.PLAYERAVATAR);

						map.put(Keys.PLAYERAVATAR, imageUrl);

						map.put(Keys.FirstName, json.getJSONObject(i)
								.getString(Keys.FirstName));
						map.put(Keys.LastName,
								json.getJSONObject(i).getString(Keys.LastName));
						map.put(Keys.Age,
								json.getJSONObject(i).getString(Keys.Age));
						sql.insertWithOnConflict(Keys.HomeFriendsTable, null,
								map, SQLiteDatabase.CONFLICT_REPLACE);
					}
				} catch (Exception e) {
					Log.e("Fetching Friends", "Error Friends" + e);
				}
			}
		}
		// sql.close();
	}

	@SuppressLint({ "SimpleDateFormat", "NewApi", "InlinedApi" })
	public void queryWhoIsPlaying(String playerID) {
		SQLiteDatabase sql = this.getWritableDatabase();
		JSONArray json = getArrayFromQuerryWithPostVariable(playerID,
				Keys.whoIsPlayingTable, "0", 0);
		if (json != null) {
			for (int i = 0; i < json.length(); i++) {

				try {
					String ID = json.getJSONObject(i).getString(Keys.ID_PLAYER);
					if (!checkRowExist(Keys.whoIsPlayingTable, playerID, ID)) {
						ContentValues map = new ContentValues();
						map.put(Keys.ID_PLAYER, ID);
						map.put(Keys.ID_GAME,
								json.getJSONObject(i).getString(Keys.ID_GAME));
						map.put(Keys.CITY,
								json.getJSONObject(i).getString(Keys.CITY));
						map.put(Keys.COUNTRY,
								json.getJSONObject(i).getString(Keys.COUNTRY));
						map.put(Keys.PLAYERNICKNAME, json.getJSONObject(i)
								.getString(Keys.PLAYERNICKNAME));
						map.put(Keys.Email,
								json.getJSONObject(i).getString(Keys.Email));
						String imageUrl = json.getJSONObject(i).getString(
								Keys.PLAYERAVATAR);
						map.put(Keys.PLAYERAVATAR, imageUrl);
						map.put(Keys.FirstName, json.getJSONObject(i)
								.getString(Keys.FirstName));
						map.put(Keys.LastName,
								json.getJSONObject(i).getString(Keys.LastName));
						map.put(Keys.Age,
								json.getJSONObject(i).getString(Keys.Age));
						sql.insertWithOnConflict(Keys.whoIsPlayingTable, null,
								map, SQLiteDatabase.CONFLICT_REPLACE);
					}
				} catch (Exception e) {
					Log.e("Fetching WhoISPlaying", "Error Friends" + e);
				}
			}
		}
	}

	@SuppressLint("NewApi")
	public void queryPlayerGames(String playerID) {
		SQLiteDatabase sql = this.getWritableDatabase();
		JSONArray json = getArrayFromQuerryWithPostVariable(playerID,
				Keys.HomeGamesTable, "0", 0);
		Set<String> gameTypes = new HashSet<String>();
		// // Print the data to the console
		if (json != null)
			for (int i = 0; i < json.length(); i++) {
				try {
					String ID = json.getJSONObject(i).getInt(Keys.ID_GAME) + "";
					if (!checkRowExist(Keys.HomeGamesTable, ID, playerID)) {
						ContentValues m = new ContentValues();

						m.put(Keys.ID_GAME, ID);
						m.put(Keys.RATING,
								json.getJSONObject(i).getString(Keys.RATING));
						m.put(Keys.GAMEESRB,
								json.getJSONObject(i).getString(Keys.GAMEESRB));
						m.put(Keys.GAMEURL,
								json.getJSONObject(i).getString(Keys.GAMEURL));
						m.put(Keys.GAMEPLAYERSCOUNT, json.getJSONObject(i)
								.getString(Keys.GAMEPLAYERSCOUNT));
						m.put(Keys.GAMEDATE,
								json.getJSONObject(i).getString(Keys.GAMEDATE));
						m.put(Keys.ID_PLAYER,
								json.getJSONObject(i).getInt(Keys.ID_PLAYER)
										+ "");
						m.put(Keys.GameComments, json.getJSONObject(i)
								.getString(Keys.GameComments));
						m.put(Keys.GAMENAME,
								json.getJSONObject(i).getString(Keys.GAMENAME));
						m.put(Keys.GAMEDESC,
								json.getJSONObject(i).getString(Keys.GAMEDESC)
										+ "");
						m.put(Keys.GameID_GAMETYPE, json.getJSONObject(i)
								.getInt(Keys.GameID_GAMETYPE) + "");
						String gameType = json.getJSONObject(i).getString(
								Keys.GAMETYPE)
								+ "";
						m.put(Keys.GAMETYPE, gameType);
						m.put(Keys.GAMETYPENAME, json.getJSONObject(i)
								.getString(Keys.GAMETYPENAME) + "");
						m.put(Keys.GameisPlaying,
								json.getJSONObject(i)
										.getInt(Keys.GameisPlaying) + "");
						m.put(Keys.GamesisSubscribed, json.getJSONObject(i)
								.getInt(Keys.GamesisSubscribed) + "");
						m.put(Keys.GamePostCount,
								json.getJSONObject(i)
										.getInt(Keys.GamePostCount) + "");
						m.put(Keys.GamesSubscriptionTime, json.getJSONObject(i)
								.getString(Keys.GamesSubscriptionTime));

						m.put(Keys.GAMEPLATFORM, json.getJSONObject(i)
								.getString(Keys.GAMEPLATFORM));
						m.put(Keys.GAMECompanyDistributor,
								json.getJSONObject(i).getString(
										Keys.GAMECompanyDistributor));
						m.put(Keys.CompanyFounded, json.getJSONObject(i)
								.getString(Keys.CompanyFounded));
						m.put(Keys.CompanyName, json.getJSONObject(i)
								.getString(Keys.GAMECompanyDeveloper));
						String imageUrl = json.getJSONObject(i).getString(
								Keys.EventIMAGEURL);
						m.put(Keys.EventIMAGEURL, imageUrl);

						gameTypes.add(json.getJSONObject(i).getString(
								Keys.GAMETYPE));

						sql.insertWithOnConflict(Keys.HomeGamesTable, null, m,
								SQLiteDatabase.CONFLICT_REPLACE);
					}
				} catch (Exception e) {
					Log.e("Fetching Games", "Error Games" + e);
				}
			}
	}

	@SuppressLint({ "SimpleDateFormat", "NewApi", "InlinedApi" })
	public void queryPlayerGroup(String playerID) {
		SQLiteDatabase sql = this.getWritableDatabase();
		JSONArray json = getArrayFromQuerryWithPostVariable(playerID,
				Keys.HomeGroupTable, "0", 0);
		// // Print the data to the console
		if (json != null)
			for (int i = 0; i < json.length(); i++) {
				try {
					String ID = json.getJSONObject(i).getInt(Keys.ID_GROUP)
							+ "";
					if (!checkRowExist(Keys.HomeGroupTable, ID, playerID)) {
						ContentValues m = new ContentValues();

						m.put(Keys.ID_GROUP, ID);
						m.put(Keys.ID_PLAYER,
								json.getJSONObject(i).getInt(Keys.ID_PLAYER)
										+ "");
						m.put(Keys.GROUPNAME,
								json.getJSONObject(i).getString(Keys.GROUPNAME));
						m.put(Keys.GROUPDESC,
								json.getJSONObject(i).getString(Keys.GROUPDESC));
						m.put(Keys.GROUPTYPE,
								json.getJSONObject(i).getString(Keys.GROUPTYPE));
						m.put(Keys.GROUPTYPE2,
								json.getJSONObject(i)
										.getString(Keys.GROUPTYPE2));
						m.put(Keys.GAMENAME,
								json.getJSONObject(i).getString(Keys.GAMENAME));
						m.put(Keys.GroupMemberCount, json.getJSONObject(i)
								.getString(Keys.GroupMemberCount));
						m.put(Keys.EventIMAGEURL, json.getJSONObject(i)
								.getString(Keys.EventIMAGEURL));
						m.put(Keys.GROUPDATE, HelperClass.convertTime(Integer
								.parseInt(json.getJSONObject(i).getString(
										Keys.GROUPDATE)), new SimpleDateFormat(
								"dd/MM/yyyy", Locale.getDefault())));
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

	@SuppressLint({ "SimpleDateFormat", "NewApi", "InlinedApi" })
	public void queryPlayerMessages(String playerID) {
		SQLiteDatabase sql = this.getWritableDatabase();
		JSONArray json = getArrayFromQuerryWithPostVariable(playerID,
				Keys.HomeMsgTable, "0", getLastIDHomeMSg());
		// // Print the data to the console
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

						map.put(Keys.ID_MESSAGE, ID);
						map.put(Keys.MessageID_CONVERSATION,
								json.getJSONObject(i).getInt(
										Keys.MessageID_CONVERSATION)
										+ "");
						map.put(Keys.ID_PLAYER,
								json.getJSONObject(i).getInt(Keys.ID_PLAYER)
										+ "");
						map.put(Keys.PLAYERNICKNAME, json.getJSONObject(i)
								.getString(Keys.PLAYERNICKNAME));
						map.put(Keys.PLAYERAVATAR, json.getJSONObject(i)
								.getString(Keys.PLAYERAVATAR));

						map.put(Keys.MessageText, returnUnserializedText(json
								.getJSONObject(i).getString(Keys.MessageText)));
						map.put(Keys.MessageTime, HelperClass.convertTime(
								Integer.parseInt(json.getJSONObject(i)
										.getString(Keys.MessageTime)),
								dataTemplate));

						sql.insertWithOnConflict(Keys.HomeMsgTable, null, map,
								SQLiteDatabase.CONFLICT_REPLACE);
					}
				} catch (Exception e) {
					Log.e("Fetching Msg", "Error Msg" + e);
				}
			}
	}

	@SuppressLint({ "SimpleDateFormat", "NewApi", "InlinedApi" })
	public void queryMiniIds() {

		JSONArray json = getArrayFromQuerryWithPostVariable("0", "miniIDs",
				"0", 0);
		// // Print the data to the console
		if (json != null)
			for (int i = 0; i < json.length(); i++) {
				try {
					setMinNewsID(json.getJSONObject(i).getInt(Keys.newsTable));
					setMinCompanyID(json.getJSONObject(i).getInt(
							Keys.companyTable));
					setMinGameID(json.getJSONObject(i).getInt(Keys.gamesTable));
					setMinGroupID(json.getJSONObject(i)
							.getInt(Keys.groupsTable));
				} catch (Exception e) {
				}
			}
	}

	@SuppressLint({ "SimpleDateFormat", "NewApi", "InlinedApi" })
	public void queryPlayerSubscription(String playerID) {
		SQLiteDatabase sql = this.getWritableDatabase();
		JSONArray json = getArrayFromQuerryWithPostVariable(playerID,
				Keys.HomeSubscriptionTable, "0", getLastIDHomeSubs());
		// // Print the data to the console
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
								dataTemplate));

						sql.insertWithOnConflict(Keys.HomeSubscriptionTable,
								null, m, SQLiteDatabase.CONFLICT_REPLACE);
					}
				} catch (Exception e) {
					Log.e("Fetching Subscription", "Error Subscription " + e);
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

	@SuppressLint({ "SimpleDateFormat", "NewApi", "InlinedApi" })
	public void queryPlayerWall(String playerID, String ownerType) {
		SQLiteDatabase sql = this.getWritableDatabase();
		System.out.println("Wall PlayerID " + playerID);
		System.out.println("Wall ownerType " + ownerType);
		JSONArray json = getArrayFromQuerryWithPostVariable(playerID,
				Keys.HomeWallTable, ownerType, 0);
		// // Print the data to the console

		if (json != null)
			for (int i = 0; i < json.length(); i++) {
				//
				try {
					String ID = json.getJSONObject(i).getInt(Keys.ID_WALLITEM)
							+ "";
					System.out.println("Wall wallitem " + ID);

					ContentValues m = new ContentValues();

					m.put(Keys.WallPosterDisplayName, json.getJSONObject(i)
							.getString(Keys.WallPosterDisplayName) + "");
					m.put(Keys.ID_WALLITEM, ID);
					m.put(Keys.ID_OWNER,
							json.getJSONObject(i).getInt(Keys.ID_OWNER) + "");
					m.put(Keys.ItemType,
							json.getJSONObject(i).getString(Keys.ItemType));
					m.put(Keys.WallLastActivityTime, json.getJSONObject(i)
							.getString(Keys.WallLastActivityTime));
					m.put(Keys.WallMessage, returnUnserializedText(json
							.getJSONObject(i).getString(Keys.WallMessage)));
					m.put(Keys.WallOwnerType,
							json.getJSONObject(i).getString(Keys.WallOwnerType));
					m.put(Keys.WallPostingTime, json.getJSONObject(i)
							.getString(Keys.WallPostingTime));

					String imageUrl = json.getJSONObject(i).getString(
							Keys.PLAYERAVATAR);

					m.put(Keys.PLAYERAVATAR, imageUrl);
					sql.insertWithOnConflict(Keys.HomeWallTable, null, m,
							SQLiteDatabase.CONFLICT_REPLACE);
				} catch (Exception e) {
					Log.e("DataConnector ", " querryPlayerWall() Error " + e);
				}
			}
	}

	@SuppressLint({ "SimpleDateFormat", "NewApi", "InlinedApi" })
	public void queryPlayerWallReplices(String wallitem, String playerID) {
		SQLiteDatabase sql = this.getWritableDatabase();
		JSONArray json = getArrayFromQuerryWithPostVariable(playerID,
				Keys.HomeWallRepliesTable, wallitem, 0);
		System.out.println("WallRep PlayerID " + playerID);
		System.out.println("WallRep ID_WALLITEM " + wallitem);
		if (json != null) {
			for (int i = 0; i < json.length(); i++) {
				try {
					String ID = json.getJSONObject(i).getInt(Keys.ID_WALLITEM)
							+ "";

					ContentValues m = new ContentValues();
					m.put(Keys.WallPosterDisplayName, json.getJSONObject(i)
							.getString(Keys.WallPosterDisplayName) + "");
					m.put(Keys.ID_WALLITEM, ID);
					m.put(Keys.ID_ORGOWNER,
							json.getJSONObject(i).getInt(Keys.ID_ORGOWNER) + "");
					m.put(Keys.PLAYERAVATAR,
							json.getJSONObject(i).getString(Keys.PLAYERAVATAR)
									+ "");
					m.put(Keys.WallLastActivityTime, HelperClass.convertTime(
							Integer.parseInt(json.getJSONObject(i).getString(
									Keys.WallLastActivityTime)), dataTemplate));
					m.put(Keys.WallMessage,
							json.getJSONObject(i).getString(Keys.WallMessage));
					m.put(Keys.WallOwnerType,
							json.getJSONObject(i).getString(Keys.WallOwnerType));
					m.put(Keys.WallPostingTime, HelperClass.convertTime(
							Integer.parseInt(json.getJSONObject(i).getString(
									Keys.WallPostingTime)), dataTemplate));

					sql.insertWithOnConflict(Keys.HomeWallRepliesTable, null,
							m, SQLiteDatabase.CONFLICT_REPLACE);

				} catch (Exception e) {
					Log.e("Fetching Wall Replies",
							"Fetching WallReplies: Error" + e);
				}
			}
		}
	}

	@SuppressLint({ "SimpleDateFormat", "NewApi", "InlinedApi" })
	public void queryPlayerMSGReplices(String wallitem, String playerID) {
		SQLiteDatabase sql = this.getWritableDatabase();
		JSONArray json = getArrayFromQuerryWithPostVariable(playerID,
				Keys.HomeMsgRepliesTable, wallitem, 0);

		// // Print the data to the console
		if (json != null)
			for (int i = 0; i < json.length(); i++) {
				//
				try {
					String ID = json.getJSONObject(i).getInt(Keys.ID_MESSAGE)
							+ "";
					if (!checkRowExist(Keys.HomeMsgRepliesTable, wallitem,
							playerID)) {
						ContentValues map = new ContentValues();

						map.put(Keys.ID_MESSAGE, ID);
						map.put(Keys.MessageID_CONVERSATION,
								json.getJSONObject(i).getInt(
										Keys.MessageID_CONVERSATION)
										+ "");
						map.put(Keys.PLAYERNICKNAME, json.getJSONObject(i)
								.getString(Keys.PLAYERNICKNAME));
						map.put(Keys.PLAYERAVATAR, json.getJSONObject(i)
								.getString(Keys.PLAYERAVATAR));

						map.put(Keys.MessageText, returnUnserializedText(json
								.getJSONObject(i).getString(Keys.MessageText)));
						map.put(Keys.MessageTime, HelperClass.convertTime(
								Integer.parseInt(json.getJSONObject(i)
										.getString(Keys.MessageTime)),
								dataTemplate));
						sql.insertWithOnConflict(Keys.HomeMsgRepliesTable,
								null, map, SQLiteDatabase.CONFLICT_REPLACE);
					}
				} catch (Exception e) {
					Log.e("Fetching MSG Replies", "Fetching MSGReplies Error"
							+ e);
				}
			}
	}

	// -----------------------------------------------------------------

	@SuppressLint({ "SimpleDateFormat", "NewApi", "InlinedApi" })
	public void queryPlayerInfo(String playerID) {
		SQLiteDatabase sql = this.getWritableDatabase();
		json = getArrayFromQuerryWithPostVariable(playerID, Keys.PlayerTable,
				"0", 0);

		// Strong limit(10) of DB for fetching big table
		// // Print the data to the console
		if (json != null)
			for (int i = 0; i < json.length(); i++) {
				try {
					ContentValues map = new ContentValues();
					String ID = json.getJSONObject(i).getString(Keys.ID_PLAYER);

					map.put(Keys.CITY,
							json.getJSONObject(i).getString(Keys.CITY));
					map.put(Keys.COUNTRY,
							json.getJSONObject(i).getString(Keys.COUNTRY));
					map.put(Keys.PLAYERNICKNAME, json.getJSONObject(i)
							.getString(Keys.PLAYERNICKNAME));

					String imageUrl = json.getJSONObject(i).getString(
							Keys.PLAYERAVATAR);
					map.put(Keys.PLAYERAVATAR, imageUrl);
					map.put(Keys.FirstName,
							json.getJSONObject(i).getString(Keys.FirstName));
					map.put(Keys.LastName,
							json.getJSONObject(i).getString(Keys.LastName));
					map.put(Keys.LastName,
							json.getJSONObject(i).getString(Keys.LastName));
					map.put(Keys.Age, json.getJSONObject(i).getString(Keys.Age));
					map.put(Keys.Email,
							json.getJSONObject(i).getString(Keys.Email));
					if (!checkRowExist(Keys.PlayerTable, ID, "")) {
						map.put(Keys.ID_PLAYER, ID);
						sql.insertWithOnConflict(Keys.PlayerTable, null, map,
								SQLiteDatabase.CONFLICT_REPLACE);
					} else {
						sql.updateWithOnConflict(Keys.PlayerTable, map,
								"ID_PLAYER=" + ID, null,
								SQLiteDatabase.CONFLICT_REPLACE);
					}

				} catch (Exception e) {
					Log.e("Fetching Info", "Error " + e);
				}
			}
	}

	public View populatePlayerGeneralInfo(View v, String nameT, String playerID) {
		setCurrentPlayer(getPlayer(playerID));

		if (v != null) {
			TextView txPlName = (TextView) v.findViewById(R.id.txPlName);
			TextView txPlNick = (TextView) v.findViewById(R.id.txPlNick);
			TextView txPlAge = (TextView) v.findViewById(R.id.txPlAge);
			TextView txPlCountry = (TextView) v.findViewById(R.id.txPlCountry);
			QuickContactBadge playerIcon = (QuickContactBadge) v
					.findViewById(R.id.quickContactBadge1);
			if (txPlName != null)
				txPlName.setText("Name : " + currentPlayer.get(Keys.FirstName)
						+ " , " + currentPlayer.get(Keys.LastName));

			if (txPlNick != null)
				txPlNick.setText("Nick : "
						+ currentPlayer.get(Keys.PLAYERNICKNAME));

			if (txPlAge != null)
				txPlAge.setText("Age : "
						+ HelperClass.convertToAge(currentPlayer
								.getString(Keys.Age)));

			if (txPlCountry != null)
				txPlCountry.setText("Country: "
						+ currentPlayer.get(Keys.COUNTRY));
			if (playerIcon != null) {
				String imageUrl = currentPlayer.getString(Keys.PLAYERAVATAR);
				playerIcon.setTag(imageUrl);
				new LoadImage(imageUrl, playerIcon, "players")
						.execute(playerIcon);

			}
		}
		return v;
	}

	public void savePersonalInfo(Bundle args, EditText first, EditText last,
			EditText nick, EditText city, EditText country, EditText email,
			Context context) {
		String temp = url;
		url += "updatePlayer.php";

		try {
			httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			url = temp;
			pairs.add(new BasicNameValuePair(Keys.ID_PLAYER, args
					.getString(Keys.ID_PLAYER)));
			pairs.add(new BasicNameValuePair(Keys.FirstName, first.getText()
					.toString().trim()));
			pairs.add(new BasicNameValuePair(Keys.LastName, last.getText()
					.toString().trim()));
			pairs.add(new BasicNameValuePair(Keys.CITY, city.getText()
					.toString().trim()));
			pairs.add(new BasicNameValuePair(Keys.COUNTRY, country.getText()
					.toString().trim()));
			pairs.add(new BasicNameValuePair(Keys.PLAYERNICKNAME, nick
					.getText().toString().trim()));
			pairs.add(new BasicNameValuePair(Keys.Email, email.getText()
					.toString().trim()));

			httppost.setEntity(new UrlEncodedFormEntity(pairs));
			httpclient.execute(httppost);
			Toast.makeText(context, "Your profile was updated successfully",
					Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Log.e("log_tag HTML Conn",
					"Error in savePersonalInfo http connection " + e.toString());
		}
	}

	/**
	 * 
	 * @param msg
	 * @param ownerType
	 *            Group,Player,Company
	 * @param postName
	 *            Name of the group,company,player(Nick)
	 * @param player
	 * @param wallOwner
	 *            ID of the object
	 */
	public void insertComment(String msg, String ownerType, String postName,
			String wallOwner) {
		String temp = url;
		url += "insertComment.php";

		try {
			httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			url = temp;
			pairs.add(new BasicNameValuePair(Keys.ID_PLAYER, wallOwner));
			pairs.add(new BasicNameValuePair(Keys.OWNERTYPE, ownerType));
			if (getCurrentPlayer() != null) {
				pairs.add(new BasicNameValuePair("CurrentObjID",
						getCurrentPlayer().getString(Keys.ID_PLAYER)));
			} else {
				pairs.add(new BasicNameValuePair("CurrentObjID", "0"));
			}
			pairs.add(new BasicNameValuePair(Keys.WallPosterDisplayName,
					postName));
			String now = DateFormat.getDateInstance().format(new Date());
			pairs.add(new BasicNameValuePair(Keys.WallPostingTime, now));
			pairs.add(new BasicNameValuePair(Keys.WallLastActivityTime, now));
			pairs.add(new BasicNameValuePair(Keys.WallMessage, msg));

			httppost.setEntity(new UrlEncodedFormEntity(pairs));
			httpclient.execute(httppost);

		} catch (Exception e) {
			Log.e("log_tag HTML Conn",
					"Error in insertComment http connection " + e.toString());
		}
	}

	public ArrayList<Bundle> queryPlayerFriendsSearch(CharSequence search) {
		searchArray = new ArrayList<Bundle>();
		JSONArray json = getArrayFromQuerryWithPostVariable(Keys.TEMPLAYERID,
				Keys.SearchFriendsTable, search.toString(), 0);

		// // Print the data to the console
		if (json != null) {
			for (int i = 0; i < json.length(); i++) {
				try {
					Bundle map = new Bundle();
					map.putString(Keys.ID_PLAYER, json.getJSONObject(i)
							.getString(Keys.ID_PLAYER));
					map.putString(Keys.CITY,
							json.getJSONObject(i).getString(Keys.CITY));
					map.putString(Keys.COUNTRY, json.getJSONObject(i)
							.getString(Keys.COUNTRY));
					map.putString(Keys.PLAYERNICKNAME, json.getJSONObject(i)
							.getString(Keys.PLAYERNICKNAME));
					map.putString(Keys.Email,
							json.getJSONObject(i).getString(Keys.Email));
					map.putString(Keys.PLAYERAVATAR, json.getJSONObject(i)
							.getString(Keys.PLAYERAVATAR));
					map.putString(Keys.FirstName, json.getJSONObject(i)
							.getString(Keys.FirstName));
					map.putString(Keys.LastName, json.getJSONObject(i)
							.getString(Keys.LastName));
					map.putString(Keys.Age,
							json.getJSONObject(i).getString(Keys.Age));

					searchArray.add(map);
				} catch (Exception e) {
					Log.e("Fetching Friends Search", "Error Friends Search" + e);
				}
			}
			return searchArray;
		} else
			return null;
	}

	public String returnEventPrivacy(int index) {
		if (index == 0) {
			return "Private";
		} else {
			return "Public";
		}
	}

	private String[] gametypes;

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
					returnBundle = new Bundle();
					returnBundle.putString(Keys.ID_PLAYER, cursor
							.getString(cursor.getColumnIndex(Keys.ID_PLAYER)));
					returnBundle.putString(Keys.CITY,
							cursor.getString(cursor.getColumnIndex(Keys.CITY)));
					returnBundle.putString(Keys.COUNTRY, cursor
							.getString(cursor.getColumnIndex(Keys.COUNTRY)));
					returnBundle.putString(Keys.PLAYERNICKNAME, cursor
							.getString(cursor
									.getColumnIndex(Keys.PLAYERNICKNAME)));
					returnBundle.putString(Keys.PLAYERAVATAR,
							cursor.getString(cursor
									.getColumnIndex(Keys.PLAYERAVATAR)));
					returnBundle.putString(Keys.FirstName, cursor
							.getString(cursor.getColumnIndex(Keys.FirstName)));
					returnBundle.putString(Keys.LastName, cursor
							.getString(cursor.getColumnIndex(Keys.LastName)));
					returnBundle.putString(Keys.LastName, cursor
							.getString(cursor.getColumnIndex(Keys.LastName)));
					returnBundle.putString(Keys.Age,
							cursor.getString(cursor.getColumnIndex(Keys.Age)));
					returnBundle
							.putString(Keys.Email, cursor.getString(cursor
									.getColumnIndex(Keys.Email)));
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
		return returnBundle;
	}

	public ArrayList<Bundle> getArrayChildren() {
		return arrayChildren;
	}

	public ArrayList<Bundle> getSearchArray() {
		return searchArray;
	}

	public String[] getGametypes() {
		return gametypes;
	}

	@SuppressWarnings("unused")
	private void setGametypes(String[] gametypess) {
		gametypes = gametypess;
	}

	// STIll NEEDED TO BE FINISHED
	public ArrayList<UserComment> getComments(String ownerId, String ownerType) {
		queryPlayerWall(ownerId, ownerType);

		ArrayList<UserComment> comments = new ArrayList<UserComment>();
		ArrayList<CommentInfo> dummy1;

		ArrayList<Bundle> list = getSQLitePWall(Keys.HomeWallTable, ownerId);
		/*
		 * if (list.size() > 0) for (Bundle bundle : list) {
		 * queryPlayerWallReplices(bundle.getString(Keys.ID_WALLITEM), ownerId);
		 * ArrayList<Bundle> dum1 = getSQLitePWallReplies(
		 * Keys.HomeWallRepliesTable, bundle.getString(Keys.ID_WALLITEM));
		 * 
		 * dummy1 = new ArrayList<CommentInfo>(); if (dum1.size() > 0) { for
		 * (Bundle bundle2 : dum1) { String date =
		 * bundle2.getString(Keys.WallPostingTime); dummy1.add(new
		 * CommentInfo(bundle2 .getString(Keys.WallPosterDisplayName), bundle2
		 * .getString(Keys.WallMessage), date)); } } String date =
		 * HelperClass.convertTime(Integer.parseInt(bundle
		 * .getString(Keys.WallPostingTime)), dataTemplate); comments.add(new
		 * UserComment(new CommentInfo(bundle
		 * .getString(Keys.WallPosterDisplayName), bundle
		 * .getString(Keys.WallMessage), date), dummy1)); }
		 */
		for (Bundle bundle : list) {
			String date = HelperClass.convertTime(
					Integer.parseInt(bundle.getString(Keys.WallPostingTime)),
					dataTemplate);
			comments.add(new UserComment(new CommentInfo(bundle
					.getString(Keys.WallPosterDisplayName), bundle
					.getString(Keys.WallMessage), bundle
					.getString(Keys.PLAYERAVATAR), date),
					new ArrayList<CommentInfo>()));
		}
		return comments;
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
				+ Keys.GameisPlaying + " INTEGER," + Keys.GamesisSubscribed
				+ " INTEGER," + Keys.GamePostCount + " INTEGER,"
				+ Keys.GamesSubscriptionTime + " TEXT," + Keys.GAMEPLATFORM
				+ " TEXT, " + Keys.GAMECompanyDistributor + " TEXT, "
				+ Keys.CompanyFounded + " TEXT, " + Keys.CompanyName + " TEXT"
				+ ");";
		db.execSQL(cREATE_HomeGamesTable);

		String cREATE_groupsTable = "CREATE TABLE " + Keys.groupsTable + " ("
				+ Keys.ID_GROUP + " INTEGER PRIMARY KEY, " + Keys.GROUPNAME
				+ " TEXT, " + Keys.GROUPTYPE + " TEXT, " + Keys.EventIMAGEURL
				+ " TEXT, " + Keys.GROUPDESC + " TEXT, " + Keys.GROUPTYPE2
				+ " TEXT, " + Keys.GroupMemberCount + " TEXT, "
				+ Keys.GROUPDATE + " TEXT, " + Keys.GruopCreatorName
				+ " TEXT);";
		db.execSQL(cREATE_groupsTable);

		String cREATE_newsTable = "CREATE TABLE " + Keys.newsTable + " ("
				+ Keys.NEWSCOLID_NEWS + " INTEGER PRIMARY KEY,"
				+ Keys.NEWSCOLNEWSTEXT + " TEXT," + Keys.NEWSCOLINTROTEXT
				+ " TEXT," + Keys.NEWSCOLPOSTINGTIME + " TEXT,"
				+ Keys.NEWSCOLHEADLINE + " TEXT," + Keys.NEWSCOLIMAGE
				+ " TEXT," + Keys.Author + " TEXT);";
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
				+ " INTEGER PRIMARY KEY," + Keys.ID_OWNER + " INTEGER,"
				+ Keys.CITY + " TEXT," + Keys.COUNTRY + " TEXT,"
				+ Keys.PLAYERNICKNAME + " TEXT," + Keys.Email + " TEXT,"
				+ Keys.PLAYERAVATAR + " TEXT," + Keys.FirstName + " TEXT,"
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
				+ Keys.ID_PLAYER + " INTEGER," + Keys.GROUPNAME + " TEXT,"
				+ Keys.GROUPDESC + " TEXT," + Keys.GROUPTYPE + " TEXT,"
				+ Keys.GROUPTYPE2 + " TEXT," + Keys.GAMENAME + " TEXT,"
				+ Keys.GroupMemberCount + " TEXT," + Keys.EventIMAGEURL
				+ " TEXT," + Keys.GROUPDATE + " TEXT," + Keys.GruopCreatorName
				+ " TEXT," + Keys.PLAYERNICKNAME + " TEXT);";
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
				+ Keys.CompanyCreatedTime + " TEXT," + Keys.CompanyOwnership
				+ " TEXT," + Keys.CompanyType + " TEXT,"
				+ Keys.CompanyNewsCount + " INTEGER," + Keys.CompanyEventCount
				+ " INTEGER," + Keys.CompanyGameCount + " INTEGER,"
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
		db.execSQL("DROP TABLE IF EXISTS " + Keys.HomeMsgTable);
		db.execSQL("DROP TABLE IF EXISTS " + Keys.HomeSubscriptionTable);
		onCreate(db);
	}

	// Still have to work on the Querry MAYBE DELETE
	public boolean checkUsernameAndPassword(String userName, String passWord) {
		SQLiteDatabase sql = this.getReadableDatabase();
		sql = getReadableDatabase();
		String selectQuery = "Select * FROM " + Keys.gamesTable;

		ArrayList<String> list = null;
		Cursor cursor = sql.rawQuery(selectQuery, null);
		if (cursor != null) {
			list = new ArrayList<String>();
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				do {
					list.add(cursor.getString(cursor
							.getColumnIndex(Keys.USERNAME)));
				} while (cursor.moveToNext());
			}
		}
		cursor.close();
		if (list != null)
			for (int i = 0; i <= list.size(); i++)
				if (list.get(i).equals(passWord))
					return true;
		return false;
	}

	public boolean checkConnection() {
		if (!connStatus)
			new CheckConnectionTask().execute();
		return connStatus;
	}

	public boolean registerPlayerMobileQuery(String nickname, EditText email,
			String password) {
		String temp = url;
		url += "chekRegisterLogin.php";
		String result = "";
		HttpEntity entity = null;

		// http post
		try {
			httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			url = temp;
			String[] ar;
			String _email = email.getText().toString();

			pairs.add(new BasicNameValuePair("checkCondition", "Register"));
			if (nickname.contains(" ")) {
				ar = nickname.split(" ");
				pairs.add(new BasicNameValuePair("FirstName", ar[0]));
				pairs.add(new BasicNameValuePair("LastName", ar[1]));
			} else {
				pairs.add(new BasicNameValuePair("FirstName", nickname));
				pairs.add(new BasicNameValuePair("LastName", ""));
			}

			pairs.add(new BasicNameValuePair("password", password));
			pairs.add(new BasicNameValuePair("email", _email));
			httppost.setEntity(new UrlEncodedFormEntity(pairs));
			HttpResponse response = httpclient.execute(httppost);
			entity = response.getEntity();
		} catch (Exception e) {
			Log.e("log_tag HTML Conn",
					"Error in registerPlayerMobile http connection "
							+ e.toString());
		}
		try {
			result = EntityUtils.toString(entity);
		} catch (Exception e) {
			Log.e("DataConnector checkLogin() ",
					"Error converting result " + e.toString());
		}
		if (result.contains("Duplicate entry")) {
			email.setError("Duplicate Email. This email already exists.");
			return false;
		} else {
			email.setError(null);
			return true;
		}
	}

	public boolean checkLogin(String email, String pass, SharedPreferences pref) {
		String result = "";
		String temp = url;
		url += "chekRegisterLogin.php";
		HttpEntity entity = null;
		JSONArray jArray = null;
		boolean results = false;
		// http post
		try {
			httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			url = temp;
			pairs.add(new BasicNameValuePair("checkCondition", "Login"));
			pairs.add(new BasicNameValuePair("FirstName", ""));
			pairs.add(new BasicNameValuePair("LastName", ""));
			pairs.add(new BasicNameValuePair("password", pass));
			pairs.add(new BasicNameValuePair("email", email));

			httppost.setEntity(new UrlEncodedFormEntity(pairs));
			HttpResponse response = httpclient.execute(httppost);
			entity = response.getEntity();
		} catch (Exception e) {
			Log.e("log_tag HTML Conn", "Error in checkLogin http connection "
					+ e.toString());
		}

		try {
			result = EntityUtils.toString(entity);
		} catch (Exception e) {
			Log.e("DataConnector checkLogin() ",
					"Error converting result " + e.toString());
		}
		try {
			if (result != null) {
				jArray = new JSONArray(result);
			}
		} catch (Exception e) {
		}

		if (jArray != null) {
			try {
				Keys.TEMPLAYERID = jArray.getJSONObject(0).getString(
						Keys.ID_PLAYER);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			SharedPreferences.Editor editPref = pref.edit();
			editPref.putString(Keys.USERNAME, email);
			editPref.putString(Keys.ID_PLAYER, Keys.TEMPLAYERID);
			editPref.putString(Keys.Password, pass);
			editPref.putBoolean(Keys.ActiveSession, true);
			editPref.commit();
			results = true;
		} else {
			results = false;
		}
		return results;
	}

	// --------------------------------------------------------
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

	@SuppressLint({ "InlinedApi", "NewApi" })
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

	@SuppressLint("NewApi")
	public void setMinGameID(int minGameID) {
		SQLiteDatabase sql = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(Keys.POSTLASTID, minGameID);
		sql.updateWithOnConflict(Keys.LASTIDTABLE, value,
				"COLNAME='MinGameID'", null, SQLiteDatabase.CONFLICT_REPLACE);
	}

	@SuppressLint({ "InlinedApi", "NewApi" })
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

	@SuppressLint("NewApi")
	public void setMinGroupID(int MinGroupID) {
		SQLiteDatabase sql = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(Keys.POSTLASTID, MinGroupID);
		sql.updateWithOnConflict(Keys.LASTIDTABLE, value,
				"COLNAME='MinGroupID'", null, SQLiteDatabase.CONFLICT_REPLACE);
	}

	@SuppressLint({ "InlinedApi", "NewApi" })
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

	@SuppressLint("NewApi")
	public void setMinCompanyID(int MinCompanyID) {
		SQLiteDatabase sql = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(Keys.POSTLASTID, MinCompanyID);
		sql.updateWithOnConflict(Keys.LASTIDTABLE, value,
				"COLNAME='MinGroupID'", null, SQLiteDatabase.CONFLICT_REPLACE);
	}

	@SuppressLint({ "InlinedApi", "NewApi" })
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

	@SuppressLint("NewApi")
	public void setMinNewsID(int minNewsID) {
		SQLiteDatabase sql = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(Keys.POSTLASTID, minNewsID);
		sql.updateWithOnConflict(Keys.LASTIDTABLE, value,
				"COLNAME='MinNewsID'", null, SQLiteDatabase.CONFLICT_REPLACE);
	}

	@SuppressLint({ "NewApi", "InlinedApi" })
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

	@SuppressLint({ "NewApi", "InlinedApi" })
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

	@SuppressLint({ "NewApi", "InlinedApi" })
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

	@SuppressLint({ "NewApi", "InlinedApi" })
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

	@SuppressLint({ "NewApi", "InlinedApi" })
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

	@SuppressLint({ "NewApi", "InlinedApi" })
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

	@SuppressLint({ "NewApi", "InlinedApi" })
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

	@SuppressLint({ "NewApi", "InlinedApi" })
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

	@SuppressLint({ "NewApi", "InlinedApi" })
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

	@SuppressLint({ "NewApi", "InlinedApi" })
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

	@SuppressLint({ "NewApi", "InlinedApi" })
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

	@SuppressLint({ "NewApi", "InlinedApi" })
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

	@SuppressLint({ "NewApi", "InlinedApi" })
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

	@SuppressLint({ "NewApi", "InlinedApi" })
	public void setLastIDHomeWallRep(int lastIDHomeWallRep) {
		SQLiteDatabase sql = this.getWritableDatabase();
		ContentValues value = new ContentValues();
		value.put(Keys.POSTLASTID, lastIDHomeWallRep);

		sql.updateWithOnConflict(Keys.LASTIDTABLE, value,
				"COLNAME='lastIDHomeWallRep'", null,
				SQLiteDatabase.CONFLICT_REPLACE);

	}
}