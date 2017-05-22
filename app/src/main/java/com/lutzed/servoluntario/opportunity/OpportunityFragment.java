package com.lutzed.servoluntario.opportunity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.dialogs.ContactDialogFragment;
import com.lutzed.servoluntario.models.Contact;
import com.lutzed.servoluntario.models.SelectableItem;
import com.lutzed.servoluntario.models.Skill;
import com.lutzed.servoluntario.selection.ItemsSelectionActivity;
import com.lutzed.servoluntario.util.Snippets;
import com.lutzed.servoluntario.util.TextInputLayoutTextWatcher;
import com.satsuware.usefulviews.LabelledSpinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.lutzed.servoluntario.opportunity.OpportunityFragment.TimeType.DATED;
import static com.lutzed.servoluntario.opportunity.OpportunityFragment.TimeType.ONGOING;

/**
 * A login screen that offers login via email/password.
 */
public class OpportunityFragment extends Fragment implements OpportunityContract.View {

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 182;
    @BindView(R.id.title) EditText mTitleView;
    @BindView(R.id.description) EditText mDescriptionView;
    @BindView(R.id.volunteersNumber) EditText mVolunteersNumberView;
    @BindView(R.id.timeCommitment) EditText mTimeCommitmentView;
    @BindView(R.id.othersRequirements) EditText mOthersRequirementsView;
    @BindView(R.id.tags) EditText mTagsView;
    @BindView(R.id.contactSpinner) LabelledSpinner mContactSpinner;
    @BindView(R.id.causesRecyclerView) RecyclerView mCausesRecyclerView;
    @BindView(R.id.skillsRecyclerView) RecyclerView mSkillsRecyclerView;
    @BindView(R.id.locationTypeGroup) RadioGroup mLocationTypeGroup;
    @BindView(R.id.isLocation) RadioButton mIsLocationRadioButton;
    @BindView(R.id.isOngoing) RadioButton mIsOngoingRadioButton;
    @BindView(R.id.location) EditText mLocationView;
    @BindView(R.id.progress) View mProgressView;
    @BindView(R.id.create_opportunity_form) View mLoginFormView;
    @BindView(R.id.form) ScrollView mScrollView;
    @BindView(R.id.timeTypeGroup) RadioGroup mTimeTypeGroup;
    @BindView(R.id.isDated) RadioButton mIsDatedRadioButton;
    @BindView(R.id.isVirtual) RadioButton mIsVirtualRadioButton;
    @BindView(R.id.dateWrapper) View mTimeWrapperView;
    @BindView(R.id.startDateAt) EditText mStartDateAtView;
    @BindView(R.id.startTimeAt) EditText mStartTimeAtView;
    @BindView(R.id.endDateAt) EditText mEndDateAtView;
    @BindView(R.id.endTimeAt) EditText mEndTimeAtView;
    @BindView(R.id.startDateAtInputLayout) TextInputLayout mStartDateAtInputLayout;
    @BindView(R.id.startTimeAtInputLayout) TextInputLayout mStartTimeAtInputLayout;
    @BindView(R.id.endDateAtInputLayout) TextInputLayout mEndDateAtInputLayout;
    @BindView(R.id.endTimeAtInputLayout) TextInputLayout mEndTimeAtInputLayout;
    @BindView(R.id.titleInputLayout) TextInputLayout mTitleInputLayout;
    @BindView(R.id.descriptionInputLayout) TextInputLayout mDescriptionInputLayout;
    @BindView(R.id.volunteersNumberInputLayout) TextInputLayout mVolunteersNumberInputLayout;
    @BindView(R.id.timeCommitmentInputLayout) TextInputLayout mTimeCommitmentInputLayout;
    @BindView(R.id.othersRequirementsInputLayout) TextInputLayout mOthersRequirementsInputLayout;
    @BindView(R.id.tagsInputLayout) TextInputLayout mTagsInputLayout;
    @BindView(R.id.locationInputLayout) TextInputLayout mLocationInputLayout;
    @BindView(R.id.causesWrapper) View mCausesWrapper;
    @BindView(R.id.skillsWrapper) View mSkillsWrapper;
    @BindView(R.id.causesError) TextView mCausesErrorView;
    @BindView(R.id.skillsError) TextView mSkillsErrorView;

    private OpportunityContract.Presenter mPresenter;
    private int mCurrentContactSpinnerSelectedPosition;
    private ColorStateList mDefaultEditTextColor;

    public enum LocationType {
        LOCATION, VIRTUAL;
    }

    public enum TimeType {
        DATED, ONGOING;
    }

