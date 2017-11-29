package com.nfl.libraryoflibrary.utils.annotation.provider;

import android.content.Context;
import android.view.View;

/**
 * Created by nfl on 2017/11/29.
 */

public interface Provider {
    Context getContext(Object source);
    View findView(Object source, int id);
}
