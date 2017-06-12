package com.example.xiaoli.twitterhashtagsearcher;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xiaoli.twitterhashtagsearcher.utils.TwitterSearchUtils;

import java.io.InputStream;

public class SearchResultDetailActivity extends AppCompatActivity {
    private TextView mSearchResultUserNameTV;
    private TextView mSearchResultTextTV;
    private TextView mSearchResultTimeTV;
    private ImageView mSearchResultPhotoIV;
    private TwitterSearchUtils.SearchResult mSearchResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_detail);

        mSearchResultUserNameTV = (TextView)findViewById(R.id.tv_user_name);
        mSearchResultTextTV = (TextView)findViewById(R.id.tv_twitter_text);
        mSearchResultTimeTV = (TextView)findViewById(R.id.tv_time);
        mSearchResultPhotoIV = (ImageView) findViewById(R.id.iv_photo);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(TwitterSearchUtils.SearchResult.EXTRA_SEARCH_RESULT)) {
            mSearchResult = (TwitterSearchUtils.SearchResult)intent.getSerializableExtra(TwitterSearchUtils.SearchResult.EXTRA_SEARCH_RESULT);
            mSearchResultUserNameTV.setText(mSearchResult.User_name);
            mSearchResultTextTV.setText(mSearchResult.Text);
            mSearchResultTimeTV.setText(mSearchResult.Time);

            new DownloadImageTask((ImageView) findViewById(R.id.iv_user_profile_image))
                    .execute(mSearchResult.Profile_image);
            new DownloadImageTask((ImageView) findViewById(R.id.iv_photo))
                    .execute(mSearchResult.Media_photo);
        }
    }

    //Download images.
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
