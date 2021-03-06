package com.myapps.playnation.Fragments.TabHosts.Game;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.myapps.playnation.R;
import com.myapps.playnation.Classes.HorizontalImageHolder;
import com.myapps.playnation.Classes.HorizontalViewHolder;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Fragments.ChallengeFragment;
import com.myapps.playnation.Fragments.DialogSendCommentFragment;
import com.myapps.playnation.Operations.Configurations;
import com.myapps.playnation.Operations.DataConnector;
import com.myapps.playnation.Operations.HelperClass;
import com.myapps.playnation.Operations.LoadImage;
import com.myapps.playnation.main.ISectionAdapter;

public class GameInfoFragment extends Fragment {
	ISectionAdapter mCallback;

	// private ImageView newsImage;
	private View view;
	private DataConnector con;
	private Button addGame;
	private View component;

	public void initGameInfo() {
		Bundle myIntent = getArguments();

		TextView txtConent = (TextView) view.findViewById(R.id.contentText);
		TextView txtReleased = (TextView) view.findViewById(R.id.releaseDateTV);
		TextView txtPlayer = (TextView) view
				.findViewById(R.id.playersPLayingTV);
		TextView txtJoined = (TextView) view.findViewById(R.id.joinedDateTV);
		TextView txtWeb = (TextView) view.findViewById(R.id.websideTV);

		TextView txtEsrbRat = (TextView) view.findViewById(R.id.esrbRat);
		TextView txtPlayRat = (TextView) view.findViewById(R.id.playnationRat);
		TextView txtUserRat = (TextView) view.findViewById(R.id.userRat);		
		
		HorizontalScrollView friendsPics = (HorizontalScrollView) component.findViewById(R.id.RotationScrollView);
		HorizontalViewHolder friendsHolder = new HorizontalViewHolder(getActivity(),getFriendsPictures());
		friendsPics.addView(friendsHolder,new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		TextView txtDeveloper = (TextView) view.findViewById(R.id.developerTV);
		TextView txtDistr = (TextView) view.findViewById(R.id.distributorTV);
		LinearLayout mainLayout = (LinearLayout) view.findViewById(R.id.mainInfoLayout);

		addGame = (Button) view.findViewById(R.id.addGameButt);
		addGame.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "Game added", Toast.LENGTH_SHORT)
						.show();
				setButtonInvis();
			}
		});

		Button challenge = (Button) view.findViewById(R.id.challengeButt);
		challenge.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment newDialog = new ChallengeFragment();
				newDialog.show(getChildFragmentManager(), "DialogFragment");

			}
		});

		// TextView txtPlatform = (TextView) view
		// .findViewById(R.id.txtgameinfoPlatforms);

		if (myIntent != null) {
			String gameName = myIntent.getString(Keys.GAMENAME);
			String gameDesc = myIntent.getString(Keys.GAMEDESC);
			String gameDate = myIntent.getString(Keys.GAMEDATE);
			String gameEsrb = myIntent.getString(Keys.GAMEESRB);
			String gamePlCount = myIntent.getString(Keys.GAMEPLAYERSCOUNT);
			String gameURL = myIntent.getString(Keys.GAMEURL);

			String gamePlatform = myIntent.getString(Keys.GAMEPLATFORM);
			String gameDistributor = myIntent
					.getString(Keys.GAMECompanyDistributor);
			String gameFounded = myIntent.getString(Keys.CompanyFounded);
			String gameDeveloper = myIntent.getString(Keys.CompanyName);

			final String id_game = myIntent.getString(Keys.ID_GAME);
			String isLiked = myIntent.getString(Keys.GameIsLiked);
			String isPlaying = myIntent.getString(Keys.GameisPlaying);

			TextView tx = (TextView) view.findViewById(R.id.addGameButt);
			if (isPlaying.equals("1")) {
				tx.setVisibility(View.GONE);
			}
			TextView tx2 = (TextView) view.findViewById(R.id.challengeButt);
			tx.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					DialogFragment dialog = new DialogSendCommentFragment();
					dialog.show(getChildFragmentManager(), "Game");
					Bundle argss = new Bundle();
					argss.putString(Keys.ID_PLAYER,
							Configurations.CurrentPlayerID);
					argss.putString(Keys.functionAnotherID, id_game);
					argss.putString(Keys.functionAction,
							Keys.POSTFUNCOMMANTAddGame);
					argss.putString(Keys.functionPhpName, "gameFunction.php");
					dialog.setArguments(argss);
				}
			});
			/*
			 * if (isPlaying.equals("1")) {tx.setVisibility(View.GONE);
			 * tx2.setVisibility(View.VISIBLE);} final TextView txlike =
			 * (TextView) view .findViewById(R.id.btnGameLike); if
			 * (isLiked.equals("1")) {
			 * txlike.setText(getActivity().getResources().getString(
			 * R.string.btnUnlike)); } else {
			 * txlike.setText(getActivity().getResources().getString(
			 * R.string.btnLike));
			 * 
			 * }
			 * 
			 * if (Configurations.getConfigs().getApplicationState() != 0) {
			 * tx.setVisibility(View.GONE); txlike.setVisibility(View.GONE); }
			 */

			txtConent.setText(Html.fromHtml(gameDesc));
			txtReleased.setText(txtReleased.getText() + " " + gameDate);
			txtWeb.setText(gameURL);
			txtJoined.setText(txtJoined.getText() + " " + gameFounded);

			txtEsrbRat.setText(txtEsrbRat.getText() + " " + gameEsrb);

			// txtPlatform.setText(gamePlatform);
			txtPlayer.setText(txtPlayer.getText() + " " + gamePlCount);
			if (HelperClass.isTablet(getActivity())) {
				txtWeb.setAutoLinkMask(0);
				txtWeb.setText(txtWeb.getText() + " " + gameURL + " ");
			}
			txtDistr.setText(txtDistr.getText() + "   " + gameDistributor);
			txtDeveloper
					.setText(txtDeveloper.getText() + "   " + gameDeveloper);
			mainLayout.addView(component, 2);
		}
	}
	
	private ArrayList<View> getFriendsPictures()
	{
		ArrayList<View> list = new ArrayList<View>();
		ArrayList<Bundle> args = con.queryPlayerFriendsSearch("");
		for(Bundle itm : args)
		{
		LinearLayout layout = getTemplateLayout();		
		String imageUrl = itm.getString(Keys.PLAYERAVATAR);
		ImageView image = getTemplatePic();
		image.setTag(imageUrl);
		layout.addView(image);
		
		TextView name = new TextView(getActivity());
		name.setText(itm.getString(Keys.FirstName));
		name.setTextColor(Color.parseColor("#CFCFCF"));
		name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
		name.setGravity(Gravity.CENTER_HORIZONTAL);
		layout.addView(name);
		
		list.add(layout);
		new LoadImage(imageUrl, image, "players").execute(image);
		}
		return list;
	}
	
	private LinearLayout getTemplateLayout()
	{
		LinearLayout temp = new LinearLayout(getActivity());
		temp.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
			     LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		temp.setGravity(Gravity.CENTER);
		return temp;
	}
	
	private ImageView getTemplatePic()
	{
		ImageView temp = new ImageView(getActivity());
		temp.setLayoutParams(new LinearLayout.LayoutParams(150, 150));
		temp.setPadding(10, 15, 10, 15);
		temp.setScaleType(ImageView.ScaleType.CENTER_CROP);
		return temp;
	}
	
	private void setButtonInvis()
	{
		addGame.setVisibility(View.GONE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_template_info, container,
				false);
		component = inflater.inflate(R.layout.component_horizontal_image_holder,null,false);
		con = DataConnector.getInst();
		initGameInfo();
		return view;
	}

	@Override
	public void onDestroy() {
		mCallback = null;
		super.onDestroy();
	}
}

