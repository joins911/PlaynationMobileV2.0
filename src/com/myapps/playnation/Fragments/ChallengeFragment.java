package com.myapps.playnation.Fragments;

import java.util.ArrayList;

import com.myapps.playnation.R;
import com.myapps.playnation.Classes.Keys;
import com.myapps.playnation.Operations.DataConnector;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ChallengeFragment extends DialogFragment{
	
	DataConnector con;
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
		con = DataConnector.getInst();
		String[] friends = getFriends();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.chose_friend)
               .setItems(friends, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int which) {
               // The 'which' argument contains the index position
               // of the selected item
           }
    });
        return builder.create();
    }
	
	public String[] getFriends()
	{
		ArrayList<Bundle> bund = con.queryPlayerFriendsSearch("");
		String[] friendsNames = new String[bund.size()];
		for(int i=0;i<bund.size();i++)
			friendsNames[i]=bund.get(i).getString(Keys.PLAYERNAME);
		return  friendsNames;
	}

}
