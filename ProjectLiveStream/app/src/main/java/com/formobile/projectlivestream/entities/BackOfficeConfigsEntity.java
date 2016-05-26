package com.formobile.projectlivestream.entities;

/**
 * Created by PTECH on 26-01-2015.
 */
public class BackOfficeConfigsEntity {

    private boolean displayQuotes;
    private String upladAppUrl;

    public BackOfficeConfigsEntity(boolean displayQuotes, String uploadUrl){
        this.upladAppUrl = uploadUrl;
        this.displayQuotes = displayQuotes;
    }

    public BackOfficeConfigsEntity(boolean displayQuotes){
        this.displayQuotes = displayQuotes;
        this.upladAppUrl = null;
    }

    public boolean isDisplayQuotes() {
        return displayQuotes;
    }

    public void setDisplayQuotes(boolean displayQuotes) {
        this.displayQuotes = displayQuotes;
    }

    public String getUpladAppUrl() {
        return upladAppUrl;
    }

    public void setUpladAppUrl(String upladAppUrl) {
        this.upladAppUrl = upladAppUrl;
    }

    public boolean shouldUploadApp(){
        return (this.upladAppUrl != null && this.upladAppUrl.length() > 0);
    }

}
