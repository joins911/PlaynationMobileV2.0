package com.myapps.playnation.Adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.myapps.playnation.R;
import com.myapps.playnation.Classes.ExpandbleParent;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Fragments.DialogSendCommentFragment;
import com.myapps.playnation.Operations.Configurations;
import com.myapps.playnation.Operations.DataConnector;
import com.myapps.playnation.Operations.HelperClass;

public class HomeWallExpListAdapter extends BaseExpandableListAdapter {
	private LayoutInflater inflater;
	private ArrayList<ExpandbleParent> mParent;
	private Context context;
	private DataConnector con;
	private FragmentManager fragmentManager;

	// Only used as mark which class is currently present.
	// private Object currentFragment;

	public HomeWallExpListAdapter(Context context,
			ArrayList<ExpandbleParent> parent, FragmentManager frag) {
		mParent = parent;
		inflater = LayoutInflater.from(context);
		this.context = context;
		con = DataConnector.getInst();
		this.fragmentManager = frag;

	}

	// ---------------------------------------------------------------

	// ---------------------------------------------------------------

	@Override
	// counts the number of group/parent items so the list knows how many
	// times calls getGroupView() method
	public int getGroupCount() {
		return mParent.size();
	}

	@Override
	// counts the number of children items so the list knows how many times
	// calls getChildView() method
	public int getChildrenCount(int i) {
		if (mParent.get(i).getArrayChildren() != null)
			return mParent.get(i).getArrayChildren().size();
		else
			return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mParent.get(groupPosition).getFirstChild();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mParent.get(groupPosition).getArrayChildren().get(childPosition);
	}

	@Override
	public long getGroupId(int i) {
		return i;
	}

	@Override
	public long getChildId(int i, int i1) {
		return i1;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	// in this method you must set the text to see the parent/group on the
	// list
	public View getGroupView(int groupPosition, boolean b, View view,
			ViewGroup viewGroup) {

		TextView txEHeadline;
		TextView txELocation;
		TextView txEDate;
		TextView txText;

		Bundle mapEntry = mParent.get(groupPosition).getFirstChild();
		view = inflater.inflate(R.layout.component_homewall_eitem_layout,
				viewGroup, false);
		txEHeadline = (TextView) view.findViewById(R.id.name_TV);
		txText = (TextView) view.findViewById(R.id.content_TV);
		txEDate = (TextView) view.findViewById(R.id.time_TV);
		// ImageView img = (ImageView) view.findViewById(R.id.imgEvent);
		// img.setImageResource(R.drawable.no_forum_100x100);

		txEHeadline
				.setText("" + mapEntry.getString(Keys.WallPosterDisplayName));
		txText.setText("" + Html.fromHtml(mapEntry.getString(Keys.WallMessage)));
		txEDate.setText("Date: "
				+ HelperClass.convertTime(Integer.parseInt(mapEntry
						.getString(Keys.WallPostingTime)),
						Configurations.dataTemplate));
		isLiked(view, mapEntry);
		addComment(view, mapEntry);
		return view;
	}

	int index = 0;

	@SuppressLint("SimpleDateFormat")
	@Override
	// in this method you must set the text to see the children on the list
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View view, ViewGroup viewGroup) {
		view = null;
		// return the entire view

		TextView txEHeadline;
		TextView txELocation;
		TextView txEDate;
		TextView txText;

		if (!mParent.get(groupPosition).getArrayChildren().isEmpty()) {
			Bundle mapEntry = mParent.get(groupPosition).getArrayChildren()
					.get(childPosition);
			if (mapEntry != null) {
				view = inflater.inflate(
						R.layout.component_homewall_eitem_layout, viewGroup,
						false);
				txEHeadline = (TextView) view.findViewById(R.id.name_TV);
				txText = (TextView) view.findViewById(R.id.content_TV);
				txEDate = (TextView) view.findViewById(R.id.time_TV);
				ImageView img = (ImageView) view.findViewById(R.id.imgEvent);
				img.setImageResource(R.drawable.event);

				txEHeadline.setText(""
						+ mapEntry.getString(Keys.WallPosterDisplayName));
				txText.setText(""
						+ Html.fromHtml(mapEntry.getString(Keys.WallMessage)));
				txEDate.setText("Date: "
						+ mapEntry.getString(Keys.WallPostingTime));
				isLiked(view, mapEntry);

			}
		}
		return view;
	}

