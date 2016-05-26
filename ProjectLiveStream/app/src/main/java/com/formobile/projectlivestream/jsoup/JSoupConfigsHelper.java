package com.formobile.projectlivestream.jsoup;

import com.formobile.projectlivestream.entities.BackOfficeConfigsEntity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by PTECH on 26-01-2015.
 */
public class JSoupConfigsHelper {

    public static final String CONFIGS_URL = "http://justpaste.it/j3n5";


    public static BackOfficeConfigsEntity loadConfigs() throws IOException {
        BackOfficeConfigsEntity backOfficeConfigsEntity = null;

        Document document = Jsoup.connect(CONFIGS_URL).get();

        Elements elements = document.select("div#articleContent div:contains(START_FORMOB)");

        if(elements != null && !elements.isEmpty()){
            for(Element element : elements){
                if(element.text() != null && element.text().startsWith("START_FORMOB")){
                    String[] split = element.text().split("%%");

                    if(split != null){
                        if(split.length == 2){
                            backOfficeConfigsEntity = new BackOfficeConfigsEntity(Boolean.parseBoolean(split[1]));
                        } else if(split.length > 2){
                            backOfficeConfigsEntity = new BackOfficeConfigsEntity(Boolean.parseBoolean(split[1]), split[2]);
                        }
                    }

                    break;
                }
            }
        }

        if(backOfficeConfigsEntity == null){
            backOfficeConfigsEntity = new BackOfficeConfigsEntity(false);
        }


        return backOfficeConfigsEntity;
    }

}
