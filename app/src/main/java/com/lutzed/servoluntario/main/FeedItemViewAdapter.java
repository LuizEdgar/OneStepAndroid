package com.lutzed.servoluntario.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.models.FeedItem;
import com.lutzed.servoluntario.models.Opportunity;
import com.lutzed.servoluntario.models.Organization;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
//        final Context context = holder.itemView.getContext();

//        if (item.get) {
//            Picasso.with(context).load(mUser.getProfileImage().getUrl()).transform(new CircleTransform(false)).into(holder.mProfilePictureView);
//        }
//
//        if (item.hasDownloadableImage()) {
//            holder.mImageView.setVisibility(View.VISIBLE);
//            Picasso.with(context).load(item.getImage().getUrl()).into(holder.mImageView);
//        } else {
//            holder.mImageView.setVisibility(View.GONE);
//        }
//
//        String headline = item.getHeadline();
//        if (!TextUtils.isEmpty(headline)) {
//            holder.mHeadlineView.setText(headline);
//            holder.mHeadlineView.setVisibility(View.VISIBLE);
//        } else {
//            holder.mHeadlineView.setVisibility(View.GONE);
//        }
//
//        String content = item.getContent();
//        if (!TextUtils.isEmpty(content)) {
//            holder.mContentView.setText(content);
//            holder.mContentView.setVisibility(View.VISIBLE);
//        } else {
//            holder.mContentView.setVisibility(View.GONE);
//        }
//
//        holder.mSubheadView.setText(DateHelper.format(DateHelper.postDatetimeFormat, item.getCreatedAt()));
//
//        holder.mShareButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mListener != null){
//                    mListener.onItemShare(holder.mItem);
//                }
//            }
//        });
//
//        if (mIsAdminMode) {
//            holder.mEditButton.setVisibility(View.VISIBLE);
//            holder.mEditButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (null != mListener) {
//                        mListener.onEditClicked(holder.mItem, holder.getAdapterPosition());
//                    }
//                }
//            });
//        } else {
//            holder.mEditButton.setVisibility(View.GONE);
//        }
    }

    private void setupOpportunity(final OpportunityViewHolder holder, int position) {
        Opportunity item = holder.mItem = (Opportunity) mValues.get(position);

        holder.mContentView.setText(item.getFeedableType());
//
//        Context context = holder.itemView.getContext();
//
//        if (item.hasDownloadableImage()) {
//            holder.mImageView.setVisibility(View.VISIBLE);
//            Picasso.with(context).load(item.getImage().getUrl()).into(holder.mImageView);
//        } else {
//            holder.mImageView.setVisibility(View.GONE);
//        }
//
//        String headline = item.getHeadline();
//        if (!TextUtils.isEmpty(headline)) {
//            holder.mHeadlineView.setText(headline);
//            holder.mHeadlineView.setVisibility(View.VISIBLE);
//        } else {
//            holder.mHeadlineView.setVisibility(View.GONE);
//        }
//
//        String content = item.getContent();
//        if (!TextUtils.isEmpty(content)) {
//            holder.mContentView.setText(content);
//            holder.mContentView.setVisibility(View.VISIBLE);
//        } else {
//            holder.mContentView.setVisibility(View.GONE);
//        }
//
//        holder.mPlaceTextView.setText(item.getPlaceTime().getPlace());
//
//        Date eventTime = DateHelper.deserialize(item.getPlaceTime().getTimeAt());
//        holder.mDayTextView.setText(DateHelper.format(DateHelper.dayFormat, eventTime));
//        holder.mMonthTextView.setText(DateHelper.format(DateHelper.monthFormat, eventTime));
//        holder.mTimeTextView.setText(DateHelper.format(DateHelper.eventDatetimeFormat, eventTime));
//
//        holder.mShareButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mListener != null){
//                    mListener.onItemShare(holder.mItem);
//                }
//            }
//        });
//
//        if (mIsAdminMode) {
//            holder.mEditButton.setVisibility(View.VISIBLE);
//            holder.mEditButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (null != mListener) {
//                        mListener.onEditClicked(holder.mItem, holder.getAdapterPosition());
//                    }
//                }
//            });
//        } else {
//            holder.mEditButton.setVisibility(View.GONE);
//        }
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
        //        @BindView(R.id.profile_picture) ImageView mProfilePictureView;
//        @BindView(R.id.name) TextView mNameView;
        @BindView(R.id.content) TextView mContentView;
//        @BindView(R.id.subhead) TextView mSubheadView;
//        @BindView(R.id.headline) TextView mHeadlineView;
//        @BindView(R.id.content) TextView mContentView;
//        @BindView(R.id.image) ImageView mImageView;
//        @BindView(R.id.share_button) ImageButton mShareButton;
//        @BindView(R.id.edit_button) ImageButton mEditButton;
        public Organization mItem;

        public OrganizationHolder(View view) {
            super(view);
            mView = view;

            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

    public class OpportunityViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        @BindView(R.id.content) TextView mContentView;
        //        @BindView(R.id.day) TextView mDayTextView;
//        @BindView(R.id.month) TextView mMonthTextView;
//        @BindView(R.id.place) TextView mPlaceTextView;
//        @BindView(R.id.time) TextView mTimeTextView;
//        @BindView(R.id.headline) TextView mHeadlineView;
//        @BindView(R.id.subhead) TextView mSubheadView;
//        @BindView(R.id.content) TextView mContentView;
//        @BindView(R.id.image) ImageView mImageView;
//        @BindView(R.id.share_button) Button mShareButton;
//        @BindView(R.id.edit_button) ImageButton mEditButton;
        public Opportunity mItem;

        public OpportunityViewHolder(View view) {
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
