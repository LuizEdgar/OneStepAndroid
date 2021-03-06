package com.lutzed.servoluntario.opportunities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.adapters.GalleryViewAdapter;
import com.lutzed.servoluntario.adapters.OpportunitiesItemsAdapter;
import com.lutzed.servoluntario.models.Contact;
import com.lutzed.servoluntario.models.Image;
import com.lutzed.servoluntario.models.Opportunity;
import com.lutzed.servoluntario.models.SelectableItem;
import com.lutzed.servoluntario.util.DataView;
import com.lutzed.servoluntario.util.Snippets;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A login screen that offers login via email/password.
 */
public class OpportunityFragment extends Fragment implements OpportunityContract.View {
    private static final String BUNDLE_CAN_EDIT = "bundle_can_edit";
    private static final int REQUEST_EDIT = 290;

    @BindView(R.id.title) TextView mTitleView;
    @BindView(R.id.description) TextView mDescriptionView;
    @BindView(R.id.creator) TextView mCreatorTextView;
    @BindView(R.id.volunteersNumber) DataView mVolunteersNumberView;
    @BindView(R.id.timeCommitment) DataView mTimeCommitmentView;
    @BindView(R.id.othersRequirements) DataView mOthersRequirementsView;
    @BindView(R.id.tags) DataView mTagsView;
    @BindView(R.id.contact) DataView mContactView;
    @BindView(R.id.location) DataView mLocationView;
    @BindView(R.id.time) DataView mTimeView;
    @BindView(R.id.causesRecyclerView) RecyclerView mCausesRecyclerView;
    @BindView(R.id.skillsRecyclerView) RecyclerView mSkillsRecyclerView;
    @BindView(R.id.imagesRecyclerView) RecyclerView mGalleryRecyclerView;
    @BindView(R.id.othersWrapper) View mOthersWrapper;
    @BindView(R.id.causesWrapper) View mCausesWrapper;
    @BindView(R.id.skillsWrapper) View mSkillsWrapper;
    @BindView(R.id.galleryWrapper) View mGalleryWrapper;

    private OpportunityContract.Presenter mPresenter;

    private ProgressDialog mLoadingProgress;

    private boolean mCanEdit;

    public static OpportunityFragment newInstance(boolean canEdit) {
        OpportunityFragment fragment = new OpportunityFragment();
        Bundle args = new Bundle();
        args.putBoolean(BUNDLE_CAN_EDIT, canEdit);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mCanEdit = getArguments().getBoolean(BUNDLE_CAN_EDIT);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void setPresenter(OpportunityContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_opportunity, container, false);
        ButterKnife.bind(this, root);

        LinearLayoutManager causesLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mCausesRecyclerView.setLayoutManager(causesLayoutManager);

        LinearLayoutManager skillsLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mSkillsRecyclerView.setLayoutManager(skillsLayoutManager);

        LinearLayoutManager imagesLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mGalleryRecyclerView.setLayoutManager(imagesLayoutManager);

        mCausesRecyclerView.setAdapter(new OpportunitiesItemsAdapter(new ArrayList<SelectableItem>(), new OpportunitiesItemsAdapter.OnAdapterListener() {
            @Override
            public void onAdapterInteraction(SelectableItem mItem, int adapterPosition) {
            }
        }));
        mSkillsRecyclerView.setAdapter(new OpportunitiesItemsAdapter(new ArrayList<SelectableItem>(), new OpportunitiesItemsAdapter.OnAdapterListener() {
            @Override
            public void onAdapterInteraction(SelectableItem mItem, int adapterPosition) {
            }
        }));
        mGalleryRecyclerView.setAdapter(new GalleryViewAdapter(new ArrayList<Image>(), false, new GalleryViewAdapter.OnGalleryInteractionListener() {
            @Override
            public void onImageClicked(List<Image> values, Image item, int position) {
            }

            @Override
            public void onPromptDeleteImage(Image image, int position) {
            }
        }));

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.opportunity_options, menu);

        if (mCanEdit) {
            menu.findItem(R.id.action_edit).setVisible(true);
            menu.findItem(R.id.action_delete).setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            mPresenter.onEditOpportunityClicked();
            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            mPresenter.onDeleteOpportunityClicked();
            return true;
        } else if (item.getItemId() == R.id.action_share) {
            mPresenter.onShareOpportunityClicked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        if (active) {
            if (mLoadingProgress == null) {
                mLoadingProgress = Snippets.createProgressDialog(getContext(), R.string.loading_progress);
                mLoadingProgress.setCancelable(false);
            }
            if (!mLoadingProgress.isShowing()) {
                mLoadingProgress.show();
            }
        } else {
            if (mLoadingProgress != null && mLoadingProgress.isShowing()) {
                mLoadingProgress.dismiss();
                mLoadingProgress = null;
            }
        }
    }

