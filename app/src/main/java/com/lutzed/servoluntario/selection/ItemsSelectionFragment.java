package com.lutzed.servoluntario.selection;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.IntentCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.main.MainActivity;
import com.lutzed.servoluntario.models.SelectableItem;
import com.lutzed.servoluntario.util.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemsSelectionFragment extends Fragment implements ItemsSelectionContract.View {

    private static final String BUNDLE_SELECTION_MODE = "bundle_selection_mode";
    private static final String BUNDLE_SAVE_ACTION_NAME = "bundle_save_action_name";

    @BindView(R.id.list) RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout mSwipeRefreshLayout;

    private ItemsSelectionContract.Presenter mPresenter;

    private OnListFragmentInteractionListener mListener;

    private EndlessRecyclerViewScrollListener mScrollListener;

    private ItemsSelectionActivity.Mode mMode;
    private String mSaveActionName;

    public ItemsSelectionFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemsSelectionFragment newInstance(ItemsSelectionActivity.Mode mode, String saveActionName) {
        ItemsSelectionFragment fragment = new ItemsSelectionFragment();
        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_SELECTION_MODE, mode);
        args.putString(BUNDLE_SAVE_ACTION_NAME, saveActionName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mMode = (ItemsSelectionActivity.Mode) getArguments().getSerializable(BUNDLE_SELECTION_MODE);
            mSaveActionName = getArguments().getString(BUNDLE_SAVE_ACTION_NAME);
        }

        //Do not show menu option if single selection
        if (mMode != ItemsSelectionActivity.Mode.SINGLE_SELECTION) {
            setHasOptionsMenu(true);
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

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mRecyclerView.setAdapter(new ItemsSelectionAdapter(new ArrayList<SelectableItem>(), mListener, mMode));


        mScrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.save, menu);

        if (mSaveActionName != null) menu.findItem(R.id.action_save).setTitle(mSaveActionName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_next || item.getItemId() == R.id.action_save) {
            if (mMode == ItemsSelectionActivity.Mode.MULTIPLE_SAVE_TO_USER) {
                mPresenter.saveItemsToUser(((ItemsSelectionAdapter) mRecyclerView.getAdapter()).getSelectedItemsIds());
                return true;
            } else if (mMode == ItemsSelectionActivity.Mode.MULTIPLE_SELECTION){
                ItemsSelectionAdapter.ItemsReturn itemsReturn = ((ItemsSelectionAdapter) mRecyclerView.getAdapter()).getItemsReturn();
                mListener.onItemsSelected(itemsReturn.selected, itemsReturn.notSelected);
                return true;
            }
            return false;
        }
        return super.onOptionsItemSelected(item);
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
    public void clearItems() {
        ItemsSelectionAdapter adapter = (ItemsSelectionAdapter) mRecyclerView.getAdapter();
        adapter.clearData();
    }

    @Override
    public void swapItems(List<? extends SelectableItem> items) {
        ItemsSelectionAdapter adapter = (ItemsSelectionAdapter) mRecyclerView.getAdapter();
        adapter.swapData(items);
    }

    @Override
    public void addItems(List<? extends SelectableItem> items) {
        ItemsSelectionAdapter adapter = (ItemsSelectionAdapter) mRecyclerView.getAdapter();
        adapter.addData(items);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setSavingIndicator(boolean active) {
        Toast.makeText(getContext(), "Saving...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToChooseSkills() {
        getActivity().finish();
        Intent intent = new Intent(getContext(), ItemsSelectionActivity.class);
        intent.putExtra(ItemsSelectionActivity.EXTRA_ITEM_SELECTION_KIND, ItemsSelectionActivity.Kind.SKILL);
        intent.putExtra(ItemsSelectionActivity.EXTRA_SAVE_ACTION_NAME, getString(R.string.action_next));
        intent.putExtra(ItemsSelectionActivity.EXTRA_ITEM_SELECTION_MODE, ItemsSelectionActivity.Mode.MULTIPLE_SAVE_TO_USER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void navigateToChooseCauses() {
        getActivity().finish();
        Intent intent = new Intent(getContext(), ItemsSelectionActivity.class);
        intent.putExtra(ItemsSelectionActivity.EXTRA_ITEM_SELECTION_KIND, ItemsSelectionActivity.Kind.CAUSE);
        intent.putExtra(ItemsSelectionActivity.EXTRA_SAVE_ACTION_NAME, getString(R.string.action_next));
        intent.putExtra(ItemsSelectionActivity.EXTRA_ITEM_SELECTION_MODE, ItemsSelectionActivity.Mode.MULTIPLE_SAVE_TO_USER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void navigateToMain() {
        getActivity().finish();
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void showDefaultSaveError() {

    }

    public interface OnListFragmentInteractionListener {
        void onItemSelected(SelectableItem item);
        void onItemsSelected(List<SelectableItem> selectedItems, List<SelectableItem> notSelectedItems);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
