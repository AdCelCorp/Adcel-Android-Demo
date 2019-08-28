package co.adcel.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.mopub.common.MoPub;
import com.mopub.common.MoPubReward;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubRewardedVideoListener;
import com.mopub.mobileads.MoPubRewardedVideos;
import com.mopub.mobileads.MoPubView;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        moPubInit();
    }

    private void moPubInit() {
        SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder("f2813ce29c244838aee10706693ec2c8")
                .withMediationSettings()
                .withAdditionalNetwork("com.mopub.mobileads.AdCelAdapterConfiguration")
                .build();
        if (MoPub.isSdkInitialized()) {
            showBanner();
        } else {
            MoPub.initializeSdk(this, sdkConfiguration, () -> MainActivity.this.showBanner());
        }
    }

    private void showBanner() {
        MoPubView moPubView = findViewById(R.id.mopub_banner);
        moPubView.setBannerAdListener(new MoPubView.BannerAdListener() {
            @Override
            public void onBannerLoaded(MoPubView banner) {

            }

            @Override
            public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {

            }

            @Override
            public void onBannerClicked(MoPubView banner) {

            }

            @Override
            public void onBannerExpanded(MoPubView banner) {

            }

            @Override
            public void onBannerCollapsed(MoPubView banner) {

            }
        });
        moPubView.setAdUnitId("62f3dd257791406a86c1311a46ff9fce"); // Enter your Ad Unit ID from www.mopub.com
        moPubView.loadAd();
    }

    private void showMoPubInterstitial() {
        MoPubInterstitial interstitial = new MoPubInterstitial(MainActivity.this, "cbcfb63cd31c444e850c2f427c5e34c0");
        interstitial.setInterstitialAdListener(new MoPubInterstitial.InterstitialAdListener() {
            @Override
            public void onInterstitialLoaded(MoPubInterstitial interstitial) {
                interstitial.show();
            }

            @Override
            public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {

            }

            @Override
            public void onInterstitialShown(MoPubInterstitial interstitial) {

            }

            @Override
            public void onInterstitialClicked(MoPubInterstitial interstitial) {

            }

            @Override
            public void onInterstitialDismissed(MoPubInterstitial interstitial) {

            }
        });
        interstitial.load();
    }

    private void showMoPubRewarded() {
        MoPubRewardedVideos.loadRewardedVideo("7ac023b78f15451fbc7c12a92c569410");
        MoPubRewardedVideos.setRewardedVideoListener(new MoPubRewardedVideoListener() {
            @Override
            public void onRewardedVideoLoadSuccess(@NonNull String adUnitId) {
                MoPubRewardedVideos.showRewardedVideo("7ac023b78f15451fbc7c12a92c569410");
            }

            @Override
            public void onRewardedVideoLoadFailure(@NonNull String adUnitId, @NonNull MoPubErrorCode errorCode) {

            }

            @Override
            public void onRewardedVideoStarted(@NonNull String adUnitId) {

            }

            @Override
            public void onRewardedVideoPlaybackError(@NonNull String adUnitId, @NonNull MoPubErrorCode errorCode) {

            }

            @Override
            public void onRewardedVideoClicked(@NonNull String adUnitId) {

            }

            @Override
            public void onRewardedVideoClosed(@NonNull String adUnitId) {

            }

            @Override
            public void onRewardedVideoCompleted(@NonNull Set<String> adUnitIds, @NonNull MoPubReward reward) {

            }
        });
    }

    public void onShowInterstitial(View view) {
        showMoPubInterstitial();
    }

    public void onShowRewarded(View view) {
        showMoPubRewarded();
    }
}
