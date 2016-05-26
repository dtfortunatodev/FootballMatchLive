package com.formobile.projectlivestream.entities;

import com.formobile.projectlivestream.enums.StreamingTypeEnum;

import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PTECH on 16-03-2015.
 */
public class MatchDetailsEntityUtil {

    // Streams Selector
    public static final String STREAM_HUNTER_GROUP_STREAMS_SELECTOR = "div.event_tvintro";
    public static final String STREAM_HUNTER_GROUP_STREAMS_NAME_SELECTOR = "tr.sectiontableheader > td";
    public static final String STREAM_HUNTER_STREAMS_SELECTOR = "tr[class^=sectiontableentry] a[target=_blank]";

    // Select Other Links
    public static final String STREAM_HUNTER_SELECT_OTHER_LINKS = "a[target=_blank][href^=http://live.realstreamunited.com/players/]";

    //

    public static MatchDetailsEntity convertStreamHunterElementToMatch(ListMatchStreamingEntity listMatchStreamingEntity, org.jsoup.nodes.Element element){
        MatchDetailsEntity matchDetailsEntity = new MatchDetailsEntity();
        matchDetailsEntity.setListMatchStreamingEntity(listMatchStreamingEntity);


        Elements elements = element.select(STREAM_HUNTER_GROUP_STREAMS_SELECTOR);
        if(elements != null && !elements.isEmpty()){
            // Init List Streams
            matchDetailsEntity.setListStreamEntities(new ArrayList<ListStreamEntity>());
            for (org.jsoup.nodes.Element el : elements){
                try {
                    List<ListStreamEntity> listStreamEntities = convertStreamHunterToStreams(el);

                    if(listStreamEntities != null){
                        matchDetailsEntity.getListStreamEntities().addAll(listStreamEntities);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        // Add other links
        matchDetailsEntity.getListStreamEntities().addAll(getOtherLinks(element, matchDetailsEntity.getListStreamEntities().size()));

        return matchDetailsEntity;
    }


    public static List<ListStreamEntity> convertStreamHunterToStreams(org.jsoup.nodes.Element element){

        List<ListStreamEntity> listStreamEntities = new ArrayList<ListStreamEntity>();

        if(element != null){
            Elements elements = element.select(STREAM_HUNTER_GROUP_STREAMS_NAME_SELECTOR);
            String streamGroupName = "";

            if(elements != null && !elements.isEmpty()){
                streamGroupName = elements.first().text();
            }

            if(streamGroupName != null && !streamGroupName.contains("Unibet")){
                // Found Stream of this group
                elements = element.select(STREAM_HUNTER_STREAMS_SELECTOR);
                if(elements != null && !elements.isEmpty()){
                    for (org.jsoup.nodes.Element el : elements){
                        ListStreamEntity listStreamEntity = new ListStreamEntity();
                        listStreamEntity.setStreamingType(StreamingTypeEnum.FLASH_STREAM);
                        listStreamEntity.setLinkUrl(el.attr("href"));
                        listStreamEntity.setExtraDescription(streamGroupName);

                        listStreamEntities.add(listStreamEntity);
                    }
                }
            }
        }

        return listStreamEntities;
    }


    public static List<ListStreamEntity> getOtherLinks(org.jsoup.nodes.Element element, int removeFirstsElements){
        List<ListStreamEntity> listStreamEntities = new ArrayList<ListStreamEntity>();


        if(element != null){
            Elements elements = element.select(STREAM_HUNTER_SELECT_OTHER_LINKS);
            if(elements != null && !elements.isEmpty()){
                for(org.jsoup.nodes.Element el : elements){

                    if(removeFirstsElements <= 0){
                        ListStreamEntity listStreamEntity = new ListStreamEntity();
                        listStreamEntity.setStreamingType(StreamingTypeEnum.FLASH_STREAM);
                        listStreamEntity.setLinkUrl(el.attr("href"));
                        listStreamEntity.setTryVideoPlayer(false);
                        listStreamEntities.add(listStreamEntity);
                    } else{
                        removeFirstsElements --;
                    }
                }
            }
        }

        return listStreamEntities;
    }
}
