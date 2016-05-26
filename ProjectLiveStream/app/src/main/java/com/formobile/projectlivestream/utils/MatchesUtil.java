package com.formobile.projectlivestream.utils;


import com.formobile.projectlivestream.entities.CompetitionMatchesCompact;
import com.formobile.projectlivestream.entities.ListMatchStreamingEntity;
import com.formobile.projectlivestream.entities.ListStreamEntity;
import com.formobile.projectlivestream.enums.StreamingTypeEnum;

import java.util.ArrayList;
import java.util.List;

public class MatchesUtil {

	
	
	public static List<CompetitionMatchesCompact> convertMatchesToCompetitionCompact(List<ListMatchStreamingEntity> listMatchStreamingEntities){
		if(listMatchStreamingEntities != null && !listMatchStreamingEntities.isEmpty()){
			List<CompetitionMatchesCompact> listCompacts = new ArrayList<CompetitionMatchesCompact>();
			
			CompetitionMatchesCompact competitionMatchesCompact = null;
			for(ListMatchStreamingEntity listMatchStreamingEntity : listMatchStreamingEntities){
				
				if(competitionMatchesCompact == null){
					competitionMatchesCompact = new CompetitionMatchesCompact(listMatchStreamingEntity.getListCompetitionEntity());
					listCompacts.add(competitionMatchesCompact);
				} else{
					if(!competitionMatchesCompact.getListCompetitionEntity().getCompetitionName().equalsIgnoreCase(listMatchStreamingEntity.getListCompetitionEntity().getCompetitionName())){
						competitionMatchesCompact = new CompetitionMatchesCompact(listMatchStreamingEntity.getListCompetitionEntity());
						listCompacts.add(competitionMatchesCompact);
					}
				}
				
				competitionMatchesCompact.getListMatchStreamingEntities().add(listMatchStreamingEntity);
			}
			
			return listCompacts;
		} else{
			return null;
		}
	}
	
	public static CompetitionMatchesCompact getCompetitionCompact(List<CompetitionMatchesCompact> lisCompacts, String compName){
		if(lisCompacts != null && !lisCompacts.isEmpty()){
			for(CompetitionMatchesCompact competitionMatchesCompact : lisCompacts){
				if(competitionMatchesCompact.getListCompetitionEntity().getCompetitionName().equalsIgnoreCase(compName)){
					return competitionMatchesCompact;
				}
			}
		}
		
		return null;
	}
	
	public static List<ListStreamEntity> getListStreamingByType(StreamingTypeEnum streamingTypeEnum, List<ListStreamEntity> listStreamEntities){
		if(listStreamEntities != null && !listStreamEntities.isEmpty()){
			List<ListStreamEntity> listStreamType = new ArrayList<ListStreamEntity>();
			
			for(ListStreamEntity listStreamEntity : listStreamEntities){
				if(listStreamEntity.getStreamingType() == streamingTypeEnum){
					listStreamType.add(listStreamEntity);
				}
			}
			
			return listStreamType;
		} else{
			return null;
		}
	}
	
}
