package com.lutzed.servoluntario.user;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.adapters.GalleryViewAdapter;
import com.lutzed.servoluntario.adapters.OpportunitiesItemsAdapter;
import com.lutzed.servoluntario.dialogs.ContactDialogFragment;
import com.lutzed.servoluntario.main.MainFragmentInterface;
import com.lutzed.servoluntario.models.Contact;
import com.lutzed.servoluntario.models.Image;
import com.lutzed.servoluntario.models.SelectableItem;
import com.lutzed.servoluntario.models.Skill;
import com.lutzed.servoluntario.selection.ItemsSelectionActivity;
import com.lutzed.servoluntario.util.Constants;
import com.lutzed.servoluntario.util.FileAndPathHolder;
import com.lutzed.servoluntario.util.Snippets;
import com.lutzed.servoluntario.util.TextInputLayoutTextWatcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A login screen that offers login via email/password.
 */
public class EditUserFragment extends Fragment implements EditUserContract.View, MainFragmentInterface {

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 182;
    private static final int REQUEST_IMAGE_PICK = 126;
    private static final int REQUEST_IMAGE_CAPTURE = 862;
    private static final int REQUEST_STORAGE_PERMISSION = 545;

    @BindView(R.id.form) ScrollView mScrollView;
    @BindView(R.id.progress) View mProgressView;
    @BindView(R.id.edit_user_form) View mLoginFormView;

    @BindView(R.id.causesRecyclerView) RecyclerView mCausesRecyclerView;
    @BindView(R.id.skillsRecyclerView) RecyclerView mSkillsRecyclerView;
    @BindView(R.id.imagesRecyclerView) RecyclerView mGalleryRecyclerView;
    @BindView(R.id.causesError) TextView mCausesErrorView;
    @BindView(R.id.skillsError) TextView mSkillsErrorView;
    @BindView(R.id.imagesError) TextView mImagesErrorView;
    @BindView(R.id.causesWrapper) View mCausesWrapperView;
    @BindView(R.id.skillsWrapper) View mSkillsWrapperView;
    @BindView(R.id.imagesWrapper) View mImagesWrapperView;

    @BindView(R.id.name) EditText mNameView;
    @BindView(R.id.size) EditText mSizeView;
    @BindView(R.id.occupation) EditText mOccupationView;
    @BindView(R.id.cnpj) EditText mCnpjView;
    @BindView(R.id.site) EditText mSiteView;
    @BindView(R.id.mission) EditText mMissionView;
    @BindView(R.id.about) EditText mAboutView;
    @BindView(R.id.location) EditText mLocationView;
    @BindView(R.id.establishedAt) EditText mEstablishedAtView;
    @BindView(R.id.birthAt) EditText mBirthAtView;

    @BindView(R.id.locationInputLayout) TextInputLayout mLocationInputLayout;

    private EditUserContract.Presenter mPresenter;
    private int mCurrentContactSpinnerSelectedPosition;
    private ColorStateList mDefaultEditTextColor;
    private String mCurrentPath;

