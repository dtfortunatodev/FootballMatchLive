package com.footballmatch.live.data.model;

/**
 * Created by David Fortunato on 12/08/2016
 * All rights reserved GoodBarber
 */
public class MatchHighlightEntity extends BaseEntity
{
    // Data
    private String publisherId;
    private String videoId;

    public MatchHighlightEntity()
    {

    }

    public String getVideoId()
    {
        return videoId;
    }

    public void setVideoId(String videoId)
    {
        this.videoId = videoId;
    }

    public String getPublisherId()
    {
        return publisherId;
    }

    public void setPublisherId(String publisherId)
    {
        this.publisherId = publisherId;
    }

    public String getGeneratedUrl()
    {
        if (getPublisherId() != null && !getPublisherId().isEmpty() && getVideoId() != null && !getVideoId().isEmpty())
        {
            return "https://cdn.video.playwire.com/" + getPublisherId() + "/videos/" + getVideoId() + "/video-sd.mp4";
        }
        else
        {
            return null;
        }

    }
}
