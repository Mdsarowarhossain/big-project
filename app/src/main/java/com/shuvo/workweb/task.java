package com.shuvo.workweb;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lazyprogrammer.motiontoast.MotionToast;
import com.lazyprogrammer.motiontoast.MotionStyle;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

public class task extends AppCompatActivity implements apicall.ApiresponseCallback {
	JSONObject jSONObject;
	boolean isRunning = false, toastrunning = false, invalidrunning = false;
	LayoutInflater layoutInflater;
	String Notice, task_view, task_point, task_imgurl, task_userclick, taskkyword,
			taskcallkey = "this is  taskcall key", Verifykey = "this is verify key";
	String domain, status, key, id, serverchek = "servercheketky", invalidkey = "thiz is invalidkye";
	int clicktime;
	TextView customTextview, task_pointtxt, task_viewtxt, task_notice, keyword;
	ImageView ssimage;
	Button copybtn;
	int i = clicktime;
	CountDownTimer chek_countlDownTimer, toasTimer;

	AlertDialog invaliddailog, alertDialog, balertDialog;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		try {
			getSupportActionBar().hide();
		} catch (Exception e) {
		}
		setContentView(R.layout.taskrun);
		layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		//	layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		domain = getString(R.string.Domain);
		key = getString(R.string.key);
		task_pointtxt = findViewById(R.id.taskrun_pointtv);
		Bundle bundle = getIntent().getExtras();
		id = bundle.getString("id");
		ssimage = findViewById(R.id.taskrun_ss);
		task_notice = findViewById(R.id.taskrun_notice);
		task_viewtxt = findViewById(R.id.taskrun_viewtv);
		keyword = findViewById(R.id.taskrun_keyword);
		copybtn = findViewById(R.id.taskrun_copy);

