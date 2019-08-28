package co.adcel.sdkdemo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import co.adcel.init.AdCel;
import co.adcel.interstitialads.InterstitialListener;
import co.adcel.interstitialads.rewarded.RewardedAdValues;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "AdCelSDKDemo";
    static final String ADCEL_SDK_KEY =
            "89fdf849-b5bc-49d0-ad51-0b790e777ae4:fc7094bb-3ca7-4450-9a7e-320b6b4f4e42";

    private TextView sdkStateTextView;
    private TextView coinsTextView;
    private Button showRewardedAdButton;

    private int coins = 0;

    private InterstitialListener adcelInterstitialListener = new InterstitialListener() {
        @Override
        public void onFirstInterstitialLoad(String adType, String provider) {
            Log.d(TAG, "onFirstInterstitialLoad");

            RewardedAdValues rewardedSettings = AdCel.getRewardedAdValues();
            String rewardedName = rewardedSettings.getName();
            String rewardedValue = rewardedSettings.getValue();

            Toast toast = Toast.makeText(MainActivity.this,
                    String.format("onFirstInterstitialLoad %s %s, rewarded name: %s, value: %s", adType, provider, rewardedName, rewardedValue),
                    Toast.LENGTH_SHORT);
            toast.show();
            sdkStateTextView.setText("SDK is ready");
            showRewardedAdButton.setEnabled(true);
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
        }

        @Override
        public boolean onInterstitialFailedToShow(String adType) {
            Log.d(TAG, "onInterstitialFailedToShow");
            Toast toast = Toast.makeText(MainActivity.this,
                    String.format("onInterstitialFailedToShow %s", adType),
                    Toast.LENGTH_SHORT);
            toast.show();

            return false;
        }

        @Override
        public void onRewardedCompleted(String adProvider, String currencyName, String currencyValue) {
            Log.d(TAG, "onRewardedCompleted");
            Toast toast = Toast.makeText(MainActivity.this, String.format("onRewardedCompleted %s %s %s", adProvider, currencyName, currencyValue), Toast.LENGTH_SHORT);
            toast.show();

            coins++;
            refreshCoins();
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
        //AdCel.disableAdNetwork(AdCel.MASK_REWARDED, AdProvider.ADCOLONY, AdProvider.UNITYADS);
        //AdCel.setTargetingParam(TargetingParam.USER_GENDER, TargetingParam.USER_GENDER_MALE);
        //AdCel.setTargetingParam(TargetingParam.USER_AGE, "18");
        AdCel.setLogging(true);
        AdCel.initializeSDK(this, ADCEL_SDK_KEY, AdCel.MASK_REWARDED);

        AdCel.setInterstitialListener(adcelInterstitialListener);

        setContentView(R.layout.activity_main);

        sdkStateTextView = (TextView)findViewById(R.id.sdk_state);
        coinsTextView = (TextView)findViewById(R.id.coins);

        showRewardedAdButton = (Button)findViewById(R.id.earn_coin);
        showRewardedAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdCel.showInterstitialAd(AdCel.REWARDED);
            }
        });
    }

    void refreshCoins() {
        coinsTextView.setText(coins + " coins");
    }

    @Override
    protected void onResume() {
        super.onResume();
        AdCel.onResume(this);
    }

    @Override
    protected void onPause() {
        AdCel.onPause(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        AdCel.onDestroy(this);
        super.onDestroy();
    }
}
