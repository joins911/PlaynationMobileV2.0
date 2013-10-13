package com.myapps.playnation.Operations;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
public class DataConnector {

	static DataConnector inst;
	InputStream is = null;
	HttpClient httpclient;
	String url;

	final String ServerIp = "10.0.2.2";

	private static JSONArray json;
	private boolean connStatus = false;
	private static ArrayList<Bundle> searchArray;
	private static ArrayList<Bundle> arrayChildren = new ArrayList<Bundle>();
	private Bundle currentPlayer;

	private MySQLinker sqlLinker;

	public Bundle getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Bundle currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	private DataConnector() {

		url = "http://playnation.eu/beta/hacks/";
		// url = "http://" + ServerIp + "/test/";

	}

	/**
	 * Single ton pattern
	 * 
	 * @return the static refrence of the class
	 */
	public static DataConnector getInst() {
		if (inst == null)
			return inst = new DataConnector();
		return inst;
	}

	public void setSQLLinker(Context con) {
		sqlLinker = new MySQLinker(con);
	}

	public MySQLinker getLinker() {
		return sqlLinker;
	}

	/**
	 * Takes the data out of the JSON response and adds it to a Map for holding
	 * until destroyed
	 * 
	 * @param jsonArray
	 *            the JSON response from the Database
	 * @throws JSONException
	 */
	public void jsonToArray(JSONArray jsonArray, String table)
			throws JSONException {
		if (jsonArray != null && table.equals(Keys.gamesTable)) {
			sqlLinker.addGames(jsonArray);
		} else if (jsonArray != null && table.equals(Keys.groupsTable)) {
			sqlLinker.addGroups(jsonArray);
		} else if (jsonArray != null && table.equals(Keys.newsTable)) {
			sqlLinker.addNews(jsonArray);
		} else if (jsonArray != null && table.equals(Keys.newsServiceTable)) {
			sqlLinker.addNews(jsonArray);
		} else if (jsonArray != null && table.equals(Keys.companyTable)) {
			sqlLinker.addCompany(jsonArray);
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
		if (tableName.equals(Keys.HomeWallTable)) {
			return sqlLinker.getSQLitePWall(tableName, sepateID);
		} else if (tableName.equals(Keys.newsTable)) {
			return sqlLinker.getSQLiteNews(tableName);
		} else if (tableName.equals(Keys.groupsTable)) {
			return sqlLinker.getSQLiteGroups(tableName);
		} else if (tableName.equals(Keys.gamesTable)) {
			return sqlLinker.getSQLiteGame(tableName);
		} else if (tableName.equals(Keys.companyTable)) {
			return sqlLinker.getSQLiteCompanies(tableName);
		} else if (tableName.equals(Keys.HomeMsgTable)) {
			return sqlLinker.getSQLitePMSG(tableName, sepateID);
		} else if (tableName.equals(Keys.HomeSubscriptionTable)) {
			return sqlLinker.getSQLitePSubscription(tableName);
		} else if (tableName.equals(Keys.HomeEventTable)) {
			return sqlLinker.getSQLitePEvent(tableName);
		} else if (tableName.equals(Keys.HomeFriendsTable)) {
			return sqlLinker.getSQLitePFriends(tableName, sepateID);
		} else if (tableName.equals(Keys.HomeGamesTable)) {
			return sqlLinker.getSQLitePGames(tableName, sepateID);
		} else if (tableName.equals(Keys.HomeGroupTable)) {
			return sqlLinker.getSQLitePGroup(tableName, sepateID);
		} else if (tableName.equals(Keys.HomeWallRepliesTable)) {
			return sqlLinker.getSQLitePWallReplies(tableName, sepateID);
		} else if (tableName.equals(Keys.HomeMsgRepliesTable)) {
			return sqlLinker.getSQLitePMsgReplies(tableName, sepateID);
		} else if (tableName.equals(Keys.whoIsPlayingTable)) {
			return sqlLinker.getSQLiteWhoIsPlaying(tableName, sepateID);
		} else if (tableName.equals(Keys.HomeNotificationTable)) {
			return sqlLinker.getSQLiteNotification(tableName, sepateID);
		}
		return null;
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
			Log.e("DataConnector ", " getQuerry() Error in http connection "
					+ e.toString());
		}
		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();

		} catch (Exception e) {
			Log.e("DataConnector ",
					"getQuerry() Error converting result " + e.toString());
		}

