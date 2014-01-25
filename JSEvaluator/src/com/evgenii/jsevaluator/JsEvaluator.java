package com.evgenii.jsevaluator;

import java.util.ArrayList;

import android.content.Context;

import com.evgenii.jsevaluator.interfaces.CallJavaResultInterface;
import com.evgenii.jsevaluator.interfaces.JsCallback;
import com.evgenii.jsevaluator.interfaces.WebViewWrapperInterface;

public class JsEvaluator implements CallJavaResultInterface {
	public final static String JS_NAMESPACE = "evgeniiJsEvaluator";

	public static String escapeSingleQuotes(String str) {
		return str.replace("'", "\\'");
	}

	public static String getJsForEval(String jsCode, int callbackIndex) {
		jsCode = escapeSingleQuotes(jsCode);
		return String.format("%s.returnResultToJava(eval('%s'), %s);",
				JS_NAMESPACE, jsCode, callbackIndex);
	}

	protected WebViewWrapperInterface mWebViewWrapper;

	private final Context mContext;

	private final ArrayList<JsCallback> mResultCallbacks = new ArrayList<JsCallback>();

	public JsEvaluator(Context context) {
		mContext = context;
	}

	public void evaluate(String jsCode, JsCallback resultCallback) {
		String js = JsEvaluator.getJsForEval(jsCode, mResultCallbacks.size());
		js = String.format("javascript: %s", js);
		mResultCallbacks.add(resultCallback);
		getWebViewWrapper().loadUrl(js);
	}

	public ArrayList<JsCallback> getResultCallbacks() {
		return mResultCallbacks;
	}

	public WebViewWrapperInterface getWebViewWrapper() {
		if (mWebViewWrapper == null) {
			mWebViewWrapper = new WebViewWrapper(mContext, this);
		}
		return mWebViewWrapper;
	}

	@Override
	public void jsCallFinished(String value, Integer callIndex) {
		// TODO Auto-generated method stub

	}

	// Used in test only to replace webViewWrapper with a mock
	public void setWebViewWrapper(WebViewWrapperInterface webViewWrapper) {
		mWebViewWrapper = webViewWrapper;
	}
}
