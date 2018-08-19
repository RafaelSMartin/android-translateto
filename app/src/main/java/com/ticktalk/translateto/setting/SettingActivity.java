package com.ticktalk.translateto.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.ticktalk.translateto.favorite.FavoriteActivity;
import com.ticktalk.translateto.history.HistoryActivity;
import com.ticktalk.translateto.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SettingActivity extends AppCompatActivity implements
        Toolbar.OnMenuItemClickListener
{

    SettingFragment settingFragment;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    public static void startSettingActivity(Activity startingActivity)
    {

        Intent intent = new Intent(startingActivity, SettingActivity.class);
        startingActivity.startActivity(intent);
//        startingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        initToolbar();


        settingFragment = (SettingFragment) getSupportFragmentManager().findFragmentByTag(SettingFragment.TAG);
        if(settingFragment == null) {
            settingFragment = new SettingFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.hold, R.anim.hold, R.anim.slide_out_right)
                    .replace(R.id.setting_fragment_root, settingFragment, SettingFragment.TAG)
                    .commit();

//            FragmentHelper.FragmentHelperBuilder.create(this)
//                    .fragment(settingFragment)
//                    .layout(R.id.setting_fragment_root)
//                    .tag(SettingFragment.TAG)
//                    .replace();
        }

    }

    private void initToolbar(){
        toolbar.inflateMenu(R.menu.menu_toolbar);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setTitle("Settings");
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
                break;

            case R.id.history:
                HistoryActivity.startHistoryActivity(this);
                break;

            case R.id.favorite:
                FavoriteActivity.startFavoriteActivity(this);
                break;
        }
        return true;
    }



}
