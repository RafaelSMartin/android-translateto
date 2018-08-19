package com.ticktalk.moreapplib;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.Pivot;

import java.util.List;

public abstract class MoreAppBasedActivity extends AppCompatActivity {
    MoreAppAdapter moreAppAdapter;

    ImageView backImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_more_app);

        backImage = (ImageView) findViewById(R.id.back_image_view);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        moreAppAdapter = new MoreAppAdapter(this, createMoreApps());
        moreAppAdapter.SetListener(new MoreAppAdapter.MoreAppListener() {
            @Override
            public void onClickedMoreAppItem(String packageName) {
                launchAppOrPlayStore(packageName);
            }
        });

        InfiniteScrollAdapter wrapper = InfiniteScrollAdapter.wrap(moreAppAdapter);

        DiscreteScrollView scrollView = (DiscreteScrollView) findViewById(R.id.picker);
        scrollView.setAdapter(wrapper);

        scrollView.setItemTransformer(new MoreAppTransformer.Builder()
                .setMaxScale(1.05f)
                .setMinScale(0.91f)
                .setPivotX(Pivot.X.CENTER)
                .setPivotY(Pivot.Y.CENTER)
                .build());

    }

    private void showPlayStoreForApp(String packageName)
    {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    private boolean isAppInstalled(String packageName) {
        try {
            getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void launchApp(String packageName)
    {
        PackageManager pm = getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(packageName);
        startActivity(intent);
    }

    private void launchAppOrPlayStore(String packageName)
    {
        if(isAppInstalled(packageName))
            launchApp(packageName);
        else
            showPlayStoreForApp(packageName);
    }


    public abstract List<MoreAppModel> createMoreApps();
}

