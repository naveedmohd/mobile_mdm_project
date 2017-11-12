package com.example.innovation_day;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.app.DialogFragment;
import android.app.Dialog;
import android.view.View.OnClickListener;
import android.content.DialogInterface;

public class DialogActivity extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		//builder.setMessage(R.string.dialog_text);
		//super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setContentView(R.layout.activity_dialog);
		
		builder.setMessage(R.string.dialog_text)
        .setPositiveButton(R.string.btn_start_dialog_label, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // FIRE ZE MISSILES!
            }
        })
        .setNegativeButton(R.string.btn_finish_dialog_label, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });		
		
		
		return builder.create();
	}



}
