package com.ticktalk.translateto.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.ticktalk.translateto.R;
import com.ticktalk.translateto.fragments.CustomDialog;
import com.ticktalk.translateto.fragments.ProfileFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rafael S. Martin
 */

public class ProfileActivity extends AppCompatActivity implements
        Toolbar.OnMenuItemClickListener,
        CustomDialog.OnCompleteListener
{
    ProfileFragment profileFragment;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public static void startProfileActivity(Activity startingActivity)
    {
        Intent intent = new Intent(startingActivity, ProfileActivity.class);
        startingActivity.startActivity(intent);
        startingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        initToolbar();

        profileFragment = (ProfileFragment) getSupportFragmentManager().findFragmentByTag(ProfileFragment.TAG);

        if(profileFragment == null) {
            profileFragment = new ProfileFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.hold, R.anim.hold, R.anim.slide_out_right)
                    .replace(R.id.profile_fragment_root, profileFragment, ProfileFragment.TAG)
                    .commit();

//            FragmentHelper.FragmentHelperBuilder.create(this)
//                    .layout(R.id.profile_fragment_root)
//                    .fragment(profileFragment)
//                    .tag(ProfileFragment.TAG)
//                    .replace();

        }

    }

    private void initToolbar(){
        toolbar.inflateMenu(R.menu.menu_toolbar);
        toolbar.setOnMenuItemClickListener(this);
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

            case R.id.favorite:
                FavoriteActivity.startFavoriteActivity(this);
                break;
        }
        return true;
    }

    @Override
    public void onCompleteCustomDialog(String text, int modo) {
        Log.d("ProfileActivity", text);
        Log.d("ProfileActivity", modo+"");
        profileFragment.onCompleteCustomDialog(text, modo);
    }





}