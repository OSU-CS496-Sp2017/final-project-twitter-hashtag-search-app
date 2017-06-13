package com.example.xiaoli.twitterhashtagsearcher.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Bo on 6/12/2017.
 */

public class HashtagSearchDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "hashtag.db";
    private static final int DATABASE_VERSION = 1;

    public HashtagSearchDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_SEARCHED_HASHTAG_TABLE =
                "CREATE TABLE " + HashtagSearchContract.SearchedHashtags.TABLE_NAME + " (" +
                        HashtagSearchContract.SearchedHashtags._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        HashtagSearchContract.SearchedHashtags.COLUMN_HASHTAG + " TEXT NOT NULL, " +
                        HashtagSearchContract.SearchedHashtags.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP " +
                        ");";
        db.execSQL(SQL_CREATE_SEARCHED_HASHTAG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + HashtagSearchContract.SearchedHashtags.TABLE_NAME);
        onCreate(db);
    }
}
