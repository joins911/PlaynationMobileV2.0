package com.myapps.playnation.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.myapps.playnation.R;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Operations.Configurations;
import com.myapps.playnation.Operations.DataConnector;
import com.myapps.playnation.Operations.HelperClass;
import com.myapps.playnation.Operations.MySQLinker;
import com.myapps.playnation.Operations.ServiceClass;
import com.myapps.playnation.main.BrowserFragment;
import com.myapps.playnation.main.MainActivity;
import com.myapps.playnation.main.PlaynationMobile;

public class LoginActivity extends Activity {
	private ProgressDialog progressDialog;
	private int progressbarStatus = 0;
	public LoadMainActivityTask task;
	private EditText username;
	private EditText password;
	private Button logButton;
	DataConnector con;
	private SharedPreferences prefrence;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Keys.internetStatus = HelperClass
				.isNetworkAvailable((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));
		stopService(new Intent(this, ServiceClass.class));
		startService(new Intent(this, ServiceClass.class));
		if (android.os.Build.VERSION.SDK_INT > 10) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		// UserLoginPreferences
		prefrence = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		clearPreviewsLoginInformation(prefrence);
		//Configurations.CurrentPlayerID = prefrence.getString(Keys.ID_PLAYER,
				//"12");
		//Keys.TEMPLAYERID = prefrence.getString(Keys.ID_PLAYER, "12");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		con = DataConnector.getInst();
		con.setSQLLinker(PlaynationMobile.getContext());
		username = (EditText) findViewById(R.id.password_logIn);
		password = (EditText) findViewById(R.id.username_logIn);
		logButton = (Button) findViewById(R.id.btnLogin);
		Button logGuestButton = (Button) findViewById(R.id.btnGuestLogin);
		TextView registerScreen = (TextView) findViewById(R.id.link_to_register);

		logButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				logButton.setError(null);
				if (HelperClass.EmailPassNickCheck(password, username, null)) {
					if (Keys.internetStatus) {
						if (checkCredentials()) {
							logOnlineUser();
						} else {
							logButton
									.setError("Incorrect UserName or Password!");
							Toast.makeText(getApplicationContext(),
									"Incorrect Username or Password",
									Toast.LENGTH_LONG).show();
						}
					} else
						Toast.makeText(getApplicationContext(),
								"No server Connection", Toast.LENGTH_SHORT)
								.show();
				} else if (username.getText().toString()
						.equalsIgnoreCase("admin")) {
					logOnlineAdmin();
				}
			}
		});

		if (logGuestButton != null)
			logGuestButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					logOnlineGuest();
				}
			});

		// Listening to register new account link
		registerScreen.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Switching to Register screen
				if (Keys.internetStatus) {
					Intent i = new Intent(getApplicationContext(),
							RegisterActivity.class);
					startActivity(i);
				} else {
					Toast.makeText(getApplicationContext(),
							"Server can not be reached", Toast.LENGTH_SHORT)
							.show();

				}
			}
		});

		if (!Keys.internetStatus) {
			Toast.makeText(
					getApplicationContext(),
					"There is no internet connection available. Offline Mode(Ignore login).",
					Toast.LENGTH_SHORT).show();
			logButton.setText(getResources().getString(
					R.string.loginOfflineString));
			registerScreen.setText(getResources().getString(
					R.string.registerOfflineDesc));
			logGuestButton.setVisibility(View.GONE);
		}

	}

	private void clearPreviewsLoginInformation(SharedPreferences pref) {
		SharedPreferences.Editor edit = pref.edit();
		edit.clear();
		edit.commit();
	}

	private void logOnlineAdmin() {
		Configurations.CurrentPlayerID = "959";
		startMainActivity(Configurations.appStateOnUser);
	}

	private void logOnlineUser() {
		// Login as User XXX
		startMainActivity(Configurations.appStateOnUser);
	}

	private void logOnlineGuest() {
		// Login as Guest
		startMainActivity(Configurations.appStateOnGuest);

	}

	private void startMainActivity(int appState) {
		task = new LoadMainActivityTask(appState);
		task.execute();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		setContentView(R.layout.activity_login);
	}

	class LoadMainActivityTask extends AsyncTask<Void, Integer, Void> {

		String tableName;
		int appState;
		Intent mInt;
		MySQLinker linker;
		
		private LoadMainActivityTask(int appState) {		
			Configurations.getConfigs().loadDefault(appState);
		linker = con.getLinker();
		}

		// Before running code in separate thread
		@Override
		protected void onPreExecute() {
			// Create a new progress dialog
			progressDialog = new ProgressDialog(LoginActivity.this);
			// progressDialog.setMax(100);
			progressDialog.setCancelable(false);
			progressDialog.setMessage("Downloading Data... Please wait");
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			// progressDialog.setProgress(0);
			progressDialog.show();
			// ProgressBar.show(LoginActivity.this,
			// "Loading...", "Please wait...", false, false);
		}

		// The code to be executed in a background thread.
		@Override
		protected Void doInBackground(Void... params) {
			// Get the current thread's token
			synchronized (this) {
				try {
					progressbarStatus += 0;
					progressDialog.setProgress(progressbarStatus);
					if (!linker.checkDBTableExits(Keys.gamesTable)) {
						con.getArrayFromQuerryWithPostVariable(Configurations.CurrentPlayerID,
								Keys.gamesTable, "", linker.getLastIDGames());
					}
					progressbarStatus += 40;
					progressDialog.setProgress(progressbarStatus);

					if (!linker.checkDBTableExits(Keys.companyTable)) {
						con.getArrayFromQuerryWithPostVariable(Configurations.CurrentPlayerID,
								Keys.companyTable, "", linker.getLastIDCompanies());
					}
					progressbarStatus += 20;
					progressDialog.setProgress(progressbarStatus);

					if (!linker.checkDBTableExits(Keys.groupsTable)) {
						con.getArrayFromQuerryWithPostVariable(Configurations.CurrentPlayerID,
								Keys.groupsTable, "", linker.getLastIDGroups());
					}
					progressbarStatus += 20;
					progressDialog.setProgress(progressbarStatus);

					if (!linker.checkDBTableExits(Keys.newsTable)) {
						con.getArrayFromQuerryWithPostVariable(Configurations.CurrentPlayerID,
								Keys.newsTable, "", linker.getLastIDNews());
						con.queryMiniIds();
					}
					progressbarStatus += 20;
					progressDialog.setProgress(progressbarStatus);

				} catch (Exception e) {
				}
				mInt = new Intent(getApplicationContext(), MainActivity.class);
				mInt.putExtra(Keys.AppState, appState);
			}
			return null;
		}

		// Update the progress
		@Override
		protected void onProgressUpdate(Integer... values) {
			// set the current progress of the progress dialog
			progressDialog.setProgress(values[0]);
		}

		// after executing the code in the thread
		@Override
		protected void onPostExecute(Void result) {
			// close the progress dialog
			if (progressDialog != null)
				progressDialog.dismiss();
			proceed(mInt);
		}
	}

	public void proceed(Intent mInt) {
		startActivity(mInt);
		finish();
	}

	@SuppressLint("NewApi")
	public boolean checkCredentials() {
		String userName = username.getText().toString();
		String passWord = password.getText().toString();

		return con.checkLogin(passWord, userName, prefrence);
		// return true;
	}
}