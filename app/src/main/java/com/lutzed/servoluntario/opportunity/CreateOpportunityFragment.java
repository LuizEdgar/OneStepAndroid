package com.lutzed.servoluntario.opportunity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.models.Contact;
import com.lutzed.servoluntario.models.Opportunity;
import com.lutzed.servoluntario.models.SelectableItem;
import com.lutzed.servoluntario.models.Skill;
import com.lutzed.servoluntario.selection.ItemsSelectionActivity;
import com.lutzed.servoluntario.util.Snippets;
import com.satsuware.usefulviews.LabelledSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
public class CreateOpportunityFragment extends Fragment implements CreateOpportunityContract.View {

    @BindView(R.id.title) EditText mTitleView;
    @BindView(R.id.description) EditText mDescriptionView;
    @BindView(R.id.volunteersNumber) EditText mVolunteersNumberView;
    @BindView(R.id.timeCommitment) EditText mTimeCommitmentView;
    @BindView(R.id.othersRequirements) EditText mOthersRequirementsView;
    @BindView(R.id.tags) EditText mTagsView;
    @BindView(R.id.contactSpinner) LabelledSpinner mContactSpinner;
    @BindView(R.id.causesRecyclerView) RecyclerView mCausesRecyclerView;
    @BindView(R.id.skillsRecyclerView) RecyclerView mSkillsRecyclerView;

    @BindView(R.id.progress) View mProgressView;
    @BindView(R.id.create_opportunity_form) View mLoginFormView;

    private CreateOpportunityContract.Presenter mPresenter;

    public static CreateOpportunityFragment newInstance() {
        return new CreateOpportunityFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.start();
    }

    @Override
    public void setPresenter(CreateOpportunityContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_create_opportunity, container, false);
        ButterKnife.bind(this, root);

