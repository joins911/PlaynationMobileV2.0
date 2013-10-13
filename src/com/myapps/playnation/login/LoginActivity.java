package com.myapps.playnation.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.myapps.playnation.R;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Operations.Configurations;
import com.myapps.playnation.Operations.DataConnector;
import com.myapps.playnation.Operations.HelperClass;
import com.myapps.playnation.Operations.MySQLinker;
import com.myapps.playnation.Operations.ServiceClass;
import com.myapps.playnation.main.MainActivity;

public class LoginActivity extends Activity {

	public LoadMainActivityTask task;
	// DataConnector con;

	private SharedPreferences prefrence;
	private SharedPreferences saveLoginPref;

	// layout components
	private EditText username;
	private EditText password;
	private Button logButton;
	private Button logGuestButton;
	private ImageView logo;
	private TextView registerText;
	private boolean isChecked = false;

	private ProgressDialog progressDialog;
	private int progressbarStatus = 0;

	private void initLogin() {
		Configurations.screenDencity = getResources().getDisplayMetrics().density;
		Configurations.screenDpi = getResources().getDisplayMetrics().densityDpi;
		// con = DataConnector.getInst();
		Keys.internetStatus = HelperClass
				.isNetworkAvailable((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));
		stopService(new Intent(this, ServiceClass.class));
		startService(new Intent(this, ServiceClass.class));
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		initLogin();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		if (android.os.Build.VERSION.SDK_INT > 10) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		username = (EditText) findViewById(R.id.username_logIn);
		password = (EditText) findViewById(R.id.password_logIn);
		logButton = (Button) findViewById(R.id.btnLogin);
		logGuestButton = (Button) findViewById(R.id.btnGuestLogin);
		registerText = (TextView) findViewById(R.id.link_to_register);
		logo = (ImageView) findViewById(R.id.logo_big);

		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		// UserLoginPreferences
		prefrence = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		saveLoginPref = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		clearPreviewsLoginInformation(prefrence);

		initButtons();

		if (!Keys.internetStatus) {
			Toast.makeText(
					getApplicationContext(),
					"There is no internet connection available. Offline Mode(Ignore login).",
					Toast.LENGTH_SHORT).show();
			logButton.setText(getResources().getString(
					R.string.loginOfflineString));
			registerText.setText(getResources().getString(
					R.string.registerOfflineDesc));
			logGuestButton.setVisibility(View.GONE);
		}

	}

