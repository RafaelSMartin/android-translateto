package com.ticktalk.translateto.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.airbnb.lottie.LottieAnimationView;
import com.ticktalk.translateto.database.DatabaseManager;
import com.ticktalk.translateto.database.FromResult;
import com.ticktalk.translateto.history.ItemHistory;
import com.ticktalk.translateto.R;

import java.util.List;

import static com.ticktalk.translateto.utils.ArraySpinner.countryNames;
import static com.ticktalk.translateto.utils.ArraySpinner.flags;

/**
 * Created by Rafael S. Martin
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>
{

    private List<ItemHistory> items;
    private LayoutInflater inflate;
    private List<FromResult> fromResults;

    protected View.OnClickListener onClickListener;

    public FavoriteAdapter(Context context,List<ItemHistory> items){
        this.items = items;
        inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void updateFavoriteItem(int position){
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size());
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int i){
        View v = inflate.inflate(R.layout.item_history, parent, false);

        v.setOnClickListener(onClickListener);

        return new FavoriteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position){

        fromResults = DatabaseManager.getInstance().getFavoriteResult();

        FavoriteViewHolder favoriteViewHolder = (FavoriteViewHolder) holder;
        favoriteViewHolder.bind(fromResults, position);
    }

    // Tamaño
    @Override
    public int getItemCount() {
        return items.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder{
        public TextView fromLanguageTitle;
        public TextView fromText;
        public TextView toLanguageTitle;
        public TextView toText;
        public LottieAnimationView favourite;
        public ImageView fromLanguageFlag;
        public ImageView toLanguageFlag;
        public Boolean favoriteAnimation;

        public FavoriteViewHolder(View itemView){
            super(itemView);
            fromLanguageTitle = (TextView) itemView.findViewById(R.id.title_from_language);
            fromText = (TextView) itemView.findViewById(R.id.from_text);
            toLanguageTitle = (TextView) itemView.findViewById(R.id.title_to_language);
            toText = (TextView) itemView.findViewById(R.id.to_text);
            favourite = (LottieAnimationView) itemView.findViewById(R.id.favourite_amimation);
            fromLanguageFlag = (ImageView) itemView.findViewById(R.id.flag_from_language);
            toLanguageFlag = (ImageView) itemView.findViewById(R.id.flag_to_language);
            favoriteAnimation = false;
        }

        public void bind(List<FromResult> fromResults, int position) {
                fromLanguageTitle.setText(fromResults.get(position).getLanguageCode());
                fromText.setText(fromResults.get(position).getText());
                toLanguageTitle.setText(fromResults.get(position).getToResultList().get(0).getLanguageCode());
                toText.setText(fromResults.get(position).getToResultList().get(0).getText());

                if (fromResults.get(position).getFavoriteAnimation() == null) {
                    favoriteAnimation = false;
                } else {
                    favoriteAnimation = fromResults.get(position).getFavoriteAnimation();
                }

                favourite.setAnimation("favourite_animation.json");
                favourite.loop(false);

                if (!favoriteAnimation) {
                    favourite.reverseAnimation();
                } else {
                    favourite.playAnimation();
                }


                // TODO guardar la posicion en GreeDao mejor que dos for
                int fromPosAux = 0, toPosAux = 0;
                for (int i = 0; i < countryNames.length; i++) {
                    if (countryNames[i].equals(fromResults.get(position).getLanguageCode())) {
                        fromPosAux = i;
                    }
                }
                for (int j = 0; j < countryNames.length; j++) {
                    if (countryNames[j].equals(fromResults.get(position).getToResultList().get(0).getLanguageCode())) {
                        toPosAux = j;
                    }
                }
                fromLanguageFlag.setImageResource(flags[fromPosAux]);
                toLanguageFlag.setImageResource(flags[toPosAux]);

            favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(favoriteAnimation){
                        favourite.reverseAnimation();
                        fromResults.get(position).setFavoriteAnimation(false);
                        favoriteAnimation = false;

                    }else{
                        favourite.playAnimation();
                        fromResults.get(position).setFavoriteAnimation(true);
                        favoriteAnimation = true;

                    }
                    updateFavoriteItem(position);

                    DatabaseManager.getInstance().updateFromResult(fromResults.get(position));
                    DatabaseManager.getInstance().getAllResult();

                }
            });

            }




    }


}