        mContactSpinner.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
            @Override
            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                if (position == adapterView.getAdapter().getCount() - 1) {
                    mPresenter.createNewContact();
                }
            }

            @Override
            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

            }
        });

        LinearLayoutManager causesLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mCausesRecyclerView.setLayoutManager(causesLayoutManager);

        LinearLayoutManager skillsLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mSkillsRecyclerView.setLayoutManager(skillsLayoutManager);

        ArrayList<SelectableItem> baseAddItem = new ArrayList<>();
        SelectableItem item = new Skill();
        item.setName("Add");
        baseAddItem.add(item);
        mCausesRecyclerView.setAdapter(new ItemsOpportunityAdapter(baseAddItem, new ItemsOpportunityAdapter.OnAdapterListener() {
            @Override
            public void onAdapterInteraction(SelectableItem mItem, int adapterPosition) {
                if (adapterPosition == mCausesRecyclerView.getAdapter().getItemCount() - 1) {
                    mPresenter.addNewCause();
                }
            }
        }));
        mSkillsRecyclerView.setAdapter(new ItemsOpportunityAdapter(new ArrayList<>(baseAddItem), new ItemsOpportunityAdapter.OnAdapterListener() {
            @Override
            public void onAdapterInteraction(SelectableItem mItem, int adapterPosition) {
                if (adapterPosition == mSkillsRecyclerView.getAdapter().getItemCount() - 1) {
                    mPresenter.addNewSkill();
                }
            }
        }));

        return root;
    }

    @OnClick(R.id.save_button)
    void onSignUpClicked() {
        String title = mTitleView.getText().toString().trim();
        String description = mDescriptionView.getText().toString().trim();

        int selectedItemPosition = mContactSpinner.getSpinner().getSelectedItemPosition();
        int spinnerCount = mContactSpinner.getSpinner().getCount();
        Contact contact = null;
        if (selectedItemPosition > 0 && selectedItemPosition < spinnerCount - 1){
            contact = (Contact) mContactSpinner.getSpinner().getSelectedItem();
        }

        List<Long> skillIds = ((ItemsOpportunityAdapter) mSkillsRecyclerView.getAdapter()).getItemsIds();
        List<Long> causeIds = ((ItemsOpportunityAdapter) mCausesRecyclerView.getAdapter()).getItemsIds();
        String volunteersNumber = mVolunteersNumberView.getText().toString().trim();
        String timeCommitment = mTimeCommitmentView.getText().toString().trim();
        String othersRequirements = mOthersRequirementsView.getText().toString().trim();
        String tags = mTagsView.getText().toString().trim();

        mPresenter.attemptCreateOpportunity(title, description, contact, skillIds, causeIds, volunteersNumber, timeCommitment, othersRequirements, tags);
    }

    public void resetErrors() {
        mTitleView.setError(null);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void setLoadingIndicator(final boolean active) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(active ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    active ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(active ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(active ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    active ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(active ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(active ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(active ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void setContacts(List<Contact> contacts, List<Long> selectedContactsIds) {
        if (contacts == null) contacts = new ArrayList<>();

        contacts.add(0, new Contact("Select…"));
        contacts.add(new Contact("Create new…"));

        mContactSpinner.setItemsArray(contacts);
    }

    @Override
    public void swapCauses(List<? extends SelectableItem> causes) {
        ItemsOpportunityAdapter adapter = (ItemsOpportunityAdapter) mCausesRecyclerView.getAdapter();
        adapter.addDataAtEnd(causes, true, true);
    }

    @Override
    public void swapSkills(List<? extends SelectableItem> skills) {
        ItemsOpportunityAdapter adapter = (ItemsOpportunityAdapter) mSkillsRecyclerView.getAdapter();
        adapter.addDataAtEnd(skills, true, true);
    }

    @Override
    public void setOpportunity(Opportunity opportunity) {

    }

    @Override
    public void showCreateNewContact() {
        Toast.makeText(getContext(), "Create new contact", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAddNewCause(List<Long> selectedCauses) {
        Intent intent = new Intent(getContext(), ItemsSelectionActivity.class);
        intent.putExtra(ItemsSelectionActivity.EXTRA_ITEM_SELECTION_KIND, ItemsSelectionActivity.Kind.CAUSE);
        intent.putExtra(ItemsSelectionActivity.EXTRA_ITEM_SELECTION_MODE, ItemsSelectionActivity.Mode.MULTIPLE_SELECTION);
        intent.putExtra(ItemsSelectionActivity.EXTRA_SHOW_BACK, true);
        if (selectedCauses != null && !selectedCauses.isEmpty()) {
            intent.putExtra(ItemsSelectionActivity.EXTRA_ITEM_SELECTION_IDS_CHECK, Snippets.toArray(selectedCauses));
        }
        startActivityForResult(intent, ItemsSelectionActivity.EXTRA_SELECTION_REQUEST_CODE);
    }

    @Override
    public void showAddNewSkill(List<Long> selectedSkills) {
        Intent intent = new Intent(getContext(), ItemsSelectionActivity.class);
        intent.putExtra(ItemsSelectionActivity.EXTRA_ITEM_SELECTION_KIND, ItemsSelectionActivity.Kind.SKILL);
        intent.putExtra(ItemsSelectionActivity.EXTRA_ITEM_SELECTION_MODE, ItemsSelectionActivity.Mode.MULTIPLE_SELECTION);
        intent.putExtra(ItemsSelectionActivity.EXTRA_SHOW_BACK, true);
        if (selectedSkills != null && !selectedSkills.isEmpty()) {
            intent.putExtra(ItemsSelectionActivity.EXTRA_ITEM_SELECTION_IDS_CHECK, Snippets.toArray(selectedSkills));
        }
        startActivityForResult(intent, ItemsSelectionActivity.EXTRA_SELECTION_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ItemsSelectionActivity.EXTRA_SELECTION_REQUEST_CODE) {
                mPresenter.onNewSelectableItemsAdded(data.<SelectableItem>getParcelableArrayListExtra(ItemsSelectionActivity.EXTRA_ITEMS_SELECTED));
            }
        }
    }

    @Override
    public void setTitle(String title) {
        mTitleView.setText(title);
    }

    @Override
    public void setDescription(String description) {
        mDescriptionView.setText(description);
    }

    @Override
    public void setVolunteersNumber(Integer volunteersNumber) {
        mVolunteersNumberView.setText(String.valueOf(volunteersNumber));
    }

    @Override
    public void setTimeCommitment(String timeCommitment) {
        mTimeCommitmentView.setText(timeCommitment);
    }

    @Override
    public void setOthersRequirements(String othersRequirements) {
        mOthersRequirementsView.setText(othersRequirements);
    }

    @Override
    public void setTags(String tags) {
        mTagsView.setText(tags);
    }
}