		showloading();
		callTaskapi();

	}

	public void setinfo() {

		if (Integer.parseInt(task_userclick) > Integer.parseInt(task_view)) {
			LinearLayout taskrun_body = findViewById(R.id.taskrun_body);

			taskrun_body.removeAllViews();
			layoutInflater.inflate(R.layout.link_submit_layout, taskrun_body);
			TextInputLayout textInputLayout = findViewById(R.id.taskrun_verify_link_layout);
			TextInputEditText textInputEditText = findViewById(R.id.taskrun_verify_link_edittext);

			task_notice.setText(Notice);
			Button verify = findViewById(R.id.taskrun_link_submint_btn);
			verify.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					String link = textInputEditText.getText().toString();
					textInputLayout.setErrorEnabled(false);
					if (link.equals("") || link == null) {

						textInputLayout.setErrorEnabled(true);
						textInputLayout.setError("Link is Requried");
					} else if (link.length() < 20) {
						textInputLayout.setErrorEnabled(true);
						textInputLayout
								.setError("এটি ভুল লিংক ad এ ক্লিক করে লিঙ্ক পেষ্ট করুন না বুঝলে কাজের ভিডিও দেখুন ");

					} else {
						new apicall(task.this).postRequest(domain + "task?key=1234&id=8173993727",
								"{\"link\":\"" + link + "\"}", Verifykey);

						/*	new apicall(task.this).postRequest(domain + "task?id" + id + "&key=" + key,
									"{\"link\":\"" + link + "\"}", Verifykey);
						*/
					}

				}

			});

		} else {

			Glide.with(task.this).load(task_imgurl).into(ssimage);
			task_notice.setText(Notice);
			task_viewtxt.setText(task_userclick + "/" + task_view);
			keyword.setText(taskkyword);

			if (Integer.parseInt(task_userclick) == Integer.parseInt(task_view)
					|| Integer.parseInt(task_userclick) > Integer.parseInt(task_view)) {
				task_notice.append("\n এবার ad এ ক্লিক করে লিঙ্ক কপি করে আনুন");

			}

			copybtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
					ClipData clipData = ClipData.newPlainText(taskkyword, taskkyword);
					clipboardManager.setPrimaryClip(clipData);
					copybtn.setVisibility(View.GONE);
					MotionToast motionToast = new MotionToast(task.this, 0, MotionStyle.LIGHT, MotionStyle.SUCCESS,
							MotionStyle.BOTTOM, "Copied", "keyword Coped To your clipboard", MotionStyle.LENGTH_SHORT)
									.show();
					if (Integer.parseInt(task_userclick) == Integer.parseInt(task_view)
							|| Integer.parseInt(task_userclick) > Integer.parseInt(task_view)) {
						startListening();
					}

				}

			});
		}

		//task_point

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		copybtn.setVisibility(View.VISIBLE);

		if (toastrunning && !invalidrunning) {
			toasTimer.cancel();

			AlertDialog.Builder alertbuilder = new AlertDialog.Builder(task.this);
			View alertview = getLayoutInflater().inflate(R.layout.invalid_layout, null);

			Button button = alertview.findViewById(R.id.invalid_okbtn);

			button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					showloading();
					new apicall(task.this).getApi(domain + "invalid?key=" + key + "&id=" + id, invalidkey);
					toastrunning = false;
					invaliddailog.cancel();
					//	showloading();
					invalidrunning = false;
					showloading();

					callTaskapi();

					//	recreate();

				}

			});
			alertbuilder.setView(alertview);
			invaliddailog = alertbuilder.create();
			invaliddailog.setCancelable(false);
			invaliddailog.show();
			invalidrunning = true;

		}

		else {
			showloading();

			callTaskapi();
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	public void callTaskapi() {
		new apicall(task.this).getApi(domain + "task?task=true&key=" + key + "&id=" + id, taskcallkey);

		/*	if(viewint<userint){
				
			}else{
				
			}
			*/
	}

	public void startListening() {

		if (isRunning) {
			chek_countlDownTimer.cancel();

		}
		chek_countlDownTimer = new CountDownTimer((20 * 60) * 1000, 3000) {

			@Override
			public void onTick(long millisUntilFinished) {
				isRunning = true;
				new apicall(task.this).getApi(domain + "code?id=" + id, serverchek);
			}

			@Override
			public void onFinish() {
				isRunning = false;
			}

		}.start();

	}

	public void errordailog(String message) {
		AlertDialog.Builder mdailog = new AlertDialog.Builder(task.this);
		View mview = getLayoutInflater().inflate(R.layout.network_error_layout, null);

		ImageView imageView = mview.findViewById(R.id.neterroricon);
		ImageView cancelicon = mview.findViewById(R.id.neterror_cancel);

		TextView textView = mview.findViewById(R.id.neterrormessagw);
		mdailog.setView(mview);
		balertDialog = mdailog.create();

		balertDialog.show();

		if (message != null) {

			imageView.setImageResource(R.drawable.alerts);
			imageView.setColorFilter(R.color.red);
			textView.setText("Sever Error");

		}
		//alertDialog.setCancelable(false);
		cancelicon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				balertDialog.cancel();

			}

		});
		balertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface arg0) {

				copybtn.setVisibility(View.VISIBLE);
				showloading();
				callTaskapi();
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

	public void startToasting() {

		toastrunning = true;
		toasTimer = new CountDownTimer(clicktime * 1000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {

				new toast(task.this).Shorttoast("Wait " + millisUntilFinished / 1000, task.this, 1);
			}

			@Override
			public void onFinish() {
				toastrunning = false;
				new toast(task.this).Shorttoast("এখন লিঙ্ক কপি করে অ্যাপ এ submit করুন", task.this, 5);
				//customtoast(" ", 5);

			}

		}.start();

	}

	public void successDailog(String Message, String title, Boolean error) {

		AlertDialog.Builder bverifyDailog = new AlertDialog.Builder(task.this);
		View inflate = getLayoutInflater().inflate(R.layout.task_complete_layout, null);
		TextView alert_title = inflate.findViewById(R.id.success_title);
		Button alert_okhbtn = inflate.findViewById(R.id.success_okbtn);
		ImageView alert_icon = inflate.findViewById(R.id.success_icon);
		LinearLayout alert_layout = inflate.findViewById(R.id.success_layout);
		TextView alert_message = inflate.findViewById(R.id.success_message);
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

						if (title.contains("vpn") || Message.contains("vpn")||title.contains("closed")||title.contains("limit")) {
							finish();

						} else {
							recreate();
						}

					}

				});
			}

		});

		alert_dailog.show();

	}

	public void showloading() {
		View inflate = getLayoutInflater().inflate(R.layout.loadlotti, null);
		AlertDialog.Builder mbuilder = new AlertDialog.Builder(task.this);
		mbuilder.setView(inflate);

		alertDialog = mbuilder.create();
		alertDialog.show();
		alertDialog.setCancelable(false);

	}

	@Override
	public void displayresponse(String result, String keyc, Boolean error) {
		/*
				if (keyc.equals(taskcallkey) || keyc.equals(serverchek)) {
					
		
				}*/
		if (alertDialog.isShowing()) {
			alertDialog.cancel();
		}

		if (error != null) {

			try {
				jSONObject = new JSONObject(result);

				if (keyc.equals(taskcallkey)) {
					status = jSONObject.getString("status");

					if (status.equals("ok")) {
						task_view = jSONObject.getJSONObject("task").getString("click");
						task_userclick = jSONObject.getString("click");
						task_imgurl = jSONObject.getJSONObject("task").getString("ssurl");
						Notice = jSONObject.getJSONObject("notice").getString("notice");
						task_point = jSONObject.getJSONObject("task").getString("point");
						taskkyword = jSONObject.getJSONObject("task").getString("keyword");
						clicktime = jSONObject.getJSONObject("task").getInt("clicktime");
						runOnUiThread(new Runnable() {

							@Override
							public void run() {

								setinfo();

							}

						});

					} else if (jSONObject.getString("status").equals("error")) {

							runOnUiThread(new Runnable() {

							@Override
							public void run() {

								try {
									successDailog(jSONObject.getString("title"), jSONObject.getString("message"),
											false);
								} catch (JSONException e) {
								}
							}

						});


					}

					else if (jSONObject.getString("status").equals("vpn")) {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {

								try {
									successDailog(jSONObject.getString("title"), jSONObject.getString("message"),
											false);
								} catch (JSONException e) {
								}
							}

						});

					}

				} //end task calll
				else if (keyc.equals(serverchek)) {

					if (jSONObject.getString("code").contains("ok")) {

						runOnUiThread(new Runnable() {

							@Override
							public void run() {

								chek_countlDownTimer.cancel();
								new toast(task.this).toast(
										"এখন ad ক্লিক করে " + clicktime
												+ " second অপেক্ষা করুন তারপর লিঙ্ক কপি করে app এ সাবমিট করুন",
										task.this);
								isRunning = false;

								startToasting();

							}

						});

					} else {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								//	new toast(task.this).toast("no" + "", task.this);

							}

						});

					}

				} else if (keyc.equals(invalidkey)) {

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							showloading();
							callTaskapi();
						}

					});

				} else if (keyc.equals(Verifykey)) {
					if (jSONObject.getString("status").equals("ok")) {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {

								successDailog("", "", true);

							}

						});

					} else if (jSONObject.getString("status").equals("notask")) {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								finish();
							}

						});

					}

					else {
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

				else {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {

							//	setinfo();
							new toast(task.this).toast(keyc, task.this);

						}

					});

				}

			} catch (

			JSONException e) {

				runOnUiThread(new Runnable() {

					@Override
					public void run() {

						//	setinfo();
						new toast(task.this).toast(result + "", task.this);

					}

				});
			}

		} else {

			if (!keyc.equals(serverchek)) {

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

}