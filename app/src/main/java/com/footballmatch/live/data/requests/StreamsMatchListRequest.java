package com.footballmatch.live.data.requests;

import com.footballmatch.live.data.managers.HTMLRequestManager;
import com.footballmatch.live.data.model.StreamLinkEntity;
import com.footballmatch.live.utils.LogUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David Fortunato on 18/07/2016
 * All rights reserved ForViews
 */
public class StreamsMatchListRequest
{
    private static final String TAG = StreamsMatchListRequest.class.getSimpleName();


    /**
     * Get List of Match Streams for the match of the URL
     *
     * @param url URL to download
     *
     * @return ResponseDataObject with a list of StreamLinkEntities
     */
    public static ResponseDataObject<StreamLinkEntity> getListMatchStreams(String url)
    {
        ResponseDataObject<StreamLinkEntity> responseDataObject = new ResponseDataObject();

        // Load Document
        Document document = null;

        try
        {
            document = HTMLRequestManager.getData(url);

            // Set Response Code
            if (document != null && document.hasText())
            {
                // Response OK
                responseDataObject.setResponseCode(ResponseDataObject.RESPONSE_CODE_OK);
            }
            else
            {
                // Response Failed
                responseDataObject.setResponseCode(ResponseDataObject.RESPONSE_CODE_FAILED_GETTING_DOCUMENT);
            }
        }
        catch (IOException e)
        {
            LogUtil.e(TAG, e.getMessage(), e);

            // Response Falied
            responseDataObject.setResponseCode(ResponseDataObject.RESPONSE_CODE_FAILED_GETTING_DOCUMENT);
        }

        // Parse Data
        if (responseDataObject.isOk())
        {
            responseDataObject.setListObjectsResponse(parseDetailsMatchToListStreams(document));
        }

        return responseDataObject;
    }

    /**
     * Parse the JSoup document to a list of Stream Links
     *
     * @param document Document to parse
     *
     * @return List of Streams
     */
    private static List<StreamLinkEntity> parseDetailsMatchToListStreams(Document document)
    {
        // Init List
        List<StreamLinkEntity> listStreams = new ArrayList<>();

        // Select Main Container
        final String SELECT_CENTER_CONTAINER = "div#main div#maincontent div#content div.single";
        Elements elCenterContainer = document.select(SELECT_CENTER_CONTAINER);

        if (elCenterContainer != null)
        {

            // Get SopCast Streams
            final String SELECT_SOPCAST_LIST = "div#sopcastlist table.streamtable a.play";
            Elements elListSopcast = elCenterContainer.select(SELECT_SOPCAST_LIST);

            if (elListSopcast != null && !elListSopcast.isEmpty())
            {
                // ForEach element
                for (Element element : elListSopcast)
                {
                    StreamLinkEntity streamLinkEntity = parseLineStreamTableToStreamLinkEntity(element);
                    if(streamLinkEntity != null)
                    {
                        listStreams.add(streamLinkEntity);
                    }
                }
            }


            // Get Other Streams
            final String SELECT_OTHER_STREAMS_LIST = "div#livelist table.streamtable a.play";
            Elements elOtherStreams = elCenterContainer.select(SELECT_OTHER_STREAMS_LIST);

            if (elOtherStreams != null && !elOtherStreams.isEmpty())
            {
                // ForEach element
                for (Element element : elOtherStreams)
                {
                    StreamLinkEntity streamLinkEntity = parseLineStreamTableToStreamLinkEntity(element);
                    if(streamLinkEntity != null)
                    {
                        listStreams.add(streamLinkEntity);
                    }
                }
            }

        }

        return listStreams;
    }


    /**
     * Parse a line from a table and parse it to a Stream Link Entity
     * @param element Element to parse
     * @return StreamLinkEntity generated
     */
    private static StreamLinkEntity parseLineStreamTableToStreamLinkEntity(Element element)
    {
        StreamLinkEntity streamLinkEntity = null;
        if(element != null)
        {
            String hrefAttr = element.attr("href");

            if(hrefAttr != null && !hrefAttr.isEmpty())
            {
                streamLinkEntity = new StreamLinkEntity();
                streamLinkEntity.setStreamLinkUrl(hrefAttr);

            }
        }

        return streamLinkEntity;
    }


}
