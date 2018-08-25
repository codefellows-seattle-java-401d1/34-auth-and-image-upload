package com.amycohen.lab34authimage;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static android.support.v7.widget.RecyclerView.*;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyFeedViewHolder>{
    private List<Feed> mFeed;

    public FeedAdapter(List<Feed> allFeedItems) {
        mFeed = allFeedItems;
    }


    @Override
    public MyFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.feed_item, parent, false);
        MyFeedViewHolder vh = new MyFeedViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(MyFeedViewHolder holder, int position) {
        Feed feed = mFeed.get(position);
        holder.bind(feed);
    }

    @Override
    public int getItemCount() {
        return mFeed.size();
    }

    public void replaceList(List<Feed> feedItems) {
        mFeed = feedItems;
    }


    public class MyFeedViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public TextView userID;
        public TextView description;
        public ImageView publishedPhoto;

        private Feed mFeedItems;


        public MyFeedViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            userID = mView.findViewById(R.id.userID);
            description = mView.findViewById(R.id.description);
            publishedPhoto = mView.findViewById(R.id.publishedPhoto);

        }

        public void bind(Feed feed) {
            mFeedItems = feed;

            this.userID.setText(feed.uid);
            this.description.setText(feed.description);
//            this.publishedPhoto.setImageBitmap(bitmap);
        }

    }
}
