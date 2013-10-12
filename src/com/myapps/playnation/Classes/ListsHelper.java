package com.myapps.playnation.Classes;

import java.util.ArrayList;

import com.myapps.playnation.Operations.Configurations;
import com.myapps.playnation.main.MainActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class ListsHelper extends BaseAdapter{

	private int counter;
	private int increment;
	ArrayList<Bundle> list;
	
	public ListsHelper(ArrayList<Bundle> list,int increment)
	{		
		this.list = list;
		this.increment = increment;
		this.counter = 0;
	}
	
	public void getListSection(int posStart,int posEnd)
	{
		
	}
	
	public boolean canShowMore()
	{
		return counter<list.size();
	}
	
	public ArrayList<Bundle> getNewList(int initialCount)
	{
		ArrayList<Bundle> temp = new ArrayList<Bundle>();
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
	public ArrayList<Bundle> getNextItems()
	{
		ArrayList<Bundle> temp = new ArrayList<Bundle>();
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
	
	public ArrayList<Bundle> getNextItems(int fromIndex)
	{
		ArrayList<Bundle> temp = new ArrayList<Bundle>();
		int initial =Configurations.getConfigs().getInitialListCount();
		for(int i=fromIndex;i<fromIndex + initial;i++)
		{
			temp.add(list.get(i));
		}
		counter = counter + fromIndex + initial;
		return temp;
	}

}
