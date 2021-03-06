package co.adcel.sdkdemo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import co.adcel.init.AdCel;
import co.adcel.nativeads.AdCelNative;
import co.adcel.nativeads.MediaLayout;
import co.adcel.nativeads.NativeAd;
import co.adcel.nativeads.NativeAdView;
import co.adcel.nativeads.OnNativeAdsAvailabilityListener;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "AdCelSDKDemo";
    static final int ADS_COUNT = 5;
    static final int MAX_ATTEMPT_COUNT = 10;

    List<String> items;
    List<NativeAd> ads;
    ListView listView;
    MyAdapter listAdapter;
    int attemptCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));

        items = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            items.add("Item " + i);
        }
        ads = new ArrayList<>();

        //AdCel.disableAdNetwork(AdCel.MASK_NATIVE, AdProvider.APPLOVIN);
        //AdCel.setTargetingParam(TargetingParam.USER_GENDER, TargetingParam.USER_GENDER_MALE);
        //AdCel.setTargetingParam(TargetingParam.USER_AGE, "18");
        AdCel.setLogging(true);
        AdCel.initializeSDK(this,
                "89fdf849-b5bc-49d0-ad51-0b790e777ae4:fc7094bb-3ca7-4450-9a7e-320b6b4f4e42",
                AdCel.MASK_NATIVE);
        AdCel.setNativeAdsAvailabilityListener(new OnNativeAdsAvailabilityListener() {
            @Override
            public void onAvailable() {
                loadAds();
            }

            @Override
            public void onError(String error) {
                Log.d(TAG, "NativeAds onError: " + error);
                Toast toast = Toast.makeText(MainActivity.this,
                        String.format("NativeAds onError:  %s", error),
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listView);
        listAdapter = new MyAdapter();
        listView.setAdapter(listAdapter);
    }

    void loadAds() {
        if (ads.size() >= ADS_COUNT) {
            return;
        }

        AdCel.loadNativeAd(this, new AdCelNative.AdLoadListener() {
            @Override
            public void onLoadAd(List<NativeAd> list, String s) {
                NativeAd ad = list.get(0);
                // Use object ad for display Native advertising
                ads.add(ad);

                listAdapter.notifyDataSetChanged();

                // Setup expired ad event listener (for RTB providers)
                ad.setOnAdExpiredListener(new NativeAd.OnAdExpiredListener() {
                    @Override
                    public void onAdExpired(NativeAd nativeAd) {
                        ads.remove(nativeAd);
                        listAdapter.notifyDataSetChanged();
                        loadAds();
                    }
                });

                loadAds();
            }

            @Override
            public AdCelNative.Action onFailLoadAd(String error, String provider) {
                // Failed to load ad one of the providers in the queue
                // By default try to load ad of the next provider
                return AdCelNative.Action.TRY_NEXT_PROVIDER;
                /**
                 * Available actions
                 * NONE, - don't try to load ad
                 * RETRY_SAME_PROVIDER, - try to load ad of the same provider
                 * TRY_NEXT_PROVIDER - try to load ad of the next provider
                 */
            }

            @Override
            public void onFailLoadAdAllProviders() {
                // Failed to load ad
                if (attemptCount < MAX_ATTEMPT_COUNT) {
                    attemptCount++;
                    loadAds();
                }
            }

            @Override
            public boolean onShouldSkipRTBProviders() {
                // return true if you want to skip ad from RTB provider
                return false;
            }
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.size() + ads.size();
        }

        @Override
        public Object getItem(int position) {
            // some magic logic for positioning ads items =)
            if (ads.size() == 0) {
                return items.get(position);
            } else {
                int adIndex = position / 10;
                if (position % 10 == 0 && adIndex < ads.size()) {
                    return ads.get(adIndex);
                } else {
                    int index = position - Math.min(adIndex, ads.size());
                    return items.get(index);
                }
            }
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Object item = getItem(position);
            if (item instanceof NativeAd) {
                return getNativeAdView(convertView, (NativeAd)item, parent);
            } else {
                return getItemView(convertView, (String)item, parent);
            }
        }

        View getNativeAdView(View convertView, NativeAd ad, ViewGroup parent) {
            if (convertView == null || !(convertView instanceof NativeAdView)) {
                NativeAdView view = (NativeAdView)View.inflate(parent.getContext(), R.layout.native_ad_view, null);
                view.setTitleView(view.findViewById(R.id.title));
                view.setDescriptionTextView(view.findViewById(R.id.text));
                view.setIconView(view.findViewById(R.id.icon));
                view.setMediaLayout((MediaLayout) view.findViewById(R.id.media));
                view.setStarRaitingView(view.findViewById(R.id.rating));
                view.setCtaTextView(view.findViewById(R.id.installButton));

                view.setOnAdClickListener(new NativeAdView.OnAdClickListener() {
                    @Override
                    public void onAdClick(NativeAdView nativeAdView) {
                        Toast.makeText(MainActivity.this, "Native Ad clicked", Toast.LENGTH_SHORT)
                                .show();
                    }
                });

                fillNativeAdView(view, ad);

                return view;
            } else if (convertView instanceof NativeAdView) {
                NativeAdView view = (NativeAdView)convertView;

                fillNativeAdView(view, ad);

                return view;
            } else {
                return convertView;
            }
        }

        void fillNativeAdView(NativeAdView view, NativeAd ad) {
            ((TextView)view.getTitleView()).setText(ad.getTitle());
            ((TextView)view.getDescriptionTextView()).setText(ad.getDescriptionText());
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(ad.getIconUrl(), (ImageView) view.getIconView());
            imageLoader.displayImage(ad.getImageUrl(), (ImageView) view.getMediaLayout().getImageView());
            ((TextView)view.getStarRaitingView()).setText("Rating " + String.valueOf(ad.getStarRaiting()));

            if (ad.getCtaText() != null) {
                ((TextView)view.getCtaTextView()).setText(ad.getCtaText());
            } else {
                ((TextView)view.getCtaTextView()).setText("Install");
            }

            view.setNativeAd(ad);
        }

        View getItemView(View convertView, String item, ViewGroup parent) {
            if (convertView == null || !(convertView.getTag() instanceof ItemViewHolder)) {
                ItemViewHolder holder = new ItemViewHolder();
                LinearLayout view = (LinearLayout)View.inflate(parent.getContext(), R.layout.item_view, null);
                holder.textView = (TextView)view.findViewById(R.id.text);
                holder.textView.setText(item);
                view.setTag(holder);
                return view;
            } else {
                ItemViewHolder holder = (ItemViewHolder)convertView.getTag();
                holder.textView.setText(item);
                return convertView;
            }
        }
    }

    class ItemViewHolder {
        TextView textView;
    }
}
