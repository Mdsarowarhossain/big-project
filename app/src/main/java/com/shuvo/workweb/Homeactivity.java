package com.shuvo.workweb;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.content.pm.SharedLibraryInfo;
import android.util.Log;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.onesignal.OneSignal;
import java.sql.Struct;
import org.json.JSONException;
import org.json.JSONObject;

public class Homeactivity extends AppCompatActivity implements apicall.ApiresponseCallback {
	LinearLayout layout;
	LayoutInflater layoutInflater;
	Button login, singup;
	String Domain, key;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		try {
			getSupportActionBar().hide();
		} catch (Exception e) {
		}
		setContentView(R.layout.home_layout);
		layout = findViewById(R.id.homelayout);
		layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.splash, layout);
		Domain = getString(R.string.Domain);
		key = getString(R.string.key);
		loadid();

	}

	public void mother() {

		try {
			onesignal();
			SharedPreferences sharedPreferences = getSharedPreferences("USER_ID", MODE_PRIVATE);
			String id = sharedPreferences.getString("USER_ID", null);

			if (id != null) {
				Intent intent = new Intent(Homeactivity.this, dashboardAcivity.class);
				intent.putExtra("id", id);

				startActivity(intent);

			} else {
				layout.removeAllViews();
				layoutInflater.inflate(R.layout.tv, layout);

				login = findViewById(R.id.gotologinacivity);
				singup = findViewById(R.id.gotosingupacivity);

				singup.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {

						Intent intent = new Intent(Homeactivity.this, MainActivity.class);
						intent.putExtra("type", "singup");

						startActivity(intent);

					}

				});

				login.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {

						Intent intent = new Intent(Homeactivity.this, MainActivity.class);
						intent.putExtra("type", "login");

						startActivity(intent);

					}

				});
			}

		} catch (Exception e) {

		}

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		loadid();
	}

	public void loadid() {

		new apicall(Homeactivity.this).getApi(Domain + "adid?key=" + key, "");
	}

	public void setmanifest() {
		try {
			ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(getPackageName(),
					PackageManager.GET_META_DATA);
			Bundle bundle = applicationInfo.metaData;
			applicationInfo.metaData.putString("com.google.android.gms.ads.APPLICATION_ID", ads.appid);
			String apiKey = bundle.getString("com.google.android.gms.ads.APPLICATION_ID");

			mother();

		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();

		} catch (NullPointerException e) {
			e.printStackTrace();

		}

	}

	public void onesignal() {

		// Enable verbose OneSignal logging to debug issues if needed.

		OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

		// OneSignal Initialization
		OneSignal.initWithContext(this);
		OneSignal.setAppId(ads.ONESIGNAL_APP_ID);

		// promptForPushNotifications will show the native Android notification permission prompt.
		// We recommend removing the following code and instead using an In-App Message to prompt for notification permission (See step 7)
		OneSignal.promptForPushNotifications();

	}

	public void errordailog(String message) {
		AlertDialog.Builder mdailog = new AlertDialog.Builder(Homeactivity.this);
		View mview = getLayoutInflater().inflate(R.layout.network_error_layout, null);

		ImageView imageView = mview.findViewById(R.id.neterroricon);
		ImageView cancelicon = mview.findViewById(R.id.neterror_cancel);

		TextView textView = mview.findViewById(R.id.neterrormessagw);
		mdailog.setView(mview);
		AlertDialog alertDialog = mdailog.create();

		alertDialog.show();

		if (message != null) {

			imageView.setImageResource(R.drawable.alerts);
			imageView.setColorFilter(R.color.red);
			textView.setText("Sever Error");

		}
		cancelicon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				alertDialog.cancel();

			}

		});
		alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface arg0) {
				finish();
			}

		}

		);

	}

	Boolean netchek() {

		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {

			return true;
		}

		else {
			return false;
		}

	}

	@Override
	public void displayresponse(String result, String key, Boolean error) {

		if (error != null) {

			try {
				JSONObject jSONObject = new JSONObject(result);
				JSONObject info = jSONObject.getJSONObject("appinfo");
				jSONObject = jSONObject.getJSONObject("id");
				ads.HomeBanner = jSONObject.getString("HomeBanner");
				ads.appid = jSONObject.getString("admobaccount");
				ads.InterstitialMain = jSONObject.getString("InterstitialMain");
				ads.TaskBannerBottom = jSONObject.getString("TaskBannerBottom");
				ads.TaskBannerTop = jSONObject.getString("TaskBannerTop");
				ads.ONESIGNAL_APP_ID = jSONObject.getString("onesingnalappid");
				ads.VERSION = info.getInt("version");
				ads.applink = info.getString("applink");
				ads.color = info.getString("color");
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						setmanifest();
						//Toast.makeText(Homeactivity.this,"fire",9000).show();

					}

				});

			} catch (JSONException e) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						//Toast.makeText(Homeactivity.this,e+"",9000).show();

					}

				});
			}

		} else {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {

					if (netchek()) {
						errordailog("net error");

					} else {
						errordailog(null);

					}
				}

			});
		}

	}

}