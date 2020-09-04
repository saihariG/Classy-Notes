package com.classyinc.noteit.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.classyinc.noteit.R;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

public class UnifiedNativeAdViewHolder extends RecyclerView.ViewHolder {

    private UnifiedNativeAdView unifiedNativeAdView;

    public UnifiedNativeAdView getAdView() {
        return unifiedNativeAdView;
    }


    public UnifiedNativeAdViewHolder(@NonNull View itemView) {
        super(itemView);

        unifiedNativeAdView = (UnifiedNativeAdView) itemView.findViewById(R.id.NativeAdvanced_ad_id);

        // The MediaView will display a video asset if one is present in the ad, and the
        // first image asset otherwise.
        unifiedNativeAdView.setMediaView((MediaView) unifiedNativeAdView.findViewById(R.id.Ad_Mediaview_id));

        // Register the view used for each individual asset.
        unifiedNativeAdView.setHeadlineView(unifiedNativeAdView.findViewById(R.id.Ad_Headline_id));
        unifiedNativeAdView.setBodyView(unifiedNativeAdView.findViewById(R.id.AdBody_id));
        unifiedNativeAdView.setCallToActionView(unifiedNativeAdView.findViewById(R.id.Call_to_Action_id));
        // unifiedNativeAdView.setIconView(unifiedNativeAdView.findViewById(R.id.ad_icon));

        unifiedNativeAdView.setPriceView(unifiedNativeAdView.findViewById(R.id.ad_price));
        unifiedNativeAdView.setStarRatingView(unifiedNativeAdView.findViewById(R.id.ad_rating));
        //unifiedNativeAdView.setStoreView(unifiedNativeAdView.findViewById(R.id.ad_store));
        unifiedNativeAdView.setAdvertiserView(unifiedNativeAdView.findViewById(R.id.Ad_Advertiser_id));
    }
}
