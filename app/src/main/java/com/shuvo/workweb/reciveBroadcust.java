package com.shuvo.workweb;

import android.content.Intent;
import android.content.Context;

import android.content.BroadcastReceiver;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.widget.Toast;

public class reciveBroadcust extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		SharedPreferences sharedPreferences = context.getSharedPreferences("bnc", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt("bnc", 0);
		editor.apply();

	}

}