	@Override
	public boolean isChildSelectable(int i, int i1) {
		return true;
	}

	/**
	 * Controls function isLiked as well send and change the current Like to
	 * UnLike and vice versa.
	 * 
	 * @param v
	 * @param b
	 */
	public void isLiked(View v, final Bundle b) {
		final TextView txlike = (TextView) v.findViewById(R.id.like_button);
		ImageView imgLIke = (ImageView) v.findViewById(R.id.like_img);
		if (b != null) {

			String isLiked = b.getString(Keys.GameIsLiked);
			if (isLiked.equals("1")) {
				txlike.setText(context.getResources().getString(
						R.string.btnUnlike));
			} else {
				txlike.setText(context.getResources().getString(
						R.string.btnLike));
			}
			if (Configurations.getConfigs().getApplicationState() != 0) {
				txlike.setVisibility(View.GONE);
				imgLIke.setVisibility(View.GONE);
			}
			txlike.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (txlike.getText().equals("Like")) {
						con.functionQuery(Configurations.CurrentPlayerID,
								b.getString(Keys.ID_WALLITEM),
								"wallFunction.php", Keys.POSTFUNCOMMANTLike, "");
						txlike.setText(context.getResources().getString(
								R.string.btnUnlike));
					} else {
						con.functionQuery(Configurations.CurrentPlayerID,
								b.getString(Keys.ID_WALLITEM),
								"wallFunction.php", Keys.POSTFUNCOMMANTUnLike,
								"");
						txlike.setText(context.getResources().getString(
								R.string.btnLike));
					}

				}
			});

			imgLIke.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (txlike.getText().equals("Like")) {
						con.functionQuery(Configurations.CurrentPlayerID,
								b.getString(Keys.ID_WALLITEM),
								"wallFunction.php", Keys.POSTFUNCOMMANTLike, "");
						txlike.setText(context.getResources().getString(
								R.string.btnUnlike));
					} else {
						con.functionQuery(Configurations.CurrentPlayerID,
								b.getString(Keys.ID_WALLITEM),
								"wallFunction.php", Keys.POSTFUNCOMMANTUnLike,
								"");
						txlike.setText(context.getResources().getString(
								R.string.btnLike));
					}
				}
			});

		}
	}

	public void addComment(View v, final Bundle b) {
		TextView txtComment = (TextView) v.findViewById(R.id.comment_text);
		txtComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogFragment dialog = new DialogSendCommentFragment();
				dialog.show(fragmentManager, "SendComment");
				Bundle argss = new Bundle();
				argss.putString(Keys.ID_PLAYER, Configurations.CurrentPlayerID);
				argss.putString(Keys.WallOwnerType, "player");
				argss.putString(Keys.ID_WALLITEM, b.getString(Keys.ID_WALLITEM));
				dialog.setArguments(argss);
			}
		});
	}
	// @Override
	// public void registerDataSetObserver(DataSetObserver observer) {
	// /* used to make the notifyDataSetChanged() method work */
	// super.registerDataSetObserver(observer);
	// }

	/*
	 * @Override public void onGroupExpanded(int groupPosition) {
	 * super.onGroupExpanded(groupPosition);
	 * //mExpandableList.expandGroup(groupPosition); if (groupPosition !=
	 * lastParent) { mExpandableList.collapseGroup(lastParent); } array = new
	 * ArrayList<Bundle>(); Bundle mapEntry =
	 * mParent.get(groupPosition).getFirstChild(); if (lastParent ==
	 * groupPosition) {
	 * con.queryPlayerWallReplices(mapEntry.getString(Keys.ID_WALLITEM),
	 * Configurations.CurrentPlayerID); array =
	 * con.getTable(Keys.HomeWallRepliesTable,
	 * mapEntry.getString(Keys.ID_WALLITEM));
	 * mParent.get(groupPosition).setArrayChildren(array); } lastParent =
	 * groupPosition; notifyDataSetChanged(); }
	 */

}