		try {
			if (result != null) {
				String suffix = "{(";
				// if (result.endsWith(suffix)) {
				jArray = new JSONArray(result);
				jsonToArray(jArray, tableName);
				// }
			}
		} catch (JSONException e) {
			Log.e("DataConnector " + tableName + " ",
					"Error parsing data " + e.toString());
		}
		url = temp;
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
			is = entity.getContent();
		} catch (Exception e) {
			Log.e("log_tag HTML Conn",
					"Error in http connection " + e.toString());
		}

		try {

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
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

	public void queryPlayerEvents(String playerID) {

		JSONArray json = getArrayFromQuerryWithPostVariable(playerID,
				Keys.HomeEventTable, "0", sqlLinker.getLastHomeIDEvents());
		sqlLinker.insertPEvents(json, playerID);
	}

	public void queryPlayerFriends(String playerID) {
		JSONArray json = getArrayFromQuerryWithPostVariable(playerID,
				Keys.HomeFriendsTable, "0", 0);
		sqlLinker.insertPFriends(json, playerID);
	}

	public String queryNewImageURL(String ids, String ownerType,
			String mainTable) {

		JSONArray json = getArrayFromQuerryWithPostVariable(ids, "newImageUrl",
				ownerType, 0);
		return sqlLinker.insertNewImgURL(json, mainTable, ids);

	}

	public void queryWhoIsPlaying(String playerID) {

		JSONArray json = getArrayFromQuerryWithPostVariable(playerID,
				Keys.whoIsPlayingTable, "0", 0);
		sqlLinker.insertWhoIsPlaying(json, playerID);
	}

	public void functionQuery(String ID_PLAYER, String Another, String phpName,
			String action, String Comments) {

		SQLiteDatabase sql = sqlLinker.getReadableDatabase();
		sql = sqlLinker.getReadableDatabase();
		String temp = url;
		if (phpName.startsWith("game")) {
			url += "gameFunction.php";
		} else if (phpName.startsWith("group")) {
			url += "groupFunction.php";
		} else if (phpName.startsWith("friend")) {
			url += "friendFunctions.php";
		} else if (phpName.startsWith("company")) {
			url += "companyFunctions.php";
		} else if (phpName.startsWith("wall")) {
			url += "wallFunction.php";
		}

		// http post
		try {
			httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(temp);
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			url = temp;
			pairs.add(new BasicNameValuePair("function", action));
			pairs.add(new BasicNameValuePair("ID_PLAYER", ID_PLAYER));
			if (phpName.startsWith("game")) {
				pairs.add(new BasicNameValuePair("ID_GAME", Another));
				pairs.add(new BasicNameValuePair("Comments", Comments));
				ContentValues values = new ContentValues();
				if (action.equals(Keys.POSTFUNCOMMANTAddGame)) {
					values.put(Keys.GameisPlaying, "1");
					sql.updateWithOnConflict(Keys.gamesTable, values,
							Keys.ID_GAME + "=" + Another, null,
							SQLiteDatabase.CONFLICT_REPLACE);
				} else if (action.equals(Keys.POSTFUNCOMMANTLike)) {
					values.put(Keys.GameIsLiked, "1");
					sql.updateWithOnConflict(Keys.gamesTable, values,
							Keys.ID_GAME + "=" + Another, null,
							SQLiteDatabase.CONFLICT_REPLACE);
				} else {
					values.put(Keys.GameIsLiked, "-1");
					sql.updateWithOnConflict(Keys.gamesTable, values,
							Keys.ID_GAME + "=" + Another, null,
							SQLiteDatabase.CONFLICT_REPLACE);
				}
			} else if (phpName.startsWith("group")) {
				pairs.add(new BasicNameValuePair("ID_GROUP", Another));
				pairs.add(new BasicNameValuePair("Comments", Comments));
				ContentValues values = new ContentValues();
				if (action.equals(Keys.POSTFUNCOMMANTSendPerson)) {
					values.put(Keys.isMember, "1");
					sql.updateWithOnConflict(Keys.groupsTable, values,
							Keys.ID_GROUP + "=" + Another, null,
							SQLiteDatabase.CONFLICT_REPLACE);
				} else if (action.equals(Keys.POSTFUNCOMMANTLike)) {
					values.put(Keys.GameIsLiked, "1");
					sql.updateWithOnConflict(Keys.groupsTable, values,
							Keys.ID_GROUP + "=" + Another, null,
							SQLiteDatabase.CONFLICT_REPLACE);
				} else {
					values.put(Keys.GameIsLiked, "-1");
					sql.updateWithOnConflict(Keys.groupsTable, values,
							Keys.ID_GROUP + "=" + Another, null,
							SQLiteDatabase.CONFLICT_REPLACE);
				}
			} else if (phpName.startsWith("friend")) {
				pairs.add(new BasicNameValuePair("ID_FRIEND", Another));
			} else if (phpName.startsWith("company")) {
				pairs.add(new BasicNameValuePair(Keys.EventID_COMPANY, Another));
				ContentValues values = new ContentValues();
				if (action.equals(Keys.POSTFUNCOMMANTLike)) {
					values.put(Keys.GameIsLiked, "1");
					sql.updateWithOnConflict(Keys.companyTable, values,
							Keys.EventID_COMPANY + "=" + Another, null,
							SQLiteDatabase.CONFLICT_REPLACE);
				} else {
					values.put(Keys.GameIsLiked, "-1");
					sql.updateWithOnConflict(Keys.companyTable, values,
							Keys.EventID_COMPANY + "=" + Another, null,
							SQLiteDatabase.CONFLICT_REPLACE);
				}
			} else if (phpName.startsWith("wall")) {
				pairs.add(new BasicNameValuePair(Keys.ID_WALLITEM, Another));
				ContentValues values = new ContentValues();
				if (action.equals(Keys.POSTFUNCOMMANTLike)) {
					values.put(Keys.GameIsLiked, "1");
					sql.updateWithOnConflict(Keys.HomeWallTable, values,
							Keys.ID_WALLITEM + "=" + Another, null,
							SQLiteDatabase.CONFLICT_REPLACE);
				} else {
					values.put(Keys.GameIsLiked, "-1");
					sql.updateWithOnConflict(Keys.HomeWallTable, values,
							Keys.ID_WALLITEM + "=" + Another, null,
							SQLiteDatabase.CONFLICT_REPLACE);
				}
			}

			httppost.setEntity(new UrlEncodedFormEntity(pairs));
			HttpResponse response = httpclient.execute(httppost);
			response.getEntity();
		} catch (Exception e) {
			Log.e("log_tag HTML Conn",
					"Error in functionQuery http connection " + e.toString());
		}
	}

	public void queryPlayerGames(String playerID) {
		JSONArray json = getArrayFromQuerryWithPostVariable(playerID,
				Keys.HomeGamesTable, "0", 0);
		sqlLinker.insertPGames(json, playerID);
	}

	public void queryPlayerGroup(String playerID) {
		JSONArray json = getArrayFromQuerryWithPostVariable(playerID,
				Keys.HomeGroupTable, "0", 0);
		sqlLinker.insertPGroups(json, playerID);
	}

	public void queryPlayerMessages(String playerID) {
		JSONArray json = getArrayFromQuerryWithPostVariable(playerID,
				Keys.HomeMsgTable, "0", 0);
		sqlLinker.insertPMessages(json, playerID);
	}

	public void queryMiniIds() {

		JSONArray json = getArrayFromQuerryWithPostVariable("0", "miniIDs",
				"0", 0);
		// // Print the data to the console
		if (json != null)
			for (int i = 0; i < json.length(); i++) {
				try {
					sqlLinker.setMinNewsID(json.getJSONObject(i).getInt(
							Keys.newsTable));
					sqlLinker.setMinCompanyID(json.getJSONObject(i).getInt(
							Keys.companyTable));
					sqlLinker.setMinGameID(json.getJSONObject(i).getInt(
							Keys.gamesTable));
					sqlLinker.setMinGroupID(json.getJSONObject(i).getInt(
							Keys.groupsTable));
				} catch (Exception e) {
				}
			}
	}

	public void queryPlayerSubscription(String playerID) {
		JSONArray json = getArrayFromQuerryWithPostVariable(playerID,
				Keys.HomeSubscriptionTable, "0", sqlLinker.getLastIDHomeSubs());
		sqlLinker.insertPSubscription(json, playerID);
	}

	public void queryPlayerWall(String playerID, String ownerType) {
		JSONArray json = getArrayFromQuerryWithPostVariable(playerID,
				Keys.HomeWallTable, ownerType, 0);
		sqlLinker.insertPWall(json, playerID);
	}

	public void queryPlayerWallReplices(String wallitem, String playerID) {
		JSONArray json = getArrayFromQuerryWithPostVariable(playerID,
				Keys.HomeWallRepliesTable, wallitem, 0);
		sqlLinker.insertPWallReplies(json, playerID);
	}

	public void queryPlayerMSGReplices(String wallitem, String playerID) {
		JSONArray json = getArrayFromQuerryWithPostVariable(playerID,
				Keys.HomeMsgRepliesTable, wallitem, 0);
		sqlLinker.insertPMessagesReplies(json, playerID, wallitem);
	}

	public void queryPlayerInfo(String playerID) {
		JSONArray json = getArrayFromQuerryWithPostVariable(playerID,
				Keys.PlayerTable, "0", 0);
		sqlLinker.insertPlayerInfo(json, playerID);
	}

	public View populatePlayerGeneralInfo(View v, String nameT,
			String playerID, Bundle mapEntry) {
		TextView txPlName = (TextView) v.findViewById(R.id.txPlName);
		TextView txPlNick = (TextView) v.findViewById(R.id.txPlNick);
		TextView txPlAge = (TextView) v.findViewById(R.id.txPlAge);
		TextView txPlCountry = (TextView) v.findViewById(R.id.txPlCountry);
		QuickContactBadge playerIcon = (QuickContactBadge) v
				.findViewById(R.id.quickContactBadge1);
		if (nameT.equals("Wall")) {
			setCurrentPlayer(sqlLinker.getPlayer(playerID));

			if (v != null) {
				if (currentPlayer != null) {
					if (txPlName != null)
						txPlName.setText("Name : "
								+ currentPlayer.get(Keys.FirstName) + " , "
								+ currentPlayer.get(Keys.LastName));

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
						String imageUrl = currentPlayer
								.getString(Keys.PLAYERAVATAR);
						playerIcon.setTag(imageUrl);
						new LoadImage(imageUrl, playerIcon, "players")
								.execute(playerIcon);

					}
				}
			}
		} else {
			if (mapEntry != null) {
				if (txPlName != null)
					txPlName.setText("Name : " + mapEntry.get(Keys.FirstName)
							+ " , " + mapEntry.get(Keys.LastName));

				if (txPlNick != null)
					txPlNick.setText("Nick : "
							+ mapEntry.get(Keys.PLAYERNICKNAME));

				if (txPlAge != null)
					txPlAge.setText("Age : "
							+ HelperClass.convertToAge(mapEntry
									.getString(Keys.Age)));

				if (txPlCountry != null)
					txPlCountry.setText("Country: "
							+ mapEntry.get(Keys.COUNTRY));
				if (playerIcon != null) {
					String imageUrl = mapEntry.getString(Keys.PLAYERAVATAR);
					playerIcon.setTag(imageUrl);
					new LoadImage(imageUrl, playerIcon, "players")
							.execute(playerIcon);
				}
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

	public ArrayList<UserComment> getComments(String ownerId, String ownerType) {
		queryPlayerWall(ownerId, ownerType);
		ArrayList<UserComment> comments = new ArrayList<UserComment>();
		ArrayList<Bundle> list = sqlLinker.getSQLitePWall(Keys.HomeWallTable,
				ownerId);
		for (Bundle bundle : list) {
			String date = HelperClass.convertTime(
					Integer.parseInt(bundle.getString(Keys.WallPostingTime)),
					Configurations.dataTemplate);
			comments.add(new UserComment(new CommentInfo(bundle
					.getString(Keys.WallPosterDisplayName), bundle
					.getString(Keys.WallMessage), bundle
					.getString(Keys.PLAYERAVATAR), date),
					new ArrayList<CommentInfo>()));
		}
		return comments;
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

	public void sendMessage(String msg, String ID_PLAYER, String ID_FRIEND) {
		String temp = url;
		url += "sendMessage.php";
		try {
			httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			url = temp;

			pairs.add(new BasicNameValuePair(Keys.ID_PLAYER, ID_PLAYER));
			pairs.add(new BasicNameValuePair(Keys.ID_FRIEND, ID_FRIEND));
			pairs.add(new BasicNameValuePair(Keys.MessageText, msg));
			String now = DateFormat.getDateInstance().format(new Date());
			pairs.add(new BasicNameValuePair(Keys.MessageTime, now));

			httppost.setEntity(new UrlEncodedFormEntity(pairs));
			httpclient.execute(httppost);
		} catch (Exception e) {
			Log.e("log_tag HTML Conn",
					"Error in insertMessage http connection " + e.toString());
		}
	}

	public ArrayList<Bundle> queryPlayerFriendsSearch(CharSequence search) {
		searchArray = new ArrayList<Bundle>();
		JSONArray json = getArrayFromQuerryWithPostVariable(
				Configurations.CurrentPlayerID, Keys.SearchFriendsTable,
				search.toString(), 0);
		// // Print the data to the console
		if (json != null) {
			for (int i = 0; i < json.length(); i++) {
				try {
					Bundle map = new Bundle();
					map.putString(Keys.ID_PLAYER, json.getJSONObject(i)
							.optString(Keys.ID_PLAYER));
					map.putString(Keys.CITY,
							json.getJSONObject(i).optString(Keys.CITY));
					map.putString(Keys.COUNTRY, json.getJSONObject(i)
							.optString(Keys.COUNTRY));
					map.putString(Keys.PLAYERNICKNAME, json.getJSONObject(i)
							.optString(Keys.PLAYERNICKNAME));
					map.putString(Keys.Email,
							json.getJSONObject(i).optString(Keys.Email));
					map.putString(Keys.PLAYERAVATAR, json.getJSONObject(i)
							.optString(Keys.PLAYERAVATAR));
					map.putString(Keys.FirstName, json.getJSONObject(i)
							.optString(Keys.FirstName));
					map.putString(Keys.LastName, json.getJSONObject(i)
							.optString(Keys.LastName));
					map.putString(Keys.Mutual,
							json.getJSONObject(i).optString(Keys.Mutual));
					map.putString(Keys.Age,
							json.getJSONObject(i).optString(Keys.Age));
					// Not sure before was needed
					String firstName = json.getJSONObject(i).optString(
							Keys.FirstName);
					String lastName = json.getJSONObject(i).optString(
							Keys.LastName);
					map.putString(Keys.PLAYERNAME, firstName + "," + lastName);

					searchArray.add(map);
				} catch (Exception e) {
					Log.e("Fetching Friends Search", "Error Friends Search" + e);
				}
			}
			return searchArray;
		} else
			return null;
	}

	public void queryNotification(String PlayerID, String tableName) {
		JSONArray json = getArrayFromQuerryWithPostVariable(PlayerID,
				tableName, "", 0);
		sqlLinker.insertPNotification(json, PlayerID);
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
		temp = temp + "chekRegisterLogin.php";
		HttpEntity entity = null;
		JSONArray jArray = null;
		boolean results = false;
		// http post
		try {
			httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(temp);
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("checkCondition", "Login"));
			pairs.add(new BasicNameValuePair("FirstName", ""));
			pairs.add(new BasicNameValuePair("LastName", ""));
			pairs.add(new BasicNameValuePair("password", pass));
			pairs.add(new BasicNameValuePair("email", email));
			Log.e("DataCon", "checkLogin " + temp);
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
				Configurations.CurrentPlayerID = Keys.TEMPLAYERID = jArray
						.getJSONObject(0).getString(Keys.ID_PLAYER);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			SharedPreferences.Editor editPref = pref.edit();
			editPref.putString(Keys.USERNAME, email);
			editPref.putString(Keys.ID_PLAYER, Configurations.CurrentPlayerID);
			editPref.putString(Keys.Password, pass);
			editPref.putBoolean(Keys.ActiveSession, true);
			editPref.commit();
			results = true;
		} else {
			results = false;
		}
		return results;
	}

}