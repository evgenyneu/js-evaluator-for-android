package com.evgenii.jsevaluatortests;

import java.util.ArrayList;

import android.test.AndroidTestCase;
import android.webkit.WebView;

import com.evgenii.jsevaluator.JsFunctionCall;
import com.evgenii.jsevaluator.JsRunner;
import com.evgenii.jsevaluator.exceptions.InitialJsHasAlreadyBeenRun;
import com.evgenii.jsevaluator.interfaces.JsCallback;
import com.evgenii.jsevaluatortests.mocks.JsCallbackMock;

public class JsRunnerTest extends AndroidTestCase {
	protected JsRunner mJsRunner;

	@Override
	protected void setUp() {
		mJsRunner = new JsRunner(mContext);
	}

	public void testAddInitialJs() throws InitialJsHasAlreadyBeenRun {
		mJsRunner.addInitialJs("function first(){};");
		mJsRunner.addInitialJs("function second(){};");
		assertEquals(" function first(){}; function second(){};",
				mJsRunner.getInitialJSConcatenated());
	}

	public void testAddInitialJs_canNotAddAfterRunning() {
		try {
			mJsRunner.runInitialJs();
			mJsRunner.addInitialJs("function second(){};");
		} catch (final InitialJsHasAlreadyBeenRun e) {
		}
	}

	public void testExecuteAllPendingJs() {
		mJsRunner.executeAllPendingJs();
	}

	public void testGetCompleteInitialJsToEvaluate()
			throws InitialJsHasAlreadyBeenRun {
		mJsRunner.addInitialJs("function first(){};");
		mJsRunner.addInitialJs("function second(){};");
		assertEquals(
				"javascript:  function first(){}; function second(){}; AESCrypto.initialJsExecuted(); ",
				mJsRunner.getCompleteInitialJsToEvaluate());
	}

	public void testGetJsForFunctionCall() {
		assertEquals("javascript: AESCrypto.result(drink('milk'), 8);",
				JsRunner.getJsForFunctionCall("drink('milk')", 8));
	}

	public void testGetWebView_shouldCreateWebView() {
		final WebView webView = mJsRunner.getWebView();
		assertNotNull(webView);
	}

	public void testInitalJsEvaluationHasFinished_executesPendingJs() {
		final JsCallbackMock callback = new JsCallbackMock();
		mJsRunner.runJsFunction("myFirstName", "param1", callback);
		mJsRunner.runJsFunction("mySecondName", "param2", callback);
		mJsRunner.initalJsEvaluationHasFinished();
		assertEquals(0, mJsRunner.getPendingJsCalls().size());
	}

	public void testInitalJsEvaluationHasFinished_markInitialJsAsFinished() {
		mJsRunner.initalJsEvaluationHasFinished();
		assertTrue(mJsRunner.getInitialJsEvaluationFinished());
	}

	public void testInitialJsEvaluationShouldNotBeFinishedByDefault() {
		assertFalse(mJsRunner.getInitialJsEvaluationFinished());
	}

	public void testInitialJsEvaluationShouldNotBeStartedByDefault() {
		assertFalse(mJsRunner.getInitialJsEvaluationStarted());
	}

	public void testJsCallFinished_runsCallback() {
		final ArrayList<JsCallback> callbacks = mJsRunner.getJsCallbacks();
		final JsCallbackMock callback = new JsCallbackMock();
		callbacks.add(callback);

		mJsRunner.jsCallFinished("my result", 0);
		assertEquals("my result", callback.resultValue);
	}

	public void testRun() {
		assertTrue(true);
	}

	public void testRunInitialJs_startInitialJsEvaluation() {
		mJsRunner.runInitialJs();
		assertTrue(mJsRunner.getInitialJsEvaluationStarted());
	}

	public void testRunJsFunction_addsJsToPendingWithoutRunning() {
		final JsCallbackMock callback = new JsCallbackMock();
		mJsRunner.runJsFunction("myFirstName", "param1", callback);
		mJsRunner.runJsFunction("mySecondName", "param2", callback);

		assertEquals(2, mJsRunner.getPendingJsCalls().size());

		// Check first function
		final JsFunctionCall functionCall = mJsRunner.getPendingJsCalls()
				.get(0);
		assertEquals("myFirstName", functionCall.getName());
		assertEquals("param1", functionCall.getParams().get(0));
		assertEquals(Integer.valueOf(0), functionCall.getCallbackIndex());

		// Check second function
		final JsFunctionCall functionCall2 = mJsRunner.getPendingJsCalls().get(
				1);
		assertEquals("mySecondName", functionCall2.getName());
		assertEquals("param2", functionCall2.getParams().get(0));
		assertEquals(Integer.valueOf(1), functionCall2.getCallbackIndex());
	}

	public void testRunJsFunction_registersCallback() {
		final JsCallbackMock callback1 = new JsCallbackMock();
		final JsCallbackMock callback2 = new JsCallbackMock();
		mJsRunner.runJsFunction("myFirstName", "param1", callback1);
		mJsRunner.runJsFunction("mySecondName", "param2", callback2);
		assertEquals(2, mJsRunner.getJsCallbacks().size());

		// Check callbacks
		assertEquals(mJsRunner.getJsCallbacks().get(0), callback1);
		assertEquals(mJsRunner.getJsCallbacks().get(1), callback2);
	}

	public void testRunJsFunction_runsJsIfInitialJsEvaluationHasFinished() {
		final JsCallbackMock callback = new JsCallbackMock();
		mJsRunner.initalJsEvaluationHasFinished();
		mJsRunner.runJsFunction("myFirstName", "param1", callback);
		assertEquals(0, mJsRunner.getPendingJsCalls().size());
	}
}
