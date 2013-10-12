package com.myapps.playnation.Classes;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class HorizontalImageHolder extends LinearLayout {

	//private ArrayList<ImageView> images;

	public HorizontalImageHolder(Context context, ArrayList<ImageView> images) {
		super(context);
		//this.images = images;
		addImages(images);
	}

	private void addImages(ArrayList<ImageView> pics) {
		for (ImageView img : pics)
			this.addView(img);
	}

}
