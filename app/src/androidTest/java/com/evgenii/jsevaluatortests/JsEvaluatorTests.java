package com.evgenii.jsevaluatortests;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.test.AndroidTestCase;

import com.evgenii.jsevaluator.JsCallbackData;
import com.evgenii.jsevaluator.JsEvaluator;
import com.evgenii.jsevaluatortests.mocks.UiThreadHandlerWrapperMock;
import com.evgenii.jsevaluatortests.mocks.JsCallbackMock;
import com.evgenii.jsevaluatortests.mocks.WebViewWrapperMock;

public class JsEvaluatorTests extends AndroidTestCase {
	protected JsEvaluator mJsEvaluator;
	protected WebViewWrapperMock mWebViewWrapperMock;

	@Override
	protected void setUp() {
		mJsEvaluator = new JsEvaluator(mContext);

		// We do not want to create real WebView in this test.
		// Because it must be done on the UI thread
		mWebViewWrapperMock = new WebViewWrapperMock();
		mJsEvaluator.setWebViewWrapper(mWebViewWrapperMock);
	}

    // Evaluate JS on UI thread
    // ----------------------

    public void testEvaluate_shouldEvaluateJsInUiThread() {
        final JsCallbackMock callbackMock = new JsCallbackMock();

        mJsEvaluator.evaluateAndRespondOnUiThread("2 * 3", callbackMock);

        assertEquals(1, mWebViewWrapperMock.mLoadedJavaScript.size());
        assertEquals("evgeniiJsEvaluator.returnResultToJava(eval('2 * 3'), 0);",
                mWebViewWrapperMock.mLoadedJavaScript.get(0));
    }

    public void testEvaluate_shouldRegisterResultCallbackInUiThread() {
        final JsCallbackMock callbackMock = new JsCallbackMock();

        mJsEvaluator.evaluateAndRespondOnUiThread("2 * 3", callbackMock);

        final ArrayList<JsCallbackData> callbacks = mJsEvaluator.getResultCallbacks();
        assertEquals(1, callbacks.size());
		JsCallbackData callback = callbacks.get(0);
		assertTrue(callback.callOnUiThread);
        assertEquals(callbackMock, callback.callback);
    }

    // Evaluate JS on background thread
    // ----------------------

    public void testEvaluate_shouldEvaluateJsInBackgroundThread() {
        final JsCallbackMock callbackMock = new JsCallbackMock();

        mJsEvaluator.evaluateAndRespondOnBackgroundThread("2 * 3", callbackMock);

        assertEquals(1, mWebViewWrapperMock.mLoadedJavaScript.size());
        assertEquals("evgeniiJsEvaluator.returnResultToJava(eval('2 * 3'), 0);",
                mWebViewWrapperMock.mLoadedJavaScript.get(0));
    }

    public void testEvaluate_shouldRegisterResultCallbackForBackgroundThread() {
        final JsCallbackMock callbackMock = new JsCallbackMock();

        mJsEvaluator.evaluateAndRespondOnBackgroundThread("2 * 3", callbackMock);

        final ArrayList<JsCallbackData> callbacks = mJsEvaluator.getResultCallbacks();
        assertEquals(1, callbacks.size());
        JsCallbackData callback = callbacks.get(0);
        assertFalse(callback.callOnUiThread);
        assertEquals(callbackMock, callback.callback);
    }

    // Block UI thread and evaluate
    // ----------------------

