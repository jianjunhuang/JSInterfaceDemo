package com.demo.jianjunhuang.jsinterfacedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class JSInterfaceActivity extends AppCompatActivity {

    private WebView webView;
    private EditText editText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        webView = findViewById(R.id.webview);
        editText = findViewById(R.id.input_edt);
        button = findViewById(R.id.show_btn);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        webView.addJavascriptInterface(new JSInterface(), "nativeBridge");
        webView.loadUrl("http://10.11.3.199/html/study.html");
//        webView.addJavascriptInterface(new JSInterface(), "jstest");
//        webView.loadUrl("file:///android_asset/test.html");
        webView.setWebChromeClient(new WebChromeClient() {
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                webView.loadUrl("javascript:show(\"" + editText.getText() + "\")");
//                JSONObject jsonObject = new JSONObject();
//                try {
//                    jsonObject.put("name", "jianjunhuang");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                webView.loadUrl("javascript:getJson('" + jsonObject + "')");
                try {
                    JSONObject jsonObject = new JSONObject("{'data':'" + editText.getText().toString() + "'}");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                webView.loadUrl("javascript:JSBridge.receive()");
            }
        });

    }

    class JSInterface {
        private static final String TAG = "JSInterface";

        @JavascriptInterface
        public void postMessage(final String msg) {
            Toast.makeText(JSInterfaceActivity.this, msg, Toast.LENGTH_SHORT).show();
            Log.i(TAG, "postMessage: " + msg);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject();
                JSONObject token = new JSONObject();
                token.put("token", "123456789");
                jsonObject.put("data", token);
                jsonObject.put("api", "getToken");
                jsonObject.put("callback", 1);
                jsonObject.put("message", "success");
                jsonObject.put("status", 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final JSONObject finalJsonObject = jsonObject;
            Log.i(TAG, "postMessage: " + jsonObject.toString());
            //所有的 webView 方法必须在同一线程使用，当前 JSInterface 里应该不是同一线程
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:JSBridge.receive(" + finalJsonObject + ")");
                }
            });
        }
//        @JavascriptInterface
//        public String showToast(String msg) {
//            Toast.makeText(JSInterfaceActivity.this, msg, Toast.LENGTH_SHORT).show();
//            return msg;
//        }
    }
}
