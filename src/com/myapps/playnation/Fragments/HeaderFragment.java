package com.myapps.playnation.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.myapps.playnation.R;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Fragments.TabHosts.CompaniesTabHostDesc;
import com.myapps.playnation.Fragments.TabHosts.GameTabHostDesc;
import com.myapps.playnation.Fragments.TabHosts.GroupTabHostDesc;
import com.myapps.playnation.Fragments.TabHosts.HomeTabHostDesc;
import com.myapps.playnation.Fragments.TabHosts.PlayersTabHostDesc;
import com.myapps.playnation.Fragments.TabHosts.TabHostDesc;
import com.myapps.playnation.Fragments.Tabs.Home.XHomeEditProfileFragment;
import com.myapps.playnation.Operations.Configurations;
import com.myapps.playnation.Operations.DataConnector;
import com.myapps.playnation.Operations.LoadImage;
import com.myapps.playnation.main.ISectionAdapter;
import com.myapps.playnation.main.MainActivity;

public class HeaderFragment extends Fragment{
	private TabHostDesc mTabFrag;
	private View mView;
	private TextView gName;
	private TextView gType;
	private TextView ratingTV;
	private TextView gRating;
	private ImageView gImage;
	private int state;
	private DataConnector con;
	ISectionAdapter actContext;
	
	public HeaderFragment()
	{
		
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		actContext = (ISectionAdapter) getActivity();
		if (state == Configurations.GamesSTATE) {
			mTabFrag = new GameTabHostDesc();
		}
		if (state == Configurations.GroupsSTATE) {
			mTabFrag = new GroupTabHostDesc();
		}
		if (state == Configurations.PlayersSTATE) {
			mTabFrag = new PlayersTabHostDesc();
		}
		if (state == Configurations.CompaniesSTATE) {
			mTabFrag = new CompaniesTabHostDesc();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		state = getArguments().getInt(Keys.ARG_POSITION);
		con = DataConnector.getInst();
		if (state == Configurations.GamesSTATE) {
			mTabFrag = new GameTabHostDesc();
			mTabFrag.setArguments(getArguments());
			getChildFragmentManager().beginTransaction()
					.replace(R.id.container_games, mTabFrag).commit();
		}
		if (state == Configurations.GroupsSTATE) {
			mTabFrag = new GroupTabHostDesc();
			mTabFrag.setArguments(getArguments());
			getChildFragmentManager().beginTransaction()
					.replace(R.id.container_games, mTabFrag).commit();
		}
		if (state == Configurations.PlayersSTATE) {
			mTabFrag = new PlayersTabHostDesc();
			mTabFrag.setArguments(getArguments());
			getChildFragmentManager().beginTransaction()
					.replace(R.id.container_home, mTabFrag).commit();
		}
		if (state == Configurations.CompaniesSTATE) {
			mTabFrag = new CompaniesTabHostDesc();
			mTabFrag.setArguments(getArguments());
			getChildFragmentManager().beginTransaction()
					.replace(R.id.container_games, mTabFrag).commit();
		}
	}
	
	public void onSavedInstanceState(Bundle outState) {
		outState.putAll(getArguments());
		super.onSaveInstanceState(outState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View header = null;

			if(!(state == Configurations.PlayersSTATE)){
			mView = inflater.inflate(R.layout.wrapper_header_games, container,
					false);
			header = mView.findViewById(R.id.include_TabHolder_Games);

			gName = (TextView) header.findViewById(R.id.gameM_NameText_TView);
			gType = (TextView) header.findViewById(R.id.gameM_TypeText_TView);
			gRating = (TextView) header
					.findViewById(R.id.gameM_RatingsNr_TView);
			ratingTV = (TextView) header
					.findViewById(R.id.gamesM_ratingString_TView);
			gImage = (ImageView) header.findViewById(R.id.gameM_headerPic);
			}
			if (state == Configurations.GamesSTATE) {
				gName.setText(getArguments().getString(Keys.GAMENAME));
				gType.setText(getArguments().getString(Keys.GAMETYPE));
				gRating.setText(getArguments().getString(Keys.RATING));
				String imageUrl = getArguments().getString(Keys.EventIMAGEURL);

				gImage.setTag(imageUrl);
				 new LoadImage(getArguments().getString(Keys.ID_GAME), "game",
						             Keys.gamesTable, imageUrl, gImage, "games")
						             .execute(gImage);

			}
			if (state == Configurations.GroupsSTATE) {
				gName.setText(getArguments().getString(Keys.GROUPNAME));
				gType.setText(getArguments().getString(Keys.GROUPTYPE) + " "
						+ getArguments().getString(Keys.GROUPTYPE2));
				ratingTV.setText("");

				gRating.setVisibility(View.INVISIBLE);
				String imageUrl = getArguments().getString(Keys.EventIMAGEURL);

				gImage.setTag(imageUrl);
				new LoadImage(getArguments().getString(Keys.ID_GROUP), "group",
						            Keys.groupsTable, imageUrl, gImage, "groups")
						            .execute(gImage);
			}
			if (state == Configurations.PlayersSTATE) {
				mView = inflater.inflate(R.layout.wrapper_header_home,
						container, false);
				header = mView.findViewById(R.id.include_TabHolder_Home);
				TextView edit = (TextView) mView.findViewById(R.id.txtEdit);
				edit.setVisibility(View.GONE);

				con.queryPlayerInfo(Configurations.CurrentPlayerID);
				header = con.populatePlayerGeneralInfo(header, "Wall",
						Configurations.CurrentPlayerID);

			}
			if (state == Configurations.CompaniesSTATE) {
				gName.setText(getArguments().getString(Keys.CompanyName));
				gType.setText(getArguments().getString(Keys.CompanyOwnership));
				gRating.setText(getArguments().getString(
						Keys.CompanySocialRating));
				String imageUrl = getArguments()
						.getString(Keys.CompanyImageURL);

				gImage.setTag(imageUrl);
				new LoadImage(getArguments().getString(Keys.EventID_COMPANY),
						            "company", Keys.companyTable, imageUrl, gImage,
						            "companies").execute(gImage);
				ratingTV.setText("");
			}		
		return mView;
	}

	public void switchToTab(int tabIndex) {
		if (mTabFrag != null)
			mTabFrag.switchToTab(tabIndex);
	}
	
	public void searchFunction(String args)
	{
		
	}
	
	@Override
	public void onDestroy() {
		mView = null;
		super.onDestroy();
	}
}
