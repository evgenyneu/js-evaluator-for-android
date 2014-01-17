package com.evgenii.jsevaluator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.evgenii.jsevaluator.interfaces.CallJavaResultInterface;
import com.evgenii.jsevaluator.interfaces.WebViewWrapperInterface;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewWrapper implements WebViewWrapperInterface {
	protected WebView mWebView;

	public WebViewWrapper(Context context,
			CallJavaResultInterface callJavaResult) {
		mWebView = new WebView(context);
		mWebView.loadData("", "text/html", null);
		final WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		final JavaScriptInterface jsInterface = new JavaScriptInterface(
				callJavaResult);
		mWebView.addJavascriptInterface(jsInterface, JsEvaluator.JS_NAMESPACE);
	}

	@Override
	public void loadUrl(String url) {
		mWebView.loadUrl(url);
	}
}
