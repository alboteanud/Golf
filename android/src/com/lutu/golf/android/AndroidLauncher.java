package com.lutu.golf.android;

import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.bugsense.trace.BugSenseHandler;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.lutu.golf.ActionResolver;
import com.lutu.golf.GolfGame;

public class AndroidLauncher extends AndroidApplication implements ActionResolver {
/*	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new GolfGame(), config);
	}*/

	private static final String AD_UNIT_ID_INTERSTITIAL = "ca-app-pub-3931793949981809/3535464770";
	private InterstitialAd interstitialAd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final String apiKey = BuildConfig.DEBUG ? getString(R.string.bugsense_api_key_testing)
				: getString(R.string.bugsense_api_key_release);

		BugSenseHandler.initAndStartSession(this, apiKey);

		final AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		config.useWakelock = true;
		config.useGL20 = true;

		initialize(new GolfGame(this), config);

		interstitialAd = new InterstitialAd(this);
		interstitialAd.setAdUnitId(AD_UNIT_ID_INTERSTITIAL);
//		requestNewInterstitial();
		interstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {

				     }

			@Override
			public void onAdClosed() {
				requestNewInterstitial();
			}
		});
	}

	@Override
	public void showInterstital() {
		   try {
			      runOnUiThread(new Runnable() {
				        public void run() {
							if (interstitialAd.isLoaded())
						            interstitialAd.show();
							else requestNewInterstitial();  //eu
					        }
				      });
			    } catch (Exception e) {
			    }
		  }

	private void requestNewInterstitial() {
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice("D7C3FED6C0273D67D38E0186CFA3B220")
				.build();
		interstitialAd.loadAd(adRequest);
	}

}
