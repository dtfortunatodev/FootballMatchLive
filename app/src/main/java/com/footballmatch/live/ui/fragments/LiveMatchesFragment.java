package com.footballmatch.live.ui.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.footballmatch.live.R;
import com.footballmatch.live.data.managers.AnalyticsHelper;
import com.footballmatch.live.data.managers.RequestAsyncTask;
import com.footballmatch.live.data.model.MatchEntity;
import com.footballmatch.live.data.requests.ResponseDataObject;
import com.footballmatch.live.ui.activities.MatchDetailsActivity;
import com.footballmatch.live.ui.adapters.LiveMatchesRecyclerAdapter;
import com.footballmatch.live.ui.views.BaseRecyclerView;
import java.util.List;

/**
 * Created by David Fortunato on 05/08/2016
 * All rights reserved GoodBarber
 */
public class LiveMatchesFragment extends BaseFragment
{

    // Data
    private List<MatchEntity> listMatchEntities;

    // Views
    private BaseRecyclerView recyclerView;
    private LiveMatchesRecyclerAdapter recyclerAdapter;

    // Pending link to open
    private String pendingLinkToOpen;

    public static LiveMatchesFragment newInstance()
    {
        LiveMatchesFragment liveMatchesFragment = new LiveMatchesFragment();

        return liveMatchesFragment;
    }

    @Override
    public void onRequestResponse(ResponseDataObject responseDataObject)
    {
        super.onRequestResponse(responseDataObject);
        if (responseDataObject.isOk())
        {
            // Get Stream Links
            listMatchEntities = responseDataObject.getListObjectsResponse();

            recyclerAdapter.addListData(responseDataObject.getListObjectsResponse(), true);

            // Check if should open
            if (pendingLinkToOpen != null)
            {
                openMatchDetailsFromLink(pendingLinkToOpen);
            }
        }
        else
        {
            Toast.makeText(getContext(), R.string.error_loading_data, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View generateFragmentView(LayoutInflater inflater)
    {
        ViewGroup viewContainer = (ViewGroup) inflater.inflate(R.layout.fragment_live_matches_layout, null);
        recyclerView = (BaseRecyclerView) viewContainer.findViewById(R.id.viewRecyclerView);
        recyclerAdapter = new LiveMatchesRecyclerAdapter(getActivity());
        recyclerView.setAdapter(recyclerAdapter);

        // Start Load Data
        loadData();

        return viewContainer;
    }

    @Override
    protected void loadData()
    {
        new RequestAsyncTask<MatchEntity>(getContext(), RequestAsyncTask.RequestType.REQUEST_LIVE_MATCHES, this).execute();
    }

    /**
     * Open a match details from a link
     * @param matchDetailsLink
     */
    public void openMatchDetailsFromLink(String matchDetailsLink)
    {
        if (matchDetailsLink != null)
        {
            if (listMatchEntities != null)
            {
                boolean matchFound = false;
                if (matchDetailsLink != null && listMatchEntities != null)
                {
                    for (MatchEntity matchEntity : listMatchEntities)
                    {
                        if (matchEntity.getLinkUrl().contains(matchDetailsLink))
                        {
                            MatchDetailsActivity.startActivity(getContext(), matchEntity);
                            matchFound = true;
                            AnalyticsHelper.getInstance().sendEvent("URLScheme Match", "Match Opened: " + matchDetailsLink);
                        }
                    }
                }

                // Display the error
                if (!matchFound)
                {
                    AnalyticsHelper.getInstance().sendEvent("URLScheme Match", "Not Found: " + matchDetailsLink);
                    Toast.makeText(getContext(), R.string.error_match_details_notfound, Toast.LENGTH_SHORT).show();
                }
                pendingLinkToOpen = null;
            } else
            {
                pendingLinkToOpen = matchDetailsLink;
            }
        }

    }

}
