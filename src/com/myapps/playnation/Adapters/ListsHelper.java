package com.myapps.playnation.Adapters;

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
	private int initialCount;
	ArrayList<Bundle> list;
	
	public ListsHelper(ArrayList<Bundle> list)
	{		
		this.list = list;
		increment = Configurations.getConfigs().getListsIncrement();
		initialCount = Configurations.getConfigs().getInitialListCount();
		counter = 0;		
	}
	
	public boolean canShowMore()
	{
		return counter<list.size();
	}
	
	public ArrayList<Bundle> getNewList(int initialIndex)
	{
		ArrayList<Bundle> temp = new ArrayList<Bundle>();
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
	}
	
	public ArrayList<Bundle> getNewList()
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
