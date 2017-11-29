package com.nfl.libraryoflibrary.utils.annotation;

import com.nfl.libraryoflibrary.utils.annotation.provider.Provider;

/**
 * Created by nfl on 2017/11/29.
 */

public interface Finder<T> {
    void inject(T host, Object source, Provider provider);
}
