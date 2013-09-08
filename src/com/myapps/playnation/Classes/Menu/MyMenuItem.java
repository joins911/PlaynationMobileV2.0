package com.myapps.playnation.Classes.Menu;

import java.util.ArrayList;

public class MyMenuItem {
	
	private String mTitle;
	private ArrayList<SubMenuItem> mSubItems;
	
	public MyMenuItem(String title)
	{
		mTitle = title;
	}
	
	public MyMenuItem(String title, ArrayList<SubMenuItem> items)
	{
		mTitle = title;
		mSubItems = items;
	}
	
	public void buildSubItems(String[] arr)
	{
		ArrayList<SubMenuItem> items = new ArrayList<SubMenuItem>();
		for(String itm : arr)
		{
			items.add(new SubMenuItem(itm));
		}
		mSubItems = items;
	}
	
	public void buildSubItems(ArrayList<String> arr)
	{
		ArrayList<SubMenuItem> items = new ArrayList<SubMenuItem>();
		for(String itm : arr)
		{
			items.add(new SubMenuItem(itm));
		}
		mSubItems = items;
	}
	
	public void setTitle(String title)
	{
		mTitle = title;
		}
	public String getTitle()
	{
		return mTitle;
	}
	public ArrayList<SubMenuItem> getSubItems()
	{
		return mSubItems;
	}
}

