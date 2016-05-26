package com.formobile.projectlivestream.entities;

import com.formobile.projectlivestream.enums.StreamingTypeEnum;

public class ListStreamEntity {
	
	public static final String URL_START_SOPCAST = "http://cdnx.livetv.sx/webplayer.php?t=sopcast";
	public static final String URL_START_TORRENT = "http://cdnx.livetv.sx/webplayer2.php?t=acestream"; 
	
	private StreamingTypeEnum streamingType;
	private String linkUrl;
    private String extraDescription;
	private boolean tryVideoPlayer;

    public ListStreamEntity(){
		this.tryVideoPlayer = true;
    }

	public ListStreamEntity(StreamingTypeEnum streamTypeEnum, String linkUrl){
		this.streamingType = streamTypeEnum;
		this.linkUrl = linkUrl;
		this.tryVideoPlayer = true;
	}

	public StreamingTypeEnum getStreamingType() {
		return streamingType;
	}

	public void setStreamingType(StreamingTypeEnum streamingType) {
		this.streamingType = streamingType;
	}

	public String getLinkUrl() {
		// Replace Issue /players/ --> /stream/
		if(linkUrl != null && linkUrl.contains("/players/")){
			linkUrl = linkUrl.replaceFirst("/players/", "/streaming/");
		}

		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

    public String getExtraDescription() {
        return extraDescription;
    }

    public void setExtraDescription(String extraDescription) {
        this.extraDescription = extraDescription;
    }

	public boolean isTryVideoPlayer() {
		return tryVideoPlayer;
	}

	public void setTryVideoPlayer(boolean tryVideoPlayer) {
		this.tryVideoPlayer = tryVideoPlayer;
	}
}
