package com.myapps.playnation.Operations;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import com.myapps.playnation.Classes.Keys;

public class BundleBuilder {
	
	public static Bundle putGameInBundle(Cursor cursor) {
		Bundle bundle = new Bundle();		
		bundle.putString(Keys.GAMENAME,
				cursor.getString(cursor.getColumnIndex(Keys.GAMENAME)));
		String gameType = cursor
				.getString(cursor.getColumnIndex(Keys.GAMETYPE));
		bundle.putString(Keys.GAMETYPE, gameType);
		bundle.putString(Keys.GAMEDESC,
				cursor.getString(cursor.getColumnIndex(Keys.GAMEDESC)));
		bundle.putString(Keys.GAMEDATE,
				cursor.getString(cursor.getColumnIndex(Keys.GAMEDATE)));
		bundle.putString(Keys.RATING,
				cursor.getString(cursor.getColumnIndex(Keys.RATING)));
		bundle.putString(Keys.GAMEESRB,
				cursor.getString(cursor.getColumnIndex(Keys.GAMEESRB)));
		bundle.putString(Keys.GAMEURL,
				cursor.getString(cursor.getColumnIndex(Keys.GAMEURL)));
		bundle.putString(Keys.GAMEPLAYERSCOUNT,
				cursor.getString(cursor.getColumnIndex(Keys.GAMEPLAYERSCOUNT)));
		String id_GAME = cursor.getString(cursor.getColumnIndex(Keys.ID_GAME));
		bundle.putString(Keys.ID_GAME, id_GAME);
		//bundle.putString(Keys.GameisPlaying,
		//		cursor.getString(cursor.getColumnIndex(Keys.GameisPlaying)));
		bundle.putString(Keys.GameIsLiked,
				cursor.getString(cursor.getColumnIndex(Keys.GameIsLiked)));
		bundle.putString(Keys.GAMETYPENAME,
				cursor.getString(cursor.getColumnIndex(Keys.GAMETYPENAME)));
		bundle.putString(Keys.GAMETYPE,
				cursor.getString(cursor.getColumnIndex(Keys.GAMETYPE)));
		bundle.putString(Keys.GAMEPLATFORM,
				cursor.getString(cursor.getColumnIndex(Keys.GAMEPLATFORM)));
		bundle.putString(Keys.GAMECompanyDistributor, cursor.getString(cursor
				.getColumnIndex(Keys.GAMECompanyDistributor)));
		bundle.putString(Keys.CompanyFounded,
				cursor.getString(cursor.getColumnIndex(Keys.CompanyFounded)));
		bundle.putString(Keys.CompanyName,
				cursor.getString(cursor.getColumnIndex(Keys.CompanyName)));
		bundle.putString(Keys.EventIMAGEURL,
				cursor.getString(cursor.getColumnIndex(Keys.EventIMAGEURL)));
		return bundle;
	}
	
	public static Bundle putPGameExtraInBundle(Cursor cursor)
	{
		Bundle bundle = putGameInBundle(cursor);					
		bundle.putString(Keys.GameComments, cursor.getString(cursor
				.getColumnIndex(Keys.GameComments)));
		bundle.putString(Keys.isMember, cursor.getString(cursor
				.getColumnIndex(Keys.isMember)));
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
		return bundle;
	}

	public static Bundle putGroupInBundle(Cursor cursor) {
		Bundle bundle = new Bundle();
		bundle.putString(Keys.GROUPNAME,
				cursor.getString(cursor.getColumnIndex(Keys.GROUPNAME)));
		bundle.putString(Keys.GROUPTYPE,
				cursor.getString(cursor.getColumnIndex(Keys.GROUPTYPE)));
		bundle.putString(Keys.GROUPDESC,
				cursor.getString(cursor.getColumnIndex(Keys.GROUPDESC)));
		bundle.putString(Keys.GROUPTYPE2,
				cursor.getString(cursor.getColumnIndex(Keys.GROUPTYPE2)));
		// Changed so date and members should be
		bundle.putString(Keys.ID_CREATOR,
				cursor.getString(cursor.getColumnIndex(Keys.ID_CREATOR)));
		bundle.putString(Keys.GameIsLiked,
				cursor.getString(cursor.getColumnIndex(Keys.GameIsLiked)));
		bundle.putString(Keys.isMember,
				cursor.getString(cursor.getColumnIndex(Keys.isMember)));
		bundle.putString(Keys.GroupMemberCount,
				cursor.getString(cursor.getColumnIndex(Keys.GroupMemberCount)));
		bundle.putString(Keys.GROUPDATE,
				cursor.getString(cursor.getColumnIndex(Keys.GROUPDATE)));
		bundle.putString(Keys.ID_GROUP,
				cursor.getString(cursor.getColumnIndex(Keys.ID_GROUP)));
		bundle.putString(Keys.EventIMAGEURL,
				cursor.getString(cursor.getColumnIndex(Keys.EventIMAGEURL)));
		bundle.putString(Keys.GruopCreatorName,
				cursor.getString(cursor.getColumnIndex(Keys.GruopCreatorName)));
		return bundle;
	}
	
