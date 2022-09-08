package com.shuvo.workweb;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class toast {
	public toast(Context context) {

	}

	public void toast(String text, Context context) {
		Toast.makeText(context, text, Toast.LENGTH_LONG)

				.show();

	}
	public void Shorttoast(String text, Context context,int time) {
		Toast.makeText(context, text, time*1000)
				

				.show();

	}

}