package com.lutzed.servoluntario.main;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.adapters.GalleryViewAdapter;
import com.lutzed.servoluntario.models.FeedItem;
import com.lutzed.servoluntario.models.Image;
import com.lutzed.servoluntario.models.Location;
import com.lutzed.servoluntario.models.Opportunity;
import com.lutzed.servoluntario.models.Organization;
import com.lutzed.servoluntario.util.CircleTransform;
import com.lutzed.servoluntario.util.DateHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.lutzed.servoluntario.R.id.infos;

public class FeedItemViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<FeedItem> mValues;
    private OnFeedItemAdapterListener mListener;

    public FeedItemViewAdapter(List<FeedItem> items, OnFeedItemAdapterListener listener) {
        mValues = items;
        mListener = listener;
    }

    public interface OnFeedItemAdapterListener {
        void onEditClicked(FeedItem feedItem, int position);

        void onItemClicked(FeedItem feedItem, int position);

        void onItemShare(FeedItem feedItem);
    }

    public void clearData() {
        int size = getItemCount();
        if (size > 0) {
            mValues.clear();
            this.notifyItemRangeRemoved(0, size);
        }
    }

    public void addData(List<FeedItem> items) {
        if (items == null || items.isEmpty()) {
            return;
        }

        int beforeSize = getItemCount();

        mValues.addAll(items);
        notifyItemRangeInserted(beforeSize, items.size());
    }

    public void swapData(List<FeedItem> items) {
        clearData();
        addData(items);
    }


    public void removeItem(int position) {
        mValues.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;

        switch (viewType) {
            case R.id.feed_item_adapter_opportunity_id:
                v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_opportunity, viewGroup, false);

                return new OpportunityViewHolder(v);
            default:
                v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_organization, viewGroup, false);

                return new OrganizationHolder(v);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case R.id.feed_item_adapter_opportunity_id:
                setupOpportunity(((OpportunityViewHolder) holder), position);
                break;
            default:
                setupOrganization(((OrganizationHolder) holder), position);
        }

    }

    private void setupOrganization(final OrganizationHolder holder, int position) {
        Organization item = holder.mItem = (Organization) mValues.get(position);

        holder.mContentView.setText(item.getFeedableType());
        final Context context = holder.itemView.getContext();

        if (item.getProfileImage() != null) {
            Picasso.with(context).load(item.getProfileImage().getUrl()).transform(new CircleTransform(true)).placeholder(R.drawable.ic_user_placeholder).into(holder.mProfilePictureView);
        }

//        if (item.getImages() != null && !item.getImages().isEmpty()) {
//            holder.mImageView.setVisibility(View.VISIBLE);
//            Picasso.with(context).load(item.getImages().get(0).getUrl()).into(holder.mImageView);
//        } else {
//            holder.mImageView.setVisibility(View.GONE);
//        }

        ((GalleryViewAdapter) holder.mRecyclerView.getAdapter()).setItems(item.getImages());

        holder.mNameView.setText(item.getName());

        String content = item.getAbout();
        if (!TextUtils.isEmpty(content)) {
            holder.mContentView.setText(content);
            holder.mContentView.setVisibility(View.VISIBLE);
        } else {
            holder.mContentView.setVisibility(View.GONE);
        }

        Location location = item.getLocations().get(0);
        if (location != null) {
            String city = location.getCity();
            String country = location.getCountry();
            String infos = country;
            if (city != null) {
                infos = city + ", " + infos;
            }
            holder.mInfosView.setText(infos);
        }

    }

    private void setupOpportunity(final OpportunityViewHolder holder, final int position) {
        Opportunity item = holder.mItem = (Opportunity) mValues.get(position);

        Context context = holder.itemView.getContext();

        Image profileImage = item.getOpportunitable().getProfileImage();
        if (profileImage != null) {
            Picasso.with(context).load(profileImage.getUrl()).transform(new CircleTransform(true)).placeholder(R.drawable.ic_user_placeholder).into(holder.mProfilePictureView);
        }

        ((GalleryViewAdapter) holder.mRecyclerView.getAdapter()).setItems(item.getImages());

        String headline = item.getTitle();
        if (!TextUtils.isEmpty(headline)) {
            holder.mTitleView.setText(headline);
            holder.mTitleView.setVisibility(View.VISIBLE);
        } else {
            holder.mTitleView.setVisibility(View.GONE);
        }

        holder.mInfosView.setText("Postado por " + item.getOpportunitable().getName());

        String content = item.getDescription();
        if (!TextUtils.isEmpty(content)) {
            holder.mContentView.setText(content);
            holder.mContentView.setVisibility(View.VISIBLE);
        } else {
            holder.mContentView.setVisibility(View.GONE);
        }

        if (!item.getVirtual()) {
            holder.mPlaceTextView.setText(item.getLocation().getName());
        } else {
            holder.mPlaceTextView.setText(R.string.virtual);
        }

        String time;
        if (!item.getOngoing()) {
            Date startTime = DateHelper.deserialize(item.getStartAt());
            if (item.getStartTimeSet())
                time = DateHelper.format(DateHelper.eventDatetimeFormat, startTime);
            else time = DateHelper.format(DateHelper.eventDateFormat, startTime);

            Date endTime = DateHelper.deserialize(item.getStartAt());
            if (item.getEndDateSet() && endTime != null) {
                if (item.getEndTimeSet())
                    time += " - " + DateHelper.format(DateHelper.eventDatetimeFormat, endTime);
                else time += " - " + DateHelper.format(DateHelper.eventDateFormat, endTime);
            }
        } else {
            time = "Flexível";
        }
        holder.mTimeTextView.setText(time);

        View.OnClickListener itemListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClicked(holder.mItem, position);
                }
            }
        };

        holder.mMoreButton.setOnClickListener(itemListener);
        holder.mTitleView.setOnClickListener(itemListener);

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mValues.get(position).getFeedableTypeAsEnum().getFeedItemAdapterType();
    }

    public class OrganizationHolder extends RecyclerView.ViewHolder {

        public final View mView;
        @BindView(R.id.profile_picture) ImageView mProfilePictureView;
        @BindView(R.id.name) TextView mNameView;
        @BindView(infos) TextView mInfosView;
        @BindView(R.id.content) TextView mContentView;
        @BindView(R.id.imagesRecyclerView) RecyclerView mRecyclerView;
        public Organization mItem;

        public OrganizationHolder(View view) {
            super(view);
            mView = view;

            ButterKnife.bind(this, view);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.HORIZONTAL));
            mRecyclerView.setAdapter(new GalleryViewAdapter(new ArrayList<Image>(), false, null));
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    public class OpportunityViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        @BindView(R.id.place) TextView mPlaceTextView;
        @BindView(R.id.time) TextView mTimeTextView;
        @BindView(R.id.profile_picture) ImageView mProfilePictureView;
        @BindView(R.id.title) TextView mTitleView;
        @BindView(infos) TextView mInfosView;
        @BindView(R.id.content) TextView mContentView;
        @BindView(R.id.imagesRecyclerView) RecyclerView mRecyclerView;
        @BindView(R.id.more_button) Button mMoreButton;
        public Opportunity mItem;

        public OpportunityViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);

            mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.HORIZONTAL));
            mRecyclerView.setAdapter(new GalleryViewAdapter(new ArrayList<Image>(), false, null));

        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
