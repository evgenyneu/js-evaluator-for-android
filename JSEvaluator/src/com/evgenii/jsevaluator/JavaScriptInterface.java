package com.evgenii.jsevaluator;

import android.webkit.JavascriptInterface;

import com.evgenii.jsevaluator.interfaces.CallJavaResultInterface;
import com.evgenii.jsevaluator.interfaces.JsRunnerCallbackInterface;

/**
 * Passed in addJavascriptInterface of WebView to allow web views's JS execute
 * Java code
 */
public class JavaScriptInterface {
	private final JsRunnerCallbackInterface mJsRunner;
	private final CallJavaResultInterface mCallJavaResultInterface;

	public JavaScriptInterface(CallJavaResultInterface callJavaResult) {
		mJsRunner = null;
		mCallJavaResultInterface = callJavaResult;
	}

	public JavaScriptInterface(JsRunnerCallbackInterface jsRunner) {
		mJsRunner = jsRunner;
		mCallJavaResultInterface = null;
	}

	@JavascriptInterface
	public void initialJsExecuted() {
		mJsRunner.initalJsEvaluationHasFinished();
	}

	@JavascriptInterface
	public void result(String value, int callIndex) {
		mJsRunner.jsCallFinished(value, callIndex);
	}

	@JavascriptInterface
	public void returnResultToJava(String value, int callIndex) {
		mCallJavaResultInterface.jsCallFinished(value, callIndex);
	}
}