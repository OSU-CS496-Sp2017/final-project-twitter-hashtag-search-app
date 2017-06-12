package com.example.xiaoli.twitterhashtagsearcher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.xiaoli.twitterhashtagsearcher.utils.TwitterSearchUtils;

public class SearchResultDetailActivity extends AppCompatActivity {
    private TextView mSearchResultNameTV;
    private TextView mSearchResultDescriptionTV;
    private TextView mSearchResultStarsTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_detail);

        mSearchResultNameTV = (TextView)findViewById(R.id.tv_search_result_name);
        mSearchResultDescriptionTV = (TextView)findViewById(R.id.tv_search_result_description);
        mSearchResultStarsTV = (TextView)findViewById(R.id.tv_search_result_stars);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(TwitterSearchUtils.SearchResult.EXTRA_SEARCH_RESULT)) {
            TwitterSearchUtils.SearchResult searchResult = (TwitterSearchUtils.SearchResult)intent.getSerializableExtra(TwitterSearchUtils.SearchResult.EXTRA_SEARCH_RESULT);
            mSearchResultNameTV.setText(searchResult.Time);
            mSearchResultDescriptionTV.setText(searchResult.Text);
            mSearchResultStarsTV.setText(searchResult.Photo);
        }
    }
}
