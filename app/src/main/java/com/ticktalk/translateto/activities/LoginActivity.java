package com.ticktalk.translateto.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ticktalk.translateto.R;
import com.ticktalk.translateto.fragments.LoginFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rafael S. Martin
 */

public class LoginActivity extends AppCompatActivity implements
        Toolbar.OnMenuItemClickListener
{

    LoginFragment loginFragment;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public static void startLoginActivity(Activity startingActivity){
        Intent intent = new Intent(startingActivity, LoginActivity.class);
        startingActivity.startActivity(intent);
        startingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initToolbar();

        loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag(LoginFragment.TAG);

        if(loginFragment == null){
            loginFragment = new LoginFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.hold, R.anim.hold, R.anim.slide_out_right)
                    .replace(R.id.login_fragment_root, loginFragment, LoginFragment.TAG)
                    .commit();

//            FragmentHelper.FragmentHelperBuilder.create(this)
//                    .fragment(loginFragment)
//                    .layout(R.id.login_fragment_root)
//                    .tag(LoginFragment.TAG)
//                    .replace();

        }
    }

    private void initToolbar(){
        toolbar.inflateMenu(R.menu.menu_toolbar);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
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
