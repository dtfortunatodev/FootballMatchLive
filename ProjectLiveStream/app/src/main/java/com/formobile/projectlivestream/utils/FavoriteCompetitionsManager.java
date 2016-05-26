package com.formobile.projectlivestream.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavoriteCompetitionsManager {

	public static final String PREFIX_FAV_COMP = "PREFIX_FAV_COMP";
	
	public static Set<String> favoriteCompetitionsSet = new HashSet<String>();
	
	
	public static void saveFavoriteCompetitions(Context context){   
	    SharedPreferences prefs = context.getSharedPreferences(PREFIX_FAV_COMP, 0);  
	    SharedPreferences.Editor editor = prefs.edit();  
	    editor.putInt(PREFIX_FAV_COMP +"_size", favoriteCompetitionsSet.size());
	    
	    List<String> listComps = new ArrayList<String>(favoriteCompetitionsSet);
	    
	    for(int i=0;i<listComps.size(); i++){
	    	editor.putString(PREFIX_FAV_COMP + "_" + i, listComps.get(i)); 
	    }
	         
	    editor.commit();
	}
	
	public static void loadFavoriteCompetitions(Context mContext) { 
		if(favoriteCompetitionsSet != null && favoriteCompetitionsSet.isEmpty()){
			SharedPreferences prefs = mContext.getSharedPreferences(PREFIX_FAV_COMP, 0);  
		    int size = prefs.getInt(PREFIX_FAV_COMP + "_size", 0);  

		    if(favoriteCompetitionsSet == null){
		    	favoriteCompetitionsSet = new HashSet<String>();
		    }
		    favoriteCompetitionsSet.clear();
		    
		    for(int i=0;i<size;i++){
		    	String comp = prefs.getString(PREFIX_FAV_COMP + "_" + i, null); 
		    	if(comp != null){
		    		favoriteCompetitionsSet.add(comp);
		    	}
		    }
		}
	    
	}  
	
	public static boolean isCompFavorite(Context context, String comp){
		if(favoriteCompetitionsSet == null){
			loadFavoriteCompetitions(context);
		}
		
		return favoriteCompetitionsSet.contains(comp);
	}
	
	public static void manageFavoriteComp(String compName, boolean add){
		if(add){
			if(!favoriteCompetitionsSet.contains(compName)){
				favoriteCompetitionsSet.add(compName);
			}
		} else{
			if(favoriteCompetitionsSet.contains(compName)){
				favoriteCompetitionsSet.remove(compName);
			}
		}
	}
	
}
