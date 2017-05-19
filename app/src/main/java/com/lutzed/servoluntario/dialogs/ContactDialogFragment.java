package com.lutzed.servoluntario.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lutzed.servoluntario.R;

import butterknife.BindView;

public class ContactDialogFragment extends DialogFragment {

    @BindView(R.id.email) AutoCompleteTextView mEmailView;
    @BindView(R.id.name) EditText mNameView;
    @BindView(R.id.phone) EditText mPhoneView;

    private Listener mListener;

    public ContactDialogFragment() {

    }

    public static ContactDialogFragment newInstance(Listener mListener) {
        ContactDialogFragment contactDialogFragment = new ContactDialogFragment();
        contactDialogFragment.mListener = mListener;
        return contactDialogFragment;
    }

    /**
     * The system calls this only when creating the layout in a dialog.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View rootView = inflater.inflate(R.layout.fragment_dialog_create_contact, null);
        builder.setView(rootView)
                // Add action buttons
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (mListener != null) {
                            mListener.onCancel();
                        }
                        ContactDialogFragment.this.getDialog().cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (mListener != null) {
                            TextView nameView = (TextView) rootView.findViewById(R.id.name);
                            TextView phoneView = (TextView) rootView.findViewById(R.id.phone);
                            TextView emailView = (TextView) rootView.findViewById(R.id.email);
                            String name = nameView.getText().toString().trim();
                            String phone = phoneView.getText().toString().trim();
                            String email = emailView.getText().toString().trim();
                            if (name.isEmpty() && phone.isEmpty() && email.isEmpty()) {
                                nameView.setError(getString(R.string.error_field_one_required));
                                phoneView.setError(getString(R.string.error_field_one_required));
                                emailView.setError(getString(R.string.error_field_one_required));
                            } else {
                                mListener.onSave(name, phone, email);
                                dialog.dismiss();
                            }
                        }
                    }
                });
            }
        });

        return alertDialog;
    }

    public interface Listener {
        void onSave(String name, String phone, String email);

        void onCancel();
    }
}