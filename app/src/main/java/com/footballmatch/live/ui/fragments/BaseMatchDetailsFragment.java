package com.footballmatch.live.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.footballmatch.live.R;
import com.footballmatch.live.data.managers.NotificationsManager;
import com.footballmatch.live.data.managers.RequestAsyncTask;
import com.footballmatch.live.data.managers.StartupManager;
import com.footballmatch.live.data.model.MatchEntity;
import com.footballmatch.live.data.model.StreamLinkEntity;
import com.footballmatch.live.data.requests.ImageLoaderHelper;
import com.footballmatch.live.data.requests.ResponseDataObject;
import com.footballmatch.live.ui.adapters.BaseRecyclerViewAdapter;
import com.footballmatch.live.ui.views.BaseRecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by David Fortunato on 05/08/2016
 * All rights reserved GoodBarber
 */
public abstract class BaseMatchDetailsFragment extends BaseFragment
{

    // Extras
    protected static final String EXTRA_MATCH_ENTITY_OBJ = "EXTRA_MATCH_ENTITY_OBJ";

    // Date Formats
    protected static final SimpleDateFormat DATE_FORMAT      = new SimpleDateFormat("dd/MM");
    protected static final SimpleDateFormat TIME_DATE_FORMAT = new SimpleDateFormat("HH:mm");

    // Data
    protected MatchEntity matchEntity;

    // Views
    protected ViewGroup viewContainer;
    protected TextView  viewTvTeamHomeName;
    protected TextView  viewTvTeamAwayName;
    protected TextView  viewTvDate;
    protected TextView  viewTvTime;
    protected ImageView viewIvTeamHomeLogo;
    protected ImageView viewIvTeamAwayLogo;
    protected TextView  viewTvStreamsNotAvailable;
    protected ViewGroup viewStreamsContainer;
    protected TextView  viewTvStreamListTitle;

    protected ImageView viewIvTeamHomeNotif;
    protected ImageView viewIvTeamAwayNotif;

    // Recycler Objects
//    protected MatchStreamLinksAdapter recyclerAdapter;
    protected BaseRecyclerView        recyclerView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        matchEntity = (MatchEntity) getArguments().getSerializable(EXTRA_MATCH_ENTITY_OBJ);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        if (viewContainer == null)
        {
            viewContainer = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);

            // Find Views
            viewTvTeamHomeName = (TextView) viewContainer.findViewById(R.id.tvMatchDetailsTeamHomeName);
            viewTvTeamAwayName = (TextView) viewContainer.findViewById(R.id.tvMatchDetailsTeamAwayName);
            viewTvDate = (TextView) viewContainer.findViewById(R.id.tvMatchDetailsDate);
            viewTvTime = (TextView) viewContainer.findViewById(R.id.tvMatchDetailsTimeOrLive);
            viewIvTeamHomeLogo = (ImageView) viewContainer.findViewById(R.id.ivMatchDetailsTeamHomeLogo);
            viewIvTeamAwayLogo = (ImageView) viewContainer.findViewById(R.id.ivMatchDetailsTeamAwayLogo);
            recyclerView = (BaseRecyclerView) viewContainer.findViewById(R.id.viewRecyclerView);
            viewTvStreamsNotAvailable = (TextView) viewContainer.findViewById(R.id.tvMatchDetailsStreamsNotAvailable);
            viewStreamsContainer = (ViewGroup) viewContainer.findViewById(R.id.layoutMatchDetailsStreamsContainer);
            viewIvTeamHomeNotif = (ImageView) viewContainer.findViewById(R.id.ivMatchDetailsTeamHomeNotif);
            viewIvTeamAwayNotif = (ImageView) viewContainer.findViewById(R.id.ivMatchDetailsTeamAwayNotif);
            viewTvStreamListTitle = (TextView) viewContainer.findViewById(R.id.tvMatchDetailsLiveStreamTitle);

