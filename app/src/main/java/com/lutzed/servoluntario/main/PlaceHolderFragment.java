package com.lutzed.servoluntario.main;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lutzed.servoluntario.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceHolderFragment extends Fragment {


    public PlaceHolderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("PlaceHolderFrag", "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("PlaceHolderFrag", "onDestroy");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("PlaceHolderFrag", "onAttach");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("PlaceHolderFrag", "onDetach");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("PlaceHolderFrag", "onCreateView");
        return inflater.inflate(R.layout.fragment_place_holder, container, false);
    }

}
