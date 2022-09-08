package com.shuvo.workweb;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.BoolRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lazyprogrammer.motiontoast.MotionStyle;
import com.lazyprogrammer.motiontoast.MotionToast;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class editUserAcivity extends AppCompatActivity implements apicall.ApiresponseCallback {
	SwipeRefreshLayout swipeRefreshLayout;
	LinearLayout edituserbody;
	LayoutInflater layoutInflater;
	RecyclerView recyclerView;
	Bundle bundle;
	TextView btntxt;
	String Nnpass;
	JSONObject jSONObject;
	TextInputLayout currentpasslayout;
	ProgressBar progressBar;
	Boolean claimable;
	CardView claim;

	JSONArray jSONArray;

	String domain, refercallkey = "this is refer call key", key, id, refer_claimkey = "this is refrr xlaim key",
			editprofileKey = "1k235edit rofil lmkeyb", passchangekey = "this is passch5858ange keyb";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		try {
			getSupportActionBar().hide();
		} catch (Exception e) {
		}
		setContentView(R.layout.edituser_layout);
		layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		edituserbody = findViewById(R.id.edit_body);
		swipeRefreshLayout = findViewById(R.id.edit_body_swip);

		bundle = getIntent().getExtras();
		id = getBundel("id");
		key = getString(R.string.key);
		domain = getString(R.string.Domain);

		Mother();

		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

			@Override
			public void onRefresh() {
				Mother();

			}

		});
	}

	public void Mother() {
		swipeRefreshLayout.setRefreshing(false);

		if (bundle != null) {
			if (Chekbundelkey("refer_history")) {
				loadReferTudu();
			} else if (Chekbundelkey("password_change")) {
				layoutInflater.inflate(R.layout.changepassword, edituserbody);
				btntxt = findViewById(R.id.edittext);
				progressBar = findViewById(R.id.edit_progress);
				CardView save = findViewById(R.id.edit_save);

				TextInputLayout newpasslayout;
				TextInputEditText currentpasstext, newpasstxt, renewpasstext;
				currentpasslayout = findViewById(R.id.edit_current_pass_layout);
				newpasslayout = findViewById(R.id.edit_new_passlayout);
				currentpasstext = findViewById(R.id.edit_current_pass);
				newpasstxt = findViewById(R.id.edit_new_pass);
				renewpasstext = findViewById(R.id.edit_retype_pass);
				save.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Boolean allok = true;

						String cpass = currentpasstext.getText().toString(), npass = newpasstxt.getText().toString(),
								rpass = renewpasstext.getText().toString();

						currentpasslayout.setErrorEnabled(false);
						newpasslayout.setErrorEnabled(false);

						if (cpass.length() == 0) {
							allok = false;
							currentpasslayout.setErrorEnabled(true);
							currentpasslayout.setError("Field is required");
						}

						if (npass.length() == 0 || rpass.length() == 0) {
							allok = false;
							newpasslayout.setErrorEnabled(true);
							newpasslayout.setError("Field is required");
						}
						if (!npass.equals(rpass)) {
							allok = false;
							newpasslayout.setErrorEnabled(true);
							newpasslayout.setError("Password Is not Match");
						}

						if (allok) {

							btntxt.setVisibility(View.GONE);
							progressBar.setVisibility(View.VISIBLE);
							Nnpass = npass;

							String json = "{\"cpass\":\"" + cpass + "\",\"npass\":\"" + npass + "\"}";

							new apicall(editUserAcivity.this).postRequest(
									domain + "changePassword?id=" + id + "&key=" + key, json, passchangekey);

						}

					}

				});

			} else if (Chekbundelkey("profile")) {
				layoutInflater.inflate(R.layout.changeprofileinfo, edituserbody);
				String name = getBundel("name"), phone = getBundel("phone");
				TextInputLayout namelayout, phonelayout;
				TextInputEditText nametext, phonetext;
				btntxt = findViewById(R.id.edittext);
				progressBar = findViewById(R.id.edit_progress);
				namelayout = findViewById(R.id.edit_namelayout);
				phonelayout = findViewById(R.id.edit_phonelayout);
				nametext = findViewById(R.id.edit_nametxt);
				phonetext = findViewById(R.id.edit_phonetext);
				nametext.setText(name);
				phonetext.setText(phone);

				CardView save = findViewById(R.id.edit_save);
				save.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						String Nname = nametext.getText().toString(), Nphone = phonetext.getText().toString();

						namelayout.setErrorEnabled(false);
						phonelayout.setErrorEnabled(false);
						Boolean allok = true;
						if (Nname.length() == 0) {
							allok = false;
							namelayout.setErrorEnabled(true);
							namelayout.setError("Invalid Name");
						}
						if (Nphone.length() == 0 || !Nphone.startsWith("01") || Nphone.length() < 11
								|| Nphone.length() > 11) {
							allok = false;
							phonelayout.setErrorEnabled(true);
							phonelayout.setError("Invalid Phone Number");
						}
						if (allok) {
							btntxt.setVisibility(View.GONE);
							progressBar.setVisibility(View.VISIBLE);

							String json = "{\"name\":\"" + Nname + "\",\"phone\":\"" + Nphone + "\"}";

							new apicall(editUserAcivity.this)
									.postRequest(domain + "editprofile?id=" + id + "&key=" + key, json, editprofileKey);

						}

					}

				});

			} else if (Chekbundelkey("notification")) {
				try {

					getSupportActionBar().show();
					getSupportActionBar()
							.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.app_main_color)));
					getSupportActionBar().setTitle("Notification");
					getSupportActionBar().setDisplayHomeAsUpEnabled(true);
					getSupportActionBar().setDisplayShowHomeEnabled(true);
				} catch (Exception e) {

				}
				View inflate = layoutInflater.inflate(R.layout.notification_layout, edituserbody);
				TextView textView = inflate.findViewById(R.id.notification_layout_notice);
				textView.setText(getBundel("notice"));

			}

		}
	}

	public void recycle(String result) {

		ArrayList<referhistorycontractore> arrayList = new ArrayList();

		try {
			jSONArray = new JSONArray(result);

			if (jSONArray.length() == 0) {
				TextView messgae = findViewById(R.id.message);
				ProgressBar progressBar = findViewById(R.id.progressbar);
				progressBar.setVisibility(View.GONE);
				messgae.setVisibility(View.VISIBLE);
				messgae.setText("No Refer History Available");

			} else {
				edituserbody.removeAllViews();
				layoutInflater.inflate(R.layout.refer_history, edituserbody);
				recyclerView = findViewById(R.id.referrcycler);
				recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
				for (int i = 0; i < jSONArray.length(); i++) {

					arrayList.add(new referhistorycontractore(jSONArray.getJSONObject(i).getString("userName"),
							jSONArray.getJSONObject(i).getString("status"),
							jSONArray.getJSONObject(i).getString("earnings"),
							jSONArray.getJSONObject(i).getString("Date"),
							jSONArray.getJSONObject(i).getString("reason")));

				}

				//	System.out.println(i);
				referhistoryAdapterrcyle referhistoryAdapterrcyle = new referhistoryAdapterrcyle(editUserAcivity.this,
						arrayList);
				recyclerView.setAdapter(referhistoryAdapterrcyle);

				claim = findViewById(R.id.Refer_claim);
				claim.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {

						btntxt = findViewById(R.id.refer_claim_txt);
						progressBar = findViewById(R.id.refer_claim_progresbar);
						btntxt.setVisibility(View.GONE);
						progressBar.setVisibility(View.VISIBLE);
						new apicall(editUserAcivity.this).postRequest(domain + "refer?id=" + id + "&key=" + key, "{}",
								refer_claimkey);
						claim.setClickable(false);
					}

				});

				if (claimable == false) {
					claim.setClickable(false);
					LinearLayout linearLayout = findViewById(R.id.referclaim_linear);
					linearLayout.setBackgroundColor(R.color.gray);

				}

			}
		} catch (JSONException e) {
			new toast(this).toast("e" + e, this);
		}

		//	recyclerView.setVisibility(View.GONE);

	}

	public void loadReferTudu() {
		edituserbody.removeAllViews();

		layoutInflater.inflate(R.layout.progressbar_layout, edituserbody);

		new apicall(editUserAcivity.this).getApi(domain + "refer?key=" + key + "&id=" + id, refercallkey);

	}

	public void errordailog(String message) {
		AlertDialog.Builder mdailog = new AlertDialog.Builder(editUserAcivity.this);
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
				finish();

			}

		}

		);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem arg0) {

		if (arg0.getItemId() == android.R.id.home) {
			finish();
		}

		return super.onOptionsItemSelected(arg0);
	}

	public Boolean Chekbundelkey(String value) {
		if (bundle.getString("type").equals(value)) {
			return true;
		} else {
			return false;
		}
	}

	public String getBundel(String key) {
		return bundle.getString(key);
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
			} catch (JSONException e) {

			}

			if (keyc.equals(refercallkey)) {
				try {

					String list = jSONObject.getString("list");
					claimable = jSONObject.getBoolean("status");
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							recycle(list);

						}

					});

				} catch (JSONException e) {
				}

			} else if (keyc.equals(refer_claimkey)) {

				runOnUiThread(new Runnable() {

					@Override
					public void run() {

						btntxt.setVisibility(View.VISIBLE);
						progressBar.setVisibility(View.GONE);
						btntxt.setText("Claime Success full");
						MotionToast motionToast = new MotionToast(editUserAcivity.this, 0, MotionStyle.LIGHT,
								MotionStyle.SUCCESS, MotionStyle.BOTTOM, "Success", "Refer Point Successfully claimed",
								MotionStyle.LENGTH_SHORT).show();
						loadReferTudu();
					}

				});

			}

			else if (keyc.equals(editprofileKey)) {

				try {

					String status = jSONObject.getString("status");
					if (status.equals("ok")) {

						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								progressBar.setVisibility(View.GONE);
								btntxt.setVisibility(View.VISIBLE);
								btntxt.setText("Change Saved Succfully");
								MotionToast motionToast = new MotionToast(editUserAcivity.this, 0, MotionStyle.LIGHT,
										MotionStyle.SUCCESS, MotionStyle.BOTTOM, "Success", "Change Saved Succfully",
										MotionStyle.LENGTH_SHORT).show();

							}

						});
					}

					else if (status.equals("no")) {

						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								progressBar.setVisibility(View.GONE);
								btntxt.setVisibility(View.VISIBLE);
								btntxt.setText("Faild To Save");

								MotionToast motionToast = new MotionToast(editUserAcivity.this, 0, MotionStyle.LIGHT,
										MotionStyle.WARNING, MotionStyle.BOTTOM, "Faild", "Phone Number Is Alredy Used",
										6000).show();

							}

						});

					}

				} catch (

				JSONException e) {
				}

			}

			else if (keyc.equals(passchangekey)) {
				try {

					String status = jSONObject.getString("status");

					if (status.equals("ok")) {

						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								progressBar.setVisibility(View.GONE);
								btntxt.setVisibility(View.VISIBLE);
								btntxt.setText("Changed Successfully");
								MotionToast motionToast = new MotionToast(editUserAcivity.this, 0, MotionStyle.LIGHT,
										MotionStyle.SUCCESS, MotionStyle.BOTTOM, "Success", "Change Saved Succfully",
										MotionStyle.LENGTH_SHORT).show();

								new AlertDialog.Builder(editUserAcivity.this).setTitle("Remember Your New Password")
										.setMessage(Nnpass).setCancelable(false)
										.setPositiveButton("ok", new DialogInterface.OnClickListener()

										{

											@Override
											public void onClick(DialogInterface arg0, int arg1) {
												arg0.cancel();

											}

										}).show()

								;

							}

						})

						;
					} else {

						runOnUiThread(new Runnable() {

							@Override
							public void run() {

								progressBar.setVisibility(View.GONE);
								btntxt.setVisibility(View.VISIBLE);
								btntxt.setText("Faild To Save");
								currentpasslayout.setErrorEnabled(true);
								currentpasslayout.setError("Password Is Incorrect");

								MotionToast motionToast = new MotionToast(editUserAcivity.this, 0, MotionStyle.LIGHT,
										MotionStyle.WARNING, MotionStyle.BOTTOM, "Faild", "Password Is Incorrect", 6000)
												.show();

							}

						});

					}

				} catch (JSONException e) {

				}

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