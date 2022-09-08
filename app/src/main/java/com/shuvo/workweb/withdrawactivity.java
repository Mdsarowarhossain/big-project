package com.shuvo.workweb;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.service.autofill.LuhnChecksumValidator;
import android.util.JsonToken;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import androidx.cardview.widget.CardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class withdrawactivity extends AppCompatActivity implements apicall.ApiresponseCallback {
	Bundle bundle;
	RecyclerView recyclerView;
	JSONArray jSONArray;
	RadioGroup radioGroup;
	Boolean inConfimPage = false;
	TextView textView;
	ProgressBar progressBar;
	LayoutInflater layoutInflater;
	LinearLayout layout;
	TextView accountBalance, errortext;
	RadioButton radioButton;
	Button withdrawbtn;
	CardView Confirm_withdraw_card;
	TextInputLayout phone_layout, point_layout;
	TextInputEditText phonetext, pointtext;
	String point, phone, id, key, withdrawcaller_call = "this_is_withdraw_68", domain,
			withdrawhitory_key = "369this is withdrawhistorykey";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		try {
			getSupportActionBar().hide();
		} catch (Exception e) {
		}
		setContentView(R.layout.withdraw_layout);
		bundle = getIntent().getExtras();
		
		layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layout = findViewById(R.id.withdraw_body);
		bundle = getIntent().getExtras();
		domain = getString(R.string.Domain);

		key = getString(R.string.key);
		if (Chekbundelkey("withdraw")) {
			withdraw();
		} else if (Chekbundelkey("withdraw_history")) {
			id = getBundel("id");
			withdrawhistoryStart();

		}

		/*getSupportActionBar().setTitle("Withdraw");
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.app_main_color)));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		*/

	}

	public void withdrawhistoryStart() {
		layout.removeAllViews();
		layoutInflater.inflate(R.layout.progressbar_layout, layout);

		new apicall(withdrawactivity.this).getApi(domain + "withdraw?id=" + id, withdrawhitory_key);
	}

	public void withdrawHistory(String result) {

		ArrayList<withdrawHistoryContractore> arrayList = new ArrayList();

		try {
			jSONArray = new JSONArray(result);

			if (jSONArray.length() == 0) {

				TextView messgae = findViewById(R.id.message);
				ProgressBar progressBar = findViewById(R.id.progressbar);
				progressBar.setVisibility(View.GONE);
				messgae.setVisibility(View.VISIBLE);
				messgae.setText("No Withdraw History Available");

			} else {
				layout.removeAllViews();
				layoutInflater.inflate(R.layout.withdraw_histoty, layout);
				recyclerView = findViewById(R.id.withdraw_history_rcycle);
				recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
				for (int i = 0; i < jSONArray.length(); i++) {

					arrayList.add(new withdrawHistoryContractore(jSONArray.getJSONObject(i).getString("phonenumber"),
							jSONArray.getJSONObject(i).getString("status"),
							jSONArray.getJSONObject(i).getString("amount"),
							jSONArray.getJSONObject(i).getString("date"),
							jSONArray.getJSONObject(i).getString("method"),
							jSONArray.getJSONObject(i).getString("id")));

				}
				withdrawHistoryAdapter withdrawHistoryAdapter = new withdrawHistoryAdapter(withdrawactivity.this,
						arrayList);
				recyclerView.setAdapter(withdrawHistoryAdapter);

				//	System.out.println(i);
				//	referhistoryAdapterrcyle referhistoryAdapterrcyle = new referhistoryAdapterrcyle(editUserAcivity.this,
				//	arrayList);
				//	recyclerView.setAdapter(referhistoryAdapterrcyle);

			}
		} catch (JSONException e) {
			new toast(this).toast("e" + e, this);
		}
	}

	@Override
	public void onBackPressed() {

		if (inConfimPage) {
			withdraw();
		} else {
			super.onBackPressed();
		}
	}

	public void withdraw() {
		inConfimPage = false;
		layout.removeAllViews();
		layoutInflater.inflate(R.layout.firststep_withdraw, layout);
		point = getBundel("point");
		phone = getBundel("phone");

		id = getBundel("id");
		phone_layout = findViewById(R.id.withdraw_phone_layour);
		point_layout = findViewById(R.id.withdraw_pointt_layour);
		phonetext = findViewById(R.id.withdraw_phone_edittext);
		pointtext = findViewById(R.id.withdraw_pointt_edittext);
		pointtext.setText(point);
		phonetext.setText(phone);
		accountBalance = findViewById(R.id.withdraw_balance);
		accountBalance.setText(point);
		withdrawbtn = findViewById(R.id.withdraw_balance_btn);
		errortext = findViewById(R.id.withdraw_errortext);
		radioGroup = findViewById(R.id.withdraw_radio_group);

		withdrawbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
				radioButton = findViewById(checkedRadioButtonId);
				errortext.setText("");

				if (chekerBot(pointtext.getText().toString(), phonetext.getText().toString())) {
					if (checkedRadioButtonId != -1) {

						String text = radioButton.getText().toString();
						if (text.equals("Bkash") || text.equals("Nagad")) {
							if (Integer.parseInt(pointtext.getText().toString()) < 5000) {
								Toast.makeText(getApplicationContext(), pointtext.getText().toString(), 5000).show();
								point_layout.setErrorEnabled(true);
								point_layout.setError(text + "  এ সর্বনিম্ন withdraw 5000 point ");

							} else {
								confirmWithdraw(pointtext.getText().toString(), phonetext.getText().toString(), text);
							}
						} else {
							confirmWithdraw(pointtext.getText().toString(), phonetext.getText().toString(), "Mobile");

						}

					} else {

						errortext.setText("Please Chose one Payment Method");

					}
				}

			}

		});

	}

	public Boolean chekerBot(String this_point, String this_phone) {

		Boolean error = true;
		phone_layout.setErrorEnabled(false);
		point_layout.setErrorEnabled(false);

		if (this_point.equals("") || Integer.parseInt(this_point) < 2500) {
			point_layout.setErrorEnabled(true);
			point_layout.setError("Less Withdrawal 2500 point");
			error = false;

		} else if (Integer.parseInt(this_point) > Integer.parseInt(point)) {
			point_layout.setErrorEnabled(true);
			point_layout.setError("Insufficiant Balance");
			error = false;
		}
		if (this_phone.equals("") || this_phone.length() < 11 || this_phone.length() > 12
				|| !this_phone.startsWith("01")) {
			phone_layout.setErrorEnabled(true);
			phone_layout.setError("Please Enter A valid Number");
			error = false;
		}

		return error;
	}

	public Boolean Chekbundelkey(String value) {
		if (bundle.getString("type").equals(value)) {
			return true;
		} else {
			return false;
		}
	}

	public String getBundel(String key) {
		if (bundle != null) {
			return bundle.getString(key);

		} else {
			return "null";
		}
	}

	public void confirmWithdraw(String this_point, String this_phone, String method) {
		inConfimPage = true;
		layout.removeAllViews();
		layoutInflater.inflate(R.layout.withdraw_confirm, layout);
		Confirm_withdraw_card = findViewById(R.id.confirm_withdraw_card);
		textView = findViewById(R.id.confirm_withdraw_text);

		progressBar = findViewById(R.id.confirm_withdraw_progressbar);

		TextView summery = findViewById(R.id.withdraw_deatels);
		int AB = Integer.parseInt(point) - Integer.parseInt(this_point);
		String text = "Total Amount : \t" + this_point + " or " + Integer.parseInt(this_point) / 100
				+ " BDT \n \n Phone Number : \t" + this_phone + "\n \n Payment Method :\t" + method
				+ " \n \n Available Balance : \t" + AB + " or " + AB / 100 + "  BDT ";
		summery.setText(text);
		Confirm_withdraw_card.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				textView.setVisibility(View.GONE);
				progressBar.setVisibility(View.VISIBLE);
				String json = "{\"point\":\"" + this_point + "\",\"phone\":\"" + this_phone + "\",\"method\":\""
						+ method + "\"}";

				new apicall(withdrawactivity.this).postRequest(domain + "withdraw?key=" + key + "&id=" + id, json,
						withdrawcaller_call);

			}

		});

	}

	public void successDailog(String Message, String title, Boolean error) {

		AlertDialog.Builder bverifyDailog = new AlertDialog.Builder(withdrawactivity.this);
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

	@Override
	public boolean onOptionsItemSelected(MenuItem arg0) {
		if (arg0.getItemId() == android.R.id.home) {
			finish();

		}

		return super.onOptionsItemSelected(arg0);
	}

	@Override
	public void displayresponse(String result, String keyc, Boolean error) {
		if (error != null) {
			try {

				JSONObject jSONObject = new JSONObject(result);

				if (keyc.equals(withdrawcaller_call)) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {

							progressBar.setVisibility(View.GONE);
							textView.setVisibility(View.VISIBLE);
							textView.setText("widthraw request succefully Submited");
							try {
								if (jSONObject.getString("status").equals("ok")) {
									successDailog(jSONObject.getString("message"), jSONObject.getString("title"), true);
								} else {

									successDailog(jSONObject.getString("message"), jSONObject.getString("title"),
											false);

								}
							} catch (JSONException e) {
							}

						}

					});

				} else if (keyc.equals(withdrawhitory_key)) {

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							try {
								withdrawHistory(jSONObject.getString("list"));
							} catch (JSONException e) {
							}

						}

					});

				}
			} catch (JSONException e) {

			}
		}

	}

}