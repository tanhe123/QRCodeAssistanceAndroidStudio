package com.sdutlinux.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogFactory {
	public static Dialog createWarningAlertDialog(Context context, String title, String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(context)
								.setTitle(title)
								.setMessage(message)
								.setIcon(android.R.drawable.stat_sys_warning)
								.setNegativeButton("确定", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										
									}
								})
								.create();
		return alertDialog;
	}
}