    public void testBlockUiThreadAndEvaluate_returnsResult() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mJsEvaluator.jsCallFinished("my result", 0);
            }
        });

        String result = mJsEvaluator.blockUIThreadAndEvaluate(1_000, "2 * 3");

        assertEquals("my result", result);
    }

    public void testBlockUiThreadAndEvaluate_sendJavaScriptToWebViewForEvaluation() {
        mJsEvaluator.blockUIThreadAndEvaluate(1, "2 * 3");

        assertEquals(1, mWebViewWrapperMock.mLoadedJavaScript.size());
        assertEquals("evgeniiJsEvaluator.returnResultToJava(eval('2 * 3'), 0);",
                mWebViewWrapperMock.mLoadedJavaScript.get(0));
    }

    public void testBlockUiThreadAndEvaluate_returnsNullWhenOnTimeout() {
        String result = mJsEvaluator.blockUIThreadAndEvaluate(1, "2 * 3");

        assertTrue(result == null);
    }

    public void testBlockUiThreadAndEvaluate_shouldRegisterResultCallbackOnBackgroundThread() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mJsEvaluator.jsCallFinished("my result", 0);
            }
        });

        mJsEvaluator.blockUIThreadAndEvaluate(50, "2 * 3");

        final ArrayList<JsCallbackData> callbacks = mJsEvaluator.getResultCallbacks();
        assertEquals(1, callbacks.size());
        JsCallbackData callback = callbacks.get(0);
        assertFalse(callback.callOnUiThread);
    }

    // Call function on UI thread
    // ----------------------

	public void testCallFunction_shouldEvaluateJsOnUiThread() {
		final JsCallbackMock callbackMock = new JsCallbackMock();

		mJsEvaluator.callFunctionAndRespondOnUiThread("1 + 2", callbackMock, "myFunction", "one", 2);

		assertEquals(1, mWebViewWrapperMock.mLoadedJavaScript.size());
		final String actualJs = mWebViewWrapperMock.mLoadedJavaScript.get(0);
		assertEquals(
				"evgeniiJsEvaluator.returnResultToJava(eval('1 + 2; myFunction(\"one\", 2)'), 0);",
				actualJs);
	}

	public void testCallFunction_shouldRegisterResultCallbackOnUiThread() {
		final JsCallbackMock callbackMock = new JsCallbackMock();

		mJsEvaluator.callFunctionAndRespondOnUiThread("1 + 2", callbackMock, "myFunction");

		final ArrayList<JsCallbackData> callbacks = mJsEvaluator.getResultCallbacks();
		assertEquals(1, callbacks.size());
		JsCallbackData callback = callbacks.get(0);
		assertTrue(callback.callOnUiThread);
		assertEquals(callbackMock, callback.callback);
	}

    // Call function on background thread
    // ----------------------

    public void testCallFunction_shouldEvaluateJsOnBackgroundThread() {
        final JsCallbackMock callbackMock = new JsCallbackMock();

        mJsEvaluator.callFunctionAndRespondOnBackgroundThread("1 + 2", callbackMock, "myFunction", "one", 2);

        assertEquals(1, mWebViewWrapperMock.mLoadedJavaScript.size());
        final String actualJs = mWebViewWrapperMock.mLoadedJavaScript.get(0);
        assertEquals(
                "evgeniiJsEvaluator.returnResultToJava(eval('1 + 2; myFunction(\"one\", 2)'), 0);",
                actualJs);
    }

    public void testCallFunction_shouldRegisterResultCallbackOnBackgroundThread() {
        final JsCallbackMock callbackMock = new JsCallbackMock();

        mJsEvaluator.callFunctionAndRespondOnBackgroundThread("1 + 2", callbackMock, "myFunction");

        final ArrayList<JsCallbackData> callbacks = mJsEvaluator.getResultCallbacks();
        assertEquals(1, callbacks.size());
        JsCallbackData callback = callbacks.get(0);
        assertFalse(callback.callOnUiThread);
        assertEquals(callbackMock, callback.callback);
    }

    // Escape JavaScript text
    // ----------------------

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


	public void testGetJsForEval() {
		final String result = JsEvaluator.getJsForEval("'hello'", 34);
		assertEquals("evgeniiJsEvaluator.returnResultToJava(eval('\\'hello\\''), 34);", result);
	}

    // Finished evaluation
    // ----------------------

	public void testJsCallFinished_doesNotRunCallBackWhenIndexIsMinusOne() {
		final UiThreadHandlerWrapperMock uiThreadHandlerWrapperMock = new UiThreadHandlerWrapperMock();
		mJsEvaluator.setUiThreadHandler(uiThreadHandlerWrapperMock);
		mJsEvaluator.jsCallFinished("my result", -1);
	}

	public void testJsCallFinished_runsCallbackInUiThread() {
		final ArrayList<JsCallbackData> callbacks = mJsEvaluator.getResultCallbacks();
		final JsCallbackMock callback = new JsCallbackMock();

        JsCallbackData callbackData = new JsCallbackData(
                callback,
                true // on UI thread
        );

		callbacks.add(callbackData);

		final UiThreadHandlerWrapperMock uiThreadHandlerWrapperMock = new UiThreadHandlerWrapperMock();
		mJsEvaluator.setUiThreadHandler(uiThreadHandlerWrapperMock);

		mJsEvaluator.jsCallFinished("my result", 0);
		assertEquals("my result", callback.resultValue);
        assertTrue(uiThreadHandlerWrapperMock.didRunMock);
	}

    public void testJsCallFinished_runsCallbackInBackroundThread() {
        final ArrayList<JsCallbackData> callbacks = mJsEvaluator.getResultCallbacks();
        final JsCallbackMock callback = new JsCallbackMock();

        JsCallbackData callbackData = new JsCallbackData(
            callback,
            false // on background thread
        );

        callbacks.add(callbackData);

        final UiThreadHandlerWrapperMock uiThreadHandlerWrapperMock = new UiThreadHandlerWrapperMock();
        mJsEvaluator.setUiThreadHandler(uiThreadHandlerWrapperMock);

        mJsEvaluator.jsCallFinished("my result", 0);
        assertEquals("my result", callback.resultValue);
        assertFalse(uiThreadHandlerWrapperMock.didRunMock);
    }
}
