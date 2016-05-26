package com.formobile.projectlivestream.entities;


import com.formobile.projectlivestream.enums.StreamingTypeEnum;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class MatchDetailsEntity {
	
	private static final String SELECT_TEAMS_NAMES_AND_IMG = "body table tbody tr td table tbody tr td table.main tbody tr td table tbody tr td table tbody tr td table tbody tr td a img[src^=http://cdn.livetv.sx/img/teams/]";
	private static final String SELECT_STREAMS = "div#links_block a[href^=http://cdn.livetv.sx/webplayer";
	private static final String SELECT_SOPCAST_STREAMS = "div#links_block a[href^=sop://broker.sopcast.com";
	
	private String teamAName;
	private String teamBName;
	private String teamAImageUrl;
	private String teamBImageUrl;
	private ListMatchStreamingEntity listMatchStreamingEntity;
	private List<ListStreamEntity> listStreamEntities;
	
	public MatchDetailsEntity(){
		this.listStreamEntities = new ArrayList<ListStreamEntity>();
	}
	
	public static MatchDetailsEntity convertDocumentToEntity(Document document){
		MatchDetailsEntity matchDetailsEntity = new MatchDetailsEntity();
		
		// Select Team names and imgs
		Elements elTeams = document.select(SELECT_TEAMS_NAMES_AND_IMG);
		if(elTeams != null && !elTeams.isEmpty() && elTeams.size() == 2){
			// Team A
			Element elTeamA = elTeams.get(0);
			matchDetailsEntity.setTeamAName(elTeamA.attr("alt"));
			matchDetailsEntity.setTeamAImageUrl(elTeamA.attr("src"));
			
			// Team B
			Element elTeamB = elTeams.get(1);
			matchDetailsEntity.setTeamBName(elTeamB.attr("alt"));
			matchDetailsEntity.setTeamBImageUrl(elTeamB.attr("src"));
		}
		
		// Select Flash Streams
		Elements elStreams = document.select(SELECT_STREAMS);
		if(elStreams != null && !elStreams.isEmpty()){
			for(Element element : elStreams){
				String url = element.attr("href");
				if(url != null && !url.isEmpty() && !url.startsWith(ListStreamEntity.URL_START_SOPCAST) && !url.startsWith(ListStreamEntity.URL_START_TORRENT)){
					matchDetailsEntity.getListStreamEntities().add(new ListStreamEntity(StreamingTypeEnum.FLASH_STREAM, url));
				}
			}
		}
		
		// Select Sopcast Streams
		Elements elSopcast = document.select(SELECT_SOPCAST_STREAMS);
		if(elSopcast != null && !elSopcast.isEmpty()){
			for(Element element : elSopcast){
				matchDetailsEntity.getListStreamEntities().add(new ListStreamEntity(StreamingTypeEnum.SOPCAST_STREAM, element.attr("href")));
			}
		}
		
		return matchDetailsEntity;
	}
	

	public String getTeamAName() {
		return teamAName;
	}

	public void setTeamAName(String teamAName) {
		this.teamAName = teamAName;
	}

	public String getTeamBName() {
		return teamBName;
	}

	public void setTeamBName(String teamBName) {
		this.teamBName = teamBName;
	}

	public String getTeamAImageUrl() {
		return teamAImageUrl;
	}

	public void setTeamAImageUrl(String teamAImageUrl) {
		this.teamAImageUrl = teamAImageUrl;
	}

	public String getTeamBImageUrl() {
		return teamBImageUrl;
	}

	public void setTeamBImageUrl(String teamBImageUrl) {
		this.teamBImageUrl = teamBImageUrl;
	}

	public ListMatchStreamingEntity getListMatchStreamingEntity() {
		return listMatchStreamingEntity;
	}

	public void setListMatchStreamingEntity(
			ListMatchStreamingEntity listMatchStreamingEntity) {
		this.listMatchStreamingEntity = listMatchStreamingEntity;
	}

	public List<ListStreamEntity> getListStreamEntities() {
		return listStreamEntities;
	}

	public void setListStreamEntities(List<ListStreamEntity> listStreamEntities) {
		this.listStreamEntities = listStreamEntities;
	}
}
