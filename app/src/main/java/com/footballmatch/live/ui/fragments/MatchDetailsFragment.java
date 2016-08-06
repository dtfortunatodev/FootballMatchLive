package com.footballmatch.live.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.footballmatch.live.R;
import com.footballmatch.live.data.managers.RequestAsyncTask;
import com.footballmatch.live.data.model.MatchEntity;
import com.footballmatch.live.data.model.StreamLinkEntity;
import com.footballmatch.live.data.requests.ImageLoaderHelper;
import com.footballmatch.live.data.requests.ResponseDataObject;
import com.footballmatch.live.ui.adapters.MatchStreamLinksAdapter;
import com.footballmatch.live.ui.views.BaseRecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by David Fortunato on 05/08/2016
 * All rights reserved GoodBarber
 */
public class MatchDetailsFragment extends BaseFragment
{

    // Extras
    private static final String EXTRA_MATCH_ENTITY_OBJ = "EXTRA_MATCH_ENTITY_OBJ";

    // Date Formats
    private static final SimpleDateFormat DATE_FORMAT      = new SimpleDateFormat("dd/MM");
    private static final SimpleDateFormat TIME_DATE_FORMAT = new SimpleDateFormat("HH:mm");

    // Data
    private MatchEntity matchEntity;

    // Views
    private ViewGroup viewContainer;
    private TextView  viewTvTeamHomeName;
    private TextView  viewTvTeamAwayName;
    private TextView  viewTvDate;
    private TextView  viewTvTime;
    private ImageView viewIvTeamHomeLogo;
    private ImageView viewIvTeamAwayLogo;

    // Recycler Objects
    private MatchStreamLinksAdapter recyclerAdapter;
    private BaseRecyclerView        recyclerView;

    /**
     * New Fragment Instance
     *
     * @param matchEntity
     *
     * @return
     */
    public static MatchDetailsFragment newInstance(MatchEntity matchEntity)
    {
        MatchDetailsFragment matchDetailsFragment = new MatchDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_MATCH_ENTITY_OBJ, matchEntity);
        matchDetailsFragment.setArguments(bundle);

        return matchDetailsFragment;
    }

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

            // Init Recycler Adapter
            recyclerAdapter = new MatchStreamLinksAdapter(getContext());
            recyclerView.setAdapter(recyclerAdapter);
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

        return viewContainer;
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
            recyclerAdapter.addListData(responseDataObject.getListObjectsResponse(), true);
        }
    }


}
