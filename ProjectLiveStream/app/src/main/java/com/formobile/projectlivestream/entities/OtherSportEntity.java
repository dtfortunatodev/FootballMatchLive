package com.formobile.projectlivestream.entities;

public class OtherSportEntity {

	private String sportName;
	private String contextName;
	private int iconRes;
	
	public OtherSportEntity(String sportName, String contextName, int iconRes){
		this.sportName = sportName;
		this.contextName = contextName;
		this.iconRes = iconRes;
	}

	public String getSportName() {
		return sportName;
	}

	public void setSportName(String sportName) {
		this.sportName = sportName;
	}

	public String getContextName() {
		return contextName;
	}

	public void setContextName(String contextName) {
		this.contextName = contextName;
	}

	public int getIconRes() {
		return iconRes;
	}

	public void setIconRes(int iconRes) {
		this.iconRes = iconRes;
	}
	
}