/*
 * tx.setOnClickListener(new OnClickListener() {
 * 
 * @Override public void onClick(View v) { DialogFragment dialog = new
 * DialogSendCommentFragment(); dialog.show(getChildFragmentManager(), "Game");
 * Bundle argss = new Bundle(); argss.putString(Keys.ID_PLAYER,
 * Configurations.CurrentPlayerID); argss.putString(Keys.functionAnotherID,
 * id_game); argss.putString(Keys.functionAction, Keys.POSTFUNCOMMANTAddGame);
 * argss.putString(Keys.functionPhpName, "gameFunction.php");
 * dialog.setArguments(argss); } }); txlike.setOnClickListener(new
 * OnClickListener() {
 * 
 * @Override public void onClick(View v) { if (txlike.getText().equals("Like"))
 * { con.functionQuery(Configurations.CurrentPlayerID, id_game,
 * "gameFunction.php", Keys.POSTFUNCOMMANTLike, "");
 * txlike.setText(getActivity().getResources().getString( R.string.btnUnlike));
 * } else { con.functionQuery(Configurations.CurrentPlayerID, id_game,
 * "gameFunction.php", Keys.POSTFUNCOMMANTUnLike, "");
 * txlike.setText(getActivity().getResources().getString( R.string.btnLike)); }
 * } });
 */

