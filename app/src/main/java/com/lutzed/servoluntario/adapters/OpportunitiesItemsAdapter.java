package com.lutzed.servoluntario.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.models.Image;
import com.lutzed.servoluntario.models.SelectableItem;
import com.lutzed.servoluntario.selection.ItemsSelectionFragment.OnListFragmentInteractionListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link RecyclerView.Adapter} that can display a {@link SelectableItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class OpportunitiesItemsAdapter extends RecyclerView.Adapter<OpportunitiesItemsAdapter.ViewHolder> {

    private final List<SelectableItem> mValues;
    private final OnAdapterListener mListener;

    public interface OnAdapterListener {
        void onAdapterInteraction(SelectableItem mItem, int adapterPosition);
    }

    public OpportunitiesItemsAdapter(List<SelectableItem> items, OnAdapterListener listener) {
        mValues = items;
        mListener = listener;
    }

    public void addAndRemoveItems(List<? extends SelectableItem> itemsToAdd, List<? extends SelectableItem> itemsToRemove) {
        if (itemsToRemove != null && !itemsToRemove.isEmpty()) {
            mValues.removeAll(itemsToRemove);
        }

        int startPosition = Math.max(mValues.size() - 1, 0);
        if (itemsToAdd != null && !itemsToAdd.isEmpty()) {
            for (SelectableItem item : itemsToAdd) {
                if (!mValues.contains(item)) mValues.add(startPosition, item);
            }
        }

        Collections.sort(mValues, new Comparator<SelectableItem>() {
            @Override
            public int compare(SelectableItem o1, SelectableItem o2) {
                if (o1.getId() == null) {
                    return 1;
                } else if (o2.getId() == null) {
                    return -1;
                } else if (o1.getId() > o2.getId()) {
                    return 1;
                } else if (o1.getId() < o2.getId()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        notifyDataSetChanged();
    }

    public void clearData() {
        int size = getItemCount();
        if (size > 0) {
            mValues.clear();
            this.notifyItemRangeRemoved(0, size);
        }
    }

    public void addItems(List<? extends SelectableItem> items) {
        if (items == null || items.isEmpty()) {
            return;
        }
        int startPosition = getItemCount();
        mValues.addAll(startPosition, items);

        Collections.sort(mValues, new Comparator<SelectableItem>() {
            @Override
            public int compare(SelectableItem o1, SelectableItem o2) {
                if (o1.getId() == null) {
                    return 1;
                } else if (o2.getId() == null) {
                    return -1;
                } else if (o1.getId() > o2.getId()) {
                    return 1;
                } else if (o1.getId() < o2.getId()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        notifyItemRangeInserted(startPosition, items.size());
    }

    public List<Long> getItemsIds() {
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < mValues.size() - 1; i++) {
            SelectableItem item = mValues.get(i);
            list.add(item.getId());
        }
        return list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_opportunity_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        if (!holder.mItem.isAddPlaceholder()) {
            Image image = holder.mItem.getImage();
            if (image != null) {
                Picasso.with(holder.mView.getContext()).load(image.getUrl()).placeholder(R.drawable.ic_group_work_black_24dp).into(holder.mImageView);
            } else {
                holder.mImageView.setImageResource(R.drawable.ic_group_work_black_24dp);
            }
        } else {
            holder.mImageView.setImageResource(R.drawable.ic_add_circle_black_24dp);
        }

        holder.mNameView.setText(mValues.get(position).getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onAdapterInteraction(holder.mItem, holder.getAdapterPosition());
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
        @BindView(R.id.image) ImageView mImageView;
        @BindView(R.id.name) TextView mNameView;

        SelectableItem mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;

            ButterKnife.bind(this, mView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
