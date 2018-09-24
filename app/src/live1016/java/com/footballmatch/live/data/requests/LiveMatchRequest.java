package com.footballmatch.live.data.requests;

import com.footballmatch.live.data.managers.GSONParser;
import com.footballmatch.live.data.managers.HTMLRequestManager;
import com.footballmatch.live.data.model.CompetitionEntity;
import com.footballmatch.live.data.model.MatchEntity;
import com.footballmatch.live.data.model.TeamEntity;
import com.footballmatch.live.data.utils.DataUtils;
import com.footballmatch.live.utils.LogUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by David Fortunato on 27/05/2016
 * With this class is done the request of the Live Matches to be displayed on the list
 */
public class LiveMatchRequest
{
    private static final String TAG = LiveMatchRequest.class.getSimpleName();

    public static final String REQUEST_URL = Urls.REQUEST_LIVE_MATCHES;

    /**
     * Get list of live matches
     * @return List of matches
     */
    public static ResponseDataObject<MatchEntity> getListLiveMatches()
    {
        ResponseDataObject responseDataObject = new ResponseDataObject();
        List<MatchEntity> listLiveMatches = new ArrayList<>();

        // Load Document
        Document document = null;
        try
        {
            document = HTMLRequestManager.getData(REQUEST_URL);

            // Set Response Code
            if(document != null && document.hasText())
            {
                // Response OK
                responseDataObject.setResponseCode(ResponseDataObject.RESPONSE_CODE_OK);
            } else
            {
                // Response Failed
                responseDataObject.setResponseCode(ResponseDataObject.RESPONSE_CODE_FAILED_GETTING_DOCUMENT);
            }

        }
        catch (IOException e)
        {
            LogUtil.e(TAG, e.getMessage(), e);

            // Response Falied
            responseDataObject.setResponseCode(ResponseDataObject.RESPONSE_CODE_FAILED_GETTING_DOCUMENT);
        }


        // Parse Data
        if(responseDataObject.isOk())
        {
            responseDataObject.setListObjectsResponse(parseLiveMatchDocumentToListMatches(document));
        }

        return responseDataObject;
    }

    /**
     * Parse Document with Live Matches list elements
     * @param document Document to parse
     * @return List of matches
     */
    private static List<MatchEntity> parseLiveMatchDocumentToListMatches(Document document)
    {
        List<MatchEntity> listMatches = new ArrayList<>();

        if(document != null)
        {

            switch (Urls.SOURCE_TYPE)
            {
                case LIVEFOOTBALLVIDEO:
                    // Parser Ids
                    final String SELECT_LIST_MATCHES = "div#main div.listmatch li"; // Should return a list of li.odd
                    // Get Matches Elements
                    Elements elements = document.select(SELECT_LIST_MATCHES);

                    if(elements == null || elements.isEmpty())
                    {
                        // Cannot parrse Matches
                        return listMatches;
                    }

                    // Complete each match
                    for (Element element : elements)
                    {
                        try
                        {
                            listMatches.add(parseLiveMatchLineToMatchEntity(element));
                        }
                        catch (Exception e)
                        {
                            LogUtil.e(TAG, e.getMessage(), e);
                        }
                    }
                    break;

                case LIVESPORTWS:
                    final String selectMatches = "ul.events li";

                    // Get Matches Elements
                    elements = document.select(selectMatches);

                    if(elements == null || elements.isEmpty())
                    {
                        // Cannot parrse Matches
                        return listMatches;
                    }

                    for (Element element : elements)
                    {
                        try
                        {
                            listMatches.add(parseLiveMatchLineToMatchEntity(element));
                        }
                        catch (Exception e)
                        {
                            LogUtil.e(TAG, e.getMessage(), e);
                        }
                    }

                    break;
            }

        }

        // Check if should log data
        if(LogUtil.IS_DEBUG)
        {
            // Logo Matches
            LogUtil.d(TAG, "Request Url: " + REQUEST_URL + " ResponseData: " + GSONParser.parseListObjectToString(listMatches));
        }

        return listMatches;
    }

    /**
     * Parse JSOUP element to a Match Entity
     * @param element Element received
     * @return Match entity generated
     */
    private static MatchEntity parseLiveMatchLineToMatchEntity(Element element)
    {
        switch (Urls.SOURCE_TYPE)
        {
            case LIVEFOOTBALLVIDEO:
                return parseFromLiveFootballVideo(element);

            case LIVESPORTWS:
                return parseFromLiveSportWS(element);
        }
        return null;
    }

