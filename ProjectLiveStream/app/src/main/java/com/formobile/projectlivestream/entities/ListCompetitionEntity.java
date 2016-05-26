package com.formobile.projectlivestream.entities;

import java.io.Serializable;

public class ListCompetitionEntity implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1677501905725441753L;

	public static final String URL_IMAGE_COMP_BASE = "/images/leagues/big/";
	
	private String competitionName;
	private String competitionImageUrl;
	
	public ListCompetitionEntity(String competitionName, String competitionImageUrl){
		this.competitionName = competitionName;
		this.competitionImageUrl = competitionImageUrl;
	}
	
	public ListCompetitionEntity(){
	}
	
	public String getCompetitionName() {
		return competitionName;
	}

	public void setCompetitionName(String competitionName) {
		this.competitionName = competitionName;

		// Replace ()
		this.competitionName = this.competitionName.replaceAll("\\(", "");
		this.competitionName = this.competitionName.replaceAll("\\)", "");
	}

	public String getCompetitionImageUrl() {
		return competitionImageUrl;
	}

	public void setCompetitionImageUrl(String competitionImageUrl) {
		this.competitionImageUrl = competitionImageUrl;
	}
}
