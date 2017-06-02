package com.lutzed.servoluntario.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.models.FeedItem;
import com.lutzed.servoluntario.models.Opportunity;
import com.lutzed.servoluntario.models.Organization;
import com.lutzed.servoluntario.models.Volunteer;
import com.lutzed.servoluntario.opportunities.OpportunityActivity;
import com.lutzed.servoluntario.organization.OrganizationActivity;
import com.lutzed.servoluntario.util.EndlessRecyclerViewScrollListener;
import com.lutzed.servoluntario.util.VerticalSpaceItemDecoration;
import com.lutzed.servoluntario.volunteer.VolunteerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedFragment extends Fragment implements FeedContract.View, MainFragmentInterface {

    @BindView(R.id.list) RecyclerView mRecyclerView;

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout mSwipeRefreshLayout;
    private FeedContract.Presenter mPresenter;

    private FeedItemViewAdapter.OnFeedItemAdapterListener mListener;

    private EndlessRecyclerViewScrollListener mScrollListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FeedFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FeedFragment newInstance() {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        }

        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.recicler_view_vertical_spacing)));

        mRecyclerView.setAdapter(new FeedItemViewAdapter(new ArrayList<FeedItem>(), new FeedItemViewAdapter.OnFeedItemAdapterListener() {
            @Override
            public void onOrganizationClicked(Organization organization) {
                mPresenter.organizationClicked(organization);
            }

            @Override
            public void onOrganizationClicked(Long organizationId) {
                mPresenter.organizationClicked(organizationId);
            }

            @Override
            public void onOpportunityClicked(Opportunity opportunity) {
                mPresenter.opportunityClicked(opportunity);
            }

            @Override
            public void onVolunteerClicked(Volunteer volunteer) {
                mPresenter.volunteerClicked(volunteer);
            }

            @Override
            public void onVolunteerClicked(Long volunteerId) {
                mPresenter.volunteerClicked(volunteerId);
            }
        }));

        mScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                mPresenter.loadItems(false);
            }
        };
        mRecyclerView.addOnScrollListener(mScrollListener);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.refreshItems();
                mScrollListener.resetState();
            }
        });

        return view;
    }

    @Override
    public void setPresenter(FeedContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void start() {
        mPresenter.start();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FeedItemViewAdapter.OnFeedItemAdapterListener) {
            mListener = (FeedItemViewAdapter.OnFeedItemAdapterListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void clearItems() {
        FeedItemViewAdapter adapter = (FeedItemViewAdapter) mRecyclerView.getAdapter();
        adapter.clearData();
    }


    @Override
    public void swapItems(List<FeedItem> items) {
        FeedItemViewAdapter adapter = (FeedItemViewAdapter) mRecyclerView.getAdapter();
        adapter.swapData(items);
    }

    @Override
    public void addItems(List<FeedItem> items) {
        FeedItemViewAdapter adapter = (FeedItemViewAdapter) mRecyclerView.getAdapter();
        adapter.addData(items);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mSwipeRefreshLayout.setRefreshing(active);
    }

    @Override
    public void showLoadingError() {

    }

    @Override
    public void showNoItems() {

    }

    @Override
    public void showOpportunity(Opportunity opportunity) {
        Intent intent = new Intent(getContext(), OpportunityActivity.class);
        intent.putExtra(OpportunityActivity.EXTRA_OPPORTUNITY, opportunity);
        startActivity(intent);
    }

    @Override
    public void showOrganization(Organization organization) {
        Intent intent = new Intent(getContext(), OrganizationActivity.class);
        intent.putExtra(OrganizationActivity.EXTRA_ORGANIZATION, organization);
        startActivity(intent);
    }

    @Override
    public void showVolunteer(Volunteer volunteer) {
        Intent intent = new Intent(getContext(), VolunteerActivity.class);
        intent.putExtra(VolunteerActivity.EXTRA_VOLUNTEER, volunteer);
        startActivity(intent);
    }

    @Override
    public void showOrganization(Long organizationId) {
        Intent intent = new Intent(getContext(), OrganizationActivity.class);
        intent.putExtra(OrganizationActivity.EXTRA_ORGANIZATION_ID, organizationId);
        startActivity(intent);
    }

    @Override
    public void showVolunteer(Long volunteerId) {
        Intent intent = new Intent(getContext(), VolunteerActivity.class);
        intent.putExtra(VolunteerActivity.EXTRA_VOLUNTEER_ID, volunteerId);
        startActivity(intent);
    }
}