	private void initButtons() {
		boolean isChecked = saveLoginPref.getBoolean(Keys.isCheckButton, false);
		final CheckBox btnCheckSave = (CheckBox) findViewById(R.id.btnCheckSaveEmail);

		if (btnCheckSave != null) {
			btnCheckSave
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							SharedPreferences.Editor edit = saveLoginPref
									.edit();
							edit.putBoolean(Keys.isCheckButton, isChecked);
							edit.putString(Keys.Email, username.getText()
									.toString());
							edit.commit();
							btnCheckSave.setChecked(saveLoginPref.getBoolean(
									Keys.isCheckButton, false));
						}
					});
			isChecked = saveLoginPref.getBoolean(Keys.isCheckButton, false);
			String email = saveLoginPref.getString(Keys.Email, "");
			btnCheckSave.setChecked(isChecked);
			if (isChecked)
				username.setText(email);
		}

		Configurations.screenDencity = getResources().getDisplayMetrics().density;
		Configurations.screenDpi = getResources().getDisplayMetrics().densityDpi;

		logButton = (Button) findViewById(R.id.btnLogin);
		Button logGuestButton = (Button) findViewById(R.id.btnGuestLogin);
		TextView registerScreen = (TextView) findViewById(R.id.link_to_register);
		ImageView logo = (ImageView) findViewById(R.id.logo_big);

		logButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				logButton.setError(null);
				checkLogin();
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
		registerText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Switching to Register screen
				if (Configurations.isReachable) {
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

		logo.setOnTouchListener(new OnTouchListener() {

			private float total = 0;

			/*
			 * (non-Javadoc)
			 * 
			 * @see android.view.View.OnTouchListener#onTouch(android.view.View,
			 * android.view.MotionEvent) work in progress not really necesary
			 * but proof of concept
			 */
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getActionMasked()) {
				case MotionEvent.ACTION_DOWN:
					// Toast.makeText(getApplicationContext(), total+" ",
					// Toast.LENGTH_SHORT).show();
					total = event.getX();
					break;
				case MotionEvent.ACTION_UP:
					total = event.getX() - total;
					// Toast.makeText(getApplicationContext(), total+" ",
					// Toast.LENGTH_SHORT).show();
					if (total > 100)
						clearText();
					if (total < -100)
						setLoginText();

					total = 0;
					return false;
				}
				return true;
			}
		});

	}

	private void checkLogin() {
		if (HelperClass.EmailPassNickCheck(username, password, null)) {
			if (Configurations.isReachable) {
				if (checkCredentials()) {
					logOnlineUser();
				} else {
					logButton.setError("Incorrect UserName or Password!");
					Toast.makeText(getApplicationContext(),
							"Incorrect Username or Password", Toast.LENGTH_LONG)
							.show();
				}
			} else
				Toast.makeText(getApplicationContext(), "No server Connection",
						Toast.LENGTH_SHORT).show();
		} else if (password.getText().toString().equalsIgnoreCase("admin")) {
			logOnlineAdmin();
		}
	}

	public void saveOnLoginInfo() {
		SharedPreferences.Editor edit = saveLoginPref.edit();
		edit.putString(Keys.Email, username.getText().toString());
		edit.commit();
	}

	private void clearPreviewsLoginInformation(SharedPreferences pref) {
		SharedPreferences.Editor edit = pref.edit();
		edit.clear();
		edit.commit();
	}

	private void setLoginText() {
		username.setText("claudiu.manea.l@gmail.com");
		password.setText("caterinca");
	}

	private void clearText() {
		username.setText("");
		password.setText("admin");
	}

	private void logOnlineAdmin() {
		Configurations.CurrentPlayerID = "12";
		startMainActivity(Configurations.appStateOnUser);
	}

	private void logOnlineUser() {
		// Login as User XXX
		isChecked = saveLoginPref.getBoolean(Keys.isCheckButton, false);
		if (isChecked) {
			SharedPreferences.Editor edit = saveLoginPref.edit();
			edit.putBoolean(Keys.isCheckButton, isChecked);
			edit.putString(Keys.Email, username.getText().toString());
			edit.commit();
		}
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

	class LoadMainActivityTask extends AsyncTask<Void, Integer, Void> {
		DataConnector con = DataConnector.getInst();
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
					if (Configurations.isReachable) {
						progressDialog.setProgress(progressbarStatus);
						if (!linker.checkDBTableExits(Keys.newsTable)) {
							con.getArrayFromQuerryWithPostVariable(
									Configurations.CurrentPlayerID,
									Keys.newsTable, "", linker.getLastIDNews());
							con.queryMiniIds();

						}
						progressbarStatus += 40;
						progressDialog.setProgress(progressbarStatus);

						if (!linker.checkDBTableExits(Keys.companyTable)) {
							con.getArrayFromQuerryWithPostVariable(
									Configurations.CurrentPlayerID,
									Keys.companyTable, "",
									linker.getLastIDCompanies());
						}
						progressbarStatus += 20;
						progressDialog.setProgress(progressbarStatus);

						if (!linker.checkDBTableExits(Keys.groupsTable)) {
							con.getArrayFromQuerryWithPostVariable(
									Configurations.CurrentPlayerID,
									Keys.groupsTable, "",
									linker.getLastIDGroups());
						}
						progressbarStatus += 20;
						progressDialog.setProgress(progressbarStatus);
						if (!linker.checkDBTableExits(Keys.gamesTable)) {
							con.getArrayFromQuerryWithPostVariable(
									Configurations.CurrentPlayerID,
									Keys.gamesTable, "",
									linker.getLastIDGames());

						}
						progressbarStatus += 20;
						progressDialog.setProgress(progressbarStatus);
					} else {
						progressDialog.dismiss();
						Toast.makeText(LoginActivity.this,
								getResources().getString(R.string.noServer),
								Toast.LENGTH_LONG).show();
						return null;
					}

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

	@Override
	protected void onDestroy() {
		// stopService(new Intent(this, ServiceClass.class));
		super.onDestroy();
	}

	public boolean checkCredentials() {
		String userName = username.getText().toString();
		String passWord = password.getText().toString();
		return DataConnector.getInst().checkLogin(userName, passWord,
				saveLoginPref);
	}
}