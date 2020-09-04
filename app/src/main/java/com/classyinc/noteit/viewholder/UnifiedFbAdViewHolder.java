package com.classyinc.noteit.viewholder;


import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.classyinc.noteit.R;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAdLayout;

public class UnifiedFbAdViewHolder extends RecyclerView.ViewHolder {

    public NativeAdLayout nativeAdLayout;
    public MediaView fbmediaView;
    public TextView fbAdHeadline , fbAdSponsoredTag , fbAdSocialContext,fbAdBody,fbAdvertiser;
    public Button fbCallToActionbtn;
    public LinearLayout fbAdChoiceContainer;

    public UnifiedFbAdViewHolder(NativeAdLayout nativeAdLayout) {
        super(nativeAdLayout);
        this.nativeAdLayout = nativeAdLayout;

        fbmediaView = nativeAdLayout.findViewById(R.id.fb_Ad_Mediaview_id);
        fbAdHeadline = nativeAdLayout.findViewById(R.id.fb_Ad_Headline_id);
        fbAdSponsoredTag = nativeAdLayout.findViewById(R.id.fb_sponsored_tag_id);
        fbAdSocialContext = nativeAdLayout.findViewById(R.id.fb_Ad_SocialContext_id);
        fbAdBody = nativeAdLayout.findViewById(R.id.fb_AdBody_id);
        fbCallToActionbtn = nativeAdLayout.findViewById(R.id.fb_Call_to_Action_id);
        fbAdvertiser = nativeAdLayout.findViewById(R.id.fb_Advertiser_id);

        fbAdChoiceContainer = nativeAdLayout.findViewById(R.id.fb_ad_choices_container);


    }



}
