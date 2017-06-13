package com.example.xiaoli.twitterhashtagsearcher;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.xiaoli.twitterhashtagsearcher.data.HashtagSearchContract;
import com.example.xiaoli.twitterhashtagsearcher.data.HashtagSearchDBHelper;
import com.example.xiaoli.twitterhashtagsearcher.utils.TwitterSearchUtils;

import java.util.ArrayList;

public class SavedHashtagActivity extends AppCompatActivity {

    private RecyclerView mSearchedHashtagRV;
    private SQLiteDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_hashtag);

        HashtagSearchDBHelper dbHelper = new HashtagSearchDBHelper(this);
        mDB = dbHelper.getReadableDatabase();

        ArrayList<String> searchedHashtagList = getAllSavedHashtagsResults();

        SavedHashtagAdapter adapter = new SavedHashtagAdapter();
        adapter.updateSavedSearchResults(searchedHashtagList);

        mSearchedHashtagRV = (RecyclerView) findViewById(R.id.tv_searched_hashtag);
        mSearchedHashtagRV.setLayoutManager(new LinearLayoutManager(this));
        mSearchedHashtagRV.setHasFixedSize(true);
        mSearchedHashtagRV.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        mDB.close();
        super.onDestroy();
    }


    private ArrayList<String> getAllSavedHashtagsResults(){
        Cursor cursor = mDB.query(
                HashtagSearchContract.SearchedHashtags.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                HashtagSearchContract.SearchedHashtags.COLUMN_TIMESTAMP + " ASC"
        );

        ArrayList<String> searchResultsList = new ArrayList<>();
        while (cursor.moveToNext()) {
            searchResultsList.add(cursor.getString(
                    cursor.getColumnIndex(HashtagSearchContract.SearchedHashtags._ID)
            ) + ". " +cursor.getString(
                    cursor.getColumnIndex(HashtagSearchContract.SearchedHashtags.COLUMN_HASHTAG)
            ));
        }
        cursor.close();
        return searchResultsList;
    }
}
