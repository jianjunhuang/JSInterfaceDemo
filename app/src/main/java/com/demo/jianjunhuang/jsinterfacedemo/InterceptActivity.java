package com.demo.jianjunhuang.jsinterfacedemo;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jianjunhuang on 18-1-13.
 */

public class InterceptActivity extends AppCompatActivity {
    private static final String TAG = "InterceptActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView webView = new WebView(this);

        if (NetWorkUtils.isNetworkConnected(getApplicationContext())) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//                String url = request.getUrl().toString();
//                if (url.contains(".png") || url.contains(".jpg") || url.contains(".gif") || url.contains("avatar") || url.contains(".jpeg")) {
//                    InputStream is = null;
//                    try {
//                        is = getApplicationContext().getAssets().open("ic_launcher.png");
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    WebResourceResponse response = new WebResourceResponse("image/png", "utf-8", is);
//                    return response;
//                }
//                return super.shouldInterceptRequest(view, request);
//            }
//
//            @Override
//            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//                if (url.contains(".png") || url.contains(".jpg") || url.contains(".gif") || url.contains("avatar") || url.contains(".jpeg")) {
//                    InputStream is = null;
//                    try {
//                        is = getApplicationContext().getAssets().open("ic_launcher.png");
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    WebResourceResponse response = new WebResourceResponse("image/png", "utf-8", is);
//                    return response;
//                }
//                return super.shouldInterceptRequest(view, url);
//            }
        });
        webView.loadUrl("http://www.dgtle.com/portal.php");
        setContentView(webView);
    }
}
