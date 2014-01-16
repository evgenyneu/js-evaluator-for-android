package com.evgenii.jsevaluator;

import java.util.ArrayList;

import android.content.Context;

import com.evgenii.jsevaluator.interfaces.JsCallback;

public class JsEvaluator {
	private final static String JS_RESULT_VAR_NAME = "evgeniiJsEvaluatorResult";

	public static String escapeSingleQuotes(String str) {
		return str.replace("'", "\\'");
	}

	public static String getJsForEval(String jsCode, int callbackIndex) {
		jsCode = escapeSingleQuotes(jsCode);
		return String.format(
				"evgeniiJsEvaluator.returnResultToJava(eval('%s'), %s);",
				jsCode, callbackIndex);
	}

	private final Context mContext;

	private final ArrayList<JsCallback> mResultCallbacks = new ArrayList<JsCallback>();

	public JsEvaluator(Context context) {
		mContext = context;
	}

	public void evaluate(String jsCode, JsCallback resultCallback) {
		final String js = JsEvaluator.getJsForEval(jsCode,
				mResultCallbacks.size());
		mResultCallbacks.add(resultCallback);
		// getWebView().loadUrl(js);
	}

	public ArrayList<JsCallback> getResultCallbacks() {
		return mResultCallbacks;
	}
}
