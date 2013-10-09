package com.myapps.playnation.Adapters;

import java.util.ArrayList;

import com.myapps.playnation.Classes.NewsFeedItem;
import com.myapps.playnation.Operations.Configurations;
import android.widget.BaseAdapter;

public abstract class NewsListHelper extends BaseAdapter{

	private int counter;
	private int increment;
	private int initialCount;
	ArrayList<NewsFeedItem> list;
	
	public NewsListHelper(ArrayList<NewsFeedItem> list)
	{		
		this.list = list;
		increment = Configurations.getConfigs().getListsIncrement();
		initialCount = Configurations.getConfigs().getInitialListCount();
		counter = 0;		
	}
	
	public NewsListHelper(ArrayList<NewsFeedItem> list,int initialCount)
	{		
		this.list = list;
		increment = Configurations.getConfigs().getListsIncrement();
		this.initialCount = initialCount;
		counter = 0;		
	}
	
	public boolean canShowMore()
	{
		return counter<list.size();
	}
	/*
	public ArrayList<NewsFeedItem> getNewList(int initialIndex)
	{
		ArrayList<NewsFeedItem> temp = new ArrayList<NewsFeedItem>();
		int max;
		int tempMax = initialIndex+initialCount;
		if(tempMax > list.subList(initialIndex, list.size()-1).size()) max = list.size();
		else max = tempMax;
		for(int i=initialIndex;i<max;i++)
		{
			temp.add(list.get(i));
		}
		counter = max;
		return temp;
	}*/
	
	public ArrayList<NewsFeedItem> getNewList()
	{
		ArrayList<NewsFeedItem> temp = new ArrayList<NewsFeedItem>();
		int max;
		if(initialCount>list.size()) max = list.size();
		else max = initialCount;
		for(int i=0;i<max;i++)
		{
			temp.add(list.get(i));
		}
		counter = max;
		return temp;
	}
	public ArrayList<NewsFeedItem> getNextItems()
	{
		ArrayList<NewsFeedItem> temp = new ArrayList<NewsFeedItem>();
		int max;
		if(counter+increment>list.size()) max = list.size();
		else max = counter+increment;
		for(int i=counter;i<max;i++)
		{
			temp.add(list.get(i));
		}
		counter = counter + increment;
		return temp;
	}
	
	public ArrayList<NewsFeedItem> getNextItems(int fromIndex)
	{
		ArrayList<NewsFeedItem> temp = new ArrayList<NewsFeedItem>();
		int initial =Configurations.getConfigs().getInitialListCount();
		for(int i=fromIndex;i<fromIndex + initial;i++)
		{
			temp.add(list.get(i));
		}
		counter = counter + fromIndex + initial;
		return temp;
	}

}
