package com.footballmatch.live.data.requests;

import android.util.Log;
import com.footballmatch.live.data.managers.HTMLRequestManager;
import com.footballmatch.live.data.model.StreamLinkEntity;
import com.footballmatch.live.utils.LogUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by David Fortunato on 18/07/2016
 * All rights reserved ForViews
 */
public class StreamsMatchListRequest
{
    private static final String TAG = StreamsMatchListRequest.class.getSimpleName();

    // Comparator Sort
    private static final Comparator<StreamLinkEntity> STREAM_LIST_SORT = new Comparator<StreamLinkEntity>()
    {
        @Override
        public int compare(StreamLinkEntity streamLinkEntity1, StreamLinkEntity streamLinkEntity2)
        {
            if (streamLinkEntity1.getStreamLinkType() == streamLinkEntity2.getStreamLinkType())
            {
                return 0;
            }
            else
            {
                return streamLinkEntity1.getStreamLinkType().getPriority() > streamLinkEntity2.getStreamLinkType().getPriority() ? 1 : -1;
            }
        }
    };

    /**
     * Get List of Match Streams for the match of the URL
     *
     * @param url URL to download
     *
     * @return ResponseDataObject with a list of StreamLinkEntities
     */
    public static ResponseDataObject<StreamLinkEntity> getListMatchStreams(String url, Document document)
    {
        ResponseDataObject<StreamLinkEntity> responseDataObject = new ResponseDataObject();

        if (document == null)
        {
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
        }
        else
        {
            responseDataObject.setResponseCode(ResponseDataObject.RESPONSE_CODE_OK);
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


        switch (Urls.SOURCE_TYPE)
        {
            case LIVEFOOTBALLVIDEO:
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
                break;

            case LIVESPORTWS:
                // TODO Parse to use LIVESPOTWS
                String selectStreams = "ul.broadcasting-types li table tbody tr";
                Elements streamsElements = document.select(selectStreams);

                if (streamsElements != null && !streamsElements.isEmpty())
                {
                    for (Element element : streamsElements)
                    {
                        StreamLinkEntity streamLinkEntity = new StreamLinkEntity();

                        // Set Link
                        final String selectLink = "td.btn-holder a";
                        Elements selectLinkElement = element.select(selectLink);
                        streamLinkEntity.setStreamLinkUrl(selectLinkElement.attr("href"));

                        // Set Lang icon
                        final String selectLang = "td.country img";
                        Elements selectLangElement = element.select(selectLang);
                        streamLinkEntity.setLangIcon(selectLangElement.attr("src"));
                        streamLinkEntity.setTitle(selectLangElement.attr("title"));

                        // Set Channel name (if exists)
                        final String selectChannel = "td.channel";
                        Elements selectChannelElement = element.select(selectChannel);
                        if (selectChannelElement != null)
                        {
                            streamLinkEntity.setTitle(selectChannelElement.text());
                        }

                        if (streamLinkEntity.getStreamLinkType() == StreamLinkEntity.StreamLinkType.ACESTREAM)
                        {
                            streamLinkEntity.setRecommended(true);
                        }

                        listStreams.add(streamLinkEntity);
                    }
                }

                Log.d("TEST", "Test");
                break;
        }

        // Sort List
        Collections.sort(listStreams, STREAM_LIST_SORT);

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
