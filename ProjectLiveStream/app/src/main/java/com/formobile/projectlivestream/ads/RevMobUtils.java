package com.formobile.projectlivestream.ads;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.formobile.projectlivestream.R;
import com.revmob.RevMob;
import com.revmob.RevMobAdsListener;
import com.revmob.RevMobUserGender;
import com.revmob.ads.banner.RevMobBanner;
import com.revmob.ads.fullscreen.RevMobFullscreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PTECH on 16-02-2015.
 */
public class RevMobUtils extends RevMobAdsListener{
    // Singleton instance
    private static RevMobUtils mInstance;

    // Revmob instance
    private RevMob mInstanceRevMob;

    // Video
    private RevMobFullscreen revMobFullscreen;
    private boolean shouldDisplayVideo;

    // Banner
    private RevMobBanner revMobBanner;

    private Activity mCurrentActivity;

    private RevMobUtils(){
        shouldDisplayVideo = true;
    }

    public static RevMobUtils getInstance(){
        if(mInstance == null){
            mInstance = new RevMobUtils();
        }

        return mInstance;
    }

    public void startSession(Activity activity){
        mInstanceRevMob = RevMob.start(activity);

        mInstanceRevMob.setUserGender(RevMobUserGender.MALE);
        mInstanceRevMob.setUserAgeRangeMin(16);
        mInstanceRevMob.setUserAgeRangeMax(60);
        List<String> interests = new ArrayList<String>();
        interests.add("sports");
        interests.add("football");
        interests.add("soccer");
        interests.add("ronaldo");
        interests.add("stream");
        interests.add("barcelona");
        interests.add("real madrid");
        mInstanceRevMob.setUserInterests(interests);
    }


    /**
     * Display Video
     * @param activity
     */
    public void displayVideo(Activity activity){
        if(mInstanceRevMob == null){
            mInstanceRevMob = RevMob.start(activity);
        }

        revMobFullscreen = mInstanceRevMob.createVideo(activity, this);

        AdvertiseController.updateLastTimeDisplayed();
    }

    /**
     * Display interstitial
     * @param activity
     */
    public void displayFullScreenInterstitial(Activity activity){
        if(mInstanceRevMob == null){
            mInstanceRevMob = RevMob.start(activity);
        }

        mInstanceRevMob.showFullscreen(activity, this);

        AdvertiseController.updateLastTimeDisplayed();
    }

    /**
     * Display Interstitial
     */
    public void displayInterstitial(Activity activity){
        if(shouldDisplayVideo){
            displayVideo(activity);
            shouldDisplayVideo = false;
        } else{
            displayFullScreenInterstitial(activity);
        }

    }



    public void displayBanner(Activity activity){

        if(mInstanceRevMob == null){
            mInstanceRevMob = RevMob.start(activity);
        }

        mCurrentActivity = activity;


        if(revMobBanner == null){
            revMobBanner = mInstanceRevMob.createBanner(activity);
        } else if(revMobBanner.getParent() != null && revMobBanner.getParent() instanceof ViewGroup){
            // Remove from parent
            ((ViewGroup) revMobBanner.getParent()).removeAllViews();
        }



        View viewGroup = activity.findViewById(R.id.adViewContainer);
        if(viewGroup != null ) {
            revMobBanner.setVisibility(View.VISIBLE);
            viewGroup.setVisibility(View.VISIBLE);
            ((ViewGroup) viewGroup).removeAllViews();

            ((ViewGroup) viewGroup).addView(revMobBanner);
        }
    }


    @Override
    public void onRevMobSessionIsStarted() {

    }

    @Override
    public void onRevMobSessionNotStarted(String s) {

    }

    @Override
    public void onRevMobAdReceived() {

    }

    @Override
    public void onRevMobAdNotReceived(String s) {

    }

    @Override
    public void onRevMobAdDisplayed() {

    }

    @Override
    public void onRevMobAdClicked() {
        if(mCurrentActivity != null){
            AdvertiseController.onClickAds(mCurrentActivity);
        }

    }

    @Override
    public void onRevMobEulaIsShown() {

    }

    @Override
    public void onRevMobEulaWasAcceptedAndDismissed() {

    }

    @Override
    public void onRevMobEulaWasRejected() {

    }

    @Override
    public void onRevMobVideoLoaded() {
        // Display Video
        if(revMobFullscreen != null){
            revMobFullscreen.show();

        }

        super.onRevMobVideoLoaded();
    }
}
