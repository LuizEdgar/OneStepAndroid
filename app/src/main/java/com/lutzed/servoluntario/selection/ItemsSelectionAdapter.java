package com.lutzed.servoluntario.selection;

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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link RecyclerView.Adapter} that can display a {@link SelectableItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ItemsSelectionAdapter extends RecyclerView.Adapter<ItemsSelectionAdapter.ViewHolder> {

    private final List<SelectableItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final ItemsSelectionActivity.Mode mMode;

    public ItemsSelectionAdapter(List<SelectableItem> items, OnListFragmentInteractionListener listener, ItemsSelectionActivity.Mode mode) {
        mValues = items;
        mListener = listener;
        mMode = mode;
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

    public class ItemsReturn {
        public List<SelectableItem> selected;
        public List<SelectableItem> notSelected;

        public ItemsReturn() {
            selected = new ArrayList<>();
            notSelected = new ArrayList<>();
        }
    }

    ItemsReturn getItemsReturn() {
        ItemsReturn itemsReturn = new ItemsReturn();
        for (SelectableItem item : mValues) {
            if (item.isSelected()) itemsReturn.selected.add(item);
            else itemsReturn.notSelected.add(item);
        }
        return itemsReturn;
    }

    List<SelectableItem> getSelectedItems() {
        List<SelectableItem> list = new ArrayList<>();
        for (SelectableItem item : mValues) {
            if (item.isSelected()) list.add(item);
        }
        return list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_selection, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mNameView.setText(mValues.get(position).getName());

        Image image = holder.mItem.getImage();
        if (image != null) {
            Picasso.with(holder.mView.getContext()).load(image.getUrl()).placeholder(R.drawable.ic_group_work_black_24dp).into(holder.mImageView);
        } else {
            holder.mImageView.setImageResource(R.drawable.ic_group_work_black_24dp);
        }

        holder.mCheckMark.setVisibility(holder.mItem.isSelected() ? View.VISIBLE : View.INVISIBLE);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMode == ItemsSelectionActivity.Mode.SINGLE_SELECTION) {
                    if (null != mListener) {
                        mListener.onItemSelected(holder.mItem);
                    }
                } else {
                    holder.mItem.setSelected(!holder.mItem.isSelected());
                    holder.mCheckMark.setVisibility(holder.mItem.isSelected() ? View.VISIBLE : View.INVISIBLE);
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
        @BindView(R.id.checkMark) ImageView mCheckMark;

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
