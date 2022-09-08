package com.shuvo.workweb;

import android.widget.Toast;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.MobileAds;
import android.content.Context;
import android.widget.LinearLayout;

public class adloader {
	
	
	
	public  AdView loadAds(LinearLayout container, Context context, String adunit) {

		MobileAds.initialize(context, new OnInitializationCompleteListener() {

			@Override
			public void onInitializationComplete(InitializationStatus arg0) {

			}

		});
		AdView adView = new AdView(context);
        adView.setAdUnitId(adunit);
        container.addView(adView);
        AdRequest adRequest =new AdRequest.Builder().build();
        adView.setAdSize(AdSize.BANNER);
        adView.loadAd(adRequest);
		
return adView; 

	}
	
	
	
	
	
}