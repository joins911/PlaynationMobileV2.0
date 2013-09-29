package com.myapps.playnation.Operations;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.myapps.playnation.R;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.main.MainActivity;

public class ServiceClass extends Service {
	private MySQLinker linker;
	private DataConnector con;
	private Timer timer;
	private ArrayList<Bundle> playersNotificationList;	
	private int checkConnectionTimerTimeout = 500; 
	private int timerCount=0; //10s
	private int TimeOut = 5*1000;
	// private static final String tag = "ServiceClass";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		linker = DataConnector.getInst().getLinker();
		con = DataConnector.getInst();
		playersNotificationList = new ArrayList<Bundle>();
		super.onCreate();
	}

	public void createNotification() {
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent();

		// The stack builder object will contain an artificial back stack for
		// the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(MainActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);

		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		if (playersNotificationList != null)
			for (Bundle bund : playersNotificationList) {
				NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
						this)
						.setSmallIcon(R.drawable.ic_launcher)
						.setContentTitle(bund.getString(Keys.NotificationType))
						.setDefaults(Notification.DEFAULT_ALL)
						.setContentText(
								"From " + bund.getString(Keys.PLAYERNICKNAME))
						.setAutoCancel(true);
				mBuilder.setContentIntent(resultPendingIntent);
				NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				// mId allows you to update the notification later on.
				mNotificationManager.notify(
						Integer.parseInt(bund.getString(Keys.ID_NOTIFICATION)),
						mBuilder.build());

			}
	}

	TimerTask serviceStarterTask = new TimerTask() {
		@Override
		public void run() {
			if (Configurations.getConfigs().getApplicationState() == 0) {
				con.queryNotification(Configurations.CurrentPlayerID);
				for (Bundle el : linker.getSQLiteNotification(
						Keys.HomeNotificationTable,
						Configurations.CurrentPlayerID)) {
					if (el.getString(Keys.NotificationisRead).equalsIgnoreCase(
							"0")) {
						linker.updateRecord(Keys.HomeNotificationTable,
								"ID_PLAYER=" + Configurations.CurrentPlayerID,
								Keys.NotificationisRead, "1");
						playersNotificationList.add(el);
					}
				}

				createNotification();
			}

			Keys.internetStatus = HelperClass
					.isNetworkAvailable((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));
			try {
				if (linker.getMinGameID() == linker.getLastIDGames()) {
					linker.setLastIDGames(0);
				}
				if (linker.getMinGroupID() == linker.getLastIDGroups()) {
					linker.setLastIDGroups(0);
				}
				if (linker.getMinCompanyID() == linker.getLastIDCompanies()) {
					linker.setLastIDCompanies(0);
				}

				con.getArrayFromQuerryWithPostVariable("", Keys.gamesTable, "",
						linker.getLastIDGames());

				con.getArrayFromQuerryWithPostVariable("", Keys.companyTable,
						"", linker.getLastIDCompanies());

				con.getArrayFromQuerryWithPostVariable("", Keys.groupsTable,
						"", linker.getLastIDGroups());

				if (linker.getLastIDNews() == linker.getMinNewsID()) {
					linker.setLastIDNews(0);
					con.getArrayFromQuerryWithPostVariable("", Keys.newsTable,
							"", linker.getLastIDNews());
				} else {
					if (linker.getLastIDNews() > 0) {
						con.getArrayFromQuerryWithPostVariable("",
								Keys.newsServiceTable, "",
								linker.getLastIDNews());
					}
				}

			} catch (Exception e) {
				Log.e("Service", "Error Service Timer" + e.toString());
			}
		}
	};
	
	TimerTask checkServerStatus = new TimerTask() {
		
		@Override
		public void run(){
			if(timerCount==4) checkConnectionTimerTimeout = 10*1000;
			 try{
			        URL myUrl = new URL("http://playnation.eu");
			        URLConnection connection = myUrl.openConnection();
			        connection.setConnectTimeout(TimeOut);
			        connection.connect();
			        Configurations.isReachable = true;
			    } catch (Exception e) {
			        // Handle your exceptions
			    	Configurations.isReachable = false;
			    }
			 timerCount++;
		}		
	};

	static int MINUTES = 1;

	private void showNotification() {
		stopTimer();

		if (Keys.internetStatus) {
			timer = new Timer();
			timer.scheduleAtFixedRate(serviceStarterTask, MINUTES * 60 * 1000,
					MINUTES * 60 * 1000);
		}
	}
	
	private void getServerConnection(){
		if(Keys.internetStatus) {
			if(timer == null) timer = new Timer();
			else timer.scheduleAtFixedRate(checkServerStatus,checkConnectionTimerTimeout , checkConnectionTimerTimeout);
		}
	}

	private void stopTimer() {
		if (timer != null)
			timer.cancel();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		showNotification();
		getServerConnection();
	}

}