    private static MatchEntity parseFromLiveSportWS(Element element)
    {
        // Selectores
        final String SELECT_COMPETITION_NAME = "span.competition";
        final String SELECT_COMPETITION_LOGO = "div.leaguelogo.column img";
        final String SELECT_DATE_CONTAINER = "span.date i";
        final String SELECT_TEAM_HOME = "div.commands.commands_match_center img.l";
        final String SELECT_TEAM_AWAY = "div.commands.commands_match_center img.r";
        final String SELECT_MATCH_LINK = "a [itemprop=url]";

        // Init Match Entity
        MatchEntity matchEntity = new MatchEntity();

        // Get Competition Info
        Elements elements = element.select(SELECT_COMPETITION_NAME);
        CompetitionEntity competitionEntity = new CompetitionEntity();
        if(DataUtils.isElementsValid(elements))
        {
            Element el = elements.first();
            competitionEntity.setCompetitionName(el.text());
            competitionEntity.setCompetitionLink(el.attr("href"));
        }

        // Get Compeition Logo
        elements = element.select(SELECT_COMPETITION_LOGO);
        if(DataUtils.isElementsValid(elements))
        {
            Element el = elements.first();
            competitionEntity.setCompetitionLogoUrl(el.attr("src"));
        }

        // Set Competition on match
        matchEntity.setCompetitionEntity(competitionEntity);

        // Get Dates
        try
        {
            elements = element.select(SELECT_DATE_CONTAINER);
            if(DataUtils.isElementsValid(elements))
            {
                try
                {
                    Element el = elements.first();
                    String startDateTime = el.attr("data-datetime");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Date date = sdf.parse(startDateTime);
                    matchEntity.setStartDateMillis(date.getTime());

                    matchEntity.setLive(el.text().equalsIgnoreCase("LIVE"));
                    matchEntity.setAvailable(!el.text().equalsIgnoreCase("OFFLINE"));
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (NumberFormatException e)
        {
            LogUtil.e(TAG, e.getMessage(), e);
        }


        // Get Team Home
        TeamEntity teamHome = new TeamEntity();
        elements = element.select(SELECT_TEAM_HOME);
        if(DataUtils.isElementsValid(elements))
        {
            // Get Team Name
            String teamName = elements.attr("alt");
            String teamLogoUrl = elements.attr("src");
            teamHome.setTeamName(teamName);
            teamHome.setTeamLogoUrl(teamLogoUrl);
        }
        matchEntity.setTeamHome(teamHome);

        // Get Team Away
        TeamEntity teamAway = new TeamEntity();
        elements = element.select(SELECT_TEAM_AWAY);
        if(DataUtils.isElementsValid(elements))
        {
            // Get Team Name
            String teamName = elements.attr("alt");
            String teamLogoUrl = elements.attr("src");
            teamAway.setTeamName(teamName);
            teamAway.setTeamLogoUrl(teamLogoUrl);
        }
        matchEntity.setTeamAway(teamAway);

        // Get Match Link
        elements = element.select(SELECT_MATCH_LINK);
        if(DataUtils.isElementsValid(elements))
        {
            matchEntity.setLinkUrl(elements.attr("href"));
        }

        return matchEntity;
    }

    private static MatchEntity parseFromLiveFootballVideo(Element element)
    {
        // Selectores
        final String SELECT_COMPETITION_NAME = "div.league.column a";
        final String SELECT_COMPETITION_LOGO = "div.leaguelogo.column img";
        final String SELECT_DATE_CONTAINER = "div.date_time.column";
        final String SELECT_TEAM_HOME = "div.team.column";
        final String SELECT_TEAM_AWAY = "div.team.away.column";
        final String SELECT_MATCH_LINK = "div.live_btn.column a";

        // Init Match Entity
        MatchEntity matchEntity = new MatchEntity();

        // Get Competition Info
        Elements elements = element.select(SELECT_COMPETITION_NAME);
        CompetitionEntity competitionEntity = new CompetitionEntity();
        if(DataUtils.isElementsValid(elements))
        {
            Element el = elements.first();
            competitionEntity.setCompetitionLink(el.attr("href"));
            competitionEntity.setCompetitionName(el.text());
        }

        // Get Compeition Logo
        elements = element.select(SELECT_COMPETITION_LOGO);
        if(DataUtils.isElementsValid(elements))
        {
            Element el = elements.first();
            competitionEntity.setCompetitionLogoUrl(el.attr("src"));
        }

        // Set Competition on match
        matchEntity.setCompetitionEntity(competitionEntity);

        // Get Dates
        try
        {
            elements = element.select(SELECT_DATE_CONTAINER);
            if(DataUtils.isElementsValid(elements))
            {
                Element el = elements.first();
                String startDateTime = el.select("span.starttime.time").attr("rel");
                matchEntity.setStartDateMillis(DataUtils.generateFinalMillis(Long.valueOf(startDateTime)));
                String endDateTime = el.select("span.endtime.time").attr("rel");
                matchEntity.setEndDateMillis(DataUtils.generateFinalMillis(Long.valueOf(endDateTime)));
            }
        }
        catch (NumberFormatException e)
        {
            LogUtil.e(TAG, e.getMessage(), e);
        }


        // Get Team Home
        TeamEntity teamHome = new TeamEntity();
        elements = element.select(SELECT_TEAM_HOME);
        if(DataUtils.isElementsValid(elements))
        {
            // Get Team Name
            String teamName = elements.select("span").first().text();
            String teamLogoUrl = elements.select("img").attr("src");
            teamHome.setTeamName(teamName);
            teamHome.setTeamLogoUrl(teamLogoUrl);
        }
        matchEntity.setTeamHome(teamHome);

        // Get Team Away
        TeamEntity teamAway = new TeamEntity();
        elements = element.select(SELECT_TEAM_AWAY);
        if(DataUtils.isElementsValid(elements))
        {
            // Get Team Name
            String teamName = elements.select("span").text();
            String teamLogoUrl = elements.select("img").attr("src");
            teamAway.setTeamName(teamName);
            teamAway.setTeamLogoUrl(teamLogoUrl);
        }
        matchEntity.setTeamAway(teamAway);

        // Get Match Link
        elements = element.select(SELECT_MATCH_LINK);
        if(DataUtils.isElementsValid(elements))
        {
            matchEntity.setLinkUrl(elements.attr("href"));

            matchEntity.setLive(elements.hasClass("online"));
        }

        return matchEntity;
    }

}