	public static Bundle putPWallInBundle(Cursor cursor)
	{
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
		bundle.putString(Keys.ItemType, cursor.getString(cursor
				.getColumnIndex(Keys.ItemType)));
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
		return bundle;
	}
	
	public static Bundle putPMsgInBundle(Cursor cursor)
	{
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
		return bundle;
	}
	
	public static Bundle putTempNewsInBundle(Cursor cursor)
	{
		Bundle bundle = new Bundle();
		bundle.putString(Keys.NEWSCOLID_NEWS, cursor.getString(cursor
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
		return bundle;
	}	
	
	public static Bundle putNotificInBundle(JSONObject obj) throws JSONException
	{
		Bundle bundle = new Bundle();
		bundle.putString(Keys.ID_NOTIFICATION, obj
				.getString(Keys.ID_NOTIFICATION));
		bundle.putString(Keys.NotificationType, obj
				.getString(Keys.NotificationType));
		int date = Integer.parseInt(obj
				.getString(Keys.NotificationTime));
		String returnDate = HelperClass.convertTime(date,
				Configurations.dataTemplate);
		bundle.putString(Keys.NotificationTime, returnDate);
		bundle.putString(
				Keys.NotificationFromType,
				obj.getString(
						Keys.NotificationFromType));
		bundle.putString(Keys.NotificationisRead, obj
				.getString(Keys.NotificationisRead));
		bundle.putString(Keys.PLAYERNICKNAME, obj
				.getString(Keys.PLAYERNICKNAME));
		bundle.putString(
				Keys.NotificationPlayerCount,
				obj.getString(
						Keys.NotificationPlayerCount));
		return bundle;
	}
	
	public static Bundle putPlayerInBundle(Cursor cursor)
	{
		Bundle bundle = new Bundle();
		bundle.putString(Keys.ID_PLAYER, cursor
				.getString(cursor.getColumnIndex(Keys.ID_PLAYER)));
		bundle.putString(Keys.CITY,
				cursor.getString(cursor.getColumnIndex(Keys.CITY)));
		bundle.putString(Keys.COUNTRY, cursor
				.getString(cursor.getColumnIndex(Keys.COUNTRY)));
		bundle.putString(Keys.PLAYERNICKNAME, cursor
				.getString(cursor
						.getColumnIndex(Keys.PLAYERNICKNAME)));
		bundle.putString(Keys.PLAYERAVATAR,
				cursor.getString(cursor
						.getColumnIndex(Keys.PLAYERAVATAR)));
		bundle.putString(Keys.FirstName, cursor
				.getString(cursor.getColumnIndex(Keys.FirstName)));
		bundle.putString(Keys.LastName, cursor
				.getString(cursor.getColumnIndex(Keys.LastName)));
		bundle.putString(Keys.LastName, cursor
				.getString(cursor.getColumnIndex(Keys.LastName)));
		bundle.putString(Keys.Mutual,
				cursor.getString(cursor.getColumnIndex(Keys.Mutual)));
		bundle.putString(Keys.Age,
				cursor.getString(cursor.getColumnIndex(Keys.Age)));
		bundle
				.putString(Keys.Email, cursor.getString(cursor
						.getColumnIndex(Keys.Email)));
		return bundle;
	}

	public static Bundle putCompanyInBundle(Cursor cursor) {
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
		return bundle;

	}

	public static Bundle putPEventInBundle(Cursor cursor) {
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
		return bundle;
	}
}
