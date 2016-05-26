/*
 * $Id$
 *
 * Copyright (c) Present Technologies Lda., All Rights Reserved.
 * (www.present-technologies.com)
 *
 * This software is the proprietary information of Present Technologies Lda.
 * Use is subject to license terms.
 *
 * Last changed on $Date$
 * Last changed by $Author$
 */
package com.formobile.projectlivestream.jsoup;

import android.content.Context;
import android.util.Log;

import com.formobile.projectlivestream.configs.AppConfigsHelper;
import com.formobile.projectlivestream.configs.StartupConfigs;
import com.formobile.projectlivestream.entities.ListMatchStreamingEntity;
import com.formobile.projectlivestream.entities.ListMatchStreamingEntityUtil;
import com.formobile.projectlivestream.entities.MatchDetailsEntity;
import com.formobile.projectlivestream.entities.MatchDetailsEntityUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * <Class description>
 *
 * @author David Fortunato
 * @version $Revision$
 */
public class JSoupHelper {
    private static final String TAG = JSoupHelper.class.getSimpleName();

    private static final String VIDEO_PLAYER_BASE_URL_REPLACE_PLACEHOLDER = "<_FID>";

    // Live Tv Consts
    public static final String LIVE_TV_URL_BASE = "http://livetv.sx";
    public static final String STREAMHUNTER_URL_BASE = "http://realstreamunited.com/";

    // List Match Streaming Games
    private static final String SELECTOR_MATCH_STREAMINGS = "tbody tr td";
    private static final int MAX_ELEMENTS = 50;
    
    // Stream Hunter Consts
    private static final String STREAM_HUNTER_SELECTOR_MATCH_STREAMING = "div.lshpanel";
    

    public static List<ListMatchStreamingEntity> getListMatchStreamingEntities(Context context, String sportUrl) throws IOException, ParseException {

        StartupConfigs startupConfigs = AppConfigsHelper.getStartupConfigs(context, false);

        switch (startupConfigs.getProviderType()){
            case LIVETV:
                return getLiveTvListMatchStreamingEntities(sportUrl);

            case STREAMHUNTER:
                return getStreamHunterListMatchStreamingEntities(sportUrl);
        }


        return null;
    }


