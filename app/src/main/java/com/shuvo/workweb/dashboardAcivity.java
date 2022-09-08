package com.shuvo.workweb;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.net.SSLCertificateSocketFactory;
import android.net.Uri;
import android.os.Bundle;
import android.service.autofill.OnClickAction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.BoolRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.lazyprogrammer.motiontoast.MotionStyle;
import com.lazyprogrammer.motiontoast.MotionToast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class dashboardAcivity extends AppCompatActivity implements apicall.ApiresponseCallback {
	Boolean isprofile = false;
	//layout start

	SwipeRefreshLayout swipeRefreshLayout;
	LinearLayout dashboard_body, footer_home, footer_profile, footer_menu;
	apicall apicall;
	LayoutInflater dashbodylayoutinflator;
	CardView card_task, menu_change_password, menu_logout, menu_edit_profile, menu_refer_history, menu_share,
			dash_admob_task, How_to_work, dash_notice, dash_withdraw, dash_withdraw_history, refer_history, telegram,
			dash_leaderboard;
	Double Userpoint;
	ImageView profile_rfrid_image, dash_notificatiom;

	//layout end
	//componemt start

	TextView user_name_in_header, user_dashphone_number, userdash_point, rfrid_inhead, dash_appname, profile_bdt,
			profile_rfrid, profile_totalrefer, profile_invalid, profile_name, profile_joining_date, profile_ip,
			profile_phone_info, profile_totalrefer_info, profile_total_withdraw;

	//component end
	JSONObject jSONObject;
	String Notice, headerkey = "setheaderkey", Profilekey = "profilekey";
	String Domain, statusfordash, userId, key;
	String userName, UserPhoneNumber, userpointInheader, appName, profile_total_refertext, profile_invalidtext,
			joiningdate, ipaddress, profile_total_withdrawtxt, refer_json, work_link, telegrame_chanellink;
	heandelbuttonclick heandelbuttonclick = new heandelbuttonclick();

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		try {
			getSupportActionBar().hide();
		} catch (Exception e) {
		}
		setContentView(R.layout.dasboard_layout);
		dashboard_body = findViewById(R.id.dashboard_body);

		Domain = getString(R.string.Domain);
		userId = authcheck();
		dashbodylayoutinflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		key = getString(R.string.key);
		userdash_point = findViewById(R.id.dash_point_in_header);
		swipeRefreshLayout = findViewById(R.id.dash_swiprefresh);
		user_name_in_header = findViewById(R.id.dash_name_in_header);
		user_dashphone_number = findViewById(R.id.dash_phone_number_head);
		rfrid_inhead = findViewById(R.id.dash_rfrid_inhead);
		dash_appname = findViewById(R.id.appName);
		footer_home = findViewById(R.id.footer_home);
		footer_profile = findViewById(R.id.footer_profile);
		footer_menu = findViewById(R.id.footer_menu);
		footer_home.setOnClickListener(heandelbuttonclick);
		footer_profile.setOnClickListener(heandelbuttonclick);
		footer_menu.setOnClickListener(heandelbuttonclick);
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

			@Override
			public void onRefresh() {

				setHeaderData();
				if (isprofile) {
					dashboard_body.removeAllViews();

					setProfileview();

				}
			}

		});

		if (update()) {
			successDailog("Update Recuarid", "please Update This app This is a Old Version", false);

		}

		rfrid_inhead.setText(userId);

		setcardlayou();

		MobileAds.initialize(this, new OnInitializationCompleteListener() {

			@Override
			public void onInitializationComplete(InitializationStatus arg0) {
			}

		});

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		isprofile = false;

		dashboard_body.removeAllViews();

		setcardlayou();

	}

	public Boolean update() {
		Boolean resulst;
		if (Integer.parseInt(getString(R.string.version)) == ads.VERSION) {
			resulst = false;

		} else {
			resulst = true;
		}
		return resulst;
	}

	public void setcardlayou() {
		swipeRefreshLayout.setRefreshing(true);
		dashbodylayoutinflator.inflate(R.layout.cards, dashboard_body);
		setHeaderData();
		card_task = findViewById(R.id.dash_card_task);
		dash_withdraw = findViewById(R.id.dash_withdraw);
		dash_withdraw.setOnClickListener(heandelbuttonclick);
		dash_withdraw_history = findViewById(R.id.dash_withdraw_history);
		dash_withdraw_history.setOnClickListener(heandelbuttonclick);
		refer_history = findViewById(R.id.refer_history);
		refer_history.setOnClickListener(heandelbuttonclick);
		telegram = findViewById(R.id.Telegram_Chanel);
		telegram.setOnClickListener(heandelbuttonclick);
		dash_leaderboard = findViewById(R.id.dash_leaderboard);
		dash_leaderboard.setOnClickListener(heandelbuttonclick);

		dash_admob_task = findViewById(R.id.dash_admonb_task);
		dash_notificatiom = findViewById(R.id.dash_notificatiom);
		dash_notificatiom.setOnClickListener(heandelbuttonclick);
		How_to_work = findViewById(R.id.dash_how_to_work);
		How_to_work.setOnClickListener(heandelbuttonclick);
		dash_notice = findViewById(R.id.dash_Notice);
		dash_notice.setOnClickListener(heandelbuttonclick);
		dash_admob_task.setOnClickListener(heandelbuttonclick);
		card_task.setOnClickListener(heandelbuttonclick);

	}

	public void setProfileview() {
		dashbodylayoutinflator.inflate(R.layout.profile, dashboard_body);
		setdatain_profile();
		setHeaderData();

	}

	public void setMenulayout() {
		dashbodylayoutinflator.inflate(R.layout.menu, dashboard_body);
		menu_edit_profile = findViewById(R.id.menu_edit_profile);
		menu_change_password = findViewById(R.id.menu_change_password);
		menu_refer_history = findViewById(R.id.menu_refer_history);
		menu_refer_history.setOnClickListener(heandelbuttonclick);
		menu_share = findViewById(R.id.menu_Share);
		menu_share.setOnClickListener(heandelbuttonclick);
		menu_logout = findViewById(R.id.menu_Logout);
		menu_logout.setOnClickListener(heandelbuttonclick);
		menu_edit_profile.setOnClickListener(heandelbuttonclick);
		menu_change_password.setOnClickListener(heandelbuttonclick);
		setHeaderData();

	}

	public void errordailog(String message) {
		AlertDialog.Builder mdailog = new AlertDialog.Builder(dashboardAcivity.this);
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
		//alertDialog.setCancelable(false);
		cancelicon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				alertDialog.cancel();

			}

		});
		alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface arg0) {

				setHeaderData();
			}

		}

		);

	}

	public void setHeaderData() {
		//reset section
		user_name_in_header.setText("...");
		new apicall(this).getApi(Domain + "dashboard?key=" + key + "&id=" + userId, headerkey);

	}

	public void putDatainheader(String user_name, String user_phone, String point, String appName) {
		user_name_in_header.setText(user_phone);
		userdash_point.setText(point);
		user_dashphone_number.setText(UserPhoneNumber);

		user_name_in_header.setText(user_name);
		dash_appname.setText(appName);

	}

	public void setdatain_profile() {
		profile_rfrid_image = findViewById(R.id.profile_referid_image);
		profile_bdt = findViewById(R.id.profile_totalbdt);
		profile_invalid = findViewById(R.id.profile_invalidclick_field);
		profile_rfrid = findViewById(R.id.profile_refrid_field);
		profile_totalrefer = findViewById(R.id.profile_total_refer);
		profile_name = findViewById(R.id.profile_name);
		profile_ip = findViewById(R.id.profile_ip);
		profile_joining_date = findViewById(R.id.profile_joining_date);

		profile_phone_info = findViewById(R.id.profile_phone_info);

		profile_totalrefer_info = findViewById(R.id.profile_totalregfer_info_section);
		profile_total_withdraw = findViewById(R.id.profile_total_withdraw);
		profile_phone_info.setText(UserPhoneNumber);
		profile_totalrefer_info.setText(profile_total_refertext);
		profile_total_withdraw.setText(profile_total_withdrawtxt);

		profile_ip.setText(ipaddress);
		profile_joining_date.setText(joiningdate);
		profile_name.setText(userName);
		profile_totalrefer.setText(profile_total_refertext);
		profile_total_withdraw.setText(profile_total_withdrawtxt);
		profile_invalid.setText(profile_invalidtext);
		profile_rfrid.setText(userId);
		profile_bdt.setText(Userpoint / 100 + " BDT");
		profile_rfrid_image.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				ClipData clipData = ClipData.newPlainText(userId, userId);
				clipboardManager.setPrimaryClip(clipData);
				MotionToast motionToast = new MotionToast(dashboardAcivity.this, 0, MotionStyle.LIGHT,
						MotionStyle.SUCCESS, MotionStyle.BOTTOM, "Copied", "Text Coped To your clipboard",
						MotionStyle.LENGTH_SHORT).show();
			}

		});

	}

	public String authcheck() {

		SharedPreferences sharedPreferences = getSharedPreferences("USER_ID", MODE_PRIVATE);
		return sharedPreferences.getString("USER_ID", null);

	}

	public void successDailog(String Message, String title, Boolean error) {

		AlertDialog.Builder bverifyDailog = new AlertDialog.Builder(dashboardAcivity.this);
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
		alert_dailog.setCancelable(false);
		alert_dailog.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface arg0) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if(update()){
							Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ads.applink));
							startActivity(browserIntent);
						}
						else{
							finishAffinity();
						}

						

					}

				});
			}

		});

		alert_dailog.show();

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
	public void onBackPressed() {
		finishAffinity();
	}

	@Override
	public void displayresponse(String result, String key, Boolean error) {
		swipeRefreshLayout.setRefreshing(false);
		jSONObject = new json().Jsonobjectreturn(result);

		try {
			if (error == null) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (netchek()) {
							errordailog("net error");

						} else {
							errordailog(null);

						}

						//	errordailog();
					}

				});

			} else {

				if (key.equals(headerkey)) {
					try {
						statusfordash = jSONObject.getString("status");
					} catch (JSONException e) {
					}
					if (statusfordash.equals("success")) {
						userName = jSONObject.getJSONObject("User").getString("name");
						UserPhoneNumber = jSONObject.getJSONObject("User").getString("phone");
						userpointInheader = jSONObject.getJSONObject("User").getString("point");
						appName = jSONObject.getJSONObject("settings").getString("appname");
						String pointD = jSONObject.getJSONObject("User").getString("point");
						Userpoint = Double.parseDouble(pointD);

						profile_total_refertext = jSONObject.getString("refer");
						profile_invalidtext = jSONObject.getJSONObject("User").getString("invalidclick");
						ipaddress = jSONObject.getString("ip");
						joiningdate = jSONObject.getJSONObject("User").getString("joiningdate");
						profile_total_withdrawtxt = jSONObject.getJSONObject("User").getString("totalwithdraw");
						Notice = jSONObject.getJSONObject("settings").getString("notice");
						work_link = jSONObject.getJSONObject("settings").getString("worklink");
						telegrame_chanellink = jSONObject.getJSONObject("settings").getString("telegramlink");

						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								putDatainheader(userName, UserPhoneNumber, userpointInheader, appName);

								//	new toast(dashboardAcivity.this).toast(result, dashboardAcivity.this);
							}

						});

					} else if (statusfordash.equals("block")) {

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

				}

			}

		} catch (Exception e) {

		}

	}
	//class 

	public class heandelbuttonclick implements View.OnClickListener {

		@Override
		public void onClick(View view) {

			if (view.getId() == R.id.dash_card_task) {
				Intent intent = new Intent(dashboardAcivity.this, taskAcivity.class);
				intent.putExtra("id", userId);
				startActivity(intent);

			} else if (view.getId() == R.id.dash_admonb_task)

			{
				Intent intent = new Intent(dashboardAcivity.this, admob.class);
				intent.putExtra("id", userId);

				startActivity(intent);

			}

			else if (view.getId() == R.id.footer_home) {
				isprofile = false;

				dashboard_body.removeAllViews();

				setcardlayou();

			} else if (view.getId() == R.id.dash_withdraw_history) {

				Intent intent = new Intent(dashboardAcivity.this, withdrawactivity.class);

				intent.putExtra("id", userId);
				intent.putExtra("type", "withdraw_history");

				startActivity(intent);

			}

			else if (view.getId() == R.id.footer_profile) {
				isprofile = true;
				dashboard_body.removeAllViews();

				setProfileview();

			} else if (view.getId() == R.id.footer_menu) {
				isprofile = false;
				dashboard_body.removeAllViews();

				setMenulayout();

			} else if (view.getId() == R.id.menu_change_password) {
				Intent intent = new Intent(dashboardAcivity.this, editUserAcivity.class);
				intent.putExtra("id", userId);
				intent.putExtra("type", "password_change");

				startActivity(intent);

			} else if (view.getId() == R.id.dash_Notice) {
				AlertDialog.Builder noticedialogbuilder = new AlertDialog.Builder(dashboardAcivity.this);
				View inflate = getLayoutInflater().inflate(R.layout.dash_notice, null);
				Button yes = inflate.findViewById(R.id.dash_notice_ok);
				ImageView telegrame = inflate.findViewById(R.id.telegrameChanel);
				TextView noticetv = inflate.findViewById(R.id.dash_notice_tv);
				noticetv.setText(Notice);
				noticedialogbuilder.setView(inflate);
				AlertDialog noticedialog = noticedialogbuilder.create();
				noticedialog.show();
				yes.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						noticedialog.cancel();

					}

				});

				telegrame.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (!telegrame_chanellink.contains("#")) {
							Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(telegrame_chanellink));
							startActivity(browserIntent);

						}

					}

				});

			}

			else if (view.getId() == R.id.menu_edit_profile) {
				Intent intent = new Intent(dashboardAcivity.this, editUserAcivity.class);
				intent.putExtra("id", userId);
				intent.putExtra("name", userName);
				intent.putExtra("phone", UserPhoneNumber);

				intent.putExtra("type", "profile");

				startActivity(intent);

			} else if (view.getId() == R.id.menu_refer_history || view.getId() == R.id.refer_history) {
				Intent intent = new Intent(dashboardAcivity.this, editUserAcivity.class);
				intent.putExtra("id", userId);
				intent.putExtra("type", "refer_history");

				try {
					intent.putExtra("json", jSONObject.getJSONObject("User").getString("rfrpoint"));
				} catch (JSONException e) {
				}

				startActivity(intent);

			} else if (view.getId() == R.id.dash_notificatiom) {
				Intent intent = new Intent(dashboardAcivity.this, editUserAcivity.class);

				intent.putExtra("type", "notification");
				intent.putExtra("notice", Notice);

				startActivity(intent);

			} else if (view.getId() == R.id.dash_how_to_work) {
				if (!work_link.contains("#")) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(work_link));
					startActivity(browserIntent);

				}

			} else if (view.getId() == R.id.dash_withdraw) {

				Intent intent = new Intent(dashboardAcivity.this, withdrawactivity.class);

				intent.putExtra("id", userId);
				intent.putExtra("type", "withdraw");
				intent.putExtra("point", userpointInheader);
				intent.putExtra("phone", UserPhoneNumber);

				startActivity(intent);
			}

			else if (view.getId() == R.id.dash_leaderboard) {
				Intent intent = new Intent(dashboardAcivity.this, leaderboard.class);
				startActivity(intent);

			}

			else if (view.getId() == R.id.menu_Share) {

				//	String Share_text = "Hello! I am inviting you to download the"+appName+" . Download by following this link: ""https://tmswp.com and haw to work https://youtu.be/aPOibYMICuA use 5827356 as refer code.";
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, "hi");
				sendIntent.setType("text/plain");

				Intent shareIntent = Intent.createChooser(sendIntent, null);
				startActivity(shareIntent);

			} else if (view.getId() == R.id.Telegram_Chanel) {
				if (!telegrame_chanellink.contains("#")) {
					Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(telegrame_chanellink));
					startActivity(browserIntent);

				}

			}

			else if (view.getId() == R.id.menu_Logout) {

				AlertDialog.Builder logoutBuilder = new AlertDialog.Builder(dashboardAcivity.this);
				View inflate = getLayoutInflater().inflate(R.layout.logout, null);
				Button yes = inflate.findViewById(R.id.logout_yes);
				Button No = inflate.findViewById(R.id.logout_no);
				logoutBuilder.setView(inflate);
				AlertDialog logoutdialog = logoutBuilder.create();
				logoutdialog.show();
				yes.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {

						SharedPreferences sharedPreferences = getSharedPreferences("USER_ID", MODE_PRIVATE);
						SharedPreferences.Editor editor = sharedPreferences.edit();
						editor.putString("USER_ID", null);
						editor.apply();
						finish();

					}

				});
				No.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {

						logoutdialog.cancel();

					}

				});

			}

		}

	}

}