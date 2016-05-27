package com.footballmatch.live.data.model;

import java.io.Serializable;

/**
 * Created by David Fortunato on 27/05/2016
 * All rights reserved GoodBarber
 */
public class StreamLinkEntity implements Serializable
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
        WEBPLAYER, SOPCAST, ACESTREAM;
    }
}
