package com.example.xiaoli.twitterhashtagsearcher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.xiaoli.twitterhashtagsearcher.utils.TwitterSearchUtils;

public class SearchResultDetailActivity extends AppCompatActivity {
    private TextView mSearchResultUserNameTV;
    private TextView mSearchResultTextTV;
    private TwitterSearchUtils.SearchResult mSearchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_detail);

        mSearchResultUserNameTV = (TextView)findViewById(R.id.tv_user_name);
        mSearchResultTextTV = (TextView)findViewById(R.id.tv_twitter_text);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(TwitterSearchUtils.SearchResult.EXTRA_SEARCH_RESULT)) {
            mSearchResult = (TwitterSearchUtils.SearchResult)intent.getSerializableExtra(TwitterSearchUtils.SearchResult.EXTRA_SEARCH_RESULT);
            mSearchResultUserNameTV.setText(mSearchResult.User_name);
            mSearchResultTextTV.setText(mSearchResult.Text);
        }
    }
}
