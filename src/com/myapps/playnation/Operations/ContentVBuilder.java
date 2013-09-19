package com.myapps.playnation.Operations;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;

import com.myapps.playnation.Classes.Keys;

public class ContentVBuilder {

	public static ContentValues putExtraGameInContentV(JSONObject obj)
			throws JSONException {
		ContentValues temp = putGameInContentV(obj);
		temp.put(Keys.ID_GAME, obj.getInt(Keys.ID_GAME));
		temp.put(Keys.ID_PLAYER, obj.getInt(Keys.ID_PLAYER) + "");
		temp.put(Keys.GameComments, obj.getString(Keys.GameComments));

		temp.put(Keys.GamesisSubscribed, obj.getInt(Keys.GamesisSubscribed)
				+ "");
		temp.put(Keys.GamePostCount, obj.getInt(Keys.GamePostCount) + "");
		temp.put(Keys.GamesSubscriptionTime,
				obj.getString(Keys.GamesSubscriptionTime));
		temp.put(Keys.CompanyFounded, obj.getString(Keys.CompanyFounded));
		temp.put(Keys.CompanyName, obj.getString(Keys.GAMECompanyDeveloper));
		String imageUrl = obj.getString(Keys.EventIMAGEURL);
		temp.put(Keys.EventIMAGEURL, imageUrl);
		return temp;
	}

	public static ContentValues putGameInContentV(JSONObject obj)
			throws JSONException {
		ContentValues temp = new ContentValues();
		temp.put(Keys.GAMENAME, obj.getString(Keys.GAMENAME));
		String gameType = obj.getString(Keys.GAMETYPE);
		temp.put(Keys.GAMETYPE, gameType);
		temp.put(Keys.GameIsLiked, obj.getString(Keys.GameIsLiked));
		temp.put(Keys.GameisPlaying, obj.getString(Keys.GameisPlaying));
		temp.put(Keys.CompanyName, obj.getString(Keys.GAMECompanyDeveloper));
		temp.put(Keys.GAMEDESC, obj.getString(Keys.GAMEDESC));
		temp.put(Keys.GAMEDATE, obj.getString(Keys.GAMEDATE));
		temp.put(Keys.RATING, obj.getString(Keys.RATING));
		temp.put(Keys.GAMEESRB, obj.getString(Keys.GAMEESRB));
		String imageUrl = obj.getString(Keys.EventIMAGEURL);
		temp.put(Keys.EventIMAGEURL, imageUrl);
		temp.put(Keys.GAMEURL, obj.getString(Keys.GAMEURL));
		temp.put(Keys.GAMEPLAYERSCOUNT, obj.getString(Keys.GAMEPLAYERSCOUNT));
		temp.put(Keys.GAMETYPENAME, obj.getString(Keys.GAMETYPENAME));
		temp.put(Keys.GAMEPLATFORM, obj.getString(Keys.GAMEPLATFORM));
		temp.put(Keys.GAMECompanyDistributor,
				obj.getString(Keys.GAMECompanyDistributor));
		temp.put(Keys.CompanyFounded, obj.getString(Keys.CompanyFounded));
		temp.put(Keys.CompanyName, obj.getString(Keys.GAMECompanyDeveloper));
		return temp;
	}

	public static ContentValues putGroupInContentV(JSONObject obj,
			String ID_Groups) throws JSONException {
		ContentValues temp = new ContentValues();
		temp.put(Keys.GROUPNAME, obj.getString(Keys.GROUPNAME));
		temp.put(Keys.GROUPTYPE, obj.getString(Keys.GROUPTYPE));
		temp.put(Keys.GROUPDESC, obj.getString(Keys.GROUPDESC));
		temp.put(Keys.GROUPTYPE2, obj.getString(Keys.GROUPTYPE2));
		temp.put(Keys.ID_CREATOR, obj.getString(Keys.ID_CREATOR));
		// Changed so date and members should be
		temp.put(Keys.GroupMemberCount, obj.getString(Keys.GroupMemberCount));
		temp.put(Keys.GameIsLiked, obj.getString(Keys.GameIsLiked));
		temp.put(Keys.isMember, obj.getString(Keys.isMember));
		// Changed so date and members should be
		temp.put(Keys.GroupMemberCount, obj.getString(Keys.GroupMemberCount));
		String imageUrl = obj.getString(Keys.EventIMAGEURL);
		temp.put(Keys.EventIMAGEURL, imageUrl);
		temp.put(Keys.GROUPDATE, HelperClass.convertTime(Integer.parseInt(obj
				.getString(Keys.GROUPDATE)), new SimpleDateFormat(
				"EEEE,MMMM d,yyyy h:mm,a", Locale.getDefault())));

		temp.put(Keys.ID_GROUP, ID_Groups);
		String creator = obj.getString(Keys.PLAYERNICKNAME);
		temp.put(Keys.GruopCreatorName, creator);
		return temp;
	}

