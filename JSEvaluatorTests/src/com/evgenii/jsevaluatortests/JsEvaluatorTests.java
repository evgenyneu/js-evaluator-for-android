package com.evgenii.jsevaluatortests;

import java.util.ArrayList;

import android.test.AndroidTestCase;
import android.util.Log;

import com.evgenii.jsevaluator.JsEvaluator;
import com.evgenii.jsevaluator.interfaces.JsCallback;
import com.evgenii.jsevaluatortests.mocks.JsCallbackMock;

public class JsEvaluatorTests extends AndroidTestCase {
	protected JsEvaluator mJsEvaluator;

	@Override
	protected void setUp() {
		mJsEvaluator = new JsEvaluator(mContext);
	}

	public void testEscapeSingleQuotes() {
		Log.d("ii", JsEvaluator.escapeSingleQuotes("'a'"));
		assertEquals("\\'a\\'", JsEvaluator.escapeSingleQuotes("'a'"));
	}

	public void testEvaluate_shouldRegusterResultCallback() {
		final JsCallbackMock callbackMock = new JsCallbackMock();

		mJsEvaluator.evaluate("2 * 3", callbackMock);

		final ArrayList<JsCallback> callbacks = mJsEvaluator
				.getResultCallbacks();
		assertEquals(1, callbacks.size());
	}

	public void testGetJsForEval() {
		final String result = JsEvaluator.getJsForEval("2 + 3; 'hello'", 34);
		assertEquals(
				"evgeniiJsEvaluator.returnResultToJava(eval('2 + 3; \\'hello\\''), 34);",
				result);
	}
}
