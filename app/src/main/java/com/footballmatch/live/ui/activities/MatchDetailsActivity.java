package com.footballmatch.live.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.footballmatch.live.data.model.MatchEntity;
import com.footballmatch.live.ui.fragments.MatchDetailsFragment;

/**
 * Created by David Fortunato on 05/08/2016
 * All rights reserved GoodBarber
 */
public class MatchDetailsActivity extends BaseNavigationActivity
{

    // Extras Keys
    private static final String EXTRA_MATCH_ENTITY_OBJ = "EXTRA_MATCH_ENTITY_OBJ";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Hide Menu
        setActionBarDrawerToggleType(ActionBarType.BACK_BUTTON);

        MatchEntity matchEntity = (MatchEntity) getIntent().getSerializableExtra(EXTRA_MATCH_ENTITY_OBJ);

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


    /**
     * Start Match Details Activity
     * @param context Context
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