	public static ContentValues putTempNewsInContentV(JSONObject obj,
			String id, String id_game) throws JSONException {
		ContentValues temp = new ContentValues();

		temp.put(Keys.NEWSCOLID_NEWS, obj.optString(Keys.NEWSCOLID_NEWS));
		temp.put(Keys.ID_OWNER, obj.optString(Keys.ID_OWNER));
		temp.put(Keys.OWNERTYPE, obj.optString(Keys.OWNERTYPE));
		temp.put(Keys.ID_GAME, id_game);
		temp.put(Keys.NEWSCOLNEWSTEXT, obj.getString(Keys.NEWSCOLNEWSTEXT));
		temp.put(Keys.NEWSCOLINTROTEXT, obj.getString(Keys.NEWSCOLINTROTEXT));
		temp.put(Keys.NEWSCOLPOSTINGTIME,
				obj.getString(Keys.NEWSCOLPOSTINGTIME));
		temp.put(Keys.NEWSCOLHEADLINE, obj.getString(Keys.NEWSCOLHEADLINE));
		temp.put(
				Keys.Author,
				obj.getString(Keys.FirstName) + " "
						+ obj.getString(Keys.LastName));
		String imageUrl = obj.getString(Keys.NEWSCOLIMAGE);

		temp.put(Keys.NEWSCOLIMAGE, imageUrl);
		return temp;
	}

	public static ContentValues putCompanyInContentV(JSONObject obj)
			throws JSONException {
		ContentValues map = new ContentValues();
		map.put(Keys.CompanyName, obj.getString(Keys.CompanyName));
		map.put(Keys.CompanyEmployees, obj.getInt(Keys.CompanyEmployees) + "");
		String imageUrl = obj.getString(Keys.CompanyImageURL);

		map.put(Keys.CompanyImageURL, imageUrl);
		map.put(Keys.CompanyAddress, obj.getString(Keys.CompanyAddress));
		map.put(Keys.CompanyDesc, obj.getString(Keys.CompanyDesc));
		map.put(Keys.GameIsLiked, obj.getString(Keys.GameIsLiked));

		String[] foundYear = obj.getString(Keys.CompanyFounded).split("-");
		map.put(Keys.CompanyFounded, foundYear[0]);
		map.put(Keys.CompanyURL, obj.getString(Keys.CompanyURL));
		map.put(Keys.CompanyCreatedTime, HelperClass.convertTime(
				Integer.parseInt(obj.getString(Keys.CompanyCreatedTime)),
				new SimpleDateFormat("dd/MM/yyyy")));
		map.put(Keys.CompanyOwnership, obj.getString(Keys.CompanyOwnership));
		map.put(Keys.CompanyType, obj.getString(Keys.CompanyType));
		map.put(Keys.CompanyNewsCount, obj.getInt(Keys.CompanyNewsCount) + "");
		map.put(Keys.CompanyEventCount, obj.getInt(Keys.CompanyEventCount) + "");
		map.put(Keys.CompanyGameCount, obj.getInt(Keys.CompanyGameCount) + "");
		map.put(Keys.CompanySocialRating,
				obj.getString(Keys.CompanySocialRating));
		return map;
	}

	public static ContentValues putPEventsInContentV(JSONObject obj)
			throws JSONException {
		ContentValues map = new ContentValues();
		map.put(Keys.EventID_COMPANY, obj.getInt(Keys.EventID_COMPANY) + "");
		map.put(Keys.ID_GAME, obj.getInt(Keys.ID_GAME) + "");
		map.put(Keys.ID_PLAYER, obj.getInt(Keys.ID_PLAYER) + "");
		map.put(Keys.ID_GROUP, obj.getInt(Keys.ID_GROUP) + "");
		map.put(Keys.EventID_TEAM, obj.getInt(Keys.EventID_TEAM) + "");
		map.put(Keys.EventIMAGEURL, obj.getString(Keys.EventIMAGEURL));
		map.put(Keys.EventDescription, obj.getString(Keys.EventDescription));
		map.put(Keys.EventHeadline, obj.getString(Keys.EventHeadline));
		map.put(Keys.EventTime, HelperClass.convertTime(
				Integer.parseInt(obj.getString(Keys.EventTime)),
				Configurations.dataTemplate));
		map.put(Keys.EventLocation, obj.getString(Keys.EventLocation));
		map.put(Keys.EventInviteLevel, obj.getString(Keys.EventInviteLevel));
		map.put(Keys.EventIsPublic,
				HelperClass.returnEventPrivacy(obj.getInt(Keys.EventIsPublic)));
		map.put(Keys.EventType, obj.getString(Keys.EventType));
		map.put(Keys.EventIsExpired, obj.getInt(Keys.EventIsExpired) + "");
		return map;
	}

