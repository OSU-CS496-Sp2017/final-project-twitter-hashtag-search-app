package com.example.xiaoli.twitterhashtagsearcher;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.xiaoli.twitterhashtagsearcher.data.HashtagSearchContract;
import com.example.xiaoli.twitterhashtagsearcher.data.HashtagSearchDBHelper;

import java.util.ArrayList;

public class SavedHashtagActivity extends AppCompatActivity {

    private TextView mSearchedHashtagRV;
    private SQLiteDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_hashtag);

        mSearchedHashtagRV = (TextView)findViewById(R.id.tv_searched_hashtag);

        HashtagSearchDBHelper dbHelper = new HashtagSearchDBHelper(this);
        mDB = dbHelper.getReadableDatabase();

        ArrayList<String> searchedHashtagList = getAllSavedHashtagsResults();
        String test = searchedHashtagList.toString();
        mSearchedHashtagRV.setText(searchedHashtagList.toString());
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
                HashtagSearchContract.SearchedHashtags.COLUMN_TIMESTAMP + " DESC"
        );

        ArrayList<String> searchResultsList = new ArrayList<>();
        while (cursor.moveToNext()) {
            searchResultsList.add(cursor.getString(
                    cursor.getColumnIndex(HashtagSearchContract.SearchedHashtags.COLUMN_HASHTAG)
            ));
        }
        cursor.close();
        return searchResultsList;
    }
}
