package com.formobile.projectlivestream.configs;

import android.content.Context;

import com.formobile.projectlivestream.R;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PTECH on 07-02-2015.
 */
public class ConnectionHelper {

    private static OkHttpClient mOkHttpClientInstance;
    private static ObjectMapper mapper;

    /**
     * Get Object Mapper Instance
     * @return
     */
    public static ObjectMapper getObjectMapperInstance(){
        if(mapper == null){
            mapper = new ObjectMapper();
            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        return mapper;
    }

    /**
     * Get a instance of Http Client
     * @return
     */
    public static OkHttpClient getHttpClientInstance(){
        if(mOkHttpClientInstance == null){
            mOkHttpClientInstance = new OkHttpClient();
        }

        return mOkHttpClientInstance;
    }

    /**
     * Get a response connection
     * @param url
     * @return
     * @throws IOException
     */
    public static String getUrlResponse(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = getHttpClientInstance().newCall(request).execute();

        // Return Response
        return response.body().string();
    }


    /**
     * Get object
     * @param objClass
     * @param url
     * @param <T>
     * @return
     */
    public static <T> T getObject(Class<T> objClass, String url) throws IOException {
        T t = null;

        String response = getUrlResponse(url);

        if(response != null){
            t = getObjectMapperInstance().readValue(response, objClass);
        }

        return t;
    }

    public static <T> List<T> getListObjects(Class<T> objClass, String url) throws IOException {
        List<T> lt = null;

        String response = getUrlResponse(url);

        if(response != null){
            lt = mapper.readValue(response,
                    TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, objClass));
        }

        return lt;
    }

    /**
     * Get StartupConfigs
     * @return Startup Configs
     */
    public static StartupConfigs getStartupConfigs(Context context) throws IOException {
        StartupConfigs startupConfigs = getObject(StartupConfigs.class, context.getString(R.string.url_startup_configs));

        return startupConfigs;
    }


    /**
     * Get other sports list
     * @param context
     * @return
     */
    public static List<OtherSportJSONEntity> getListOtherSport(Context context) throws IOException {
        List<OtherSportJSONEntity> listOtherSports = getListObjects(OtherSportJSONEntity.class, context.getString(R.string.url_other_sports));

        return listOtherSports;
    }

}
