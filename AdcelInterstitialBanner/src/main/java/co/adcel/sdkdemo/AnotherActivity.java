package co.adcel.sdkdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import co.adcel.init.AdCel;
import co.adcel.interstitialads.DefaultInterstitialListener;

public class AnotherActivity extends AppCompatActivity {
    static final String TAG = "AdCelSDKDemo";

    private DefaultInterstitialListener adcelInterstitialListener = new DefaultInterstitialListener() {
        @Override
        public void onFirstInterstitialLoad(String adType, String provider) {
            Log.d(TAG, "onFirstInterstitialLoad");
            Toast toast = Toast.makeText(AnotherActivity.this,
                    String.format("onFirstInterstitialLoad %s %s", adType, provider),
                    Toast.LENGTH_SHORT);
            toast.show();
        }

        @Override
        public void onInterstitialStarted(String adType, String provider) {
            Log.d(TAG, "onInterstitialStarted");
            Toast toast = Toast.makeText(AnotherActivity.this,
                    String.format("onInterstitialStarted %s %s", adType, provider),
                    Toast.LENGTH_SHORT);
            toast.show();
        }

        @Override
        public void onInterstitialClicked(String adType, String provider) {
            Log.d(TAG, "onInterstitialClicked");
            Toast toast = Toast.makeText(AnotherActivity.this,
                    String.format("onInterstitialClicked %s %s", adType, provider),
                    Toast.LENGTH_SHORT);
            toast.show();
        }

        @Override
        public void onInterstitialClosed(String adType, String provider) {
            Log.d(TAG, "onInterstitialClosed");
            Toast toast = Toast.makeText(AnotherActivity.this,
                    String.format("onInterstitialClosed %s %s", adType, provider),
                    Toast.LENGTH_SHORT);
            toast.show();

            AnotherActivity.super.onBackPressed();
        }

        @Override
        public boolean onInterstitialFailedToShow(String adType) {
            Log.d(TAG, "onInterstitialFailedToShow");
            Toast toast = Toast.makeText(AnotherActivity.this,
                    String.format("onInterstitialFailedToShow %s", adType),
                    Toast.LENGTH_SHORT);
            toast.show();

            AnotherActivity.super.onBackPressed();

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AdCel.setInterstitialListener(adcelInterstitialListener);
    }

    @Override
    public void onBackPressed() {
        AdCel.showInterstitialAd();
    }
}
