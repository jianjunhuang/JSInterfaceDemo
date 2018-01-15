package com.demo.jianjunhuang.jsinterfacedemo;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by jianjunhuang on 18-1-12.
 */

public class UrlActivity extends AppCompatActivity {

    private WebView webView;
    private EditText editText;
    private Button button;
    private static final String TAG = "UrlActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        webView = findViewById(R.id.webview);
        editText = findViewById(R.id.input_edt);
        button = findViewById(R.id.show_btn);


        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebViewClient(new WebViewClient() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                if (uri.getScheme().equals("tentcoobridge")) {

                    if (uri.getAuthority().equals("getUserInfo")) {
                        Set<String> collection = uri.getQueryParameterNames();
                        Toast.makeText(UrlActivity.this, uri.getQuery(), Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "shouldOverrideUrlLoading: " + uri.getQuery());
                        JSONObject jsonObject = new JSONObject();
                        for (String str : collection) {
                            Log.i(TAG, "key: " + str);
                            try {
                                Log.i(TAG, "value: "+uri.getQueryParameter(str));
                                jsonObject.put(str,uri.getQueryParameter(str));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                        webView.evaluateJavascript("javascript:JSBridge.receive({'callback':1,'hello':'JSBridge'})", new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {

                            }
                        });
                    }
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Uri uri = Uri.parse(url);
                if (uri.getScheme().equals("tentcoobridge")) {

                    if (uri.getAuthority().equals("getUserInfo")) {
//                        Set<String> collection = uri.getQueryParameterNames();
                        Toast.makeText(UrlActivity.this, uri.getQuery(), Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "shouldOverrideUrlLoading: " + uri.getQuery());
//                        for (String str : collection) {
//                            Log.i(TAG, "shouldOverrideUrlLoading: " + str);
//                        }
                    }
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        webView.loadUrl("http://10.11.3.199/html/study.html");
        webView.setWebChromeClient(new WebChromeClient() {
        });
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
//                webView.loadUrl("javascript:invokeUrl(\"" + editText.getText() + "\")");
//                webView.evaluateJavascript("javascript:invokeUrl(\"" + editText.getText() + "\")", new ValueCallback<String>() {
//                    @Override
//                    public void onReceiveValue(String value) {
//                        Log.i(TAG, "onReceiveValue: "+value);
//                    }
//                });
            }
        });
    }
}
