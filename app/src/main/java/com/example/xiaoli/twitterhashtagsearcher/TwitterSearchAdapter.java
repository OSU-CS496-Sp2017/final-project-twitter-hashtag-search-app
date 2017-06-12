package com.example.xiaoli.twitterhashtagsearcher;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xiaoli.twitterhashtagsearcher.utils.TwitterSearchUtils;

import java.util.ArrayList;

/**
 * Created by hessro on 4/21/17.
 */

public class TwitterSearchAdapter extends RecyclerView.Adapter<TwitterSearchAdapter.SearchResultViewHolder> {
    private ArrayList<TwitterSearchUtils.SearchResult> mSearchResultsList;
    private OnSearchResultClickListener mSearchResultClickListener;

    public TwitterSearchAdapter(OnSearchResultClickListener clickListener) {
        mSearchResultClickListener = clickListener;
    }

    public void updateSearchResults(ArrayList<TwitterSearchUtils.SearchResult> searchResultsList) {
        mSearchResultsList = searchResultsList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mSearchResultsList != null) {
            return mSearchResultsList.size();
        } else {
            return 0;
        }
    }

    @Override
    public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.search_result_item, parent, false);
        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchResultViewHolder holder, int position) {
        holder.bind(mSearchResultsList.get(position));
    }

    public interface OnSearchResultClickListener {
        void onSearchResultClick(TwitterSearchUtils.SearchResult searchResult);
    }

    class SearchResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mSearchResultTV;

        public SearchResultViewHolder(View itemView) {
            super(itemView);
            mSearchResultTV = (TextView)itemView.findViewById(R.id.tv_search_result);
            itemView.setOnClickListener(this);
        }

        public void bind(TwitterSearchUtils.SearchResult searchResult) {
            mSearchResultTV.setText(searchResult.Text);
        }

        @Override
        public void onClick(View v) {
            TwitterSearchUtils.SearchResult searchResult = mSearchResultsList.get(getAdapterPosition());
            mSearchResultClickListener.onSearchResultClick(searchResult);
        }
    }
}
