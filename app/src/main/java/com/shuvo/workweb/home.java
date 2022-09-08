package com.shuvo.workweb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class home extends AppCompatActivity {
	Button singup, login;
	//	String id;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		getSupportActionBar().hide();
		setContentView(R.layout.home_layout);
		singup = findViewById(R.id.gotosingupacivity);
		singup.setOnClickListener(new View.OnClickListener(){
			
			@Override
			public void onClick(View arg0) {
			}
			
		});
		
		
	//	login = findViewById(R.id.gotologinacivity);

	/*	singup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(home.this, MainActivity.class);

				intent.putExtra("type", "singup");
				startActivity(intent);

			}

		});

		login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(home.this, MainActivity.class);
				intent.putExtra("type", "login");
				startActivity(intent);
			}

		}

		);

			String chek = authcheck();
		
			try {
				if (chek == null) {
		
					
		
				} else if (authcheck() != null) {
					Intent intent = new Intent(home.this, dashboardAcivity.class);
					startActivity(intent);
		
				}
		
			} catch (Exception e) {
		
				Toast.makeText(home.this, e + "", Toast.LENGTH_LONG).show();
		
			}
		*/
	}

	/*	*/

}