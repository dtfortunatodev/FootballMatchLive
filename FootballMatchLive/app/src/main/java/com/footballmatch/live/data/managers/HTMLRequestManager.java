package com.footballmatch.live.data.managers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

/**
 * Created by David Fortunato on 27/05/2016
 *
 * The Html request
 */
public class HTMLRequestManager
{


    /**
     * Get HTML data from Jsoup Implementation
     * @param url Url to connect
     * @return
     * @throws IOException
     */
    public static Document getData(String url) throws IOException
    {
        Document document = Jsoup.connect(url).get();

        return document;
    }

}
