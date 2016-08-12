package com.footballmatch.live.data.model;

/**
 * Created by David Fortunato on 27/05/2016
 * All rights reserved ForViews
 */
public class StreamLinkEntity extends BaseEntity
{
    private String streamLinkUrl;
    private StreamLinkType streamLinkType;
    private boolean isRecommended;

    public StreamLinkEntity()
    {
        this.isRecommended = false;
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
        if (streamLinkType == StreamLinkType.ARENAVISION)
        {
            setRecommended(true);
        }
        this.streamLinkType = streamLinkType;
    }

    public boolean isRecommended()
    {
        return isRecommended;
    }

    public void setRecommended(boolean recommended)
    {
        isRecommended = recommended;
    }

    /**
     * Different types of streaming
     */
    public enum StreamLinkType
    {
        SOPCAST(0), ACESTREAM(1), ARENAVISION(3), WEBPLAYER(4);

        private int priority;

        StreamLinkType(int priority)
        {
            this.priority = priority;
        }

        public int getPriority()
        {
            return priority;
        }

        public void setPriority(int priority)
        {
            this.priority = priority;
        }
    }
}
