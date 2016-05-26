package com.formobile.projectlivestream.entities;

import java.util.ArrayList;
import java.util.List;

public class CompetitionMatchesCompact {

	private ListCompetitionEntity listCompetitionEntity;
	private List<ListMatchStreamingEntity> listMatchStreamingEntities;
	
	public CompetitionMatchesCompact(ListCompetitionEntity listCompetitionEntity){
		this.listCompetitionEntity = listCompetitionEntity;
		this.listMatchStreamingEntities = new ArrayList<ListMatchStreamingEntity>();
	}

	public ListCompetitionEntity getListCompetitionEntity() {
		return listCompetitionEntity;
	}

	public void setListCompetitionEntity(ListCompetitionEntity listCompetitionEntity) {
		this.listCompetitionEntity = listCompetitionEntity;
	}

	public List<ListMatchStreamingEntity> getListMatchStreamingEntities() {
		return listMatchStreamingEntities;
	}

	public void setListMatchStreamingEntities(
			List<ListMatchStreamingEntity> listMatchStreamingEntities) {
		this.listMatchStreamingEntities = listMatchStreamingEntities;
	}
}
