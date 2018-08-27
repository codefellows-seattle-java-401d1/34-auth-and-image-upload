package com.droid.yutani.myinsta.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.droid.yutani.myinsta.R;
import com.droid.yutani.myinsta.model.Post;
import com.koushikdutta.ion.Ion;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> mPosts;


    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.post_item, viewGroup, false);
        PostViewHolder holder = new PostViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder postViewHolder, int i) {
        Post post = mPosts.get(i);
        postViewHolder.bind(post);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public void setPosts(List<Post> posts) {
        mPosts = posts;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public ImageView mPostImg;
        public TextView mAuthor;
        public TextView mDescription;

        private Post mPost;

        public PostViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            mPostImg = mView.findViewById(R.id.photo);
            mAuthor = mView.findViewById(R.id.username);
            mDescription = mView.findViewById(R.id.description);
        }

        public void bind(Post post) {
            mPost = post;

            mAuthor.setText(post.author);
            mDescription.setText(post.description);

            Ion.with(mPostImg).load(post.imgUrl);
        }
    }
}
