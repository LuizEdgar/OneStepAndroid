package com.lutzed.servoluntario.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.models.Image;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryViewAdapter extends RecyclerView.Adapter<GalleryViewAdapter.ViewHolder> {

    private final ArrayList<Image> mValues;
    private final OnGalleryInteractionListener mListener;
    private final boolean showDelete;

    public GalleryViewAdapter(ArrayList<Image> items, boolean showDeleteOption, OnGalleryInteractionListener listener) {
        mValues = items;
        mListener = listener;
        showDelete = showDeleteOption;
    }

    public interface OnGalleryInteractionListener {
        void onImageClicked(ArrayList<Image> values, Image item, int position);

        void onPromptDeleteImage(Image item, int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gallery, parent, false);
        return new ViewHolder(view);
    }

    public void addItemBeforeLast(List<Image> items) {
        if (items == null || items.isEmpty()) {
            return;
        }

        int beforeSize = getItemCount();
        int startPosition = Math.max(beforeSize - 1, 0);

        mValues.addAll(startPosition, items);
        notifyItemRangeInserted(startPosition, items.size());
    }

    public void removeItem(int position) {
        mValues.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        boolean isAddPlaceholder = holder.mItem.isAddPlaceholder();

        if (isAddPlaceholder) {
            Picasso.with(holder.mView.getContext()).load(R.drawable.ic_arrow_drop_down_black_24dp).into(holder.mThumbnailImageView);
        } else if (holder.mItem.getBitmap() != null) {
            holder.mThumbnailImageView.setImageBitmap(holder.mItem.getBitmap());
        } else {
            Picasso.with(holder.mView.getContext()).load(holder.mItem.getUrl()).into(holder.mThumbnailImageView);
        }

        holder.mView.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onImageClicked(mValues, holder.mItem, holder.getAdapterPosition());
                }
            }
        });

        if (showDelete && !isAddPlaceholder)

        {
            holder.mRemoveButton.setVisibility(View.VISIBLE);
            holder.mRemoveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != mListener) {
                        mListener.onPromptDeleteImage(holder.mItem, holder.getAdapterPosition());
                    }
                }
            });
            holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (null != mListener) {
                        mListener.onPromptDeleteImage(holder.mItem, holder.getAdapterPosition());
                        return true;
                    }
                    return false;
                }
            });
        } else

        {
            holder.mRemoveButton.setVisibility(View.GONE);
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        @BindView(R.id.thumbnail) ImageView mThumbnailImageView;
        @BindView(R.id.remove_button) ImageButton mRemoveButton;
        Image mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);

        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