            // Init Recycler Adapter
            recyclerView.setAdapter(getRecyclerAdapter());
        }

        // Setup UI
        viewTvTeamHomeName.setText(matchEntity.getTeamHome().getTeamName());
        viewTvTeamAwayName.setText(matchEntity.getTeamAway().getTeamName());
        viewTvDate.setText(DATE_FORMAT.format(new Date(matchEntity.getStartDateMillis())));


        // Set text Time or Live
        if (matchEntity.isLive())
        {
            viewTvTime.setText(getString(R.string.list_match_is_live));
            viewTvTime.setTextColor(getResources().getColor(R.color.list_match_item_live));
        }
        else
        {
            viewTvTime.setText(TIME_DATE_FORMAT.format(new Date(matchEntity.getStartDateMillis())));
            viewTvTime.setTextColor(getResources().getColor(R.color.list_match_item_time));
        }


        // Load Logos
        ImageLoaderHelper.loadImage(matchEntity.getTeamHome().getTeamLogoUrl(), viewIvTeamHomeLogo, R.drawable.team_generic_logo);
        ImageLoaderHelper.loadImage(matchEntity.getTeamAway().getTeamLogoUrl(), viewIvTeamAwayLogo, R.drawable.team_generic_logo);

        // Load Data
        loadData();

        // Process Block Screen
        processBlockScreen();


        // Set Listener on Click
        if (StartupManager.getInstance(getContext()).getAppConfigs().isSubsNotificationEnabled())
        {
            viewIvTeamAwayNotif.setVisibility(View.VISIBLE);
            viewIvTeamHomeNotif.setVisibility(View.VISIBLE);

            viewIvTeamHomeNotif.setImageResource(NotificationsManager.getInstance(getContext()).isTeamRegistered(matchEntity.getTeamHome().getTeamName()) ? R.drawable.ic_notification_registered : R.drawable.ic_notification_unregistered);
            viewIvTeamHomeNotif.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (NotificationsManager.getInstance(getContext()).isTeamRegistered(matchEntity.getTeamHome().getTeamName()))
                    {
                        viewIvTeamHomeNotif.setImageResource(R.drawable.ic_notification_unregistered);
                        NotificationsManager.getInstance(getContext()).unregisterTeam(matchEntity.getTeamHome().getTeamName());
                    }
                    else
                    {
                        viewIvTeamHomeNotif.setImageResource(R.drawable.ic_notification_registered);
                        NotificationsManager.getInstance(getContext()).registerTeam(matchEntity.getTeamHome().getTeamName());
                    }
                }
            });

            // Set Listener on Click
            viewIvTeamAwayNotif.setImageResource(NotificationsManager.getInstance(getContext()).isTeamRegistered(matchEntity.getTeamAway().getTeamName()) ? R.drawable.ic_notification_registered : R.drawable.ic_notification_unregistered);
            viewIvTeamAwayNotif.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (NotificationsManager.getInstance(getContext()).isTeamRegistered(matchEntity.getTeamAway().getTeamName()))
                    {
                        viewIvTeamAwayNotif.setImageResource(R.drawable.ic_notification_unregistered);
                        NotificationsManager.getInstance(getContext()).unregisterTeam(matchEntity.getTeamAway().getTeamName());
                    }
                    else
                    {
                        viewIvTeamAwayNotif.setImageResource(R.drawable.ic_notification_registered);
                        NotificationsManager.getInstance(getContext()).registerTeam(matchEntity.getTeamAway().getTeamName());
                    }
                }
            });
        } else
        {
            viewIvTeamAwayNotif.setVisibility(View.GONE);
            viewIvTeamHomeNotif.setVisibility(View.GONE);
        }


        return viewContainer;
    }


    /**
     * Block Screen
     */
    private void processBlockScreen()
    {
        if (StartupManager.getInstance(getContext()).getAppConfigs().checkShouldBlockSensibleData())
        {
            viewIvTeamAwayLogo.setVisibility(View.GONE);
            viewIvTeamHomeLogo.setVisibility(View.GONE);
            viewStreamsContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public View generateFragmentView(LayoutInflater inflater)
    {
        return inflater.inflate(R.layout.fragment_match_details_layout, null);
    }

    @Override
    protected void loadData()
    {
        // Load Data
        new RequestAsyncTask<StreamLinkEntity>(getContext(), RequestAsyncTask.RequestType.REQUEST_LIST_STREAMS, this).setRequestUrl(matchEntity.getLinkUrl())
                .execute();
    }

    @Override
    public void onRequestResponse(ResponseDataObject responseDataObject)
    {
        super.onRequestResponse(responseDataObject);

        // Add Data to Adapter
        if (responseDataObject.isOk())
        {
            getRecyclerAdapter().addListData(responseDataObject.getListObjectsResponse(), true);
        } else
        {
            Toast.makeText(getContext(), R.string.error_loading_data, Toast.LENGTH_LONG).show();
        }

        // Check if is empty
        if (getRecyclerAdapter().getItemCount() > 0)
        {
            recyclerView.setVisibility(View.VISIBLE);
            viewTvStreamsNotAvailable.setVisibility(View.GONE);
        } else
        {
            recyclerView.setVisibility(View.GONE);
            viewTvStreamsNotAvailable.setVisibility(View.VISIBLE);
        }
    }


    protected abstract BaseRecyclerViewAdapter getRecyclerAdapter();

}
