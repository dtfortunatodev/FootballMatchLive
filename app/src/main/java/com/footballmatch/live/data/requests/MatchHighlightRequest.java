package com.footballmatch.live.data.requests;

import com.footballmatch.live.data.managers.HTMLRequestManager;
import com.footballmatch.live.data.model.MatchHighlightEntity;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David Fortunato on 12/08/2016
 * All rights reserved GoodBarber
 */
public class MatchHighlightRequest
{

    /**
     * Getting Match Highlight Link
     * @param url Url to analyze
     * @return List of highlight streams
     */
    public static ResponseDataObject<MatchHighlightEntity> getMatchHighlightLink(String url)
    {
        ResponseDataObject<MatchHighlightEntity> responseDataObject = new ResponseDataObject<>();

        List<MatchHighlightEntity> listMatchHighlightLinks = new ArrayList<>();
        try
        {
            Document document = HTMLRequestManager.getData(url);

            // Trying getting AceStream
            Elements elHighlightContainer = document.select("div#highlights");
            if (elHighlightContainer != null && !elHighlightContainer.isEmpty())
            {
                Elements elements = elHighlightContainer.select("script");

                // Check if got
                if (elements != null && !elements.isEmpty())
                {

                    // Get Streams
                    for (Element element : elements)
                    {
                        String publisherId = element.attr("data-publisher-id");
                        String videoId = element.attr("data-video-id");

                        if (publisherId != null && !publisherId.isEmpty() && videoId != null && !videoId.isEmpty())
                        {
                            MatchHighlightEntity matchHighlightEntity = new MatchHighlightEntity();
                            matchHighlightEntity.setPublisherId(publisherId);
                            matchHighlightEntity.setVideoId(videoId);
                            listMatchHighlightLinks.add(matchHighlightEntity);
                        }
                    }

                }
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        responseDataObject.setResponseCode(ResponseDataObject.RESPONSE_CODE_OK);
        responseDataObject.setListObjectsResponse(listMatchHighlightLinks);

        return responseDataObject;
    }

}
