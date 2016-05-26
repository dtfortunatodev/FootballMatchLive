package com.formobile.projectlivestream.entities;

import com.formobile.projectlivestream.jsoup.JSoupHelper;

import org.jsoup.select.Elements;

/**
 * Created by PTECH on 16-03-2015.
 */
public class ListCompetitionEntityUtil {

    // Stream Hunter Consts
    public static final String STREAM_HUNTER_COMPETITION_NAME_SELECTOR = "span.category";
    public static final String STREAM_HUNTER_COMPETITION_IMG_SELECTOR = "h3 table tbody tr td"; // First Element

    public static ListCompetitionEntity convertStreamHunterToCompetition(org.jsoup.nodes.Element element){
        ListCompetitionEntity listCompetitionEntity = new ListCompetitionEntity();

        // Get Img Base Url
        Elements elements = element.select(STREAM_HUNTER_COMPETITION_IMG_SELECTOR);
        if(elements != null && !elements.isEmpty()){
            org.jsoup.nodes.Element compImgElement = elements.first();

            if(compImgElement != null){
                String bgStyleAttr = compImgElement.attr("style");
                if(bgStyleAttr != null && bgStyleAttr.contains("url(")){
                    bgStyleAttr = bgStyleAttr.substring(bgStyleAttr.indexOf("url(") + 4);
                    int indexClose = bgStyleAttr.indexOf(")");
                    bgStyleAttr = bgStyleAttr.substring(0, indexClose);

                    listCompetitionEntity.setCompetitionImageUrl(JSoupHelper.STREAMHUNTER_URL_BASE + bgStyleAttr);
                }
            }
        }

        // Get Competition Name
        elements = element.select(STREAM_HUNTER_COMPETITION_NAME_SELECTOR);
        if(elements != null && !elements.isEmpty()){
            listCompetitionEntity.setCompetitionName(elements.first().text());
        }

        return listCompetitionEntity;
    }
}
