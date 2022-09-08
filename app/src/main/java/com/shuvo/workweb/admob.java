package com.shuvo.workweb;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import java.util.concurrent.RunnableFuture;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class admob extends AppCompatActivity implements apicall.ApiresponseCallback {
	Button button;
	Bundle bundle;
	Boolean istimerruning = true, isAdclicked = false;
	CountDownTimer countDownTimer;
	LayoutInflater layoutInflater;
	LinearLayout linearLayout;
	JSONObject jSONObject;
	int waitingtime, clicktime, targetview, presentview, invalid, block, min = 0, max, limit, Banneradlick, maxBNC=5;
	private AdView ButtomBanerAd;
	InterstitialAd mInterstitialAd;
	LinearLayout lin;
	String domain, key, adloadkey = "this isad load key", admobsubmitkey = "thids jis admob submit key35u", id;
	ProgressBar progressBar;
	String Notice, InterstitialMain, HomeBanner, TaskBannerTop, TaskBannerBottom, admobaccount, Tasklimit, messagetxt;
	TextView notice, message, ad_count;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		try {
			getSupportActionBar().hide();
		} catch (Exception e) {
		}

		setContentView(R.layout.admob_task);
		bundle = getIntent().getExtras();
		if (bundle != null) {
			id = bundle.getString("id");

		}
		SharedPreferences sharedPreferences = getSharedPreferences("view", MODE_PRIVATE);
		presentview = sharedPreferences.getInt("view", 0);
		SharedPreferences sharedPreferences2 = getSharedPreferences("invalid", MODE_PRIVATE);
		invalid = sharedPreferences2.getInt("invalid", 0);
		layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		linearLayout = findViewById(R.id.ad_body);
		domain = getString(R.string.Domain);
		key = getString(R.string.key);
		lin = findViewById(R.id.adMobView);
		Banneradclickupdate();
		progressBar = findViewById(R.id.ad_loadin_bar);
		loadaddata();
		
	}

	public void loadaddata() {
		//Toast.makeText(admob.this,  "vall", 8000).show();

		new apicall(admob.this).getApi(domain + "admob?id=" + id + "&key=" + key, adloadkey);

	}

	public void settextview() {
		Toast.makeText(getApplicationContext(),Banneradlick+"",9000).show();
		Banneradclickupdate();
		linearLayout.removeAllViews();
		layoutInflater.inflate(R.layout.adtask, linearLayout);
	
			if(Banneradlick!=maxBNC){
				AdView adView = new adloader().loadAds(lin, admob.this, ads.TaskBannerBottom);
			adView.setAdListener(new AdListener() {

				@Override
				public void onAdOpened() {
					super.onAdOpened();
				}

				@Override
				public void onAdLoaded() {
					super.onAdLoaded();
				}

				@Override
				public void onAdImpression() {
					super.onAdImpression();
				}

				@Override
				public void onAdFailedToLoad(LoadAdError arg0) {
					super.onAdFailedToLoad(arg0);
					loadaddata();
				}

				@Override
				public void onAdClosed() {
					super.onAdClosed();
				}

				@Override
				public void onAdClicked() {
					super.onAdClicked();

					Toast.makeText(admob.this, Banneradlick + "", Toast.LENGTH_LONG).show();
					if (Banneradlick == maxBNC) {
						lin.setVisibility(View.GONE);
						startalaerm();
						Toast.makeText(admob.this,"start",Toast.LENGTH_LONG).show();

					} else {
						SharedPreferences sharedPreferences = getSharedPreferences("bnc", MODE_PRIVATE);
						SharedPreferences.Editor editor = sharedPreferences.edit();
						editor.putInt("bnc", Banneradlick + 1);
						editor.apply();
						Banneradlick = Banneradlick + 1;
					}

				}

			});
			}

		

		button = findViewById(R.id.ad_btn);
		message = findViewById(R.id.ad_message);
		ad_count = findViewById(R.id.ad_count);
		ad_count.setVisibility(View.VISIBLE);
		ad_count.setText(presentview + "/" + targetview);
		String txt = message();
		message.setText(txt);

		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showAd();

			}

		});

	}

	public void startalaerm() {

		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		long time = System.currentTimeMillis() + (60 * (60 * 1000));
		Intent intent = new Intent(admob.this, reciveBroadcust.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(admob.this, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);

	}

	public void Banneradclickupdate() {

		SharedPreferences sharedPreferences = getSharedPreferences("bnc", MODE_PRIVATE);
		Banneradlick = sharedPreferences.getInt("bnc", 0);

	}

	public void showAd() {
		if (mInterstitialAd != null) {
			mInterstitialAd.show(admob.this);
			calback();
			if (targetview == presentview) {
				isAdclicked = false;
				Toast.makeText(admob.this, "এখন ad এ ক্লিক করে " + clicktime + " second অপেক্ষা করুণ", 4000).show();

			}
			if (targetview != presentview && targetview > presentview) {
				isAdclicked = false;
				startTimer();

			}

		} else {
			Toast.makeText(admob.this, " AD Is Not Loaded", Toast.LENGTH_SHORT).show();

		}

	}

	public void loadAd() {
		SharedPreferences sharedPreferences = getSharedPreferences("view", MODE_PRIVATE);
		presentview = sharedPreferences.getInt("view", 0);
		mInterstitialAd = null;

		AdRequest adRequest = new AdRequest.Builder().build();

		InterstitialAd.load(this, InterstitialMain, adRequest, new InterstitialAdLoadCallback() {
			@Override
			public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
				// The mInterstitialAd reference will be null until
				// an ad is loaded.

				mInterstitialAd = interstitialAd;
			}

			@Override
			public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
				// Handle the error

				Toast.makeText(admob.this, " Fail To Load Ad", Toast.LENGTH_SHORT).show();
				mInterstitialAd = null;
			}
		});

	}

	public void calback() {
		mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
			@Override
			public void onAdShowedFullScreenContent() {
				super.onAdShowedFullScreenContent();
			}

			@Override
			public void onAdImpression() {
				super.onAdImpression();
			}

			@Override
			public void onAdFailedToShowFullScreenContent(AdError arg0) {
				super.onAdFailedToShowFullScreenContent(arg0);
				//	dataheandler();
			}

			@Override
			public void onAdDismissedFullScreenContent() {
				super.onAdDismissedFullScreenContent();

				dataheandler(); //Toast.makeText(admob.this,"yes",9000).show();

				loadAd();

			}

			@Override
			public void onAdClicked() {
				super.onAdClicked();
				rfresh();
				if (presentview == targetview) {

					starttimerforclick();

				} else {
					if (invalid == block) {
						SharedPreferences sharedPreferences = getSharedPreferences("invalid", MODE_PRIVATE);
						SharedPreferences.Editor editor = sharedPreferences.edit();
						editor.putInt("invalid", 0);
						editor.apply();
						new apicall(admob.this).postRequest(domain + "block?key=" + key + "&id=" + id, "{}", "");

						successDailog("Ad view করার সময়ে ad ক্লিক করছেন কেনো ", "Block", false);

					} else {

						SharedPreferences sharedPreferences = getSharedPreferences("invalid", MODE_PRIVATE);
						SharedPreferences.Editor editor = sharedPreferences.edit();
						editor.putInt("invalid", invalid + 1);
						editor.apply();

						successDailog("Ad view করার সময়ে ad ক্লিক করছেন কেনো ", "Invalid Click " + invalid, false);

					}

				}

			}
		});

	}

	public void starttimerforclick() {

		istimerruning = true;
		countDownTimer = new CountDownTimer(clicktime * 1000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				Toast.makeText(admob.this, "Wait" + millisUntilFinished / 1000, 1000).show();

			}

			@Override
			public void onFinish() {
				istimerruning = false;
				isAdclicked = true;

				rfresh();
			}

		}.start();
	}

	public void startTimer() {

		istimerruning = true;
		countDownTimer = new CountDownTimer(waitingtime * 1000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				Toast.makeText(admob.this, "Wait" + millisUntilFinished / 1000, 1000).show();

			}

			@Override
			public void onFinish() {
				istimerruning = false;

				rfresh();
			}
		}.start();

	}

	public void dataheandler() {
		if (istimerruning) {

			countDownTimer.cancel();
			istimerruning = false;

			Toast.makeText(admob.this, "Task failed  - সময় শেষ হওয়ার আগে কেটে দিচ্ছেন কেনো ", Toast.LENGTH_SHORT)
					.show();

			rfresh();
		} else {

			if (isAdclicked) {
				SharedPreferences sharedPreferences = getSharedPreferences("view", MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putInt("view", 0);
				editor.apply();
				new apicall(admob.this).postRequest(domain + "admob?key=" + key + "&id=" + id, "{}", "");
				rfresh();
				loadaddata();

			} else if (targetview > presentview) {

				SharedPreferences sharedPreferences = getSharedPreferences("view", MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putInt("view", presentview + 1);
				editor.apply();
				isAdclicked = false;

				rfresh();
			}
		}

	}

	@Override
	protected void onRestart() {
		super.onRestart();

		if (Banneradlick == maxBNC) {
			lin.setVisibility(View.GONE);

		} else if(Banneradlick ==0){
			lin.setVisibility(View.VISIBLE);
		}
	

	}

	public void rfresh() {
		String txt = message();
		message.setText(txt);
		SharedPreferences sharedPreferences = getSharedPreferences("view", MODE_PRIVATE);
		presentview = sharedPreferences.getInt("view", 0);
		SharedPreferences sharedPreferences2 = getSharedPreferences("invalid", MODE_PRIVATE);
		invalid = sharedPreferences2.getInt("invalid", 0);

		ad_count.setText(presentview + "/" + targetview);
	}

	public void successDailog(String Message, String title, Boolean error) {

		AlertDialog.Builder bverifyDailog = new AlertDialog.Builder(admob.this);
		View inflate = getLayoutInflater().inflate(R.layout.task_complete_layout, null);
		TextView alert_title = inflate.findViewById(R.id.success_title);
		Button alert_okhbtn = inflate.findViewById(R.id.success_okbtn);
		ImageView alert_icon = inflate.findViewById(R.id.success_icon);
		LinearLayout alert_layout = inflate.findViewById(R.id.success_layout);
		TextView alert_message = inflate.findViewById(R.id.success_message);
		alert_title.setText(title);
		alert_message.setText(Message);
		bverifyDailog.setView(inflate);
		AlertDialog alert_dailog = bverifyDailog.create();
		if (!error) {
			alert_layout.setBackgroundResource(R.color.red);
			alert_title.setText(title);
			alert_icon.setImageResource(R.drawable.close);
			alert_message.setText(Message);

		}
		alert_okhbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				alert_dailog.cancel();

			}

		});
		alert_dailog.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface arg0) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						finish();
					}

				});
			}

		});

		alert_dailog.show();

	}

	public void errordailog(String message) {
		AlertDialog.Builder mdailog = new AlertDialog.Builder(admob.this);
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
	public void displayresponse(String result, String keyc, Boolean error) {

		if (error != null) {

			try {
				jSONObject = new JSONObject(result);
				String status = jSONObject.getString("status");

				if (keyc.equals(adloadkey)) {

					if (status.equals("ok")) {
						jSONObject = jSONObject.getJSONObject("data");
						messagetxt = jSONObject.getString("message");
						waitingtime = jSONObject.getInt("waitingtime");
						clicktime = jSONObject.getInt("clicktime");

						InterstitialMain = jSONObject.getString("InterstitialMain");
						HomeBanner = jSONObject.getString("HomeBanner");
						TaskBannerTop = jSONObject.getString("TaskBannerTop");
						TaskBannerBottom = jSONObject.getString("TaskBannerBottom");
						admobaccount = jSONObject.getString("admobaccount");
						Tasklimit = jSONObject.getString("Tasklimit");
						targetview = jSONObject.getInt("view");
						block = jSONObject.getInt("block");

						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								//Toast.makeText(admob.this, messagetxt, 8000).show();

								settextview();
								loadAd();
							}

						});

					} else if (status.equals("error")) {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								try {
									successDailog(jSONObject.getString("message"), jSONObject.getString("title"),
											false);
								} catch (JSONException e) {
								}

							}

						});

					}

				}

			} catch (JSONException e) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(admob.this, e + "", 8000).show();

					}

				});

			}

		} else {//net error
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

	public String message() {

		int random = 0;
		try {
			JSONArray jSONArray = new JSONArray(messagetxt);
			max = jSONArray.length() - 1;
			random = (int) (Math.random() * (max - min + 1) + min);

			messagetxt = jSONArray.getString(random);
		} catch (JSONException e) {
		}
		return messagetxt;

	}

}