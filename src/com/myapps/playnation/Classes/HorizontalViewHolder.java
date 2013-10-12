package com.myapps.playnation.Classes;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

public class HorizontalViewHolder extends LinearLayout {

	//private ArrayList<ImageView> images;

	public HorizontalViewHolder(Context context, ArrayList<View> views) {
		super(context);
		//this.images = images;
		addViews(views);
	}

	private void addViews(ArrayList<View> views) {
		for (View view : views)
			this.addView(view);
	}

}
