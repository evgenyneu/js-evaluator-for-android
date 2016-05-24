package com.evgenii.jsevaluator;

import java.io.UnsupportedEncodingException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Base64;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.evgenii.jsevaluator.interfaces.CallJavaResultInterface;
import com.evgenii.jsevaluator.interfaces.WebViewWrapperInterface;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewWrapper implements WebViewWrapperInterface {
	protected WebView mWebView;

	public WebViewWrapper(Context context, CallJavaResultInterface callJavaResult) {
		mWebView = new WebView(context);

		// web view will not draw anything - turn on optimizations
		mWebView.setWillNotDraw(true);

		final WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDefaultTextEncodingName("utf-8");
		final JavaScriptInterface jsInterface = new JavaScriptInterface(callJavaResult);
		mWebView.addJavascriptInterface(jsInterface, JsEvaluator.JS_NAMESPACE);
	}

	@Override
	public void loadJavaScript(String javascript) {
		byte[] data;
		try {
			javascript = "<script>" + javascript + "</script>";
			data = javascript.getBytes("UTF-8");
			final String base64 = Base64.encodeToString(data, Base64.DEFAULT);
			mWebView.loadUrl("data:text/html;charset=utf-8;base64," + base64);
		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	// Destroys the web view in order to free the memory
	// The web view can not be accessed after is has been destroyed
    // To check open the page in Chrome: chrome://inspect/#devices
	public void destroy() {
		if(mWebView != null) {
			mWebView.removeJavascriptInterface(JsEvaluator.JS_NAMESPACE);
			mWebView.loadUrl("about:blank");
			mWebView.stopLoading();

			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
				mWebView.freeMemory();
			}

			mWebView.clearHistory();
			mWebView.removeAllViews();
			mWebView.destroyDrawingCache();
			mWebView.destroy();

			mWebView = null;
		}
	}

	// Returns the WebView object
	public WebView getWebView() {
		return mWebView;
	}
}
