package com.evgenii.jsevaluatortests;

import android.test.AndroidTestCase;

import com.evgenii.jsevaluator.JavaScriptInterface;
import com.evgenii.jsevaluatortests.mocks.CallJavaResultInterfaceMock;
import com.evgenii.jsevaluatortests.mocks.JsRunnerMock;

public class JavaScriptInterfaceTest extends AndroidTestCase {
	protected JavaScriptInterface mJavaScriptInterface;
	protected JsRunnerMock mJsRunnerMock;
	protected CallJavaResultInterfaceMock mCallJavaResultInterfaceMock;

	public void testInitialJsExecuted_executesAllPendingJs() {
		mJsRunnerMock = new JsRunnerMock();
		mJavaScriptInterface = new JavaScriptInterface(mJsRunnerMock);

		assertFalse(mJsRunnerMock.mInitalEvaluationHasFinished);
		mJavaScriptInterface.initialJsExecuted();
		assertTrue(mJsRunnerMock.mInitalEvaluationHasFinished);
	}

	public void testResult() {
		mJsRunnerMock = new JsRunnerMock();
		mJavaScriptInterface = new JavaScriptInterface(mJsRunnerMock);

		mJavaScriptInterface.result("test value", 12);
		assertEquals("test value", mJsRunnerMock.jsCallFinished_paramValue);
		assertEquals(Integer.valueOf(12),
				mJsRunnerMock.jsCallFinished_paramCallIndex);
	}

	public void testReturnResultToJava() {
		mCallJavaResultInterfaceMock = new CallJavaResultInterfaceMock();
		mJavaScriptInterface = new JavaScriptInterface(
				mCallJavaResultInterfaceMock);

		mJavaScriptInterface.returnResultToJava("test value", 12);
		assertEquals("test value",
				mCallJavaResultInterfaceMock.jsCallFinished_paramValue);
		assertEquals(Integer.valueOf(12),
				mCallJavaResultInterfaceMock.jsCallFinished_paramCallIndex);
	}
}