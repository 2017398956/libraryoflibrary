package com.nfl.libraryoflibrary.view;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;

/**
 * Created by fuli.niu on 2016/9/21.
 */

public class CustomV4BaseFragment extends Fragment {

    protected Context context;
    protected Activity parentActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.parentActivity = getActivity();
        if (null == parentActivity) {
            this.parentActivity = (Activity) context;
        }
    }
}