	public static ContentValues putPlayerInContentV(JSONObject obj)
			throws JSONException {
		ContentValues map = new ContentValues();

		map.put(Keys.ID_PLAYER, obj.getString(Keys.ID_PLAYER));
		map.put(Keys.CITY, obj.getString(Keys.CITY));
		map.put(Keys.COUNTRY, obj.getString(Keys.COUNTRY));
		map.put(Keys.PLAYERNICKNAME, obj.getString(Keys.PLAYERNICKNAME));
		map.put(Keys.Email, obj.getString(Keys.Email));
		String imageUrl = obj.getString(Keys.PLAYERAVATAR);
		map.put(Keys.PLAYERAVATAR, imageUrl);
		String firstName = obj.getString(Keys.FirstName);
		String lastName = obj.getString(Keys.LastName);
		map.put(Keys.FirstName, firstName);
		map.put(Keys.LastName, lastName);
		map.put(Keys.Age, obj.getString(Keys.Age));
		return map;
	}

	public static ContentValues putMessageInContentV(JSONObject obj)
			throws JSONException {
		ContentValues map = new ContentValues();
		map.put(Keys.ID_MESSAGE, obj.getInt(Keys.ID_MESSAGE));
		map.put(Keys.MessageID_CONVERSATION,
				obj.getInt(Keys.MessageID_CONVERSATION) + "");
		map.put(Keys.ID_PLAYER, obj.getInt(Keys.ID_PLAYER) + "");
		map.put(Keys.PLAYERNICKNAME, obj.getString(Keys.PLAYERNICKNAME));
		map.put(Keys.PLAYERAVATAR, obj.getString(Keys.PLAYERAVATAR));
		map.put(Keys.MessageText, HelperClass.returnUnserializedText(obj
				.getString(Keys.MessageText)));
		map.put(Keys.MessageTime, HelperClass.convertTime(
				Integer.parseInt(obj.getString(Keys.MessageTime)),
				Configurations.dataTemplate));
		return map;

	}

	public static ContentValues putPWallInContentV(JSONObject obj)
			throws JSONException {
		ContentValues map = new ContentValues();

		map.put(Keys.WallPosterDisplayName,
				obj.getString(Keys.WallPosterDisplayName) + "");
		map.put(Keys.ID_WALLITEM, obj.getInt(Keys.ID_WALLITEM));
		map.put(Keys.ID_OWNER, obj.getInt(Keys.ID_OWNER) + "");
		map.put(Keys.ItemType, obj.getString(Keys.ItemType));
		map.put(Keys.WallLastActivityTime,
				obj.getString(Keys.WallLastActivityTime));
		map.put(Keys.WallMessage, HelperClass.returnUnserializedText(obj
				.getString(Keys.WallMessage)));
		map.put(Keys.WallOwnerType, obj.getString(Keys.WallOwnerType));
		map.put(Keys.WallPostingTime, obj.getString(Keys.WallPostingTime));
		String imageUrl = obj.getString(Keys.PLAYERAVATAR);
		map.put(Keys.PLAYERAVATAR, imageUrl);
		return map;
	}

	// Same as above only diff WallPostingTime&WallLastActivity
	public static ContentValues putPWallRepInContentV(JSONObject obj)
			throws JSONException {
		ContentValues m = new ContentValues();
		m.put(Keys.WallPosterDisplayName,
				obj.getString(Keys.WallPosterDisplayName) + "");
		m.put(Keys.ID_WALLITEM, obj.getInt(Keys.ID_WALLITEM));
		m.put(Keys.ID_ORGOWNER, obj.getInt(Keys.ID_ORGOWNER) + "");
		m.put(Keys.PLAYERAVATAR, obj.getString(Keys.PLAYERAVATAR) + "");
		m.put(Keys.WallLastActivityTime, HelperClass.convertTime(
				Integer.parseInt(obj.getString(Keys.WallLastActivityTime)),
				Configurations.dataTemplate));
		m.put(Keys.WallMessage, obj.getString(Keys.WallMessage));
		m.put(Keys.WallOwnerType, obj.getString(Keys.WallOwnerType));
		m.put(Keys.WallPostingTime, HelperClass.convertTime(
				Integer.parseInt(obj.getString(Keys.WallPostingTime)),
				Configurations.dataTemplate));
		return m;
	}
}
