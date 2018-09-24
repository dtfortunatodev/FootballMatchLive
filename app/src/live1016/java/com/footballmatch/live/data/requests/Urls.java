package com.footballmatch.live.data.requests;

/**
 * Created by David Fortunato on 27/05/2016
 */
public class Urls
{
    public static final String REQUEST_LIVE_MATCHES = "https://livesport.ws/en/live-football";
    public static final String STARTUP_CONFIGS_URL = "https://www.dropbox.com/s/hj0ae0q82jlthbo/AppConfigsV2.json?raw=1";

    public static final SOURCE_TYPE SOURCE_TYPE = Urls.SOURCE_TYPE.LIVESPORTWS;

    public enum SOURCE_TYPE { LIVEFOOTBALLVIDEO, LIVESPORTWS}

}
