package com.library.jianjunhuang.jsbridge;

import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.library.jianjunhuang.jsbridge.bean.Data;

import java.lang.reflect.Method;

/**
 * Created by jianjunhuang on 18-1-13.
 * <p>
 * 该类提供了默认的 {@link #postMessage(String)} 方法让前端调用，前端传一个 {@link Data}类型的 JSON 数据。
 * 客户端把 JSON 数据解析成 {@link Data} 对象，获取里面的 Api 信息，再调用当前类中的同名方法。
 * 若找不到该方法，客户端会自动回调给网页端发送错误信息。
 * <p>
 * 与前端共同定义的方法可以不带返回值，但是，最后必须调用 receive(Data) 方法，把数据回调给前端，
 * 这里没有自动处理，主要是如果涉及到异步操作时不好处理。所以只能麻烦一下，在定义方法的时候加上 receive(Data) 方法了
 * <p>
 * 如果前端不用默认的 {@link #postMessage(String)} 可以继承后创建新的方法（记得加上 {@link JavascriptInterface}）
 * 然后调用{@link #postMessage(String)}，即可。
 */

public abstract class AbstractJSBridge {

    private WebView webView;
    private Gson gson;
    private String jsObjName;
    private String callbackMethodName;

    public static final int ERR_CODE = -1;
    public static final int METHOD_NOT_FOUND = -2;

    /**
     *
     * @param webView use to invoke JS method
     * @param jsObjName JS Object name
     * @param callbackMethodName JS callback method
     */
    public AbstractJSBridge(WebView webView, String jsObjName, @NonNull String callbackMethodName) {
        this.webView = webView;
        gson = new Gson();
        this.jsObjName = jsObjName;
        this.callbackMethodName = callbackMethodName;
    }

    private static final String TAG = "AbstractJSBridge";

    /**
     * expose this method to the FRONT_END.
     * When it is invoked by the FRONT_END ,it will invoke the specified method automatically.
     * but make sure your method's parameter must be only Data, if not it will throw a RuntimeException.
     * and if not found the specified method ,it will send a err msg to FRONT_END
     *
     * @param msg msg from the FRONT_END
     */
    @JavascriptInterface
    public void postMessage(String msg) {
        Data data = null;
        try {
            data = gson.fromJson(msg, Data.class);
        } catch (Exception e) {
            e.printStackTrace();
            data = new Data();
            data.setData(null);
            data.setStatus(ERR_CODE);
            data.setMessage("json can't convert to Object , may be parameter name not match , please check it out");
            data.setCallback(ERR_CODE);
            data.setApi(null);
            receive(data);
            return;
        }
        Method[] methods = getClass().getMethods();
        boolean tag = false;
        for (Method method : methods) {

            if (method.getName().equals(data.getApi())) {

                if (method.getParameterTypes().length > 1 && method.getParameterTypes()[0] != Data.class) {
                    throw new RuntimeException(method.getName() + "() parameter type is not Data or parameter amount > 1. make sure your methods' parameter are only have Data type");
                }
                try {
                    method.invoke(this, data);
                    tag = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    data.setStatus(ERR_CODE);
                    data.setMessage("can't invoke " + data.getApi() + "() , make sure client had define this method!");
                    data.setCallback(ERR_CODE);
                    receive(data);
                    return;
                }
            }
        }
        if (!tag) {
            Exception e = new Exception("we have not " + data.getApi() + "() method,please check your method name or tell client check their method name");
            e.printStackTrace();
            data.setStatus(METHOD_NOT_FOUND);
            data.setMessage(e.getMessage());
            receive(data);
        }

    }

    /**
     * invoke the JS to return data to FRONT_END.
     * you should invoke this method in your method,which in the subclass. so FRONT_END can get the data.
     *
     * @param data {@link Data}
     */
    protected void receive(final Data data) {
        webView.post(new Runnable() {
                         @Override
                         public void run() {
                             try {
                                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                     webView.evaluateJavascript(getJSUrl(data), new ValueCallback<String>() {
                                         @Override
                                         public void onReceiveValue(String value) {
                                         }
                                     });
                                 } else {
                                     webView.loadUrl(getJSUrl(data));
                                 }
                             } catch (Exception e) {
                                 e.printStackTrace();
                             }
                         }
                     }
        );

    }

    /**
     * generate JavaScript url string to execute
     *
     * @param data {@link Data}
     * @return the JavaScript url string to execute
     * @throws Exception if {@link #callbackMethodName} is not or empty it will throw a RuntimeException
     */
    private String getJSUrl(Data data) throws Exception {
        StringBuilder urlSb = new StringBuilder("javascript:");
        if (TextUtils.isEmpty(callbackMethodName)) {
            throw new RuntimeException("callback method did not define!!");
        }
        if (TextUtils.isEmpty(jsObjName)) {
            urlSb.append(callbackMethodName)
                    .append("(")
                    .append(gson.toJson(data))
                    .append(")");
        } else {
            urlSb.append(jsObjName)
                    .append(".")
                    .append(callbackMethodName)
                    .append("(")
                    .append(gson.toJson(data))
                    .append(")");
        }
        return urlSb.toString();
    }

}
