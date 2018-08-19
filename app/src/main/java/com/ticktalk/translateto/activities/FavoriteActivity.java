package com.ticktalk.translateto.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.ticktalk.translateto.fragments.FavoriteFragment;
import com.ticktalk.translateto.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rafael S. Martin
 */

public class FavoriteActivity extends AppCompatActivity implements
        Toolbar.OnMenuItemClickListener
{

    private FavoriteFragment favoriteFragment;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public static void startFavoriteActivity(Activity startingActivity){

        Intent intent = new Intent(startingActivity, FavoriteActivity.class);
        startingActivity.startActivity(intent);
        startingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);

        initToolbar();

        launchFavoriteFragment();


    }

    private void launchFavoriteFragment(){
        favoriteFragment = (FavoriteFragment) getSupportFragmentManager().findFragmentByTag(FavoriteFragment.TAG);
        if(favoriteFragment == null) {
            favoriteFragment = new FavoriteFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.hold, R.anim.hold, R.anim.slide_out_right)
                    .replace(R.id.history_fragment_root, favoriteFragment, FavoriteFragment.TAG)
                    .commit();
        }
    }

    private void initToolbar(){
        toolbar.inflateMenu(R.menu.menu_favorite);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setTitle("Favorites");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                SettingActivity.startSettingActivity(this);
                break;

            case R.id.history:
                HistoryActivity.startHistoryActivity(this);
                break;

        }
        return true;
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

}