    public static OpportunityFragment newInstance() {
        return new OpportunityFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.save, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            onSaveClicked();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setPresenter(OpportunityContract.Presenter presenter) {
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
                } else {
                    mCurrentContactSpinnerSelectedPosition = position;
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
        mCausesRecyclerView.setAdapter(new OpportunityItemsAdapter(baseAddItem, new OpportunityItemsAdapter.OnAdapterListener() {
            @Override
            public void onAdapterInteraction(SelectableItem mItem, int adapterPosition) {
                if (adapterPosition == mCausesRecyclerView.getAdapter().getItemCount() - 1) {
                    mPresenter.addNewCause(((OpportunityItemsAdapter) mCausesRecyclerView.getAdapter()).getItemsIds());
                }
            }
        }));
        mSkillsRecyclerView.setAdapter(new OpportunityItemsAdapter(new ArrayList<>(baseAddItem), new OpportunityItemsAdapter.OnAdapterListener() {
            @Override
            public void onAdapterInteraction(SelectableItem mItem, int adapterPosition) {
                if (adapterPosition == mSkillsRecyclerView.getAdapter().getItemCount() - 1) {
                    mPresenter.addNewSkill(((OpportunityItemsAdapter) mSkillsRecyclerView.getAdapter()).getItemsIds());
                }
            }
        }));

        ActionMode.Callback noCopyPasteCallback = new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        };

        mLocationView.setFocusable(false);
        mLocationView.setCustomSelectionActionModeCallback(noCopyPasteCallback);
        mLocationView.setClickable(true);

        mLocationTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.isLocation:
                        mPresenter.onLocationTypeChanged(LocationType.LOCATION);
                        break;
                    case R.id.isVirtual:
                        mPresenter.onLocationTypeChanged(LocationType.VIRTUAL);
                        break;
                }
            }
        });

        mStartDateAtView.setFocusable(false);
        mStartDateAtView.setCustomSelectionActionModeCallback(noCopyPasteCallback);
        mStartDateAtView.setClickable(true);
        mStartTimeAtView.setFocusable(false);
        mStartTimeAtView.setCustomSelectionActionModeCallback(noCopyPasteCallback);
        mStartTimeAtView.setClickable(true);
        mEndDateAtView.setFocusable(false);
        mEndDateAtView.setCustomSelectionActionModeCallback(noCopyPasteCallback);
        mEndDateAtView.setClickable(true);
        mEndTimeAtView.setFocusable(false);
        mEndTimeAtView.setCustomSelectionActionModeCallback(noCopyPasteCallback);
        mEndTimeAtView.setClickable(true);

        mTimeTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.isDated:
                        mPresenter.onTimeTypeChanged(DATED);
                        break;
                    case R.id.isOngoing:
                        mPresenter.onTimeTypeChanged(ONGOING);
                        break;
                }
            }
        });

        mTitleView.addTextChangedListener(new TextInputLayoutTextWatcher(mTitleView, mTitleInputLayout));
        mDescriptionView.addTextChangedListener(new TextInputLayoutTextWatcher(mDescriptionView, mDescriptionInputLayout));
        mVolunteersNumberView.addTextChangedListener(new TextInputLayoutTextWatcher(mVolunteersNumberView, mVolunteersNumberInputLayout));
        mTimeCommitmentView.addTextChangedListener(new TextInputLayoutTextWatcher(mTimeCommitmentView, mTimeCommitmentInputLayout));
        mOthersRequirementsView.addTextChangedListener(new TextInputLayoutTextWatcher(mOthersRequirementsView, mTitleInputLayout));
        mTagsView.addTextChangedListener(new TextInputLayoutTextWatcher(mTagsView, mTagsInputLayout));
        mStartDateAtView.addTextChangedListener(new TextInputLayoutTextWatcher(mStartDateAtView, mStartDateAtInputLayout));
        mEndDateAtView.addTextChangedListener(new TextInputLayoutTextWatcher(mEndDateAtView, mEndDateAtInputLayout));
        mLocationView.addTextChangedListener(new TextInputLayoutTextWatcher(mLocationView, mLocationInputLayout));

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDefaultEditTextColor = mTitleView.getTextColors(); //save original colors

        mPresenter.start();
    }

    void onSaveClicked() {
        String title = mTitleView.getText().toString().trim();
        String description = mDescriptionView.getText().toString().trim();

        int selectedItemPosition = mContactSpinner.getSpinner().getSelectedItemPosition();
        int spinnerCount = mContactSpinner.getSpinner().getCount();
        Contact contact = null;
        if (selectedItemPosition > 0 && selectedItemPosition < spinnerCount - 1) {
            contact = (Contact) mContactSpinner.getSpinner().getSelectedItem();
            contact.setId(null);
        }

        List<Long> skillIds = ((OpportunityItemsAdapter) mSkillsRecyclerView.getAdapter()).getItemsIds();
        List<Long> causeIds = ((OpportunityItemsAdapter) mCausesRecyclerView.getAdapter()).getItemsIds();
        String volunteersNumber = mVolunteersNumberView.getText().toString().trim();
        String timeCommitment = mTimeCommitmentView.getText().toString().trim();
        String othersRequirements = mOthersRequirementsView.getText().toString().trim();
        String tags = mTagsView.getText().toString().trim();

        mPresenter.attemptCreateOpportunity(title, description, contact, skillIds, causeIds, volunteersNumber, timeCommitment, othersRequirements, tags);
    }

    public void resetErrors() {
//        mTitleView.setError(null);
//        mContactSpinner.setDefaultErrorText("");
//        mContactSpinner.getErrorLabel().setError("");
//        mContactSpinner.setDefaultErrorEnabled(false);
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
    public void setContacts(List<Contact> contacts, Long selectedContactId) {
        ArrayList<Contact> viewContacts = new ArrayList<>();
        if (contacts != null) viewContacts.addAll(contacts);

        viewContacts.add(0, new Contact("Select…"));
        viewContacts.add(new Contact("Create new…"));

        mContactSpinner.setItemsArray(viewContacts);
        if (selectedContactId != null) {
            mCurrentContactSpinnerSelectedPosition = viewContacts.indexOf(new Contact(selectedContactId));
            mContactSpinner.setSelection(mCurrentContactSpinnerSelectedPosition);
        }
    }

    @Override
    public void addUniqueCauses(List<? extends SelectableItem> causes, List<? extends SelectableItem> causesToRemove) {
        OpportunityItemsAdapter adapter = (OpportunityItemsAdapter) mCausesRecyclerView.getAdapter();
        adapter.addAndRemoveItems(causes, causesToRemove);
        mCausesRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
        mCausesErrorView.setVisibility(View.GONE);
    }

    @Override
    public void addUniqueSkills(List<? extends SelectableItem> skills, List<? extends SelectableItem> skillsToRemove) {
        OpportunityItemsAdapter adapter = (OpportunityItemsAdapter) mSkillsRecyclerView.getAdapter();
        adapter.addAndRemoveItems(skills, skillsToRemove);
        mSkillsRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
        mSkillsErrorView.setVisibility(View.GONE);
    }

    @Override
    public void showCreateNewContact() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("contactDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        ContactDialogFragment.newInstance(new ContactDialogFragment.Listener() {
            @Override
            public void onSave(String name, String phone, String email) {
                mPresenter.addNewContact(name, phone, email);
            }

            @Override
            public void onCancel() {
                mContactSpinner.setSelection(mCurrentContactSpinnerSelectedPosition);
            }
        }).show(ft, "contactDialog");
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
        if (requestCode == ItemsSelectionActivity.EXTRA_SELECTION_REQUEST_CODE) {
            clearAllFocus();
            if (resultCode == RESULT_OK) {
                mPresenter.onNewItemsSelection(data.<SelectableItem>getParcelableArrayListExtra(ItemsSelectionActivity.EXTRA_ITEMS_SELECTED), data.<SelectableItem>getParcelableArrayListExtra(ItemsSelectionActivity.EXTRA_ITEMS_NOT_SELECTED));
            }
        } else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                clearAllFocus();
                mPresenter.onNewPlaceSelected(PlaceAutocomplete.getPlace(getContext(), data));
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getContext(), data);
                // TODO: Handle the error.
                Log.i("Tag", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {

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

    @Override
    public void setSavingIndicator(boolean active) {
        if (active) Toast.makeText(getContext(), "Saving...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void close() {
        getActivity().finish();
    }

    @OnClick(R.id.location)
    public void onLocationClicked() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(getActivity());
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    public void setLocation(String location) {
        mLocationView.setText(location);
    }

    @OnClick(R.id.startDateAt)
    public void onStartDateAtClicked() {
        mPresenter.startDateClicked();
    }

    @Override
    public void showStartDatePicker(int year, int month, int dayOfMonth) {
        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mPresenter.onStartDateSelected(year, month, dayOfMonth);
            }
        }, year, month, dayOfMonth).show();
    }

    @Override
    public void showEndDatePicker(int year, int month, int dayOfMonth) {
        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mPresenter.onEndDateSelected(year, month, dayOfMonth);
            }
        }, year, month, dayOfMonth).show();
    }

    @Override
    public void showStartTimePicker(int hourOfDay, int minute, boolean is24HourView) {
        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mPresenter.onStartTimeSelected(hourOfDay, minute);
            }
        }, hourOfDay, minute, is24HourView).show();
    }

    @Override
    public void showEndTimePicker(int hourOfDay, int minute, boolean is24HourView) {
        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mPresenter.onEndTimeSelected(hourOfDay, minute);
            }
        }, hourOfDay, minute, is24HourView).show();
    }

    @OnClick(R.id.endDateAt)
    public void onEndDateAtClicked() {
        mPresenter.endDateClicked();
    }

    @OnClick(R.id.startTimeAt)
    public void onStartTimeAtClicked() {
        mPresenter.startTimeClicked();
    }

    @OnClick(R.id.endTimeAt)
    public void onEndTimeAtClicked() {
        mPresenter.endTimeClicked();
    }

    @Override
    public void setShowTimeGroup(boolean visible) {
        mTimeWrapperView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setShowLocationGroup(boolean visible) {
        mLocationView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setStartDate(String startDate) {
        mStartDateAtView.setText(startDate);
        mStartDateAtView.setTextColor(mDefaultEditTextColor);
    }

    @Override
    public void setEndDate(String endDate) {
        mEndDateAtView.setText(endDate);
        mEndDateAtView.setTextColor(mDefaultEditTextColor);
    }

    @Override
    public void setStartTime(String startTime) {
        mStartTimeAtView.setText(startTime);
        mStartTimeAtView.setTextColor(mDefaultEditTextColor);
    }

    @Override
    public void setEndTime(String endTime) {
        mEndTimeAtView.setText(endTime);
        mEndTimeAtView.setTextColor(mDefaultEditTextColor);
    }

    @Override
    public void setTimeGroupType(TimeType timeType) {
        mIsDatedRadioButton.setChecked(timeType == DATED);
        mIsOngoingRadioButton.setChecked(timeType == ONGOING);
    }

    @Override
    public void setLocationGroupType(LocationType locationType) {
        mIsLocationRadioButton.setChecked(locationType == LocationType.LOCATION);
        mIsVirtualRadioButton.setChecked(locationType == LocationType.VIRTUAL);
    }

    @Override
    public void showTitleRequiredError() {
        mTitleInputLayout.setError(getString(R.string.error_field_required));
    }

    @Override
    public void setTitleFocus() {
        mTitleView.requestFocus();
    }

    @Override
    public void showContactRequiredError() {
//        mContactSpinner.setDefaultErrorText(getString(R.string.error_field_required));
        mContactSpinner.getErrorLabel().setText(getString(R.string.error_field_required));
        mContactSpinner.setDefaultErrorEnabled(true);
    }

    @Override
    public void setFocusContactField() {
        mScrollView.smoothScrollTo(0, mContactSpinner.getTop());
    }

    @Override
    public void showSkillsMinimumRequiredError(int minSkillsRequired) {
        mSkillsErrorView.setText("Você deve escolher pelo menos " + minSkillsRequired + " habilidades");
        mSkillsErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setFocusSkills() {
        mScrollView.smoothScrollTo(0, mSkillsWrapper.getTop());
    }

    @Override
    public void showCausesMinimumRequiredError(int minCausesRequired) {
        mCausesErrorView.setText("Você deve escolher pelo menos " + minCausesRequired + " causas");
        mCausesErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setFocusCauses() {
        mScrollView.smoothScrollTo(0, mCausesWrapper.getTop());
    }

    @Override
    public void showLocationRequiredError() {
        clearAllFocus();
        mLocationInputLayout.setError(getString(R.string.error_field_required));
    }

    @Override
    public void setFocusLocation() {
        mScrollView.smoothScrollTo(0, mLocationTypeGroup.getTop());
    }

    @Override
    public void showEndBeforeStartError() {
        mEndDateAtInputLayout.setError(getString(R.string.error_end_before_start));
    }

    @Override
    public void showStartDateRequiredError() {
        mStartDateAtInputLayout.setError(getString(R.string.error_field_required));
    }

    @Override
    public void showEndDateRequiredError() {
        mEndDateAtInputLayout.setError(getString(R.string.error_field_required));
    }

    @Override
    public void setFocusTime() {
        mScrollView.smoothScrollTo(0, mTimeTypeGroup.getTop());
    }

    private void clearAllFocus() {
        mTitleView.clearFocus();
        mDescriptionView.clearFocus();
        mVolunteersNumberView.clearFocus();
        mTimeCommitmentView.clearFocus();
        mOthersRequirementsView.clearFocus();
        mTagsView.clearFocus();
        mLocationView.clearFocus();
        mStartDateAtView.clearFocus();
        mStartTimeAtView.clearFocus();
        mEndDateAtView.clearFocus();
        mEndTimeAtView.clearFocus();
    }

}

