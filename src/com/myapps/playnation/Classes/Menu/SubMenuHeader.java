package com.myapps.playnation.Classes.Menu;

import android.os.Bundle;

public class SubMenuHeader extends SubMenuItem{

	private Bundle mHeaderInfo; 
	
	public SubMenuHeader(String title, Bundle bundle) {
		super(title);	
		mHeaderInfo= bundle;
	}
	
	public Bundle getInfo()
	{
		return mHeaderInfo;
	}

}

