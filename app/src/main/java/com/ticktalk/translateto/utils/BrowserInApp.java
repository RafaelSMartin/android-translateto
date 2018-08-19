package com.ticktalk.translateto.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ticktalk.translateto.setting.SettingActivity;
import com.ticktalk.translateto.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Rafael S. Martin
 */

public class BrowserInApp extends AppCompatActivity implements Toolbar.OnMenuItemClickListener
{

    @BindView(R.id.webView)
    WebView myWebView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browser_inapp);

        ButterKnife.bind(this);

        initToolbar();

        webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new WebViewClient());

//        myWebView.getSettings().setJavaScriptEnabled(true);

//        myWebView.loadUrl("https://www.ticktalksoft.com/");
//        myWebView.loadUrl("https://www.sandbox.paypal.com/webapps/hermes?token=8W183882JK2841005&useraction=commit&rm=2&mfid=1515760206197_c7895894ccce#/checkout/login");
        myWebView.loadUrl("https://www.sandbox.paypal.com/");

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    goBack();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initToolbar() {
        toolbar.inflateMenu(R.menu.menu_toolbar);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                SettingActivity.startSettingActivity(this);
                break;

        }
        return true;
    }

    private void goBack(){
        if (myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            finish();
        }
    }

}