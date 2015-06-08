package com.evgenii.jsevaluatortests;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CallJsFunctionActivityTests extends
		ActivityInstrumentationTestCase2<CallJsFunctionActivity> {

	private CallJsFunctionActivity mActivity;

	public CallJsFunctionActivityTests() {
		super(CallJsFunctionActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
	}

	@MediumTest
	public void testCallFunctionButton_clickButtonAndShowResult() throws InterruptedException {
		final Button callFunctionButton = (Button) mActivity.findViewById(R.id.buttonCallFunction);

		final TextView resultTextView = (TextView) mActivity
				.findViewById(R.id.textViewCallFunctionResult);

		TouchUtils.clickView(this, callFunctionButton);
		assertTrue(View.VISIBLE == resultTextView.getVisibility());

		final String expectedResult = "Result: Hello, World";

		for (int i = 0; i < 100; i++) {
			Thread.sleep(100);
			if (resultTextView.getText().toString().equals(expectedResult)) {
				break;
			}
		}
		assertEquals(expectedResult, resultTextView.getText().toString());
	}

	@MediumTest
	public void testFunctionParameter_shouldHaveJsText() {
		final TextView jsFunctionTextView = (TextView) mActivity
				.findViewById(R.id.editTextParameter);

		final String expected = mActivity.getString(R.string.js_parameter_edit_text);
		final String actual = jsFunctionTextView.getText().toString();
		assertEquals(expected, actual);
	}

	@MediumTest
	public void testJsFunctionTextView_shouldHaveJsText() {
		final TextView jsFunctionTextView = (TextView) mActivity
				.findViewById(R.id.js_function_edit_text);

		final String expected = mActivity.getString(R.string.js_function_edit_text);
		final String actual = jsFunctionTextView.getText().toString();
		assertEquals(expected, actual);
	}

	public void testPreconditions() {
		assertNotNull("mActivity is null", mActivity);
	}
}
