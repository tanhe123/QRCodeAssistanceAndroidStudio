package com.sdutlinux.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class SimpleProgressDialog extends ProgressDialog {

	public SimpleProgressDialog(Context context, String title, String message) {
		super(context);
		this.setTitle(title);
		this.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		this.setMessage(message);
		this.setCancelable(true);
	}

//	public void show() {
//		this.show();
//	}
//	
//	public void dismiss() {
//		this.dismiss();
//	}
}
