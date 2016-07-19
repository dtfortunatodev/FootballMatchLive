package com.footballmatch.live.data.model;

import com.footballmatch.live.data.managers.GSONParser;
import java.io.Serializable;

/**
 * Created by David Fortunato on 19/07/2016
 * All rights reserved ForViews
 */
public class BaseEntity implements Serializable
{

    @Override
    public String toString()
    {
        return GSONParser.parseObjectToString(this);
    }
}
