package com.example.xiaoli.twitterhashtagsearcher;

/**
 * Created by Xiaoli on 2017/6/13.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;



public class SavedHashtagAdapter extends RecyclerView.Adapter<SavedHashtagAdapter.SearchResultViewHolder> {
    private ArrayList<String> mSavedSearchResultsList;

    public SavedHashtagAdapter() {

    }

    public void updateSavedSearchResults(ArrayList<String> savedsearchResultsList) {
        mSavedSearchResultsList = savedsearchResultsList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mSavedSearchResultsList != null) {
            return mSavedSearchResultsList.size();
        } else {
            return 0;
        }
    }

    @Override
    public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.saved_search_result, parent, false);
        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchResultViewHolder holder, int position) {
        holder.bind(mSavedSearchResultsList.get(position));
    }


    class SearchResultViewHolder extends RecyclerView.ViewHolder {
        private TextView mSearchResultTV;

        public SearchResultViewHolder(View itemView) {
            super(itemView);
            mSearchResultTV = (TextView) itemView.findViewById(R.id.tv_saved_search_result);
            //itemView.setOnClickListener(this);
        }

        public void bind(String str) {
            mSearchResultTV.setText(str);
        }

        /*@Override
        public void onClick(View v) {
            GitHubUtils.SearchResult searchResult = mSearchResultsList.get(getAdapterPosition());
            mSearchResultClickListener.onSearchResultClick(searchResult);
        }
    }*/
    }
}