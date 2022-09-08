package com.shuvo.workweb;

import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class json {
	JSONObject jSONObject;

	JSONObject Jsonobjectreturn(String response) {
		try {
			//	String responString = response.body().string();

			jSONObject = new JSONObject(response);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jSONObject;
	}

}