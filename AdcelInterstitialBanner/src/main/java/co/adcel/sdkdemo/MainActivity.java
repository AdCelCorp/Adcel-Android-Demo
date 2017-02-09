package co.adcel.sdkdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import co.adcel.adbanner.BannerAdContainer;
import co.adcel.adbanner.BannerListener;
import co.adcel.init.AdCel;
import co.adcel.interstitialads.DefaultInterstitialListener;

public class MainActivity extends Activity {
    static final String TAG = "AdCelSDKDemo";
    static final String ADCEL_SDK_KEY =
            "89fdf849-b5bc-49d0-ad51-0b790e777ae4:fc7094bb-3ca7-4450-9a7e-320b6b4f4e42";

    private TextView bannerStateTextView;
    private TextView interstitialStateTextView;
    private Button showInterstitialButton;
    private Button showAdOnNewActivityButton;
    private BannerAdContainer banner;

    private boolean showAdOnNewActivity = false;

    private DefaultInterstitialListener adcelInterstitialListener = new DefaultInterstitialListener() {
        @Override
        public void onFirstInterstitialLoad(String adType, String provider) {
            Log.d(TAG, "onFirstInterstitialLoad");
            Toast toast = Toast.makeText(MainActivity.this,
                    String.format("onFirstInterstitialLoad %s %s", adType, provider),
                    Toast.LENGTH_SHORT);
            toast.show();
            interstitialStateTextView.setText("Interstitial is ready");
            showInterstitialButton.setEnabled(true);
            showAdOnNewActivityButton.setEnabled(true);
        }

        @Override
        public void onInterstitialStarted(String adType, String provider) {
            Log.d(TAG, "onInterstitialStarted");
            Toast toast = Toast.makeText(MainActivity.this,
                    String.format("onInterstitialStarted %s %s", adType, provider),
                    Toast.LENGTH_SHORT);
            toast.show();
        }

        @Override
        public void onInterstitialClicked(String adType, String provider) {
            Log.d(TAG, "onInterstitialClicked");
            Toast toast = Toast.makeText(MainActivity.this,
                    String.format("onInterstitialClicked %s %s", adType, provider),
                    Toast.LENGTH_SHORT);
            toast.show();
        }

        @Override
        public void onInterstitialClosed(String adType, String provider) {
            Log.d(TAG, "onInterstitialClosed");
            Toast toast = Toast.makeText(MainActivity.this,
                    String.format("onInterstitialClosed %s %s", adType, provider),
                    Toast.LENGTH_SHORT);
            toast.show();

            if (showAdOnNewActivity) {
                startNewActivity();
                showAdOnNewActivity = false;
            }
        }

        @Override
        public boolean onInterstitialFailedToShow(String adType) {
            Log.d(TAG, "onInterstitialFailedToShow");
            Toast toast = Toast.makeText(MainActivity.this,
                    String.format("onInterstitialFailedToShow %s", adType),
                    Toast.LENGTH_SHORT);
            toast.show();

            if (showAdOnNewActivity) {
                startNewActivity();
                showAdOnNewActivity = false;
            }

            return false;
        }

        @Override
        public void onInterstitialFailLoad(String adType, String provider) {
            Log.d(TAG, "onInterstitialFailLoad");
            Toast toast = Toast.makeText(MainActivity.this,
                    String.format("onInterstitialFailLoad %s %s", adType, provider),
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //AdCel.setTestMode(true);
        //AdCel.disableAdNetwork(AdCel.MASK_BANNER | AdCel.MASK_IMAGE, AdProvider.ADMOB, AdProvider.STARTAPP);
        //AdCel.disableAdNetwork(AdCel.MASK_INTERSTITIAL | AdCel.MASK_REWARDED, AdProvider.ADCOLONY, AdProvider.UNITYADS);
        //AdCel.setTargetingParam(TargetingParam.USER_GENDER, TargetingParam.USER_GENDER_MALE);
        //AdCel.setTargetingParam(TargetingParam.USER_AGE, "18");
        AdCel.setLogging(true);
        AdCel.initializeSDK(this, ADCEL_SDK_KEY,
                AdCel.MASK_BANNER | AdCel.MASK_INTERSTITIAL);

        AdCel.setInterstitialListener(adcelInterstitialListener);


        setContentView(R.layout.activity_main);

        bannerStateTextView = (TextView)findViewById(R.id.banner_state);

        interstitialStateTextView = (TextView)findViewById(R.id.interstitial_state);

        showInterstitialButton = (Button)findViewById(R.id.button_show_interstitial);
        showInterstitialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdCel.showInterstitialAd();
            }
        });

        showAdOnNewActivityButton = (Button)findViewById(R.id.button_show_interstitial_on_new_activity);
        showAdOnNewActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAdOnNewActivity = true;
                AdCel.showInterstitialAd();
            }
        });

        if (!AdCel.isAvailableAd(AdCel.BANNER)) {
            bannerStateTextView.setText("Banner is loading...");
        } else {
            bannerStateTextView.setText("Banner is ready");
        }
        if (!AdCel.isAvailableAd(AdCel.INTERSTITIAL)) {
            interstitialStateTextView.setText("Interstitial is loading...");
            showInterstitialButton.setEnabled(false);
            showAdOnNewActivityButton.setEnabled(false);
        } else {
            interstitialStateTextView.setText("Interstitial is ready");
        }

        banner = (BannerAdContainer)findViewById(R.id.adcel_banner);
        banner.setBannerListener(new BannerListener() {

            @Override
            public void onBannerLoad() {
                Log.d(TAG, "onBannerLoad");
                Toast toast = Toast.makeText(MainActivity.this, "onBannerLoad",
                        Toast.LENGTH_SHORT);
                toast.show();
                bannerStateTextView.setText("Banner is ready");
            }

            @Override
            public void onBannerFailedToLoad() {
                Log.d(TAG, "onBannerFailedToLoad");
                Toast toast = Toast.makeText(MainActivity.this, "onBannerFailedToLoad",
                        Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onBannerClicked() {
                Log.d(TAG, "onBannerClicked");
                Toast toast = Toast.makeText(MainActivity.this, "onBannerClicked",
                        Toast.LENGTH_SHORT);
                toast.show();
            }

        });
    }

    void startNewActivity() {
        Intent intent = new Intent(this, AnotherActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AdCel.setInterstitialListener(adcelInterstitialListener);
        AdCel.onResume(this);
        banner.resume();
    }

    @Override
    protected void onPause() {
        AdCel.onPause(this);
        banner.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        AdCel.onDestroy(this);
        banner.destroy();
        super.onDestroy();
    }
}
