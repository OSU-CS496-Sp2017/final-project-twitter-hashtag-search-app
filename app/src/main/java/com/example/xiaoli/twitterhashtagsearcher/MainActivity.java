package com.example.xiaoli.twitterhashtagsearcher;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.xiaoli.twitterhashtagsearcher.data.HashtagSearchContract;
import com.example.xiaoli.twitterhashtagsearcher.data.HashtagSearchDBHelper;
import com.example.xiaoli.twitterhashtagsearcher.utils.NetworkUtils;
import com.example.xiaoli.twitterhashtagsearcher.utils.TwitterSearchUtils;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, TwitterSearchAdapter.OnSearchResultClickListener, LoaderManager.LoaderCallbacks<String> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String SEARCH_RESULTS_LIST_KEY = "searchResultsList";
    private static final String SEARCH_URL_KEY = "twitterSearchURL";
    private static final int TWITTER_SEARCH_LOADER_ID = 0;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private RecyclerView mSearchResultsRV;
    private EditText mSearchBoxET;
    private ProgressBar mLoadingIndicatorPB;
    private TextView mLoadingErrorMessageTV;
    private TwitterSearchAdapter mTwitterSearchAdapter;
    private SQLiteDatabase mDB;

    private ArrayList<TwitterSearchUtils.SearchResult> mSearchResultsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchResultsList = null;

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mSearchBoxET = (EditText)findViewById(R.id.et_search_box);
        mLoadingIndicatorPB = (ProgressBar)findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessageTV = (TextView)findViewById(R.id.tv_loading_error_message);
        mSearchResultsRV = (RecyclerView)findViewById(R.id.rv_search_results);

        mSearchResultsRV.setLayoutManager(new LinearLayoutManager(this));
        mSearchResultsRV.setHasFixedSize(true);

        mTwitterSearchAdapter = new TwitterSearchAdapter(this);
        mSearchResultsRV.setAdapter(mTwitterSearchAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        HashtagSearchDBHelper dbHelper = new HashtagSearchDBHelper(this);
        mDB = dbHelper.getWritableDatabase();

        getSupportLoaderManager().initLoader(TWITTER_SEARCH_LOADER_ID, null, this);

        Button searchButton = (Button)findViewById(R.id.btn_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                String searchQuery = mSearchBoxET.getText().toString();
                values.put(HashtagSearchContract.SearchedHashtags.COLUMN_HASHTAG, searchQuery);
                mDB.insert(HashtagSearchContract.SearchedHashtags.TABLE_NAME, null, values);
                if (!TextUtils.isEmpty(searchQuery)) {
                    doTwitterSearch(searchQuery);
                }
            }
        });

        NavigationView navigationView = (NavigationView)findViewById(R.id.nv_navigation_drawer);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onDestroy() {
        mDB.close();
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void doTwitterSearch(String searchQuery) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String result_type = preferences.getString(getString(R.string.pref_result_type_key), getString(R.string.pref_result_type_default));
        String language = preferences.getString(getString(R.string.pref_language_key), getString(R.string.pref_language_default));
        String  count = preferences.getString(getString(R.string.pref_count_key), "");
        String githubSearchUrl = TwitterSearchUtils.builTwitterSearchURL(searchQuery, result_type, language, count);

        Bundle argsBundle = new Bundle();
        argsBundle.putString(SEARCH_URL_KEY, githubSearchUrl);
        getSupportLoaderManager().restartLoader(TWITTER_SEARCH_LOADER_ID, argsBundle, this);
    }

    @Override
    public void onSearchResultClick(TwitterSearchUtils.SearchResult searchResult) {
        Intent intent = new Intent(this, SearchResultDetailActivity.class);
        intent.putExtra(TwitterSearchUtils.SearchResult.EXTRA_SEARCH_RESULT, searchResult);
        startActivity(intent);
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            String mSearchResultsJSON;

            @Override
            protected void onStartLoading() {
                if (args != null) {
                    if (mSearchResultsJSON != null) {
                        Log.d(TAG, "AsyncTaskLoader delivering cached results");
                        deliverResult(mSearchResultsJSON);
                    } else {
                        mLoadingIndicatorPB.setVisibility(View.VISIBLE);
                        forceLoad();
                    }
                }
            }

            @Override
            public String loadInBackground() {
                if (args != null) {
                    String githubSearchUrl = args.getString(SEARCH_URL_KEY);
                    Log.d(TAG, "AsyncTaskLoader making network call: " + githubSearchUrl);
                    String searchResults = null;
                    try {
                        searchResults = NetworkUtils.doHTTPGet(githubSearchUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return searchResults;
                } else {
                    return null;
                }
            }

            @Override
            public void deliverResult(String data) {
                mSearchResultsJSON = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.d(TAG, "AsyncTaskLoader's onLoadFinished called");
        mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
        if (data != null) {
            mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
            mSearchResultsRV.setVisibility(View.VISIBLE);
            mSearchResultsList = TwitterSearchUtils.parseGitHubSearchResultsJSON(data);
            mTwitterSearchAdapter.updateSearchResults(mSearchResultsList);
        } else {
            mSearchResultsRV.setVisibility(View.INVISIBLE);
            mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        // Nothing to do...
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.nav_search:
                mDrawerLayout.closeDrawers();
                return true;
            case R.id.nav_saved_results:
                mDrawerLayout.closeDrawers();
                Intent savedResultsIntent =
                        new Intent(this, SavedHashtagActivity.class);
                startActivity(savedResultsIntent);
                return true;
            case R.id.nav_settings:
                mDrawerLayout.closeDrawers();
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return false;
        }
    }
}
