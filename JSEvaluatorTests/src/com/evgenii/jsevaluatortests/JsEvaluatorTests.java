package com.evgenii.jsevaluatortests;

import java.util.ArrayList;

import android.test.AndroidTestCase;

import com.evgenii.jsevaluator.JsEvaluator;
import com.evgenii.jsevaluator.interfaces.JsCallback;
import com.evgenii.jsevaluatortests.mocks.HandlerWrapperMock;
import com.evgenii.jsevaluatortests.mocks.JsCallbackMock;
import com.evgenii.jsevaluatortests.mocks.WebViewWrapperMock;

public class JsEvaluatorTests extends AndroidTestCase {
	protected JsEvaluator mJsEvaluator;

	@Override
	protected void setUp() {
		mJsEvaluator = new JsEvaluator(mContext);
	}

	public void testCallFunction_shouldEvaluateJs() {
		final WebViewWrapperMock webViewWrapperMock = new WebViewWrapperMock();
		mJsEvaluator.setWebViewWrapper(webViewWrapperMock);

		mJsEvaluator.callFunction(null, "myFunction", "one", 2);

		assertEquals(1, webViewWrapperMock.mLoadedUrls.size());
		assertEquals(
				"javascript: evgeniiJsEvaluator.returnResultToJava(eval('myFunction(\"one\", 2)'), 0);",
				webViewWrapperMock.mLoadedUrls.get(0));
	}

	public void testCallFunction_shouldRegusterResultCallback() {
		final JsCallbackMock callbackMock = new JsCallbackMock();

		mJsEvaluator.callFunction(callbackMock, "myFunction");

		final ArrayList<JsCallback> callbacks = mJsEvaluator
				.getResultCallbacks();
		assertEquals(1, callbacks.size());
		assertEquals(callbackMock, callbacks.get(0));
	}

	public void testEscapeSingleQuotes() {
		assertEquals("\\'a\\'", JsEvaluator.escapeSingleQuotes("'a'"));
	}

	public void testEvaluate_shouldEvaluateJs() {
		final WebViewWrapperMock webViewWrapperMock = new WebViewWrapperMock();
		mJsEvaluator.setWebViewWrapper(webViewWrapperMock);

		mJsEvaluator.evaluate("2 * 3", null);

		assertEquals(1, webViewWrapperMock.mLoadedUrls.size());
		assertEquals(
				"javascript: evgeniiJsEvaluator.returnResultToJava(eval('2 * 3'), 0);",
				webViewWrapperMock.mLoadedUrls.get(0));
	}

	public void testEvaluate_shouldRegusterResultCallback() {
		final JsCallbackMock callbackMock = new JsCallbackMock();

		mJsEvaluator.evaluate("2 * 3", callbackMock);

		final ArrayList<JsCallback> callbacks = mJsEvaluator
				.getResultCallbacks();
		assertEquals(1, callbacks.size());
		assertEquals(callbackMock, callbacks.get(0));
	}

	public void testGetJsForEval() {
		final String result = JsEvaluator.getJsForEval("2 + 3; 'hello'", 34);
		assertEquals(
				"evgeniiJsEvaluator.returnResultToJava(eval('2 + 3; \\'hello\\''), 34);",
				result);
	}

	public void testJsCallFinished_runsCallback() {
		final ArrayList<JsCallback> callbacks = mJsEvaluator
				.getResultCallbacks();
		final JsCallbackMock callback = new JsCallbackMock();
		callbacks.add(callback);

		final HandlerWrapperMock handlerWrapperMock = new HandlerWrapperMock();
		mJsEvaluator.setHandler(handlerWrapperMock);

		mJsEvaluator.jsCallFinished("my result", 0);
		assertEquals("my result", callback.resultValue);
	}
}