    private static List<ListMatchStreamingEntity> getStreamHunterListMatchStreamingEntities(String sportUrl) throws IOException {
        List<ListMatchStreamingEntity> listMatchStreamingEntities = new ArrayList<ListMatchStreamingEntity>();

        Document document = Jsoup.connect(sportUrl).get();
        Elements elements = document.select(STREAM_HUNTER_SELECTOR_MATCH_STREAMING);

        if(elements != null && !elements.isEmpty()){
            long currentMillis = System.currentTimeMillis();
            // Add List Matches
            for(Element element : elements){
                try {
                    ListMatchStreamingEntity listMatchStreamingEntity = ListMatchStreamingEntityUtil.convertStreamHunterElementToMatch(element);

                    if(listMatchStreamingEntity != null){
                        listMatchStreamingEntities.add(listMatchStreamingEntity);
                    }

                    if(listMatchStreamingEntities.size() >= MAX_ELEMENTS){
                        break;
                    }

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }

        return listMatchStreamingEntities;
    }


    /**
     * Get list streaming matches
     * @return
     * @throws java.io.IOException
     * @throws java.text.ParseException
     */
    private static final List<ListMatchStreamingEntity> getLiveTvListMatchStreamingEntities(String sportUrl) throws IOException, ParseException{
    	List<ListMatchStreamingEntity> listMatchStreamingEntities = new ArrayList<ListMatchStreamingEntity>();

    	Document document = Jsoup.connect(LIVE_TV_URL_BASE + sportUrl).get();
    	Elements elements = document.select(SELECTOR_MATCH_STREAMINGS);
    	
    	if(elements != null && !elements.isEmpty()){
    		long currentMillis = System.currentTimeMillis();
    		
    		for(Element element : elements){
    			if(element.id().contains("event_")){
    				ListMatchStreamingEntity listMatchStreamingEntity = ListMatchStreamingEntity.convertElementToEntity(element);
        			
        			if(listMatchStreamingEntity != null){
        				if(!listMatchStreamingEntity.isLive() && (currentMillis - listMatchStreamingEntity.getDate().getTime()) > 7200000){
        					continue;
        				} else{
        					listMatchStreamingEntities.add(listMatchStreamingEntity);
        				}
        				
        			}
        			
        			if(listMatchStreamingEntities.size() >= MAX_ELEMENTS){
        				break;
        			}
    			}
    			
    		}
    	}
    	
    	return listMatchStreamingEntities;
    }


    public static final MatchDetailsEntity getMatchDetailsEntity(Context context, ListMatchStreamingEntity listMatchStreamingEntity, String url) throws IOException {

        StartupConfigs startupConfigs = AppConfigsHelper.getStartupConfigs(context, false);

        switch (startupConfigs.getProviderType()){
            case LIVETV:
                return getLiveTvMatchDetailsEntity(url);

            case STREAMHUNTER:
                return getStreamHunterMatchDetailsEntity(listMatchStreamingEntity, url);
        }

        return null;
    }

    public static MatchDetailsEntity getStreamHunterMatchDetailsEntity(ListMatchStreamingEntity listMatchStreamingEntity, String url) throws IOException {
        Document document = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.81 Safari/537.36").get();

        return MatchDetailsEntityUtil.convertStreamHunterElementToMatch(listMatchStreamingEntity, document.select("div.event_details").first());
    }


    
    public static final MatchDetailsEntity getLiveTvMatchDetailsEntity(String url) throws IOException{
    	Document document = Jsoup.connect(url).get();
    	
    	
    	return MatchDetailsEntity.convertDocumentToEntity(document);
    }

    public static final String getHtmlPlayerContainer(Context context, String url) throws IOException {
        switch (AppConfigsHelper.getStartupConfigs(context, false).getProviderType()){
            case LIVETV:
                return getHtmlPlayerContainerLivetv(context, url);

            case STREAMHUNTER:
                return getHtmlPlayerContainerStreamHunter(url);
        }

        return null;
    }


    public static final String getHtmlPlayerContainerStreamHunter(String url) throws IOException {
        Document document = Jsoup.connect(url).get();

        Element element = document.select("div.banner-3 iframe").first();

        if(element != null && element.outerHtml() != null && !element.outerHtml().isEmpty()){
            return element.outerHtml();
        }

        return null;
    }

    public static String getStreamhunterVideoPlayerLinkFromHTML(Context context, String html) throws IOException {
        Document document = Jsoup.parse(html);

        return getStreamHunterVideoPlayerIDFromDocument(context, document);
    }



    public static String getStreamHunterVideoPlayerLink(Context context, String url) throws IOException {
        Document document = Jsoup.connect(url)
                .referrer(AppConfigsHelper.getStartupConfigs(context, false).getUrlSport())
                .get();

        return getStreamHunterVideoPlayerIDFromDocument(context, document);
    }

    public static String getStreamHunterVideoPlayerIDFromDocument(Context context, Document document) throws IOException {
        // Try to get Fid
        Element divBan3 = document.select("div.banner-3").first();

        Elements elements = null;
        Element fidElContainer = null;

        if(divBan3 != null){
            elements = divBan3.select("script");
        }

        for(Element el : elements){
            String elText = el.text();
            String elOwnText = el.ownText();
            String elHtml = el.html();

            if(el.html() != null && el.html().contains("fid=")){
                fidElContainer = el;
                break;
            }
        }

        String fidContainer = null;

        if(fidElContainer != null){
            // Get fid id
            fidContainer = fidElContainer.html();

            // Remove White Spaces
            fidContainer = fidContainer.replaceAll(" ", "");

            //Split semicolon and get the first
            String[] split = fidContainer.split(";");

            // Find fid contains
            if(split != null && split.length > 0){
                fidContainer = null;
                for(int i = 0; i < split.length; i++){
                    if(split[i].contains("fid=")){
                        fidContainer = split[i];
                        break;
                    }
                }

            } else{
                Log.e(TAG, "Cannot split with semicolon");
                return null;
            }

            if(fidContainer == null){
                Log.e(TAG, "Cannot find fid on split");
                return null;
            }

            // Remove other data
            fidContainer = fidContainer.replaceAll("fid=\"", "");
            fidContainer = fidContainer.replaceAll("\"", "");

            // Complete with url
            fidContainer = AppConfigsHelper.getStartupConfigs(context, false).getVideoPlayerBaseUrl().replaceFirst(VIDEO_PLAYER_BASE_URL_REPLACE_PLACEHOLDER, fidContainer);

        }

        return fidContainer;
    }
    
    public static final String getHtmlPlayerContainerLivetv(Context context, String url) throws IOException{
    	Document document = Jsoup.connect(url).get();

    	Element element = document.select("table#playerblock td").first();
    	
    	if(element != null){

            // Try to find iFrame
//            Elements elements = element.select("iframe");
//
//            if(elements != null && !elements.isEmpty()){
//                Element element1 = elements.first();
//
//                String srcAttr = element1.attr("src");
//
//                if(srcAttr != null && !srcAttr.isEmpty()){
//                    return removingAds(context, srcAttr);
//                }
//            }

    		element.attr("style", "");
        	
        	return element.outerHtml();
    	}
    	
    	return null;
    }


    public static String removingAds(Context context, String url) throws IOException {
        if(url != null && url.startsWith("http")) {

            Document document = Jsoup.connect(url).get();
            boolean foundSomething = false;
            if (document != null) {

                // Try to find div starts with id=ad***

                Elements elements = document.select("div[id^=timer]");
                if (elements != null && !elements.isEmpty()) {
                    foundSomething = true;
                    elements.remove();
                }

                elements = document.select("div[id^=ad]");
                if (elements != null && !elements.isEmpty()) {
                    foundSomething = true;
                    elements.remove();
                }

                elements = document.select("div[title^=Click to see what's popular on this site!]");
                if (elements != null && !elements.isEmpty()) {
                    foundSomething = true;
                    elements.remove();
                }

                elements = document.select("div.floater");
                if (elements != null && !elements.isEmpty()) {
                    foundSomething = true;
                    elements.remove();
                }

                elements = document.select("div.unfullscreener");
                if (elements != null && !elements.isEmpty()) {
                    foundSomething = true;
                    elements.remove();
                }


                if (foundSomething) {
                    return document.outerHtml();
                } else {
                    return null;
                }
            } else{
                return null;
            }

        } else{
            return null;
        }
    }
    
}
