package com.footballmatch.live.data.requests;

import com.footballmatch.live.data.managers.HTMLRequestManager;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;

/**
 * Created by David Fortunato on 07/08/2016
 * All rights reserved GoodBarber
 */
public class ArenaVisionGetLinkRequest
{

    /**
     * Getting Arena Vision Link
     * @param url Url to analyze
     * @return Url to play or null
     */
    public static String getArenaVisionLink(String url)
    {
        String arenaVisionUrl = null;
        try
        {
            Document document = HTMLRequestManager.getData(url);

            // Trying getting AceStream
            Elements elements = document.select("p.auto-style1 a[href^=acestream://]");

            // Check if got
            if (elements != null && !elements.isEmpty())
            {
                arenaVisionUrl = elements.attr("href");
            }
            else
            {
                // Trying getting sopcast
                elements = document.select("a[href^=sop://]");

                if (elements != null && !elements.isEmpty())
                {
                    arenaVisionUrl = elements.attr("href");
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return arenaVisionUrl;
    }

}
