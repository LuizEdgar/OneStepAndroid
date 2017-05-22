package com.lutzed.servoluntario.util;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by luizfreitas on 22/05/2017.
 */

public class TextInputLayoutTextWatcher implements TextWatcher {

    private TextInputLayout mTextInputLayout;
    private EditText mEditText;

    public TextInputLayoutTextWatcher(EditText mEditText, TextInputLayout mTextInputLayout) {
        this.mTextInputLayout = mTextInputLayout;
        this.mEditText = mEditText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mTextInputLayout.setError(null);
        mTextInputLayout.setErrorEnabled(false);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
