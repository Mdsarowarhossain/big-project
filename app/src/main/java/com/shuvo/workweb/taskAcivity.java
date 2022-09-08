package com.shuvo.workweb;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

public class taskAcivity extends AppCompatActivity implements apicall.ApiresponseCallback {
	Bundle intentbundel;
	Button task_start;
	ProgressBar progressBar;
	SwipeRefreshLayout swipeRefreshLayout;
	TextView notasktextview, task_view, task_point, task_title, task_notice;
	LinearLayout main_layout;
	String id, Domain, taskhomecallerKey = "taskHomeCaller";
	LayoutInflater layoutInflater;
	String taskTitele, view, point, Notice;
	JSONObject jSONObject;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		try {
			getSupportActionBar().hide();
		} catch (Exception e) {
		} //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.app_main_color)));
		setContentView(R.layout.task_acivity);
		Domain = getString(R.string.Domain);
		layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		swipeRefreshLayout = findViewById(R.id.task_swiprefresh);
		intentbundel = getIntent().getExtras();
		main_layout = findViewById(R.id.task_main_layout);
		id = getBundelinfo("id");
		loadTaskhome();
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

			@Override
			public void onRefresh() {
				loadTaskhome();

			}

		});

	}

	public void successDailog(String Message, String title, Boolean error) {

		AlertDialog.Builder bverifyDailog = new AlertDialog.Builder(taskAcivity.this);
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
						finish();
					}

				});
			}

		});

		alert_dailog.show();

	}

	public void loadTaskhome() {

		//	new apicall(this).getApi("http://localhost:3000/task?key=1234&id=8173993727", taskhomecallerKey);

		new apicall(this).getApi(Domain + "task?key=" + getString(R.string.key) + "&id=" + id, taskhomecallerKey);
	}

	public void settaskhome(Boolean status) {
		if (status) {
			main_layout.removeAllViews();
			layoutInflater.inflate(R.layout.taskhome, main_layout);
			task_notice = findViewById(R.id.task_notice);
			task_view = findViewById(R.id.task_view);
			task_point = findViewById(R.id.task_point);
			task_title = findViewById(R.id.task_title);
			task_start = findViewById(R.id.task_start_task);

			task_notice.setText(Notice);
			task_title.setText(taskTitele);
			task_view.setText(view + " view");
			task_point.setText(point);
			task_start.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {

					Intent intent = new Intent(taskAcivity.this, task.class);
					intent.putExtra("id", id);
					startActivity(intent);

				}

			});

		} else if (!status) {
			main_layout.removeAllViews();
			layoutInflater.inflate(R.layout.notask, main_layout);
			progressBar = findViewById(R.id.task_progressbar);
			notasktextview = findViewById(R.id.task_no_task_message);

			progressBar.setVisibility(View.GONE);
			notasktextview.setVisibility(View.VISIBLE);
		}

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		loadTaskhome();
	}

	public String getBundelinfo(String key) {

		String id = intentbundel.getString(key);
		return id;
	}

	public void errordailog(String message) {
		AlertDialog.Builder mdailog = new AlertDialog.Builder(taskAcivity.this);
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
		swipeRefreshLayout.setRefreshing(false);
		if (error != null) {
			if (taskhomecallerKey.equals(key)) {
				try {
					jSONObject = new json().Jsonobjectreturn(result);

					if (jSONObject.getString("status").equals("ok")) {
						point = jSONObject.getJSONObject("task").getString("point");
						view = jSONObject.getJSONObject("task").getString("click");
						Notice = jSONObject.getJSONObject("notice").getString("notice");
						taskTitele = jSONObject.getJSONObject("task").getString("title");

						runOnUiThread(new Runnable() {

							@Override
							public void run() {

								settaskhome(true);

							}

						});
					} else if (jSONObject.getString("status").equals("vpn")) {
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

					else if (jSONObject.getString("status").equals("error")) {

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
				} catch (JSONException e) {

				}
			}
		}

		else {
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