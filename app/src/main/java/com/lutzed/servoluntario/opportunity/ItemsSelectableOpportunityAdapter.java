package com.lutzed.servoluntario.opportunity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.models.SelectableItem;
import com.lutzed.servoluntario.selection.ItemsSelectionFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link RecyclerView.Adapter} that can display a {@link SelectableItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ItemsSelectableOpportunityAdapter extends RecyclerView.Adapter<ItemsSelectableOpportunityAdapter.ViewHolder> {

    private final List<SelectableItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public ItemsSelectableOpportunityAdapter(List<SelectableItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public void clearData() {
        int size = getItemCount();
        if (size > 0) {
            mValues.clear();
            this.notifyItemRangeRemoved(0, size);
        }
    }

    public void addData(List<? extends SelectableItem> items) {
        if (items == null || items.isEmpty()) {
            return;
        }

        int beforeSize = getItemCount();

        mValues.addAll(items);
        notifyItemRangeInserted(beforeSize, items.size());
    }

    public void addDataAtStart(List<? extends SelectableItem> items) {
        if (items == null || items.isEmpty()) {
            return;
        }

        mValues.addAll(0, items);
        notifyItemRangeInserted(0, items.size());
    }

    public void addSingleBeforeLast(SelectableItem item) {
        if (item == null) {
            return;
        }
        int position = Math.max(mValues.size() - 1, 0);

        mValues.add(position, item);
        notifyItemInserted(position);
    }


    public void swapData(List<? extends SelectableItem> items) {
        clearData();
        addData(items);
    }

    List<Long> getSelectedItemsIds() {
        List<Long> list = new ArrayList<>();
        for (SelectableItem item : mValues) {
            if (item.isSelected()) list.add(item.getId());
        }
        return list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_selectable_opportunity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getId() + "");
        holder.mContentView.setText(mValues.get(position).getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem, holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        @BindView(R.id.id) TextView mIdView;
        @BindView(R.id.content) TextView mContentView;

        SelectableItem mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;

            ButterKnife.bind(this, mView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
