package com.footballmatch.live.data.model;

/**
 * Created by David Fortunato on 27/05/2016
 * All rights reserved ForViews
 */
public class StreamLinkEntity extends BaseEntity
{
    private String streamLinkUrl;
    private StreamLinkType streamLinkType;

    public StreamLinkEntity()
    {

    }

    public String getStreamLinkUrl()
    {
        return streamLinkUrl;
    }

    public void setStreamLinkUrl(String streamLinkUrl)
    {
        this.streamLinkUrl = streamLinkUrl;

        // Check wich type it is
        if (streamLinkUrl != null)
        {

            if (streamLinkUrl.startsWith("sop"))
            {
                // Sopcast link
                setStreamLinkType(StreamLinkType.SOPCAST);
            }
            else if (streamLinkUrl.startsWith("acestream"))
            {
                // Sopcast link
                setStreamLinkType(StreamLinkType.ACESTREAM);
            }
            else if (streamLinkUrl.startsWith("http://arenavision.in"))
            {
                // ArenaVision Link
                setStreamLinkType(StreamLinkType.ARENAVISION);
            }
            else
            {
                // WebPlayer. Open it in the
                setStreamLinkType(StreamLinkType.WEBPLAYER);
            }

        }

    }

    public StreamLinkType getStreamLinkType()
    {
        return streamLinkType;
    }

    public void setStreamLinkType(StreamLinkType streamLinkType)
    {
        this.streamLinkType = streamLinkType;
    }

    /**
     * Different types of streaming
     */
    public enum StreamLinkType
    {
        WEBPLAYER, SOPCAST, ACESTREAM, ARENAVISION;
    }
}
