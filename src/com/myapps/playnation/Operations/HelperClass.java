package com.myapps.playnation.Operations;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.myapps.playnation.R;
import com.myapps.playnation.Classes.DataSection;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Classes.NewsFeed;
import com.myapps.playnation.Classes.NewsFeedItem;
import com.myapps.playnation.Classes.UserComment;

public class HelperClass {
	/**
	 * Checks if current device is tablet.
	 * 
	 * @param content
	 * @return boolean
	 */
	@SuppressLint("InlinedApi")
	public static boolean isTablet(Context content) {
		boolean large = ((content.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
		boolean xlarge = ((content.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);
		return (large || xlarge);
	}

	public static String returnNotificationMessage(Bundle arg) {
		String returns = "";
		if (arg.getString(Keys.NotificationType).equals("FriendReplay")) {
			returns = "Player " + arg.getString(Keys.PLAYERNICKNAME)
					+ " has accepted your friend request.";
		} else if (arg.getString(Keys.NotificationType).equals("FriendRequest")) {
			returns = "Player " + arg.getString(Keys.PLAYERNICKNAME)
					+ " has send you friend request.";
		} else if (arg.getString(Keys.NotificationType).equals("CompanyNews")) {
			returns = "News from company" + arg.getString(Keys.CompanyName)
					+ " .";
		} else if (arg.getString(Keys.NotificationType).equals("GameNews")) {
			returns = " News from " + arg.getString(Keys.GAMENAME) + ".";
		} else if (arg.getString(Keys.NotificationType).equals("GroupNews")) {
			returns = " News from " + arg.getString(Keys.GROUPNAME) + ".";
		}

		return returns;
	}

	public static void getImage(String imageLoc, ImageView tvImage) {
		URL imageURL = null;
		Bitmap bitmap = null;
		try {
			imageURL = new URL("http://playnation.eu/global/pub_img/"
					+ imageLoc);
		} catch (MalformedURLException e) {
		}

		try {
			HttpURLConnection connection = (HttpURLConnection) imageURL
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream inputStream = connection.getInputStream();

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPurgeable = true;
			options.inJustDecodeBounds = false;
			options.outHeight = 150;
			bitmap = BitmapFactory.decodeStream(inputStream, null, options);

		} catch (IOException e) {
		}

		if (bitmap == null) {
			if (imageLoc != null) {
				String[] arr = imageLoc.split("/");
				if (arr[0].equals("games")) {
					tvImage.setImageResource(R.drawable.no_game_100x100);
				} else if (arr[0].equals("players")) {
					tvImage.setImageResource(R.drawable.no_player_100x100);
				} else if (arr[0].equals("newsitems")) {
					tvImage.setImageResource(R.drawable.no_news_100x100);
				} else if (arr[0].equals("groups")) {
					tvImage.setImageResource(R.drawable.no_group_100x100);
				} else if (arr[0].equals("wall_photos")) {
					tvImage.setImageResource(R.drawable.no_forum_100x100);
				} else if (arr[0].equals("companies")) {
					tvImage.setImageResource(R.drawable.no_company_100x100);
				}
			}
		} else {
			bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), 150,
					true);
			tvImage.setImageBitmap(bitmap);

		}
	}

	public static boolean isNetworkAvailable(ConnectivityManager cm) {
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		NetworkInfo[] mNetInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : mNetInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		return haveConnectedWifi || haveConnectedMobile;
	}

	public static void disableAddComments(View footer, TextView commentText,
			Button commentBut) {
		if (Configurations.getConfigs().getApplicationState() != 0) {
			TextView lbl = (TextView) footer
					.findViewById(R.id.wallsF_comment_TView);
			lbl.setVisibility(View.GONE);
			commentText.setVisibility(View.GONE);
			commentBut.setVisibility(View.GONE);
		}
	}

	public static void getListViewSize(ListView myListView) {
		BaseAdapter myListAdapter = (BaseAdapter) myListView.getAdapter();
		if (myListAdapter == null) {
			// do nothing return null
			return;
		}
		// set listAdapter in loop for getting final size
		int totalHeight = 0;
		for (int size = 0; size < myListAdapter.getCount(); size++) {
			View listItem = myListAdapter.getView(size, null, myListView);
			if (listItem instanceof ViewGroup)
				listItem.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight() / 2.8;
		}
		// setting listview item in adapter
		ViewGroup.LayoutParams params = myListView.getLayoutParams();
		params.height = totalHeight
				+ (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
		myListView.setLayoutParams(params);
		// print height of adapter on log
		Log.i("height of listItem:", String.valueOf(totalHeight));
	}

	public static String returnEventPrivacy(int index) {
		if (index == 0) {
			return "Private";
		} else {
			return "Public";
		}
	}

	public static ArrayList<UserComment> modifyDataSet(String id, String owner) {
		DataConnector con = DataConnector.getInst();
		return con.getComments(id, owner);
	}

	public static String returnUnserializedText(String text) {
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

	/**
	 * Checks if particular game that person is tracking has comments.If not
	 * returns the specific game description.
	 * 
	 * @param game
	 * @return string
	 */
	public static String checkGameComments(Bundle game) {
		String result = "";
		if (game.getString(Keys.GameComments).equalsIgnoreCase("")) {
			result = game.getString(Keys.GAMEDESC);
		} else {
			result = game.getString(Keys.GameComments);
		}
		return result;
	}

	public static String sqliteQueryStrings(String tableName,
			String separeteID, String anotherID, String limit) {
		if (tableName.equals(Keys.HomeWallTable)) {
			return "SELECT * FROM " + tableName + " WHERE " + Keys.ID_OWNER
					+ "=" + separeteID + " Order by " + Keys.WallPostingTime
					+ " desc;";
		} else if (tableName.equals(Keys.newsTable)) {
			return "SELECT * FROM " + tableName + " Order by "
					+ Keys.NEWSCOLPOSTINGTIME + " desc;";
		} else if (tableName.equals(Keys.groupsTable)
				|| tableName.equals(Keys.gamesTable)
				|| tableName.equals(Keys.companyTable)
				|| tableName.equals(Keys.HomeSubscriptionTable)) {
			return "SELECT * FROM " + tableName + ";";
		} else if (tableName.equals(Keys.HomeMsgTable)) {
			return "SELECT * FROM " + tableName + " Where " + Keys.ID_PLAYER
					+ "=" + separeteID + ";";
		} else if (tableName.equals(Keys.HomeEventTable)) {
			return "SELECT * FROM " + tableName + " Where ID_PLAYER="
					+ Configurations.CurrentPlayerID + ";";
		} else if (tableName.equals(Keys.HomeFriendsTable)) {
			return "SELECT * FROM " + tableName + " Where ID_OWNER=?;";
		} else if (tableName.equals(Keys.HomeGamesTable)) {
			return "SELECT * FROM " + tableName + " Where ID_PLAYER=?;";
		} else if (tableName.equals(Keys.HomeGroupTable)) {
			return "SELECT * FROM " + tableName + " Where ID_PLAYER=?;";
		} else if (tableName.equals(Keys.HomeWallRepliesTable)) {
			return "SELECT * FROM " + tableName + " Where " + Keys.ID_WALLITEM
					+ "=" + separeteID + " Order By PostingTime desc;";
		} else if (tableName.equals(Keys.HomeMsgRepliesTable)) {
			return "SELECT * FROM " + tableName + " Where "
					+ Keys.MessageID_CONVERSATION + "=" + separeteID
					+ " Order By MessageTime desc;";
		} else if (tableName.equals(Keys.PlayerTable)) {
			return "SELECT * FROM " + tableName + " Where ID_PLAYER="
					+ separeteID + ";";
		} else if (tableName.equals(Keys.whoIsPlayingTable)) {
			return "SELECT * FROM " + tableName + " Where ID_GAME="
					+ separeteID + ";";
		} else if (tableName.equals(Keys.HomeNotificationTable)) {
			return "SELECT * FROM " + tableName + " Where ID_PLAYER="
					+ separeteID + ";";
		}
		return "SELECT * FROM " + tableName + ";";
	}

	public static String sqliteQueryStringsChecker(String tableName,
			String separeteID, String anotherID) {
		String returns = "";
		if (tableName.equals(Keys.HomeWallTable)) {
			returns = "SELECT * FROM " + tableName
					+ " WHERE ID_WALLITEM=? and " + Keys.ID_OWNER + "="
					+ separeteID + " Order by " + Keys.WallPostingTime
					+ " desc;";
		} else if (tableName.equals(Keys.newsTable)) {
			returns = "SELECT * FROM " + tableName
					+ " Where ID_NEWS=? Order by " + Keys.NEWSCOLPOSTINGTIME
					+ " desc;";
		} else if (tableName.equals(Keys.gamesTable)) {
			returns = "SELECT * FROM " + tableName + " Where ID_GAME=?;";
		} else if (tableName.equals(Keys.companyTable)) {
			returns = "SELECT * FROM " + tableName + " Where ID_COMPANY=?;";
		} else if (tableName.equals(Keys.HomeSubscriptionTable)) {
			returns = "SELECT * FROM " + tableName + " Where ID_ITEM=?;";
		} else if (tableName.equals(Keys.groupsTable)) {
			returns = "SELECT * FROM " + tableName + " Where ID_GROUP=?;";
		} else if (tableName.equals(Keys.HomeMsgTable)) {
			returns = "SELECT * FROM " + tableName + " Where ID_MESSAGE=? and "
					+ Keys.ID_PLAYER + "=" + Configurations.CurrentPlayerID
					+ ";";
		} else if (tableName.equals(Keys.HomeEventTable)) {
			returns = "SELECT * FROM " + tableName
					+ " Where ID_EVENT=? and ID_PLAYER="
					+ Configurations.CurrentPlayerID + ";";
		} else if (tableName.equals(Keys.HomeFriendsTable)) {
			returns = "SELECT * FROM " + tableName
					+ " Where ID_PLAYER=? and ID_OWNER=" + anotherID + ";";
		} else if (tableName.equals(Keys.HomeGamesTable)) {
			returns = "SELECT * FROM " + tableName
					+ " Where ID_GAME=? and ID_PLAYER=" + anotherID + ";";
		} else if (tableName.equals(Keys.HomeGroupTable)) {
			returns = "SELECT * FROM " + tableName
					+ " Where ID_GROUP=? and ID_PLAYER=" + anotherID + ";";
		} else if (tableName.equals(Keys.HomeWallRepliesTable)) {
			returns = "SELECT * FROM " + tableName + " Where ID_WALLITEM="
					+ separeteID + " and " + Keys.ID_ORGOWNER + "=" + anotherID
					+ ";";
		} else if (tableName.equals(Keys.HomeMsgRepliesTable)) {
			returns = "SELECT * FROM " + tableName + " Where ID_MESSAGE="
					+ anotherID + " and " + Keys.MessageID_CONVERSATION + "=?;";
		} else if (tableName.equals(Keys.newsTempTable)) {
			returns = "SELECT * FROM " + tableName
					+ " Where ID_NEWS=? and ID_OWNER=" + anotherID + ";";
		} else if (tableName.equals(Keys.companyTempTable)) {
			returns = "SELECT * FROM " + tableName
					+ " Where ID_NEWS=? and ID_OWNER=" + anotherID + ";";
		} else if (tableName.equals(Keys.whoIsPlayingTable)) {
			returns = "SELECT * FROM " + tableName
					+ " Where ID_GAME=? and ID_PLAYER=" + anotherID + ";";
		} else if (tableName.equals(Keys.PlayerTable)) {
			returns = "SELECT * FROM " + tableName + " Where ID_PLAYER=?;";
		} else if (tableName.equals(Keys.HomeNotificationTable)) {
			returns = "SELECT * FROM " + tableName + " Where ID_PLAYER=?;";
		}
		return returns;
	}

	public static boolean EmailPassNickCheck(EditText email, EditText password,
			EditText nick) {
		if (nick != null) {
			if (nick.getText().toString().trim().equals("")) {
				nick.setError("Empty name please revise!");
				return false;
			}
		} else if (email.getText().toString().trim().equals("")) {
			email.setError("Empty email please revise!");
			return false;
		} else if (password.getText().toString().trim().equals("")) {
			password.setError("Empty password please revise!");
			return false;
		} else if (!EmailValidation(email)) {
			email.setError("Incorrect Email please revise!");
			return false;
		}
		return true;
	}

	public static boolean isEmpty(EditText etText) {
		if (etText.getText().toString().trim().length() > 0) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean EmailValidation(EditText email) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher;
		matcher = pattern.matcher(email.getText().toString());

		return matcher.matches();
	}

	public static String getDate(String integer) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE,MMMM d,yyyy h:mm,a",
				Locale.ENGLISH);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		String formattedDate = sdf.format(new Date());
		if (!integer.equalsIgnoreCase("null")) {
			long seconds = Integer.valueOf(integer);
			long millis = seconds * 1000;
			Date date = new Date(millis);
			formattedDate = sdf.format(date);
			return formattedDate;
		}
		return formattedDate;

	}

	public static String convertTime(int fetchDate, SimpleDateFormat sdf) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date((long) fetchDate * 1000));
		return sdf.format(cal.getTime());
	}

	public static String convertToAge(String age) {
		String[] dates = age.split("-");
		int year = Integer.parseInt(dates[0]);
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		return (currentYear - year) + "";
	}

	public static String durationConverter(String time, Context v) {
		String returns = v.getResources().getString(R.string.Duration)
				+ v.getResources().getString(R.string.DayEvent);
		long duration = Long.parseLong(time);
		if (duration == 0)
			returns = v.getResources().getString(R.string.Duration)
					+ v.getResources().getString(R.string.DayEvent);
		else if (duration > 0) {
			long hours = duration / 3600;
			long min = (duration % 3600) / 60;
			returns = v.getResources().getString(R.string.Duration) + hours
					+ ":" + min;
		}
		return returns;
	}

	public static ArrayList<NewsFeedItem> createHeaderListView(
			ArrayList<NewsFeedItem> newsFeedList) {
		Collections.sort(newsFeedList, new Comparator<NewsFeedItem>() {
			@Override
			public int compare(NewsFeedItem lhs, NewsFeedItem rhs) {
				int i = 0;
				if (lhs instanceof NewsFeed) {
					if (rhs instanceof NewsFeed) {
						long d1 = ((NewsFeed) lhs).getKey_NewsDate().getTime()
								.getTime();
						long d2 = ((NewsFeed) rhs).getKey_NewsDate().getTime()
								.getTime();
						if (d2 > d1) {
							i = 1;
						} else if (d1 > d2) {
							i = -1;
						} else {
							i = 0;
						}
					}
				}
				return i;
			}
		});
		ArrayList<NewsFeedItem> temp = new ArrayList<NewsFeedItem>();
		int lastIndex = 0;
		for (int i = 0; i < newsFeedList.size(); i++) {
			final NewsFeed nf = (NewsFeed) newsFeedList.get(i);
			DataSection ds = new DataSection();
			ds.setKey_Title(dateWriter(nf));
			for (int a = lastIndex; a < temp.size(); a++) {
				DataSection newsFeedItem = (DataSection) temp.get(a);
				if (newsFeedItem instanceof DataSection) {
					if (ds.getKey_Title().equals(newsFeedItem.getKey_Title())) {
						int index = temp.size();
						temp.add(index, nf);
						break;
					}
					if (!((DataSection) temp.get(lastIndex)).getKey_Title()
							.equals(ds.getKey_Title())) {
						temp.add(ds);
						temp.add(nf);

						lastIndex = temp.indexOf(ds);
						break;
					}
				}
			}
			if (temp.size() == 0) {
				temp.add(ds);
				temp.add(nf);
			}
		}

		return temp;
	}

	@SuppressLint("SimpleDateFormat")
	private static String dateWriter(NewsFeed feed) {
		SimpleDateFormat time = new SimpleDateFormat("MMMMM dd, yyyy");
		Calendar date = Calendar.getInstance();
		Calendar compareDate = feed.getKey_NewsDate();
		int comparableDay = compareDate.get(Calendar.DAY_OF_YEAR);
		int currentDay = date.get(Calendar.DAY_OF_YEAR);
		if (comparableDay == currentDay)
			return "Today";
		if ((comparableDay + 1) == currentDay)
			return "Yesterday";
		if (comparableDay < currentDay)
			return time.format(compareDate.getTime());
		else {
			return time.format(compareDate.getTime());
		}
	}

	public static ArrayList<NewsFeedItem> queryNewsList(ArrayList<Bundle> result) {
		ArrayList<NewsFeedItem> newsFeedList = new ArrayList<NewsFeedItem>();
		for (int i = 0; i < result.size(); i++) {
			try {
				NewsFeed feed = new NewsFeed();
				feed.setKey_NewsFeedID(Integer.valueOf(result.get(i).getString(
						Keys.NEWSCOLID_NEWS)));
				feed.setKey_NewsText(result.get(i).getString(
						Keys.NEWSCOLNEWSTEXT));
				feed.setKey_NewsIntroText(result.get(i).getString(
						Keys.NEWSCOLINTROTEXT));
				feed.setKey_NewsImage(result.get(i)
						.getString(Keys.NEWSCOLIMAGE));
				String s = result.get(i).getString(Keys.Author);
				if (!s.equalsIgnoreCase(" "))
					feed.setKey_Author(s);

				feed.setKey_NewsFeedID(Integer.parseInt(result.get(i)
						.getString(Keys.NEWSCOLID_NEWS)));
				feed.setKey_NewsTitle(result.get(i).getString(
						Keys.NEWSCOLHEADLINE));
				feed.setKey_NewsDate(feed.convertTime(Integer.valueOf(result
						.get(i).getString(Keys.NEWSCOLPOSTINGTIME))));
				newsFeedList.add(feed);
			} catch (Exception e) {
				Log.e("HelperClass ", " queryNewsList Error " + e);
			}
		}
		return newsFeedList;
	}

}
