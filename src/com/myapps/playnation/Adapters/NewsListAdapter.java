package com.myapps.playnation.Adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myapps.playnation.R;
import com.myapps.playnation.Classes.DataSection;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Classes.NewsFeed;
import com.myapps.playnation.Classes.NewsFeedItem;
import com.myapps.playnation.Operations.LoadImage;
import com.myapps.playnation.main.ISectionAdapter;

public class NewsListAdapter extends NewsListHelper implements IShowMore {
	private LayoutInflater inflator;
	private List<NewsFeedItem> newsFeedsLists;
	ISectionAdapter context;

	public NewsListAdapter(Activity context, ArrayList<NewsFeedItem> items) {
		super(items);
		this.context = (ISectionAdapter) context;
		this.newsFeedsLists = getNewList();
		inflator = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public NewsListAdapter(Activity context, ArrayList<NewsFeedItem> items, int initialCount) {
		super(items,initialCount);
		this.context = (ISectionAdapter) context;
		this.newsFeedsLists = getNewList();
		inflator = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		NewsFeedItem item = newsFeedsLists.get(position);
		if (item != null) {
			if (item.isSection()) {
				DataSection ds = (DataSection) item;
				row = inflator.inflate(
						R.layout.component_newslist_dateselected, null);
				TextView txtNewsTitle = (TextView) row
						.findViewById(R.id.txtNewsDate);
				txtNewsTitle.setText(ds.getKey_Title());
				row.setOnClickListener(null);
				row.setOnLongClickListener(null);
				row.setSelected(false);
				row.setLongClickable(false);
				row.setEnabled(false);
				row.setClickable(false);
				row.setFocusable(false);
			} else {
				NewsFeed feed = (NewsFeed) item;
				row = inflator.inflate(R.layout.component_newslist_itemlayout,
						null);
				TextView txtTitle = (TextView) row.findViewById(R.id.txtTitle);
				ImageView img = (ImageView) row
						.findViewById(R.id.imgPlayerAvatarLog);

				String imageUrl = feed.getKey_NewsImage();

				TextView txtText = (TextView) row
						.findViewById(R.id.txtNickNameText);
				txtTitle.setText(Html.fromHtml(feed.getKey_NewsTitle().replace(
						"\\", "")));

				txtText.setText(Html.fromHtml(feed.getKey_NewsIntroText()
						.replace("\\", "")));
				img.setTag(imageUrl);
				new LoadImage(feed.getKey_NewsFeedID() + "", "news",
						Keys.newsTable, imageUrl, img, "newsitems")
						.execute(img);
			}
		}
		return row;
	}

	@Override
	public int getCount() {
	return newsFeedsLists.size();
	}

	@Override
	public Object getItem(int position) {
		return newsFeedsLists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public void showMore() {
	newsFeedsLists.addAll(getNextItems());
	}
	
	@Override
	public boolean canShowMore() {
		return super.canShowMore();
	}
}
