package com.demo.jianjunhuang.jsinterfacedemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.library.jianjunhuang.jsbridge.AbstractJSBridge;
import com.library.jianjunhuang.jsbridge.bean.Data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jianjunhuang on 18-1-13.
 */

public class LibExampleActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.addJavascriptInterface(new LibJSBridge(webView, "TentcooJSBridge", "receive"), "nativeBridge");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        setContentView(webView);
        webView.loadUrl("http://10.11.3.199/html/study.html");
    }

    class LibJSBridge extends AbstractJSBridge {
        private static final String TAG = "LibJSBridge";

        public LibJSBridge(WebView webView, String jsObjName, String callbackMethodName) {
            super(webView, jsObjName, callbackMethodName);
        }

        public void getToken(final Data data) {
            data.setMessage("success");
            Token token = new Token();
            token.setToken("123456");
            data.setData(token);
            data.setStatus(1);
            receive(data);
        }

        public void getNickName(final Data data) {
            data.setMessage("success");
            Usr usr = new Usr();
            usr.setNickName("hahaha");
            data.setData(usr);
            data.setStatus(1);
            receive(data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
