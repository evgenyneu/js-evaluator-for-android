package com.evgenii.jsevaluatortests;

import android.test.AndroidTestCase;

import com.evgenii.jsevaluator.JsEvaluator;
import com.evgenii.jsevaluator.JsEvaluatorFuture;
import com.evgenii.jsevaluator.interfaces.JsCallback;
import com.evgenii.jsevaluatortests.mocks.HandlerWrapperMock;
import com.evgenii.jsevaluatortests.mocks.JsCallbackMock;
import com.evgenii.jsevaluatortests.mocks.WebViewWrapperMock;

import java.util.ArrayList;
import java.util.concurrent.FutureTask;

public class JsEvaluatorFutureTests extends AndroidTestCase {
	protected JsEvaluatorFuture mJsEvaluator;
	protected WebViewWrapperMock mWebViewWrapperMock;

	@Override
	protected void setUp() {
		mJsEvaluator = new JsEvaluatorFuture(getContext());
	}

	public void testEvaluateFuture_shouldEvaluateJs() throws Exception{
		final FutureTask<String> futureTask = mJsEvaluator.evaluateFutureTask("2 + 3");
		futureTask.run();
		String result = futureTask.get();

		assertNotNull(result);
		assertEquals((2 + 3), Integer.parseInt(result));
	}

	public void testCallJsFuture_shouldEvaluateJs() throws Exception{
		final FutureTask<String> futureTask = mJsEvaluator.callFunctionFutureTask("function myFunction(a, b) { return a + b; }", "myFunction",2,3);
		futureTask.run();
		String result = futureTask.get();

		assertNotNull(result);
		assertEquals((2 + 3), Integer.parseInt(result));
	}
}
