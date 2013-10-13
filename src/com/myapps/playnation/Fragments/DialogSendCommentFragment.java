package com.myapps.playnation.Fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.myapps.playnation.R;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Fragments.TabHosts.Home.HomeMessagesFragment;
import com.myapps.playnation.Operations.Configurations;
import com.myapps.playnation.Operations.DataConnector;
import com.myapps.playnation.main.MainActivity;

public class DialogSendCommentFragment extends DialogFragment {
	private Button btnSave;
	private Button btnCancel;
	private TextView txtComment;
	private TextView lblSendComment;
	private String currentTag;
	private DataConnector con;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		con = DataConnector.getInst();
		View mView = inflater
				.inflate(R.layout.component_sendgamegroupcommentinvite,
						container, false);
		currentTag = this.getTag();

		btnSave = (Button) mView.findViewById(R.id.btnSend);
		btnCancel = (Button) mView.findViewById(R.id.btnCancel);
		txtComment = (TextView) mView.findViewById(R.id.txtComment);
		lblSendComment = (TextView) mView.findViewById(R.id.txtNotificationMsg);
		if (currentTag.equals("Group")) {
			lblSendComment.setText(getActivity().getResources().getString(
					R.string.lblSendGroupComment));
		} else if (currentTag.equals("Game")) {
			lblSendComment.setText(getActivity().getResources().getString(
					R.string.lblSendGroupComment));
		} else if (currentTag.equals("SendComment")) {
			lblSendComment.setText(getActivity().getResources().getString(
					R.string.lblSendComment));
		} else {
			Bundle args = getArguments();
			lblSendComment.setText(getActivity().getResources().getString(
					R.string.sendMsgText)
					+ ": " + args.getString(Keys.PLAYERNICKNAME));
		}

		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle args = getArguments();
				if (args != null) {
					if (currentTag.equals("SendMessage")) {
						if (!txtComment.getText().toString().isEmpty()) {
							con.sendMessage(txtComment.getText().toString(),
									Configurations.CurrentPlayerID,
									args.getString(Keys.functionAnotherID));
							dismiss();
							HomeMessagesFragment frag = new HomeMessagesFragment();
							MainActivity.passCurrFragment.getActivity()
									.getSupportFragmentManager()
									.beginTransaction()
									.add(R.id.content_frame, frag).commit();
							MainActivity.passCurrFragment = frag;
							MainActivity.passmBrowserFragment.setInvisible();
							dismiss();

						}
					} else if (currentTag.equals("SendComment")) {
						if (!txtComment.getText().toString().isEmpty()) {
							con.insertComment(
									txtComment.getText().toString(),
									args.getString(Keys.WallOwnerType),
									con.getCurrentPlayer().getString(
											Keys.PLAYERNICKNAME), args
											.getString(Keys.ID_WALLITEM));
							dismiss();
						}
					} else {
						con.functionQuery(args.getString(Keys.ID_PLAYER), args
								.getString(Keys.functionAnotherID), args
								.getString(Keys.functionPhpName), args
								.getString(Keys.functionAction), txtComment
								.getText().toString());
						dismiss();
					}
				}

			}
		});

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		return mView;
	}
}