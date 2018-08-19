package com.ticktalk.translateto.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.ticktalk.translateto.R;
import com.ticktalk.translateto.fragments.SignUpFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rafael S. Martin
 */

public class SignUpActivity extends AppCompatActivity implements
        Toolbar.OnMenuItemClickListener
{
    SignUpFragment signUpFragment;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public static void startSignUpActivity(Activity startingActivity)
    {
        Intent intent = new Intent(startingActivity, SignUpActivity.class);
        startingActivity.startActivity(intent);
        startingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        initToolbar();

        signUpFragment = (SignUpFragment) getSupportFragmentManager().findFragmentByTag(SignUpFragment.TAG);
        if(signUpFragment == null){
            signUpFragment = new SignUpFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.hold, R.anim.hold, R.anim.slide_out_right)
                    .replace(R.id.signup_fragment_root, signUpFragment, SignUpFragment.TAG)
                    .commit();
        }
    }

    private void initToolbar(){
        toolbar.inflateMenu(R.menu.menu_toolbar);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setTitle("SignUp");
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








}
