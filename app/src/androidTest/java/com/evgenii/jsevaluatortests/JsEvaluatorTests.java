package com.evgenii.jsevaluatortests;

import android.os.Handler;
import android.os.Looper;
import android.test.AndroidTestCase;

import com.evgenii.jsevaluator.JsEvaluator;
import com.evgenii.jsevaluatortests.mocks.HandlerWrapperMock;
import com.evgenii.jsevaluatortests.mocks.JsCallbackMock;
import com.evgenii.jsevaluatortests.mocks.WebViewWrapperMock;

import java.util.concurrent.CountDownLatch;

public class JsEvaluatorTests extends AndroidTestCase {
	protected JsEvaluator mJsEvaluator, mRealJsEvaluator;
	protected WebViewWrapperMock mWebViewWrapperMock;

	@Override
	protected void setUp() {
		mJsEvaluator = new JsEvaluator(mContext);

		// We do not want to create real WebView in this test.
		// Because it must be done on the UI thread
		mWebViewWrapperMock = new WebViewWrapperMock();
		mJsEvaluator.setWebViewWrapper(mWebViewWrapperMock);
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
		mJsEvaluator.callFunction("1 + 2", callbackMock, "myFunction", "one", 2);

		assertEquals(1, mWebViewWrapperMock.mLoadedJavaScript.size());
		final String actualJs = mWebViewWrapperMock.mLoadedJavaScript.get(0);
		assertEquals(
				"evgeniiJsEvaluator.returnResultToJava(eval('try{1 + 2; myFunction(\"one\", 2)}catch(e){\"evgeniiJsEvaluatorException\"+e}'));",
				actualJs);
	}

	public void testCallFunction_shouldRegisterResultCallback() {
		final JsCallbackMock callbackMock = new JsCallbackMock();
        mJsEvaluator.callFunction("1 + 2", callbackMock, "myFunction");

		assertEquals(callbackMock, mJsEvaluator.getCallback());
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
		mJsEvaluator.evaluate("2 * 3", callbackMock);

		assertEquals(1, mWebViewWrapperMock.mLoadedJavaScript.size());
		assertEquals("evgeniiJsEvaluator.returnResultToJava(eval('try{2 * 3}catch(e){\"evgeniiJsEvaluatorException\"+e}'));",
				mWebViewWrapperMock.mLoadedJavaScript.get(0));
	}

	public void testEvaluate_shouldEvaluateWithoutCallback() {
		mJsEvaluator.evaluate("2 * 3");

		assertEquals(1, mWebViewWrapperMock.mLoadedJavaScript.size());
		assertEquals("evgeniiJsEvaluator.returnResultToJava(eval('try{2 * 3}catch(e){\"evgeniiJsEvaluatorException\"+e}'));",
				mWebViewWrapperMock.mLoadedJavaScript.get(0));
	}

	public void testEvaluate_shouldNotRegisterResultCallbackWhenCallbackIsNotSupplied() {
		mJsEvaluator.evaluate("2 * 3");

		assertNull(mJsEvaluator.getCallback());
	}

	public void testEvaluate_shouldRegisterResultCallback() {
		final JsCallbackMock callbackMock = new JsCallbackMock();

		mJsEvaluator.evaluate("2 * 3", callbackMock);

		assertEquals(callbackMock, mJsEvaluator.getCallback());
	}

    public void testEvaluate_shouldHoldOnlyLastCallback() {
        final JsCallbackMock callbackMock = new JsCallbackMock();
        final JsCallbackMock callbackMockSecond = new JsCallbackMock();

        mJsEvaluator.evaluate("2 * 3", callbackMock);
        mJsEvaluator.evaluate("2 * 3", callbackMockSecond);

        assertEquals(callbackMockSecond, mJsEvaluator.getCallback());
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
		final String result = JsEvaluator.getJsForEval("'hello'");
		assertEquals("evgeniiJsEvaluator.returnResultToJava(eval('try{\\'hello\\'}catch(e){\"evgeniiJsEvaluatorException\"+e}'));", result);
	}


	public void testJsCallFinished_runsCallbackAndDereference() {

		final JsCallbackMock callback = new JsCallbackMock();
		mJsEvaluator.evaluate("any", callback);

		final HandlerWrapperMock handlerWrapperMock = new HandlerWrapperMock();
		mJsEvaluator.setHandler(handlerWrapperMock);

		mJsEvaluator.jsCallFinished("my result");
		assertEquals("my result", callback.resultValue);
		assertNull(mJsEvaluator.getCallback());
	}

	public void testDestroy() {
		mJsEvaluator.destroy();
	}
}
