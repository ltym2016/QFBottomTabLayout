package com.samluys.tab.frgment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.samluys.tab.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FiveFragment extends Fragment {


    public static FiveFragment newInstance() {
        FiveFragment fragment = new FiveFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_five, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.e("LUYS", "FiveFragment");
    }
}
