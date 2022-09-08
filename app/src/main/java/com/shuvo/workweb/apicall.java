package com.shuvo.workweb;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.json.JSONObject;

public class apicall {

	String result;
	String key;
	ApiresponseCallback apiresponseCallback;

	public apicall(Context context) {
		apiresponseCallback = (ApiresponseCallback) context;
	}

	/*	public  api(ApiresponseCallback apiresponseCallback) {
			
			apiresponseCallback = (ApiresponseCallback)context;
			
			
		//	this.apiresponseCallback = apiresponseCallback;
			
		}
		*/
	public void getApi(String url, String keyc) {
		key = keyc;
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(url).build();
		result = url;
		//apiresponseCallback.displayresponse("hi");
		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				apiresponseCallback.displayresponse(null, key, null);
				arg1.printStackTrace();
				result = "e";

			}

			@Override
			public void onResponse(Call arg0, Response arg1) {
				//	apiresponseCallback.displayresponse("hi");
				if (arg1.isSuccessful()) {
					try {
						//	result = "+";

						apiresponseCallback.displayresponse(arg1.body().string(), key, true);

						//	Toast.makeText(context,"+"+arg1.body().string(),Toast.LENGTH_LONG).show();

					} catch (Exception e) {
						//apiresponseCallback.displayresponse(null, key, false);
						//Toast.makeText(context, "+" + e, Toast.LENGTH_LONG).show();
					}

				}
			}

		});

	}

	public void postRequest(String url, String Json, String keyc) {
		key = keyc;

		OkHttpClient client = new OkHttpClient();
		RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), Json);
		Request request = new Request.Builder()

				.url(url).post(requestBody).build();
		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				apiresponseCallback.displayresponse(null, key, null);

			}

			@Override
			public void onResponse(Call arg0, Response arg1) {

				try {
					//apiresponseCallback.displayresponse(e+"", key, true);

					apiresponseCallback.displayresponse(arg1.body().string(), key, true);
				} catch (Exception e) {

					//	apiresponseCallback.displayresponse(null,""+e,false);

				}

			}

		});

	}

	public interface ApiresponseCallback {

	public	void displayresponse(String result, String key, Boolean error);

	}

}