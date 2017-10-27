package com.example.mojiehua93.wechathomepage;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by MOJIEHUA93 on 2017/10/22.
 */

public class TabFragment extends Fragment {
    @Nullable
    public static final String TITLE = "title";
    private String mTile;
    private TextView mTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null){
            mTile = getArguments().getString(TITLE);
        }
        mTextView = new TextView(getActivity());
        mTextView.setTextSize(20);
        mTextView.setBackgroundColor(Color.parseColor("#ffffffff"));
        mTextView.setText(mTile);
        mTextView.setGravity(Gravity.CENTER);
        return mTextView;
    }
}
