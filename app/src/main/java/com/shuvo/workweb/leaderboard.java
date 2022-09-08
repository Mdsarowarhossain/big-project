package com.shuvo.workweb;

import android.content.Context;
import android.content.DialogInterface;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class leaderboard extends AppCompatActivity implements apicall.ApiresponseCallback {
	LinearLayout loading;
	String domain;
	SwipeRefreshLayout swipeRefreshLayout;
	TextView textView;
	RecyclerView recyclerView;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		try {
			getSupportActionBar().hide();
		} catch (Exception e) {

		}
		setContentView(R.layout.topleader_layout);
		domain = getString(R.string.Domain);
		loading = findViewById(R.id.top_progress);
		textView = findViewById(R.id.top_message);
		swipeRefreshLayout = findViewById(R.id.leader_refresh);

		recyclerView = findViewById(R.id.leaderBoard_Rycyle);
		//	layoutInflater.inflate(R.layout.progressbar_layout, layout);
		recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
		loadstart();
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

			@Override
			public void onRefresh() {
			loadstart();
			}

		});
	}

	public void loadstart() {
		loading.setVisibility(View.VISIBLE);
		textView.setVisibility(View.GONE);
		new apicall(leaderboard.this).getApi(domain + "top-user", "");
	}

	public void rcycle(JSONArray jSONArray) {
		loading.setVisibility(View.GONE);
		if (jSONArray.length() != 0) {
			ArrayList<topUserContractor> arrayList = new ArrayList();

			for (int i = 0; i < jSONArray.length(); i++) {
				try {
					/*	*/

					arrayList.add(new topUserContractor(jSONArray.getJSONObject(i).getString("name"),
							jSONArray.getJSONObject(i).getString("point"),
							jSONArray.getJSONObject(i).getString("totalwithdraw"),
							jSONArray.getJSONObject(i).getString("totalrefer")));

				} catch (JSONException e) {
					Toast.makeText(leaderboard.this, e + "", Toast.LENGTH_LONG).show();

				}

			}
			topuserrcycle topuserrcycle = new topuserrcycle(leaderboard.this, arrayList);
			recyclerView.setAdapter(topuserrcycle);
 			
		} else {
			textView.setVisibility(View.VISIBLE);
			textView.setTextColor(getColor(R.color.red));
			textView.setText("No Records Found");
		}

	}

	public void errordailog(String message) {
		AlertDialog.Builder mdailog = new AlertDialog.Builder(leaderboard.this);
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
		swipeRefreshLayout.setRefreshing(false);

		if (error != null) {
			try {

				JSONObject jSONObject = new JSONObject(result);

				String st = jSONObject.getString("list");
				JSONArray jSONArray = new JSONArray(st);
				String st2 = jSONArray.getJSONObject(1).getString("name");

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						rcycle(jSONArray);
					}

				});

			} catch (JSONException e) {

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