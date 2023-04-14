package com.governmentjobonline;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.URI;
import java.net.URISyntaxException;

public class WebViewActivity extends AppCompatActivity {
    public ActionBar actionBar;

    private Toolbar toolbar;
    private String url;
    private WebView webView;
    private ProgressBar progressBar;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        url = getIntent().getStringExtra("url");

        progressBar =  findViewById(R.id.progressBar);
        initComponent();
        initToolbar();
        loadWebFromUrl();

        //pull to refresh

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        webView.reload();
                    }
                }, 2000);
            }
        });
    }
    private void initComponent() {
//        this.appbar_layout = (AppBarLayout) findViewById(C1820R.C1823id.appbar_layout);
        this.webView = (WebView) findViewById(R.id.webview);

    }

    private void initToolbar() {
        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar = toolbar2;
//        toolbar2.setNavigationIcon((int) C1820R.C1822drawable.ic_arrow_back);
//        this.toolbar.getNavigationIcon().setColorFilter(getResources().getColor(C1820R.C1821color.grey_80), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(this.toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        this.actionBar = supportActionBar;
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        this.actionBar.setHomeButtonEnabled(true);
        this.actionBar.setTitle((CharSequence) null);
//
//        Tools.changeOverflowMenuIconColor(this.toolbar, getResources().getColor(C1820R.C1821color.grey_80));
//        Tools.setSystemBarColor(this, 17170443);
//        Tools.setSystemBarLight(this);
    }

    private void loadWebFromUrl() {
        setDesktopMode(webView, true);
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setBuiltInZoomControls(true);
        this.webView.getSettings().setDisplayZoomControls(false);
        this.webView.getSettings().setDefaultTextEncodingName("utf-8");
        String newUA= "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
        this.webView.getSettings().setUserAgentString(newUA);
        this.webView.setBackgroundColor(0);
        this.webView.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
                super.onPageStarted(webView, str, bitmap);
                WebViewActivity.this.actionBar.setTitle((CharSequence) null);
                WebViewActivity.this.actionBar.setSubtitle(getHostName(str));
            }

            public void onPageFinished(WebView webView, String str) {
                super.onPageFinished(webView, str);
                WebViewActivity.this.actionBar.setTitle((CharSequence) webView.getTitle());

            }

        });
        this.webView.loadUrl(this.url);
        this.webView.setWebChromeClient(new MyChromeClient());
    }


    private String getHostName(String str) {
        try {
            String host = new URI(str).getHost();
            if (host.startsWith("www.")) {
                return host;
            }
            return "www." + host;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return str;
        }
    }
    public void setDesktopMode(WebView webView,boolean enabled) {
        String newUserAgent = webView.getSettings().getUserAgentString();
        if (enabled) {
            try {
                String ua = webView.getSettings().getUserAgentString();
                String androidOSString = webView.getSettings().getUserAgentString().substring(ua.indexOf("("), ua.indexOf(")") + 1);
                newUserAgent = webView.getSettings().getUserAgentString().replace(androidOSString, "(X11; Linux x86_64)");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            newUserAgent = null;
        }

        webView.getSettings().setUserAgentString(newUserAgent);
        webView.getSettings().setUseWideViewPort(enabled);
        webView.getSettings().setLoadWithOverviewMode(enabled);
        webView.reload();
    }
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.openBrowser) {
            directLinkToBrowser(this, this.url);
        } else if (menuItem.getItemId() == R.id.copyLink) {
            ((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("clipboard", this.webView.getUrl()));
            Toast.makeText(this, "Url Copied", Toast.LENGTH_SHORT).show();
        } else if (menuItem.getItemId() == R.id.refresh) {
            swipeRefreshLayout.setRefreshing(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                    webView.reload();
                }
            }, 2000);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void directLinkToBrowser(Activity activity, String str) {
        if (!URLUtil.isValidUrl(str)) {
            Toast.makeText(activity, "Ops, Cannot open url", Toast.LENGTH_SHORT).show();
        } else {
            activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.webview_option_menu, menu);
        return true;
    }

    private class MyChromeClient extends WebChromeClient {
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        MyChromeClient() {
        }

        public void onProgressChanged(WebView webView, int i) {
            super.onProgressChanged(webView, i);
            if (i >= 100) {
                WebViewActivity.this.actionBar.setTitle((CharSequence) webView.getTitle());
            }
            if (i < 100 && progressBar.getVisibility() == ProgressBar.GONE) {
                progressBar.setVisibility(ProgressBar.VISIBLE);
            }

            if (i == 100) {
                progressBar.setVisibility(ProgressBar.GONE);
            }
            progressBar.setProgress(i);

        }

        public Bitmap getDefaultVideoPoster() {
            if (this.mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(WebViewActivity.this.getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView() {
            ((FrameLayout) WebViewActivity.this.getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            WebViewActivity.this.getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            WebViewActivity.this.setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View view, WebChromeClient.CustomViewCallback customViewCallback) {
            if (this.mCustomView != null) {
                onHideCustomView();
                return;
            }
            this.mCustomView = view;
            view.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
            this.mOriginalSystemUiVisibility = WebViewActivity.this.getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = WebViewActivity.this.getRequestedOrientation();
            this.mCustomViewCallback = customViewCallback;
            ((FrameLayout) WebViewActivity.this.getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            WebViewActivity.this.getWindow().getDecorView().setSystemUiVisibility(3846);
        }
    }

    public void onResume() {
        this.webView.onResume();
        super.onResume();
    }

    public void onPause() {
        this.webView.onPause();
        super.onPause();
    }
}