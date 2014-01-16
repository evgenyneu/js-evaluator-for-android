package com.evgenii.jsevaluator;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.evgenii.jsevaluator.exceptions.InitialJsHasAlreadyBeenRun;
import com.evgenii.jsevaluator.interfaces.JsCallback;
import com.evgenii.jsevaluator.interfaces.JsRunnerCallbackInterface;

/**
 * Executes JavaScript code. Loads JS code. Calls a JavaScript function. Allows
 * to pass an argument to function call. Stores the function's return value.
 */
public class JsRunner implements JsRunnerCallbackInterface {
	public static String getJsForFunctionCall(String functionCallStr,
			Integer callbackIndex) {
		return String.format("javascript: %s.%s(%s, %s);", JS_NAMESPACE,
				JS_RESULT_FUNCTION, functionCallStr, callbackIndex);
	}

	private final Context mContext;
	private WebView mWebView;
	private String mInitialJSConcatenated = "";
	private boolean mInitialJsEvaluationStarted;
	private boolean mInitialJsEvaluationFinished;
	private JavaScriptInterface mJsInterface;
	private final static String JS_NAMESPACE = "AESCrypto";
	private final static String JS_RESULT_FUNCTION = "result";

	private final static String INITIAL_JS_EXECUTED_CALLBACK = JS_NAMESPACE
			+ ".initialJsExecuted(); ";

	private final ArrayList<JsCallback> mJsCallbacks = new ArrayList<JsCallback>();

	private final ArrayList<JsFunctionCall> mPendingJsCalls = new ArrayList<JsFunctionCall>();

	public JsRunner(Context context) {
		mContext = context;
	}

	public void addInitialJs(String js) throws InitialJsHasAlreadyBeenRun {
		if (mInitialJsEvaluationStarted)
			throw new InitialJsHasAlreadyBeenRun();

		mInitialJSConcatenated += " " + js;
	}

	public void executeAllPendingJs() {
		if (mPendingJsCalls.size() == 0)
			return;

		while (mPendingJsCalls.size() > 0) {
			final JsFunctionCall jsFunctionCall = mPendingJsCalls
					.get(mPendingJsCalls.size() - 1);

			mPendingJsCalls.remove(mPendingJsCalls.size() - 1);

			final String js = JsRunner.getJsForFunctionCall(
					jsFunctionCall.toString(),
					jsFunctionCall.getCallbackIndex());

			getWebView().loadUrl(js);
		}
	}

	public String getCompleteInitialJsToEvaluate() {
		return String.format("javascript: %s %s", getInitialJSConcatenated(),
				INITIAL_JS_EXECUTED_CALLBACK);
	}

	public String getInitialJSConcatenated() {
		return mInitialJSConcatenated;
	}

	public boolean getInitialJsEvaluationFinished() {
		return mInitialJsEvaluationFinished;
	}

	public boolean getInitialJsEvaluationStarted() {
		return mInitialJsEvaluationStarted;
	}

	public ArrayList<JsCallback> getJsCallbacks() {
		return mJsCallbacks;
	}

	public ArrayList<JsFunctionCall> getPendingJsCalls() {
		return mPendingJsCalls;
	}

	public WebView getWebView() {
		initWebView();
		return mWebView;
	}

	@Override
	public void initalJsEvaluationHasFinished() {
		mInitialJsEvaluationFinished = true;
		executeAllPendingJs();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView() {
		if (mWebView != null)
			return;

		mWebView = new WebView(mContext);
		mWebView.loadData("", "text/html", null);
		final WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		mJsInterface = new JavaScriptInterface(this);
		mWebView.addJavascriptInterface(mJsInterface, JS_NAMESPACE);
	}

	@Override
	public void jsCallFinished(String value, Integer callbackIndex) {
		final JsCallback callback = mJsCallbacks.get(callbackIndex);
		callback.onResult(value);
	}

	public void runInitialJs() {
		mInitialJsEvaluationStarted = true;
		getWebView().loadUrl(getCompleteInitialJsToEvaluate());
	}

	public void runJsFunction(String name, String param, JsCallback callback) {
		final ArrayList<Object> params = new ArrayList<Object>();
		params.add(param);

		getPendingJsCalls().add(
				new JsFunctionCall(name, params, getJsCallbacks().size()));
		getJsCallbacks().add(callback);

		if (!getInitialJsEvaluationFinished())
			return;

		executeAllPendingJs();
	}
}
