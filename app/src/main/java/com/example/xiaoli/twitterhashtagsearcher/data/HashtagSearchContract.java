package com.example.xiaoli.twitterhashtagsearcher.data;

import android.provider.BaseColumns;

/**
 * Created by Bo on 6/12/2017.
 */

public class HashtagSearchContract {
    private HashtagSearchContract() {}

    public static class SearchedHashtags implements BaseColumns {
        public static final String TABLE_NAME = "searchedHashtags";
        public static final String COLUMN_HASHTAG = "hashtags";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
