package com.shuvo.workweb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.provider.Settings;
import android.transition.SidePropagation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.BoolRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.cardview.widget.CardView;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lazyprogrammer.motiontoast.MotionStyle;
import com.lazyprogrammer.motiontoast.MotionToast;

import java.util.function.ToDoubleBiFunction;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements apicall.ApiresponseCallback {

	String singupReq = "singup";
	String status;
	CardView singupbtn, login;

	TextView btntxt;
	ProgressBar progressBar;
	View view;
	String loginKey = "logincall";

	String deviceID;
	LinearLayout mainlayout;

	LayoutInflater layoutInflater;
	Boolean allFieldOk;
	JSONObject jSONObject;
	TextInputEditText singupName, singupPhone, singupPassword, singupCPassword, referCode;
	String nametext, domain, refertext, cpasswordtext, phonetext, passwordtext, refercodetext;
	TextInputLayout phonelayout, namelayout, passwordlayout, referlayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			getSupportActionBar().hide();
		} catch (Exception e) {
		}

		setContentView(R.layout.activity_main);
		domain = getString(R.string.Domain);
		layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mainlayout = findViewById(R.id.main_layout);

		try {
			Bundle bundle = getIntent().getExtras();
			if (bundle != null) {
				String type = bundle.getString("type");
				if (type.equals("singup")) {
					SingupMother();

				} else {
					loginMother();
				}

			}

		} catch (Exception e) {
			toast(e + "mainactivity oncreat error");

		}

	}

	public void SingupMother() {

		try {

			deviceID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

			layoutInflater.inflate(R.layout.singup, mainlayout);
			btntxt = findViewById(R.id.btntxt);
			progressBar = findViewById(R.id.progress);

			phonelayout = findViewById(R.id.singup_phonelayout);
			namelayout = findViewById(R.id.namelayout);
			passwordlayout = findViewById(R.id.singup_passwordlayout);
			referlayout = findViewById(R.id.Refer_codelayout);

			singupName = findViewById(R.id.name);
			singupPhone = findViewById(R.id.singup_phone);
			singupPassword = findViewById(R.id.singup_password);
			singupCPassword = findViewById(R.id.singup_cpassword);
			referCode = findViewById(R.id.Refer_code);
			singupbtn = findViewById(R.id.singup);
			referlayout = findViewById(R.id.Refer_codelayout);
			singupbtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					passwordtext = singupPassword.getText().toString();
					cpasswordtext = singupCPassword.getText().toString();
					phonetext = singupPhone.getText().toString();
					nametext = singupName.getText().toString();
					nametext = nametext.replace("\n", " ");

					//new apicall(MainActivity.this).getApi(domain + "login", singupReq);
					//	toast(seterror() + "" + s);
					if (seterror()) {
						btntxt.setVisibility(View.GONE);
						progressBar.setVisibility(View.VISIBLE);

						refertext = referCode.getText().toString();
						String json = "{\"fingar\":\"" + deviceID + "\",\"name\":\"" + nametext + "\",\"phone\":\""
								+ phonetext + "\",\"password\":\"" + passwordtext + "\",\"rfrid\":\"" + refertext
								+ "\"}";
						String api = domain + "singup";
						new apicall(MainActivity.this).postRequest(api, json, singupReq);

					}

				}

			});

		} catch (Exception e) {
			toast(e + "singup Mother Error");
		}

	}

	public void loginMother() {
		try {
			layoutInflater.inflate(R.layout.login, mainlayout);
			btntxt = findViewById(R.id.btntxt);
			progressBar = findViewById(R.id.progress);

			phonelayout = findViewById(R.id.login_phonelayout);
			passwordlayout = findViewById(R.id.login_passlayout);
			singupPhone = findViewById(R.id.login_phone);
			singupPassword = findViewById(R.id.login_password);
			login = findViewById(R.id.login);

			login.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					phonetext = singupPhone.getText().toString();
					passwordtext = singupPassword.getText().toString();

					//	apicall apicall = new apicall();
					if (setloginerror()) {
						String json = "{\"phone\":\"" + phonetext + "\",\"password\":\"" + passwordtext + "\"}";
						String api = domain + "login";
						btntxt.setVisibility(View.GONE);
						progressBar.setVisibility(View.VISIBLE);

						//toast(api);

						new apicall(MainActivity.this).postRequest(api, json, loginKey);

					}

				}

			});

		} catch (Exception e) {
			toast(e + "lohin mlther etrrtor");

		}

	}

	//find start

	/*	try {
	
			domain = getString(R.string.Domain);
	
			mainlayout = findViewById(R.id.main_layout);
			//apicall.setapicallback(MainActivity.this);
	
			//find end
			if (authcheck() == null) {
				singup();
	
			} else {
				Intent intent = new Intent(MainActivity.this, dashboardAcivity.class);
				startActivity(intent);
	
			}
	
		} catch (Exception e) {
			toast("error" + e);
		}
	*/

	@Override
	protected void onRestart() {
		super.onRestart();

	}

	public Boolean setloginerror() {
		allFieldOk = true;
		phonelayout.setErrorEnabled(false);
		passwordlayout.setErrorEnabled(false);
		if (phonetext == null || phonetext.length() < 11 || phonetext.length() > 12) {
			phonelayout.setErrorEnabled(true);
			phonelayout.setError("Phone Number Is Not  Valid");
			allFieldOk = false;
		}
		if (passwordtext == null || passwordtext.length() < 1) {
			passwordlayout.setErrorEnabled(true);
			passwordlayout.setError("Password Is   required");
			allFieldOk = false;

		}
		return allFieldOk;
	}

	public Boolean seterror() {
		allFieldOk = true;

		phonelayout.setErrorEnabled(false);
		passwordlayout.setErrorEnabled(false);
		namelayout.setErrorEnabled(false);

		if (passwordtext.equals("") || !passwordtext.equals(cpasswordtext)) {
			passwordlayout.setErrorEnabled(true);
			passwordlayout.setError("Password is not Same");
			allFieldOk = false;
		}
		if (nametext.length() < 3) {
			namelayout.setErrorEnabled(true);
			namelayout.setError("Name more then 3 crachter");
			allFieldOk = false;
		}

		if (nametext.length() > 64) {
			namelayout.setErrorEnabled(true);
			namelayout.setError("Name Maximum 30 caracter");
			allFieldOk = false;
		}

		if (phonetext.isEmpty() || phonetext.length() < 11 || !phonetext.startsWith("01") || phonetext.length() >= 12)

		{
			phonelayout.setErrorEnabled(true);
			phonelayout.setError("Phone Number Is invalid");
			allFieldOk = false;
		}

		return allFieldOk;

	}

	//datasaving

	public void userLoggin(String userid) {

		SharedPreferences sharedPreferences = getSharedPreferences("USER_ID", MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("USER_ID", userid);
		editor.apply();

		Intent intent = new Intent(MainActivity.this, dashboardAcivity.class);
		startActivity(intent);
		this.finish();

	}

	public void seterrorForsingup(String Fieldname, String message) {

		if (Fieldname.equals("phone")) {
			phonelayout.setErrorEnabled(true);
			phonelayout.setError(message);
		} else if (Fieldname.equals("device")) {
			MotionToast motionToast = new MotionToast(MainActivity.this, 0, MotionStyle.WARNING, MotionStyle.BOTTOM,
					MotionStyle.BOTTOM, "Error", message, MotionStyle.LENGTH_LONG).show();
		} else if (Fieldname.equals("Refercode")) {
			referlayout.setErrorEnabled(true);
			referlayout.setError(message);

		}

	}

	public void setloginerrorafterApiCall(String fieldName, String Message) {
		phonelayout.setErrorEnabled(false);
		passwordlayout.setErrorEnabled(false);
		if (fieldName.equals("phone")) {

			phonelayout.setErrorEnabled(true);
			phonelayout.setError(Message);
		} else {
			passwordlayout.setErrorEnabled(true);
			passwordlayout.setError(Message);

		}

		//	}	});

	}

	@Override
	public void displayresponse(String result, String keyc, Boolean error) {
		if (error != null) {
			try {
				jSONObject = new JSONObject(result);
				status = jSONObject.getString("status");

				if (keyc.equals(singupReq)) {
					if (status.equals("success")) {
						String id = jSONObject.getString("userid");

						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								btntxt.setVisibility(View.VISIBLE);
								progressBar.setVisibility(View.GONE);

								userLoggin(id);

							}

						});

					} else if (status.equals("error")) {
						String FieldName = jSONObject.getString("errorField");
						String message = jSONObject.getString("message");

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								btntxt.setVisibility(View.VISIBLE);
								progressBar.setVisibility(View.GONE);

								seterrorForsingup(FieldName, message);

							}

						});

					}

				}

				else if (keyc.equals(loginKey)) {
					if (status.equals("success")) {

						String id = jSONObject.getString("userid");

						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								btntxt.setVisibility(View.VISIBLE);
								progressBar.setVisibility(View.GONE);

								userLoggin(id);

							}

						});

					} else if (status.equals("error")) {

						String FieldName = jSONObject.getString("errorField");
						String message = jSONObject.getString("message");

						runOnUiThread(new Runnable() {
							@Override
							public void run() {

								btntxt.setVisibility(View.VISIBLE);
								progressBar.setVisibility(View.GONE);
								setloginerrorafterApiCall(FieldName, message);

							}

						});

					}

				}

			} catch (JSONException e) {

			}

		} else {//net erroer

			runOnUiThread(new Runnable() {

				@Override
				public void run() {

					btntxt.setVisibility(View.VISIBLE);
					progressBar.setVisibility(View.GONE);

				}

			});

		}

		/*	try {
				if (error != null) {
		
					jSONObject = new JSONObject(result);
		
					if (key.equals(loginKey)) {
		
						status = jSONObject.getString("status");
		
						if (status.equals("error")) {
		
							String message = jSONObject.getString("message");
							String erroField = jSONObject.getString("errorField");
							//	toast(message);
							runOnUiThread(new Runnable() {
		
								@Override
								public void run() {
									
								}
		
							});
		
						} else {
							String userId = jSONObject.getString("userid");
		
							runOnUiThread(new Runnable() {
		
								@Override
								public void run() {
									
		
								}
		
							});
		
						}
		
					} else if (key.equals(singupReq)) {
						status = jSONObject.getString("status");
		
						if (status.equals("error")) {
		
							
							runOnUiThread(new Runnable() {
		
								@Override
								public void run() {
		
									seterrorForsingup(FieldName, message);
		
								}
		
							});
		
						} else if (status.equals("success"))
		
						{
							String userid = jSONObject.getString("userid");
		
							runOnUiThread(new Runnable() {
		
								@Override
								public void run() {
									userLoggin(userid);
		
								}
		
							});
		
						}
					}
		
				}
		
			} catch (Exception e) {
				toast(e + "");
		
			}*/
	}

	public void toast(String text) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();

			}

		});

	}
}