    @Override
    public void addCauses(List<? extends SelectableItem> causes) {
        if (causes == null || causes.isEmpty()) {
            mCausesRecyclerView.setVisibility(View.GONE);
            mCausesWrapper.setVisibility(View.GONE);
        } else {
            OpportunitiesItemsAdapter adapter = (OpportunitiesItemsAdapter) mCausesRecyclerView.getAdapter();
            adapter.addItems(causes);
            mCausesRecyclerView.setVisibility(View.VISIBLE);
            mCausesWrapper.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void addSkills(List<? extends SelectableItem> skills) {
        if (skills == null || skills.isEmpty()) {
            mSkillsRecyclerView.setVisibility(View.GONE);
            mSkillsWrapper.setVisibility(View.GONE);
        } else {
            OpportunitiesItemsAdapter adapter = (OpportunitiesItemsAdapter) mSkillsRecyclerView.getAdapter();
            adapter.addItems(skills);
            mSkillsRecyclerView.setVisibility(View.VISIBLE);
            mSkillsWrapper.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void addImages(List<Image> images) {
        if (images == null || images.isEmpty()) {
            mGalleryRecyclerView.setVisibility(View.GONE);
            mGalleryWrapper.setVisibility(View.GONE);
        } else {
            GalleryViewAdapter adapter = (GalleryViewAdapter) mGalleryRecyclerView.getAdapter();
            adapter.addItems(images);
            mGalleryRecyclerView.setVisibility(View.VISIBLE);
            mGalleryWrapper.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setTitle(String title) {
        mTitleView.setText(title);
    }

    @Override
    public void setDescription(String description) {
        if (TextUtils.isEmpty(description)) {
            mDescriptionView.setVisibility(View.GONE);
        } else {
            mDescriptionView.setText(description);
        }
    }

    @Override
    public void setVolunteersNumber(Integer volunteersNumber) {
        if (volunteersNumber == null) {
            mVolunteersNumberView.setVisibility(View.GONE);
        } else {
            mOthersWrapper.setVisibility(View.VISIBLE);
            mVolunteersNumberView.setData(String.valueOf(volunteersNumber));
        }

    }

    @Override
    public void setTimeCommitment(String timeCommitment) {
        if (TextUtils.isEmpty(timeCommitment)) {
            mTimeCommitmentView.setVisibility(View.GONE);
        } else {
            mOthersWrapper.setVisibility(View.VISIBLE);
            mTimeCommitmentView.setData(timeCommitment);
        }
    }

    @Override
    public void setOthersRequirements(String othersRequirements) {
        if (TextUtils.isEmpty(othersRequirements)) {
            mOthersRequirementsView.setVisibility(View.GONE);
        } else {
            mOthersWrapper.setVisibility(View.VISIBLE);
            mOthersRequirementsView.setData(othersRequirements);
        }
    }

    @Override
    public void setTags(String tags) {
        if (TextUtils.isEmpty(tags)) {
            mTagsView.setVisibility(View.GONE);
        } else {
            mOthersWrapper.setVisibility(View.VISIBLE);
            mTagsView.setData(tags);
        }
    }

    @Override
    public void setLocation(String location) {
        if (TextUtils.isEmpty(location)) {
            mLocationView.setVisibility(View.GONE);
        } else {
            mLocationView.setData(location);
        }
    }

    @Override
    public void setTime(String date) {
        if (TextUtils.isEmpty(date)) {
            mTimeView.setVisibility(View.GONE);
        } else {
            mTimeView.setData(date);
        }
    }

    @Override
    public void setContact(Contact contact) {
        if (contact == null) {
            mContactView.setVisibility(View.GONE);
        } else {
            mContactView.setData(contact.toString());
        }
    }

    @Override
    public void setCreator(String creator) {
        mCreatorTextView.setText(creator);
    }

    @Override
    public void setLocationToVirtual() {
        mLocationView.setData(getString(R.string.virtual));
    }

    @Override
    public void setTimeToOngoing() {
        mTimeView.setData(getString(R.string.ongoing));
    }

    @Override
    public void showEditOpportunity(Opportunity opportunity) {
        Intent intent = new Intent(getContext(), EditOpportunityActivity.class);
        intent.putExtra(EditOpportunityActivity.EXTRA_OPPORTUNITY, opportunity);
        startActivity(intent);
    }

    @Override
    public void showShareOpportunity(Opportunity opportunity) {

    }

    @Override
    public void showDeleteOpportunity(Opportunity opportunity) {

    }
}

