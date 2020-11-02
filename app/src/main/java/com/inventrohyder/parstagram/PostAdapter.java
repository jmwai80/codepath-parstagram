package com.inventrohyder.parstagram;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    static final String PAYLOAD_LIKES_COUNT = "payload_likes_count";
    private final Context mContext;
    private final List<Post> mPosts;

    public PostAdapter(Context context, List<Post> posts) {
        mContext = context;
        mPosts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = mPosts.get(position);
        holder.bind(post, position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (!payloads.isEmpty()) {
            final Post post = mPosts.get(position);
            for (final Object payload : payloads) {

                if (payload.equals(PAYLOAD_LIKES_COUNT)) {
                    // in this case only like will be updated
                    holder.mTvLikesCount.setText(mContext.getResources()
                            .getQuantityString(R.plurals.numberOfLikes, post.getLikesCount(), post.getLikesCount())
                    );
                }

            }
        } else {
            // in this case regular onBindViewHolder will be called
            super.onBindViewHolder(holder, position, payloads);
        }

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTvUsername;
        private final ImageView mIvImage;
        private final TextView mTvDescription;
        private final ImageView mIvProfile;
        private final TextView mTvCreated;
        private final CheckBox mCbLike;
        private final TextView mTvLikesCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvUsername = itemView.findViewById(R.id.tvUsername);
            mIvImage = itemView.findViewById(R.id.ivPostImage);
            mTvDescription = itemView.findViewById(R.id.tvDescription);
            mIvProfile = itemView.findViewById(R.id.ivProfile);
            mTvCreated = itemView.findViewById(R.id.tvCreated);
            mCbLike = itemView.findViewById(R.id.cbLike);
            mTvLikesCount = itemView.findViewById(R.id.tvLikes);
        }

        public void bind(Post post, int position) {
            // Bind the post data to the view elements
            ParseUser user = post.getUser();
            String sourceString = "<b>" + user.getUsername() + "</b> " + post.getDescription();
            mTvDescription.setText(Html.fromHtml(sourceString));
            mTvUsername.setText(user.getUsername());
            mTvCreated.setText(
                    DateUtils.getRelativeTimeSpanString(post.getCreatedAt().getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS)
            );
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(mContext).load(image.getUrl()).into(mIvImage);
            }

            ParseFile profilePicture = user.getParseFile("profilePicture");
            if (profilePicture != null) {
                Glide.with(mContext)
                        .load(profilePicture.getUrl())
                        .circleCrop()
                        .fallback(R.drawable.default_profile)
                        .into(mIvProfile);
            } else {
                Glide.with(mContext)
                        .load(R.drawable.default_profile)
                        .circleCrop()
                        .into(mIvProfile);
            }

            mCbLike.setChecked(post.getIsLiked());
            mCbLike.setOnClickListener(view -> {
                CheckBox checkBox = (CheckBox) view;
                post.setIsLiked(checkBox.isChecked(), true);
                notifyItemChanged(position, PAYLOAD_LIKES_COUNT);
            });

            int likesCount = post.getLikesCount();
            mTvLikesCount.setText(
                    mContext.getResources()
                            .getQuantityString(R.plurals.numberOfLikes, likesCount, likesCount)
            );
        }
    }
}
