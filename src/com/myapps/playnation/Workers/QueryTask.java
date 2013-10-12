package com.myapps.playnation.Workers;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import com.myapps.playnation.main.IQueryTask;


public class QueryTask extends PlaynationTask {
		private ArrayList<Bundle> results;
		private IQueryTask mFrag;

		public QueryTask(IQueryTask frag,Context context) {
			super(context);
			results = new ArrayList<Bundle>();
			this.mFrag = frag;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			mFrag.setQueryResult(results);
		}
		
		public void setResults(ArrayList<Bundle> results)
		{
			this.results = results;
		}
	}
