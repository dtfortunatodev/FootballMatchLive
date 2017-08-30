package com.footballmatch.live.ui.activities;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.footballmatch.live.R;
import com.footballmatch.live.managers.ads.AdsManager;

/**
 * Created by David Fortunato on 26/05/2016
 */
public class BaseNavigationActivity extends AppCompatActivity
{

    // Views
    protected AppBarLayout appBarLayout;
    protected Toolbar viewToolBar;
    protected ViewGroup viewBaseContainer; // This is the container to set the new data

    // Flags
    private boolean mAlreadyInitLayout = false;

    // Menu Type
    private ActionBarType currentActionBarType;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Lifecycle to the AdsManager
        AdsManager.getInstance(this).onCreate(this);

        // Default Menu Type
        currentActionBarType = ActionBarType.MENU;

        mAlreadyInitLayout = false;
        setContentView(R.layout.activity_base_navigation);

        // Find Views
        viewToolBar = (Toolbar) findViewById(R.id.toolbar);
        viewBaseContainer = (ViewGroup) findViewById(R.id.layoutBaseContainer);
        appBarLayout = (AppBarLayout) findViewById(R.id.viewAppBarLayout);

        // Setup ToolBar
        setSupportActionBar(viewToolBar);

        viewToolBar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        viewToolBar.offsetLeftAndRight(0);
        viewToolBar.setLogo(R.drawable.ic_launcher);

        // Show Interstitial
        AdsManager.getInstance(getBaseContext()).showInsterstitial(this);
    }


    @Override
    public void setContentView(@LayoutRes int layoutResID)
    {
        if (!mAlreadyInitLayout)
        {
            super.setContentView(layoutResID);
            mAlreadyInitLayout = true;
        }
        else
        {
            View inflatedView = LayoutInflater.from(getBaseContext()).inflate(layoutResID, null);
            viewBaseContainer.removeAllViews();
            viewBaseContainer.addView(inflatedView);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params)
    {
        if (!mAlreadyInitLayout)
        {
            super.setContentView(view, params);
            mAlreadyInitLayout = true;
        }
        else
        {
            viewBaseContainer.removeAllViews();
            viewBaseContainer.addView(view);
        }
    }


    public void setActionBarDrawerToggleType(ActionBarType actionBarType)
    {
        switch (actionBarType)
        {
            case MENU:
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setHomeButtonEnabled(false);
                break;

            case BACK_BUTTON:
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
                break;
            case HIDDEN:
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setHomeButtonEnabled(false);
                break;
        }
    }

    /**
     * Set Content with a Fragment
     * @param fragment Fragment to be the content
     */
    public void setContentFragment(Fragment fragment)
    {
        if (fragment != null)
        {
            viewBaseContainer.removeAllViews();
            getSupportFragmentManager().beginTransaction().add(R.id.layoutBaseContainer, fragment).commit();
        }
    }


    @Override
    public void setContentView(View view)
    {
        if (!mAlreadyInitLayout)
        {
            super.setContentView(view);
            mAlreadyInitLayout = true;
        }
        else
        {
            viewBaseContainer.removeAllViews();
            viewBaseContainer.addView(view);
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        // Lifecycle to the AdsManager
        AdsManager.getInstance(this).onResume(this);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        // Lifecycle to the AdsManager
        AdsManager.getInstance(this).onStart(this);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        // Lifecycle to the AdsManager
        AdsManager.getInstance(this).onStop(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        // Lifecycle to the AdsManager
        AdsManager.getInstance(this).onDestory(this);
    }

    /**
     * Type
     */
    public enum ActionBarType
    {
        MENU, BACK_BUTTON, HIDDEN;
    }

}
