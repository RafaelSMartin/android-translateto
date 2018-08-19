package com.ticktalk.translateto.history;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ticktalk.translateto.MainActivity;
import com.ticktalk.translateto.database.DatabaseManager;
import com.ticktalk.translateto.favorite.FavoriteActivity;
import com.ticktalk.translateto.setting.SettingActivity;
import com.ticktalk.translateto.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rafael S. Martin
 */

public class HistoryActivity extends AppCompatActivity implements
        Toolbar.OnMenuItemClickListener
{

    private HistoryFragment historyFragment;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public static void startHistoryActivity(Activity startingActivity){

        Intent intent = new Intent(startingActivity, HistoryActivity.class);
        startingActivity.startActivity(intent);
        startingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);

        initToolbar();

        launchHistoryFragment();


    }

    private void launchHistoryFragment(){
        historyFragment = (HistoryFragment) getSupportFragmentManager().findFragmentByTag(HistoryFragment.TAG);
        if(historyFragment == null) {
            historyFragment = new HistoryFragment();

            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.hold, R.anim.hold, R.anim.slide_out_right)
                    .replace(R.id.history_fragment_root, historyFragment, HistoryFragment.TAG)
                    .commit();

        }
    }

    private void initToolbar(){
        toolbar.inflateMenu(R.menu.menu_history);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setTitle("History");
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

            case R.id.favorite:
                FavoriteActivity.startFavoriteActivity(this);
                break;

            case R.id.delete:
                if(!DatabaseManager.getInstance().isClearedDatabase()){
                    showOpenClearHistory();
                }
                break;
        }
        return true;
    }

    private void showHistoryHasBeenCleared()
    {
        new MaterialDialog.Builder(this)
                .content(R.string.all_history_has_been_cleared)
                .positiveText(R.string.close)
                .onPositive((dialog, which) -> {
                    Intent intent = new Intent(HistoryActivity.this, HistoryActivity.class);
                    HistoryActivity.this.startActivity(intent);
                    HistoryActivity.this.finish();
                })
                .show();

    }

    public void showOpenClearHistory()
    {

        new MaterialDialog.Builder(this)
                .iconRes(R.drawable.ic_delete)
                .title(R.string.app_name)
                .content(R.string.clear_history_question)
                .positiveText(R.string.Ok)
                .onPositive((dialog, which) -> {
                    DatabaseManager.getInstance().clear();
                    showHistoryHasBeenCleared();
                })
                .negativeText(R.string.no)
                .show();
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
