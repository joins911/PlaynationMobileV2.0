package com.myapps.playnation.Workers;

import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;

import com.myapps.playnation.Classes.UserComment;
import com.myapps.playnation.Fragments.IWallFragment;
import com.myapps.playnation.Operations.DataConnector;
import com.myapps.playnation.main.ISectionAdapter;

public class PlaynationTask extends AsyncTask<Void, Void, Void> {

		private ISectionAdapter mActConn;


		public PlaynationTask(Context context) {
			mActConn = (ISectionAdapter) context;
		}

		@Override
		protected void onPreExecute() {
			mActConn.setIndeterminateVisibility(true);
		}

		@Override
		protected Void doInBackground(Void... params) {
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mActConn.setIndeterminateVisibility(false);
		}

	
}
