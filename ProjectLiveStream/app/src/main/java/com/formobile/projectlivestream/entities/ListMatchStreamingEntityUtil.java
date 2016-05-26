package com.formobile.projectlivestream.entities;


import com.formobile.projectlivestream.jsoup.JSoupHelper;

import org.jsoup.select.Elements;

import java.util.Calendar;

/**
 * Created by PTECH on 16-03-2015.
 */
public class ListMatchStreamingEntityUtil {

    private static final long MATCH_TIME_LONG = 5700000;

    // Stream Hunter Consts
    public static final String STREAM_HUNTER_MATCH_DATE_SELECTOR = "span.original_time";
    public static final String STREAM_HUNTER_MATCH_TEAM_NAMES_SELECTOR = "span.lshevent";
    public static final String STREAM_HUNTER_MATCH_URL_SELECTOR = "a.open_event_tab";

    public static ListMatchStreamingEntity convertStreamHunterElementToMatch(org.jsoup.nodes.Element element){
        // Get Competition Entity
        ListCompetitionEntity listCompetitionEntity = ListCompetitionEntityUtil.convertStreamHunterToCompetition(element);

        if(listCompetitionEntity != null){
            ListMatchStreamingEntity listMatchStreamingEntity = new ListMatchStreamingEntity();

            // Set Competition
            listMatchStreamingEntity.setListCompetitionEntity(listCompetitionEntity);

            // Set Time Millis
            Elements elements = element.select(STREAM_HUNTER_MATCH_DATE_SELECTOR);
            if(elements != null && !elements.isEmpty()){
                long timeMillis = Long.parseLong(elements.first().text());
                timeMillis *= 1000;
                listMatchStreamingEntity.setDate(timeMillis);
            }

            // Set Team Names
            elements = element.select(STREAM_HUNTER_MATCH_TEAM_NAMES_SELECTOR);
            if(elements != null && !elements.isEmpty()){
                listMatchStreamingEntity.setTitleMatch(elements.first().text());
            }

            // Set Match Url
            elements = element.select(STREAM_HUNTER_MATCH_URL_SELECTOR);
            if(elements != null && !elements.isEmpty()){
                listMatchStreamingEntity.setUrlMatch(JSoupHelper.STREAMHUNTER_URL_BASE + elements.first().attr("href"));
            }

            // Check if is live
            if(Calendar.getInstance().getTimeInMillis() >= listMatchStreamingEntity.getTimeMillis() && Calendar.getInstance().getTimeInMillis() <= listMatchStreamingEntity.getTimeMillis() + MATCH_TIME_LONG){
                listMatchStreamingEntity.setLive(true);
            } else{
                listMatchStreamingEntity.setLive(false);
            }

            return listMatchStreamingEntity;
        }

        return null;
    }

}
