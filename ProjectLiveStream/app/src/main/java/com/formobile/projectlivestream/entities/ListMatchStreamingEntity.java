package com.formobile.projectlivestream.entities;

import com.formobile.projectlivestream.jsoup.JSoupHelper;
import com.formobile.projectlivestream.utils.JSoupUtils;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ListMatchStreamingEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4563043358153326032L;

	public static final String SELECT_COMPETITION_IMAGE_URL = "td[valign=top] img";
	public static final String SELECT_TITLE_TEXT = "td a.live";
	public static final String SELECT_IS_LIVE = "td a.live img[src=http://cdn.livetv.sx/img/live.gif]";
	public static final String SELECT_DATE_AND_COMP_NAME = "td span.evdesc";
	
	private ListCompetitionEntity listCompetitionEntity;
	private Date date;
    private long timeMillis;
	private String titleMatch;
	private String result;
	private String urlMatch;
	private boolean isLive;

    // Static Data
    private static String currentYear = null;
	
	public ListMatchStreamingEntity(){
		this.isLive = false;
	}

	public static ListMatchStreamingEntity convertElementToEntity(Element element) throws ParseException{
		ListMatchStreamingEntity listMatchStreamingEntity = new ListMatchStreamingEntity();
		
		// Competition Entity
		ListCompetitionEntity listCompetitionEntity = new ListCompetitionEntity();
		
		// Set Competition Image Url
		Elements elCompUrl = element.select(SELECT_COMPETITION_IMAGE_URL);
		if(elCompUrl != null && !elCompUrl.isEmpty()){
			String src = elCompUrl.attr("src");
			if(src != null && !src.isEmpty()){
				listCompetitionEntity.setCompetitionImageUrl(src);
			}
		}
		
		// Select Title
		Elements elTitle = element.select(SELECT_TITLE_TEXT);
		if(elTitle != null && !elTitle.isEmpty()){
			Element el = elTitle.first();
			if(el != null && el.text() != null && !el.text().isEmpty()){
				listMatchStreamingEntity.setTitleMatch(el.text());
				
				// Get url
				listMatchStreamingEntity.setUrlMatch(JSoupHelper.LIVE_TV_URL_BASE + el.attr("href"));
			} else{
				return null;
			}
		} else{
			return null;
		}
		
		// Is Live
		Elements elIsLive = element.select(SELECT_IS_LIVE);
		if(elIsLive != null && !elIsLive.isEmpty()){
			listMatchStreamingEntity.setLive(true);
		}
		
		// Set Date
		Elements elDateComp = element.select(SELECT_DATE_AND_COMP_NAME);
		if(elDateComp != null && !elDateComp.isEmpty()){
			String[] elements = JSoupUtils.splitBrContents(elDateComp.first());
			if(elements != null && elements.length == 2){
				listMatchStreamingEntity.setDate(elements[0]);
				
				listCompetitionEntity.setCompetitionName(elements[1]);
			}
			else{
				return null;
			}
		}
		
		// Set Competition
		listMatchStreamingEntity.setListCompetitionEntity(listCompetitionEntity);
		
		return listMatchStreamingEntity;
	}

	public ListCompetitionEntity getListCompetitionEntity() {
		return listCompetitionEntity;
	}

	public void setListCompetitionEntity(ListCompetitionEntity listCompetitionEntity) {
		this.listCompetitionEntity = listCompetitionEntity;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public void setDate(String date) throws ParseException{
        if(currentYear == null || currentYear.equalsIgnoreCase("")){
            SimpleDateFormat format = new SimpleDateFormat("yyyy");
            currentYear = format.format(System.currentTimeMillis());
        }
		date = date.replaceFirst("at", currentYear);
		date = date.replaceAll("\"", "");
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm", Locale.ENGLISH);
		this.date = simpleDateFormat.parse(date);
	}

    public void setDate(long timeMillis){
        date = new Date(timeMillis);
        this.timeMillis = timeMillis;
    }

	public String getTitleMatch() {
		return titleMatch;
	}

	public void setTitleMatch(String titleMatch) {
		this.titleMatch = titleMatch;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getUrlMatch() {
		return urlMatch;
	}

	public void setUrlMatch(String urlMatch) {
		this.urlMatch = urlMatch;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

    public long getTimeMillis() {
        return timeMillis;
    }

    public void setTimeMillis(long timeMillis) {
        this.timeMillis = timeMillis;
    }
}
