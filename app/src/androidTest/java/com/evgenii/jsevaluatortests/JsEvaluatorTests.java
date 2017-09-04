package com.evgenii.jsevaluatortests;

import android.os.Handler;
import android.os.Looper;
import android.test.AndroidTestCase;

import com.evgenii.jsevaluator.JsEvaluator;
import com.evgenii.jsevaluator.interfaces.JsCallback;
import com.evgenii.jsevaluatortests.mocks.HandlerWrapperMock;
import com.evgenii.jsevaluatortests.mocks.JsCallbackMock;
import com.evgenii.jsevaluatortests.mocks.RequestIdGeneratorMock;
import com.evgenii.jsevaluatortests.mocks.WebViewWrapperMock;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class JsEvaluatorTests extends AndroidTestCase {
	protected JsEvaluator mJsEvaluator, mRealJsEvaluator;
	protected WebViewWrapperMock mWebViewWrapperMock;
	protected RequestIdGeneratorMock requestIdGeneratorMock = new RequestIdGeneratorMock();

	@Override
	protected void setUp() {
		mJsEvaluator = new JsEvaluator(mContext);

		// We do not want to create real WebView in this test.
		// Because it must be done on the UI thread
		mWebViewWrapperMock = new WebViewWrapperMock();
		mJsEvaluator.setWebViewWrapper(mWebViewWrapperMock);
		mJsEvaluator.setRequestIdGenerator(requestIdGeneratorMock);
	}

	/**
	 * This should be called from the main thread.
	 */
	protected JsEvaluator getRealJsEvaluator(){
		if(mRealJsEvaluator == null) {
			mRealJsEvaluator = new JsEvaluator(mContext);
		}
		return mRealJsEvaluator;
	}

	public void testCallFunction_shouldEvaluateJs() {
		final JsCallbackMock callbackMock = new JsCallbackMock();
		requestIdGeneratorMock.setNextGenerated(0);
		mJsEvaluator.callFunction("1 + 2", callbackMock, "myFunction", "one", 2);

		assertEquals(1, mWebViewWrapperMock.mLoadedJavaScript.size());
		final String actualJs = mWebViewWrapperMock.mLoadedJavaScript.get(0);
		assertEquals(
				"evgeniiJsEvaluator.returnResultToJava(eval('try{1 + 2; myFunction(\"one\", 2)}catch(e){\"evgeniiJsEvaluatorException\"+e}'), 0);",
				actualJs);
	}

	public void testCallFunction_shouldRegisterResultCallback() {
		final JsCallbackMock callbackMock = new JsCallbackMock();

		mJsEvaluator.callFunction("1 + 2", callbackMock, "myFunction");

		final Map<Integer, JsCallback> callbacks = mJsEvaluator.getResultCallbacks();
		assertEquals(1, callbacks.size());
		assertEquals(callbackMock, callbacks.entrySet().iterator().next().getValue());
	}

	public void testEscapeCarriageReturn() {
		assertEquals("one\\rtwo", JsEvaluator.escapeCarriageReturn("one\rtwo"));
	}

	public void testEscapeSclosingScript() {
		assertEquals("<\\/script><\\/ScRipt>",
				JsEvaluator.escapeClosingScript("</script></ScRipt>"));
	}

	public void testEscapeSingleQuotes() {
		assertEquals("\\'a\\'", JsEvaluator.escapeSingleQuotes("'a'"));
	}

	public void testEvaluate_shouldEvaluateJs() {
		final JsCallbackMock callbackMock = new JsCallbackMock();
		requestIdGeneratorMock.setNextGenerated(0);
		mJsEvaluator.evaluate("2 * 3", callbackMock);

		assertEquals(1, mWebViewWrapperMock.mLoadedJavaScript.size());
		assertEquals("evgeniiJsEvaluator.returnResultToJava(eval('try{2 * 3}catch(e){\"evgeniiJsEvaluatorException\"+e}'), 0);",
				mWebViewWrapperMock.mLoadedJavaScript.get(0));
	}

	public void testEvaluate_shouldEvaluateWithoutCallback() {
		requestIdGeneratorMock.setNextGenerated(-1);
		mJsEvaluator.evaluate("2 * 3");

		assertEquals(1, mWebViewWrapperMock.mLoadedJavaScript.size());
		assertEquals("evgeniiJsEvaluator.returnResultToJava(eval('try{2 * 3}catch(e){\"evgeniiJsEvaluatorException\"+e}'), -1);",
				mWebViewWrapperMock.mLoadedJavaScript.get(0));
	}

	public void testEvaluate_shouldNotRegisterResultCallbackWhenCallbackIsNotSupplied() {
		mJsEvaluator.evaluate("2 * 3");

		assertEquals(0, mJsEvaluator.getResultCallbacks().size());
	}

	public void testEvaluate_shouldRegisterResultCallback() {
		final JsCallbackMock callbackMock = new JsCallbackMock();

		mJsEvaluator.evaluate("2 * 3", callbackMock);

		final Map<Integer, JsCallback> callbacks = mJsEvaluator.getResultCallbacks();
		assertEquals(1, callbacks.size());
		assertEquals(callbackMock, callbacks.entrySet().iterator().next().getValue());
	}

	public void testEvaluate_shouldError() {
		final CountDownLatch latch = new CountDownLatch(1);
		Handler handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable() {
			@Override
			public void run() {
				final JsCallbackMock callbackMock = new JsCallbackMock() {
					@Override
					public void onError(String errorMessage) {
						assertEquals("ReferenceError: unknownVariable is not defined", errorMessage);
						latch.countDown();
					}
				};
				getRealJsEvaluator().evaluate("unknownVariable.test()", callbackMock);
			}
		});
		try {
			// Wait for the callback to finish.
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void testGetJsForEval() {
		final String result = JsEvaluator.getJsForEval("'hello'", 34);
		assertEquals("evgeniiJsEvaluator.returnResultToJava(eval('try{\\'hello\\'}catch(e){\"evgeniiJsEvaluatorException\"+e}'), 34);", result);
	}

	public void testJsCallFinished_doesNotRunCallBackWhenIndexIsMinusOne() {
		final HandlerWrapperMock handlerWrapperMock = new HandlerWrapperMock();
		mJsEvaluator.setHandler(handlerWrapperMock);

		mJsEvaluator.jsCallFinished("my result", -1);
	}

	public void testJsCallFinished_runsCallbackAndRemove() {
		final Map<Integer, JsCallback> callbacks = mJsEvaluator.getResultCallbacks();
		final JsCallbackMock callback = new JsCallbackMock();
		callbacks.put(0, callback);

		final HandlerWrapperMock handlerWrapperMock = new HandlerWrapperMock();
		mJsEvaluator.setHandler(handlerWrapperMock);

		mJsEvaluator.jsCallFinished("my result", 0);
		assertEquals("my result", callback.resultValue);
		assertTrue(callbacks.isEmpty());
	}

	public void testDestroy() {
		mJsEvaluator.destroy();
	}
}