    public static EditUserFragment newInstance() {
        return new EditUserFragment();
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
    public void setPresenter(EditUserContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_edit_user, container, false);
        ButterKnife.bind(this, root);

        LinearLayoutManager causesLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mCausesRecyclerView.setLayoutManager(causesLayoutManager);

        LinearLayoutManager skillsLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mSkillsRecyclerView.setLayoutManager(skillsLayoutManager);

        LinearLayoutManager imagesLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mGalleryRecyclerView.setLayoutManager(imagesLayoutManager);

        ArrayList<SelectableItem> baseAddItem = new ArrayList<>();
        SelectableItem item = new Skill();
        item.setName("Add");
        baseAddItem.add(item);
        mCausesRecyclerView.setAdapter(new OpportunitiesItemsAdapter(baseAddItem, new OpportunitiesItemsAdapter.OnAdapterListener() {
            @Override
            public void onAdapterInteraction(SelectableItem mItem, int adapterPosition) {
                if (adapterPosition == mCausesRecyclerView.getAdapter().getItemCount() - 1) {
                    mPresenter.addNewCause(((OpportunitiesItemsAdapter) mCausesRecyclerView.getAdapter()).getItemsIds());
                }
            }
        }));
        mSkillsRecyclerView.setAdapter(new OpportunitiesItemsAdapter(new ArrayList<>(baseAddItem), new OpportunitiesItemsAdapter.OnAdapterListener() {
            @Override
            public void onAdapterInteraction(SelectableItem mItem, int adapterPosition) {
                if (adapterPosition == mSkillsRecyclerView.getAdapter().getItemCount() - 1) {
                    mPresenter.addNewSkill(((OpportunitiesItemsAdapter) mSkillsRecyclerView.getAdapter()).getItemsIds());
                }
            }
        }));
        ArrayList<Image> baseImageAddItem = new ArrayList<>();
        Image image = new Image();
        image.setAddPlaceholder(true);
        baseImageAddItem.add(image);
        mGalleryRecyclerView.setAdapter(new GalleryViewAdapter(baseImageAddItem, true, new GalleryViewAdapter.OnGalleryInteractionListener() {
            @Override
            public void onImageClicked(List<Image> values, Image item, int position) {
                if (position == mGalleryRecyclerView.getAdapter().getItemCount() - 1) {
                    mPresenter.addNewImage();
                }
            }

            @Override
            public void onPromptDeleteImage(Image image, int position) {
                mPresenter.removeImage(image, position);
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

        mEstablishedAtView.setFocusable(false);
        mEstablishedAtView.setCustomSelectionActionModeCallback(noCopyPasteCallback);
        mBirthAtView.setClickable(true);
        mBirthAtView.setFocusable(false);

        mLocationView.addTextChangedListener(new TextInputLayoutTextWatcher(mLocationView, mLocationInputLayout));

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDefaultEditTextColor = mNameView.getTextColors(); //save original colors
    }

    void onSaveClicked() {
//        String title = mNameView.getText().toString().trim();
//        String description = mDescriptionView.getText().toString().trim();
//
//        int selectedItemPosition = mContactSpinner.getSpinner().getSelectedItemPosition();
//        int spinnerCount = mContactSpinner.getSpinner().getCount();
//        Contact contact = null;
//        if (selectedItemPosition > 0 && selectedItemPosition < spinnerCount - 1) {
//            contact = (Contact) mContactSpinner.getSpinner().getSelectedItem();
//            contact.setId(null);
//        }
//
//        List<Long> skillIds = ((OpportunitiesItemsAdapter) mSkillsRecyclerView.getAdapter()).getItemsIds();
//        List<Long> causeIds = ((OpportunitiesItemsAdapter) mCausesRecyclerView.getAdapter()).getItemsIds();
//        String volunteersNumber = mVolunteersNumberView.getText().toString().trim();
//        String timeCommitment = mTimeCommitmentView.getText().toString().trim();
//        String othersRequirements = mOthersRequirementsView.getText().toString().trim();
//        String tags = mTagsView.getText().toString().trim();

//        mPresenter.attemptSaveUser(title, description, contact, skillIds, causeIds, volunteersNumber, timeCommitment, othersRequirements, tags);

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
    public void setContacts(List<Contact> contacts) {
//        ArrayList<Contact> viewContacts = new ArrayList<>();
//        if (contacts != null) viewContacts.addAll(contacts);
//
//        viewContacts.add(0, new Contact("Select…"));
//        viewContacts.add(new Contact("Create new…"));
//
//        mContactSpinner.setItemsArray(viewContacts);
//        if (selectedContactId != null) {
//            mCurrentContactSpinnerSelectedPosition = viewContacts.indexOf(new Contact(selectedContactId));
//            mContactSpinner.setSelection(mCurrentContactSpinnerSelectedPosition);
//        }
    }

    @Override
    public void addUniqueCauses(List<? extends SelectableItem> causes, List<? extends SelectableItem> causesToRemove) {
        OpportunitiesItemsAdapter adapter = (OpportunitiesItemsAdapter) mCausesRecyclerView.getAdapter();
        adapter.addAndRemoveItems(causes, causesToRemove);
        mCausesRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
        mCausesErrorView.setVisibility(View.GONE);
    }

    @Override
    public void addUniqueSkills(List<? extends SelectableItem> skills, List<? extends SelectableItem> skillsToRemove) {
        OpportunitiesItemsAdapter adapter = (OpportunitiesItemsAdapter) mSkillsRecyclerView.getAdapter();
        adapter.addAndRemoveItems(skills, skillsToRemove);
        mSkillsRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
        mSkillsErrorView.setVisibility(View.GONE);
    }

    @Override
    public void addImages(List<Image> images) {
        GalleryViewAdapter adapter = (GalleryViewAdapter) mGalleryRecyclerView.getAdapter();
        adapter.addItemsBeforeLast(images);
        mGalleryRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
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
            if (resultCode == RESULT_OK) {
                mPresenter.onNewItemsSelection(data.<SelectableItem>getParcelableArrayListExtra(ItemsSelectionActivity.EXTRA_ITEMS_SELECTED), data.<SelectableItem>getParcelableArrayListExtra(ItemsSelectionActivity.EXTRA_ITEMS_NOT_SELECTED));
            } else if (resultCode == RESULT_CANCELED) {

            }
        } else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                mPresenter.onNewPlaceSelected(PlaceAutocomplete.getPlace(getContext(), data));
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getContext(), data);
                // TODO: Handle the error.
                Log.i("Tag", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {

            }
        } else if (requestCode == REQUEST_IMAGE_PICK) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    final Bundle extras = data.getExtras();
                    if (extras != null) {
                        //Get image
                        Bitmap bitmap = extras.getParcelable("data");
                        mPresenter.onNewImageAdded(Snippets.getProportionalResizedBitmap(bitmap, Constants.MAX_IMAGE_SIZE));
                    }
                } else {
                    InputStream inputStream = null;
                    try {
                        inputStream = getContext().getContentResolver().openInputStream(data.getData());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    mPresenter.onNewImageAdded(bitmap);
                }
            } else if (resultCode == RESULT_CANCELED) {

            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                int rotation;
                try {
                    rotation = Snippets.fixCameraRotation(mCurrentPath);
                } catch (IOException e) {
                    rotation = 0;
                }
                mPresenter.onNewImageAdded(Snippets.bitmapFromPath(mCurrentPath, Constants.MAX_IMAGE_SIZE, true, rotation));
            } else if (resultCode == RESULT_CANCELED) {
                mCurrentPath = null;
            }
        }
    }

    @Override
    public void setName(String name) {
        mNameView.setText(name);
    }

    @Override
    public void setAbout(String about) {
        mAboutView.setText(about);
    }

    @Override
    public void setMission(String timeCommitment) {
        mMissionView.setText(timeCommitment);
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

    @OnClick(R.id.establishedAt)
    public void onEstablishedAtClicked() {
        mPresenter.establishedAtClicked();
    }

    @OnClick(R.id.birthAt)
    public void onBirthAtClicked() {
        mPresenter.birthAtClicked();
    }

    @Override
    public void showEstablishedAtPicker(int year, int month, int dayOfMonth) {
        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mPresenter.onEstablishedDateSelected(year, month, dayOfMonth);
            }
        }, year, month, dayOfMonth).show();
    }

    @Override
    public void showBirthAtPicker(int year, int month, int dayOfMonth) {
        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mPresenter.onBirthDateSelected(year, month, dayOfMonth);
            }
        }, year, month, dayOfMonth).show();
    }

    @Override
    public void showNameRequiredError() {
//        mNameInputLayout.setError(getString(R.string.error_field_required));
    }

    @Override
    public void setNameFocus() {
        mNameView.requestFocus();
    }

    @Override
    public void showContactRequiredError() {
        //TODO
    }

    @Override
    public void setFocusContactField() {
        //TODO
    }

    @Override
    public void showSkillsMinimumRequiredError(int minSkillsRequired) {
        mSkillsErrorView.setText("Você deve escolher pelo menos " + minSkillsRequired + " habilidades");
        mSkillsErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setFocusSkills() {
        mScrollView.smoothScrollTo(0, mSkillsWrapperView.getTop());
    }

    @Override
    public void showCausesMinimumRequiredError(int minCausesRequired) {
        mCausesErrorView.setText("Você deve escolher pelo menos " + minCausesRequired + " causas");
        mCausesErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setFocusCauses() {
        mScrollView.smoothScrollTo(0, mCausesWrapperView.getTop());
    }

    @Override
    public void showLocationRequiredError() {
        mLocationInputLayout.setError(getString(R.string.error_field_required));
    }

    @Override
    public void setFocusLocation() {
        mScrollView.smoothScrollTo(0, mLocationView.getTop());
    }

    @Override
    public void showImageTypePicker() {
        new AlertDialog.Builder(getContext()).setItems(R.array.imagePickerOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    mPresenter.addNewImageFromCamera();
                } else {
                    mPresenter.addNewImageFromGallery();
                }
            }
        }).show();
    }

    @Override
    public void showAddNewImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    public void showAddNewImageFromCamera() {
        boolean hasPermissions = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if (!hasPermissions) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_STORAGE_PERMISSION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        } else {

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    FileAndPathHolder fileAndPathHolder = Snippets.createImageFile(getContext());
                    photoFile = fileAndPathHolder.file;
                    mCurrentPath = fileAndPathHolder.path;
                } catch (IOException ex) {
                    // Error occurred while creating the File
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getContext(), "com.lutzed.servoluntario.fileprovider", photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResultFromPresenter(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    mPresenter.addNewImageFromCamera();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
        }
    }

    @Override
    public void setCnpj(String cnpj) {

    }

    @Override
    public void setProfileImage(String url) {

    }

    @Override
    public void setEstablishedAt(String format) {

    }

    @Override
    public void removeImageItem(Image image, int position) {
        ((GalleryViewAdapter) mGalleryRecyclerView.getAdapter()).removeItem(position);
    }

    @Override
    public void start() {
        mPresenter.start();
    }
}

