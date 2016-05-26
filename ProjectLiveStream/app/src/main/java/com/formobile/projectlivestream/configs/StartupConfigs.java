package com.formobile.projectlivestream.configs;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.formobile.projectlivestream.utils.AnalyticsHelper;

import org.codehaus.jackson.annotate.JsonCreator;

import java.io.Serializable;
import java.util.List;

/**
 * Created by PTECH on 07-02-2015.
 */
public class StartupConfigs implements Serializable {

    private UpdateEntity updateEntity;
    private StartupMessage startupMessage;
    private LeadBoltConfigs leadBoltConfigs;
    private AdmobConfigs admobConfigs;
    private String appoDealId;
    private List<String> listAdsKeywords;
    private AdProviderType adProviderTypeV2;
    private String appName;
    private String urlSport;
    private String urlFallbackData;
    private String videoPlayerBaseUrl; // Replace <_FID>
    private ProviderType providerType;
    private boolean hasOtherSports;
    private long timeFreeOfAds;
    private long periodToDisplayAds;
    private int probabilityAds; // 0 - 100
    private boolean hideMatches;
    private CatchThem catchThem;

    public StartupConfigs(){

    }

    public String getAppName() {
        if(appName == null){
            return "live stream";
        }
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public List<String> getListAdsKeywords() {
        return listAdsKeywords;
    }

    public void setListAdsKeywords(List<String> listAdsKeywords) {
        this.listAdsKeywords = listAdsKeywords;
    }

    public String getVideoPlayerBaseUrl() {
        if(videoPlayerBaseUrl != null){
            return videoPlayerBaseUrl;
        }

        return "http://195.154.227.38:1935/live/<_FID>/playlist.m3u8";
    }

    public void setVideoPlayerBaseUrl(String videoPlayerBaseUrl) {
        this.videoPlayerBaseUrl = videoPlayerBaseUrl;
    }

    public UpdateEntity getUpdateEntity() {
        return updateEntity;
    }

    public void setUpdateEntity(UpdateEntity updateEntity) {
        this.updateEntity = updateEntity;
    }

    public StartupMessage getStartupMessage() {
        return startupMessage;
    }

    public void setStartupMessage(StartupMessage startupMessage) {
        this.startupMessage = startupMessage;
    }

    public boolean isHasOtherSports() {
        return hasOtherSports;
    }

    public void setHasOtherSports(boolean hasOtherSports) {
        this.hasOtherSports = hasOtherSports;
    }

    public LeadBoltConfigs getLeadBoltConfigs() {
        return leadBoltConfigs;
    }

    public void setLeadBoltConfigs(LeadBoltConfigs leadBoltConfigs) {
        this.leadBoltConfigs = leadBoltConfigs;
    }

    public AdProviderType getAdProviderTypeV2() {
        return adProviderTypeV2;
    }

    public void setAdProviderTypeV2(AdProviderType adProviderTypeV2) {
        this.adProviderTypeV2 = adProviderTypeV2;
    }

    public String getAppoDealId() {
        return appoDealId;
    }

    public void setAppoDealId(String appoDealId) {
        this.appoDealId = appoDealId;
    }

    public String getUrlSport() {
        return urlSport;
    }

    public void setUrlSport(String urlSport) {
        this.urlSport = urlSport;
    }

    public String getUrlFallbackData() {
        return urlFallbackData;
    }

    public void setUrlFallbackData(String urlFallbackData) {
        this.urlFallbackData = urlFallbackData;
    }

    public ProviderType getProviderType() {
        return providerType;
    }

    public void setProviderType(ProviderType providerType) {
        this.providerType = providerType;
    }

    public AdmobConfigs getAdmobConfigs() {
        return admobConfigs;
    }

    public void setAdmobConfigs(AdmobConfigs admobConfigs) {
        this.admobConfigs = admobConfigs;
    }

    public long getTimeFreeOfAds() {
        return timeFreeOfAds;
    }

    public void setTimeFreeOfAds(long timeFreeOfAds) {
        this.timeFreeOfAds = timeFreeOfAds;
    }

    public long getPeriodToDisplayAds() {
        return periodToDisplayAds;
    }

    public void setPeriodToDisplayAds(long periodToDisplayAds) {
        this.periodToDisplayAds = periodToDisplayAds;
    }

    public int getProbabilityAds() {
        return probabilityAds;
    }

    public void setProbabilityAds(int probabilityAds) {
        this.probabilityAds = probabilityAds;
    }

    public boolean isHideMatches() {
        return hideMatches;
    }

    public void setHideMatches(boolean hideMatches) {
        this.hideMatches = hideMatches;
    }

    public CatchThem getCatchThem() {
        return catchThem;
    }

    public void setCatchThem(CatchThem catchThem) {
        this.catchThem = catchThem;
    }

    /**
     * Update entity
     */
    public class UpdateEntity{
        private boolean enabled;
        private int minVersion;
        private String urlUpdate;

        public UpdateEntity(){

        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public int getMinVersion() {
            return minVersion;
        }

        public void setMinVersion(int minVersion) {
            this.minVersion = minVersion;
        }

        public String getUrlUpdate() {
            return urlUpdate;
        }

        public void setUrlUpdate(String urlUpdate) {
            this.urlUpdate = urlUpdate;
        }

        public boolean checkIfShouldUpdate(Context context) {
            try {
                int versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;

                return ((versionCode < minVersion) && isEnabled());
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("VersionUpdate", e.getMessage(), e);
            }

            return false;
        }

        public UpdateEntity generateUpdateEntityExampleObj(){
            this.setEnabled(true);
            this.setMinVersion(2);
            this.setUrlUpdate("http://www.google.pt");

            return this;
        }
    }


    /**
     * A message to be displayed when
     */
    public class StartupMessage{
        private boolean enabled;
        private String title;
        private String message;
        private int id;

        public StartupMessage(){

        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public StartupMessage generateStartupMessageExample(){
            this.setEnabled(true);
            this.setTitle("Startup Title");
            this.setId(1);
            this.setMessage("Startup Message");

            return this;
        }
    }

    /**
     * Leadbolt Ids Configs
     */
    public class LeadBoltConfigs{
        private String interstitialId;
        private String audioAdId;

        public LeadBoltConfigs(){

        }

        public String getInterstitialId() {
            return interstitialId;
        }

        public void setInterstitialId(String interstitialId) {
            this.interstitialId = interstitialId;
        }

        public String getAudioAdId() {
            return audioAdId;
        }

        public void setAudioAdId(String audioAdId) {
            this.audioAdId = audioAdId;
        }

        public LeadBoltConfigs generateExample(){
            this.setAudioAdId("500614032");
            this.setInterstitialId("540749344");

            return this;
        }
    }

    /**
     * Admob configs
     */
    public class AdmobConfigs{
        private String bannerId;
        private String interstitialId;

        public AdmobConfigs(){

        }

        public String getBannerId() {
            return bannerId;
        }

        public void setBannerId(String bannerId) {
            this.bannerId = bannerId;
        }

        public String getInterstitialId() {
            return interstitialId;
        }

        public void setInterstitialId(String interstitialId) {
            this.interstitialId = interstitialId;
        }

        public AdmobConfigs generateExample(){
            this.setInterstitialId("ca-app-pub-5492791201497132/8404586407");
            this.setBannerId("ca-app-pub-5492791201497132/5451120000");

            return this;
        }
    }

    /**
     * Provider types
     */
    public enum AdProviderType{
        LEADBOLT, ADMOB, REVMOB, APPO_DEAL, NONE;

        @JsonCreator
        public static AdProviderType create(String value) {
            if(value == null) {
                return NONE;
            }

            for(AdProviderType adsProvider : values()){
                if(adsProvider.name().equals(value)){
                    return adsProvider;
                }
            }

            return NONE;
        }

    }


    /**
     * Catch ip
     */
    public class CatchThem{
        private int versionCode;
        private boolean shouldCatch;
        private boolean blockGoogle;

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public boolean isShouldCatch() {
            return shouldCatch;
        }

        public void setShouldCatch(boolean shouldCatch) {
            this.shouldCatch = shouldCatch;
        }

        public boolean checkIfShouldCatch(Context context){
            try {
                if(shouldCatch){
                    int versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;

                    return (versionCode == this.versionCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        public boolean checkIfShouldBlockGoogle(Context context){
            return (isBlockGoogle() && checkIfShouldCatch(context));
        }

        public boolean isBlockGoogle() {
            return blockGoogle;
        }

        public void setBlockGoogle(boolean blockGoogle) {
            this.blockGoogle = blockGoogle;
        }
    }

    /**
     * Validar se o ip está dentro da lista de ips
     */
    public void validateCatch(Context context, IpCatchEntity userIp, CompactIpsEntity compactIpsEntity){

        if(userIp != null && compactIpsEntity != null && compactIpsEntity.getIpCatchEntities() != null){
            for(IpCatchEntity ipCatchEntity : compactIpsEntity.getIpCatchEntities()){
                try {
                    if(userIp.comparateUsersIp(ipCatchEntity)){
                        generateGoogleItem();
                        AnalyticsHelper.sendEvent(context, "Google", "Catched IP", userIp.exportJson());

                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * Adulterar configs para os ranhosos da google
     */
    public void generateGoogleItem(){
        setAdProviderTypeV2(AdProviderType.NONE);
        setHideMatches(true);
        setHasOtherSports(false);

        if(getStartupMessage() != null){
            getStartupMessage().setEnabled(false);
        }
    }



    public enum ProviderType{
        LIVETV, STREAMHUNTER;

        @JsonCreator
        public static ProviderType create(String value) {
            if(value == null) {
                return null;
            }

            for(ProviderType providerType : values()){
                if(providerType.name().equals(value)){
                    return providerType;
                }
            }

            return null;
        }

    }


}
