package com.footballmatch.live.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.footballmatch.live.R;
import com.footballmatch.live.data.managers.StartupManager;
import com.footballmatch.live.data.model.MatchEntity;
import com.footballmatch.live.ui.fragments.MatchDetailsFragment;
import com.footballmatch.live.utils.Utils;

/**
 * Created by David Fortunato on 05/08/2016
 * All rights reserved GoodBarber
 */
public class MatchDetailsActivity extends BaseNavigationActivity
{

    // Extras Keys
    private static final String EXTRA_MATCH_ENTITY_OBJ = "EXTRA_MATCH_ENTITY_OBJ";

    // Data
    private MatchEntity matchEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Hide Menu
        setActionBarDrawerToggleType(ActionBarType.BACK_BUTTON);

        matchEntity = (MatchEntity) getIntent().getSerializableExtra(EXTRA_MATCH_ENTITY_OBJ);

        if (matchEntity != null)
        {
            setContentFragment(MatchDetailsFragment.newInstance(matchEntity));
        }
        else
        {
            Toast.makeText(MatchDetailsActivity.this, "Erro on opening the match", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_match_details, menu);

        // Set Icon White
        menu.findItem(R.id.action_share_button).getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_share_button:
                Utils.shareLink(this, getMatchShareLink(),
                               "Football Match: " + matchEntity.getTeamHome().getTeamName() + " vs " + matchEntity.getTeamAway().getTeamName());
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * @return
     */
    private String getMatchShareLink()
    {
        StringBuilder stringBuilder = new StringBuilder();

        // Generate Base Url
        stringBuilder.append(StartupManager.getInstance(getBaseContext()).getAppConfigs().getShareBaseLink());
        stringBuilder.append("?apn=").append(getApplicationContext().getPackageName());
        stringBuilder.append("&al=fmlive://matchDetails?link=").append(matchEntity.getLinkUrl());
        stringBuilder.append("&link=").append(StartupManager.getInstance(getBaseContext()).getAppConfigs().getUpdateRedirectDialog().getUpdateLink());
        return stringBuilder.toString();
    }

    /**
     * Start Match Details Activity
     *
     * @param context     Context
     * @param matchEntity Match Entity to open
     */
    public static void startActivity(Context context, MatchEntity matchEntity)
    {
        Intent intent = new Intent(context, MatchDetailsActivity.class);
        intent.putExtra(EXTRA_MATCH_ENTITY_OBJ, matchEntity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


}
