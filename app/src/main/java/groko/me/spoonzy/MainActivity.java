package groko.me.spoonzy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private final String strUrl = "https://groko.me/version-test/dashboard_new";

    @BindView(R.id.webView)
    WebView mWebView;
    @BindView(R.id.rlSplash)
    View rlSplash;
    @BindView(R.id.imgBackground)
    ImageView imgBackground;
    @BindView(R.id.tvRetry)
    TextView tvRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setStatusBarColor(Color.WHITE);
        }

        CookieManager.getInstance().setAcceptCookie(true);
        load();
    }

    @OnClick(R.id.tvRetry)
    public void onRetry() {
        load();
    }

    private void load() {
        tvRetry.setVisibility(View.GONE);
        if (isNetworkAvailable()) {
            imgBackground.setImageResource(R.drawable.loading);
            loadWebView();
        } else {
            imgBackground.setImageResource(R.drawable.no_internet_connection);
            tvRetry.setVisibility(View.VISIBLE);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void loadWebView() {

        final Activity activity = this;

        mWebView.setVisibility(View.VISIBLE);
        mWebView.setWebViewClient(new WebViewClient(){

            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.contains("android_asset")) {
                    return false;
                }

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }
            public void onLoadResource(WebView view, String url) {

            }
            public void onPageFinished(WebView view, String url) {
                rlSplash.setVisibility(View.GONE);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(strUrl);

    }
}
