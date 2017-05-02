package com.lutzed.servoluntario.selection;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.models.SelectableItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemsSelectionFragment extends Fragment implements ItemsSelectionContract.View {

    @BindView(R.id.list) RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout mSwipeRefreshLayout;

    private ItemsSelectionContract.Presenter mPresenter;

    private OnListFragmentInteractionListener mListener;

    public ItemsSelectionFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemsSelectionFragment newInstance() {
        ItemsSelectionFragment fragment = new ItemsSelectionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(ItemsSelectionContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_items_selections, container, false);

        ButterKnife.bind(this, view);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        mRecyclerView.setAdapter(new ItemsSelectionAdapter(new ArrayList<SelectableItem>(), mListener));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadItems(true);
            }
        });

        return view;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mSwipeRefreshLayout.setRefreshing(active);
    }

    @Override
    public void showNoItems() {

    }

    @Override
    public void showLoadingError() {

    }

    @Override
    public void showItems(List<? extends SelectableItem> items, boolean isSwap) {
        ItemsSelectionAdapter adapter = (ItemsSelectionAdapter) mRecyclerView.getAdapter();

        if (isSwap)
            adapter.swapData(items);
        else
            adapter.addData(items);

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showDefaultSaveError() {

    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(SelectableItem item);
    }

}
