package com.ticktalk.moreapplib;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by indogroup on 9/1/17.
 */

public class MoreAppAdapter extends RecyclerView.Adapter<MoreAppAdapter.MoreAppViewHolder> {

    public interface MoreAppListener
    {
        void onClickedMoreAppItem(String packageName);
    }

    private Context context;
    private List<MoreAppModel> moreAppModels;
    private MoreAppListener moreAppListener;

    public MoreAppAdapter(Context context, List<MoreAppModel> moreAppModels) {
        this.context = context;
        this.moreAppModels = moreAppModels;
        setHasStableIds(true);
    }

    public void SetListener(MoreAppListener moreAppListener)
    {
        this.moreAppListener = moreAppListener;
    }

    @Override
    public int getItemViewType(int position) {
        if(position % 4 == 0)
            return 0;
        else if(position % 3 == 0)
            return 1;
        else if(position % 2 == 0)
            return 2;
        else
            return 3;
    }

    @Override
    public MoreAppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.more_app_item_view_holder, parent, false);
        return new MoreAppViewHolder(view, moreAppListener);
    }

    @Override
    public void onBindViewHolder(MoreAppViewHolder holder, int position) {
        holder.bind(moreAppModels.get(position));
    }

    @Override
    public int getItemCount() {
        return moreAppModels.size();
    }

    public static class MoreAppViewHolder extends RecyclerView.ViewHolder
    {
        RelativeLayout freeLayout;
        SimpleDraweeView backgroundImageView;
        SimpleDraweeView iconImageView;
        SimpleDraweeView playStoreImageView;
        TextView appNameTextView;
        TextView descriptionTextView;

        private MoreAppListener moreAppListener;

        public MoreAppViewHolder(final View itemView, final MoreAppListener moreAppListener) {
            super(itemView);

            this.moreAppListener = moreAppListener;

            freeLayout = (RelativeLayout) itemView.findViewById(R.id.free_badge_layout);
            backgroundImageView = (SimpleDraweeView) itemView.findViewById(R.id.background_image_view);
            iconImageView = (SimpleDraweeView) itemView.findViewById(R.id.app_icon_image_view);
            playStoreImageView = (SimpleDraweeView) itemView.findViewById(R.id.play_store_image_view);
            appNameTextView = (TextView) itemView.findViewById(R.id.app_name_text_view);
            descriptionTextView = (TextView) itemView.findViewById(R.id.app_description_text_view);
        }

        public void bind(final MoreAppModel moreAppModel)
        {
            freeLayout.setVisibility(moreAppModel.isFree() ? View.VISIBLE : View.GONE);

            backgroundImageView.setImageResource(moreAppModel.getBackground());
            iconImageView.setImageResource(moreAppModel.getIcon());
            playStoreImageView.setImageResource(R.drawable.en_badge_web_generic);

            appNameTextView.setText(moreAppModel.getAppName());
            descriptionTextView.setText(moreAppModel.getDescription());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(moreAppListener != null)
                        moreAppListener.onClickedMoreAppItem(moreAppModel.getPackageName());
                }
            });
        }
    }
}
