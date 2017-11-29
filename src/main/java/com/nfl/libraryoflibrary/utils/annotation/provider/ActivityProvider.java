package com.nfl.libraryoflibrary.utils.annotation.provider;

import android.app.Activity;
import android.content.Context;
import android.view.View;

/**
 * Created by nfl on 2017/11/29.
 */

public class ActivityProvider implements Provider {
    @Override
    public Context getContext(Object source) {
        return ((Activity) source);
    }

    @Override
    public View findView(Object source, int id) {
        return ((Activity) source).findViewById(id);
    }
}
