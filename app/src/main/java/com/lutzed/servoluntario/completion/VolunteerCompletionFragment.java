package com.lutzed.servoluntario.completion;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lutzed.servoluntario.R;
import com.lutzed.servoluntario.main.MainActivity;
import com.lutzed.servoluntario.selection.ItemsSelectionActivity;
import com.lutzed.servoluntario.util.Constants;
import com.lutzed.servoluntario.util.FileAndPathHolder;
import com.lutzed.servoluntario.util.LocalCircleTransform;
import com.lutzed.servoluntario.util.Snippets;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A login screen that offers login via email/password.
 */
public class VolunteerCompletionFragment extends Fragment implements VolunteerCompletionContract.View {

    private static final int REQUEST_IMAGE_PICK_PROFILE = 396;
    private static final int REQUEST_IMAGE_CAPTURE_PROFILE = 27;
    private static final int REQUEST_STORAGE_PERMISSION_PROFILE = 213;

    @BindView(R.id.profileImage) ImageView mProfileImage;
    @BindView(R.id.about) EditText mAboutView;
    @BindView(R.id.occupation) EditText mOccupationView;
    @BindView(R.id.progress) View mProgressView;
    @BindView(R.id.volunteer_completion_form) View mLoginFormView;

    private VolunteerCompletionContract.Presenter mPresenter;
    private String mCurrentPath;
    private Target mTarget;

    public static VolunteerCompletionFragment newInstance() {
        return new VolunteerCompletionFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(VolunteerCompletionContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_volunteer_completion, container, false);
        ButterKnife.bind(this, root);

        mOccupationView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.save || id == EditorInfo.IME_NULL) {
                    onSaveClicked();
                    return true;
                }
                return false;
            }
        });

        return root;
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

    void onSaveClicked() {
        mPresenter.saveProfile(mAboutView.getText().toString(), mOccupationView.getText().toString());
    }

    public void resetErrors() {
        mAboutView.setError(null);
        mOccupationView.setError(null);
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
    public void navigateToChooseSkills() {
        getActivity().finish();
        Intent intent = new Intent(getContext(), ItemsSelectionActivity.class);
        intent.putExtra(ItemsSelectionActivity.EXTRA_ITEM_SELECTION_KIND, ItemsSelectionActivity.Kind.SKILL);
//        intent.putExtra(ItemsSelectionActivity.EXTRA_SAVE_ACTION_NAME, getString(R.string.action_next));
        intent.putExtra(ItemsSelectionActivity.EXTRA_ITEM_SELECTION_MODE, ItemsSelectionActivity.Mode.MULTIPLE_SAVE_TO_USER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void navigateToChooseCauses() {
        getActivity().finish();
        Intent intent = new Intent(getContext(), ItemsSelectionActivity.class);
        intent.putExtra(ItemsSelectionActivity.EXTRA_ITEM_SELECTION_KIND, ItemsSelectionActivity.Kind.CAUSE);
//        intent.putExtra(ItemsSelectionActivity.EXTRA_SAVE_ACTION_NAME, getString(R.string.action_next));
        intent.putExtra(ItemsSelectionActivity.EXTRA_ITEM_SELECTION_MODE, ItemsSelectionActivity.Mode.MULTIPLE_SAVE_TO_USER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void navigateToMain() {
        getActivity().finish();
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void setAboutField(String about) {
        mAboutView.setText(about);
    }

    @Override
    public void setOccupationField(String occupation) {
        mOccupationView.setText(occupation);
    }

    @Override
    public void showDefaultSaveError() {
        Toast.makeText(getContext(), "DEFAULT SAVE ERROR", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.profileImage)
    public void onProfileImageClicked() {
        mPresenter.addNewProfileImage();
    }

    @Override
    public void showProfileImageTypePicker() {
        new AlertDialog.Builder(getContext()).setItems(R.array.imagePickerOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    mPresenter.addNewImageFromCamera(REQUEST_IMAGE_CAPTURE_PROFILE);
                } else {
                    mPresenter.addNewImageFromGallery(REQUEST_IMAGE_PICK_PROFILE);
                }
            }
        }).show();
    }

    @Override
    public void showAddNewImageFromGallery(int request) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, request);
    }

    @Override
    public void showAddNewImageFromCamera(int request) {
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
                        REQUEST_STORAGE_PERMISSION_PROFILE);

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
                    startActivityForResult(takePictureIntent, request);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResultFromPresenter(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSION_PROFILE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    mPresenter.addNewImageFromCamera(REQUEST_IMAGE_CAPTURE_PROFILE);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
        }
    }

    @Override
    public void setProfileImage(String url) {
        if (mTarget == null){
            mTarget = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    mPresenter.onNewProfileImageAdded(Snippets.getProportionalResizedBitmap(bitmap, Constants.PROFILE_IMAGE_SIZE));
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            };
        }

        Picasso.with(getContext()).load(url).into(mTarget);
    }

    @Override
    public void setProfileImage(Bitmap bitmap) {
        LocalCircleTransform circleTransform = new LocalCircleTransform(true);
        mProfileImage.setImageBitmap(circleTransform.transform(bitmap));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK_PROFILE) {
            if (resultCode == RESULT_OK) {
                if (data == null) {
                    final Bundle extras = data.getExtras();
                    if (extras != null) {
                        //Get image
                        Bitmap bitmap = extras.getParcelable("data");

                        mPresenter.onNewProfileImageAdded(Snippets.getProportionalResizedBitmap(bitmap, Constants.PROFILE_IMAGE_SIZE));
                    }
                } else {
                    InputStream inputStream = null;
                    try {
                        inputStream = getContext().getContentResolver().openInputStream(data.getData());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    mPresenter.onNewProfileImageAdded(Snippets.getProportionalResizedBitmap(bitmap, Constants.PROFILE_IMAGE_SIZE));

                }
            } else if (resultCode == RESULT_CANCELED) {

            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE_PROFILE) {
            if (resultCode == RESULT_OK) {
                int rotation;
                try {
                    rotation = Snippets.fixCameraRotation(mCurrentPath);
                } catch (IOException e) {
                    rotation = 0;
                }
                mPresenter.onNewProfileImageAdded(Snippets.bitmapFromPath(mCurrentPath, Constants.PROFILE_IMAGE_SIZE, true, rotation));
            } else if (resultCode == RESULT_CANCELED) {
                mCurrentPath = null;
            }
        }
    }
}

