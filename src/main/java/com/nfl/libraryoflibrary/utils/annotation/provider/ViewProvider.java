package com.nfl.libraryoflibrary.utils.annotation.provider;

import android.content.Context;
import android.view.View;

/**
 * Created by nfl on 2017/11/29.
 */

public class ViewProvider implements Provider {
    @Override
    public Context getContext(Object source) {
        return ((View) source).getContext();
    }

    @Override
    public View findView(Object source, int id) {
        return ((View) source).findViewById(id);
    }
}
