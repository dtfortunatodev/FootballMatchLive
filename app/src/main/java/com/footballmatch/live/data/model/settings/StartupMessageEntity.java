package com.footballmatch.live.data.model.settings;

import com.footballmatch.live.data.model.BaseEntity;

/**
 * Created by David Fortunato on 12/08/2016
 * All rights reserved GoodBarber
 */
public class StartupMessageEntity extends BaseEntity
{

    // Data
    private boolean isEnabled;
    private String message;
    private int id;

    StartupMessageEntity()
    {
        isEnabled = false;
        id = 0;
        message = null;

    }

    public boolean isEnabled()
    {
        return isEnabled;
    }

    public void setEnabled(boolean enabled)
    {
        isEnabled = enabled;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }
}
