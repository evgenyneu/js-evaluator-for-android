package com.evgenii.jsevaluator;

import java.io.UnsupportedEncodingException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.evgenii.jsevaluator.interfaces.CallJavaResultInterface;
import com.evgenii.jsevaluator.interfaces.HandlerWrapperInterface;
import com.evgenii.jsevaluator.interfaces.WebViewWrapperInterface;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewWrapper implements WebViewWrapperInterface {
	protected WebView mWebView;

    protected final HandlerWrapperInterface mMainThreadHandler;

    public WebViewWrapper(final Context context, final CallJavaResultInterface callJavaResult){
        this(context,callJavaResult, new HandlerWrapper(new Handler(Looper.getMainLooper())));
    }
	public WebViewWrapper(final Context context, final CallJavaResultInterface callJavaResult, HandlerWrapperInterface mainThreadHandler) {
        mMainThreadHandler = mainThreadHandler;
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                mWebView = new WebView(context);

                // web view will not draw anything - turn on optimizations
                mWebView.setWillNotDraw(true);

                final WebSettings webSettings = mWebView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webSettings.setDefaultTextEncodingName("utf-8");
                final JavaScriptInterface jsInterface = new JavaScriptInterface(callJavaResult);
                mWebView.addJavascriptInterface(jsInterface, JsEvaluator.JS_NAMESPACE);
            }
        });

	}

	@Override
	public void loadJavaScript(final String javascript) {
        mMainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                byte[] data;
                try {
                    data = ("<script>" + javascript + "</script>").getBytes("UTF-8");
                    final String base64 = Base64.encodeToString(data, Base64.DEFAULT);
                    mWebView.loadUrl("data:text/html;charset=utf-8;base64," + base64);
                } catch (final UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });


	}
}
