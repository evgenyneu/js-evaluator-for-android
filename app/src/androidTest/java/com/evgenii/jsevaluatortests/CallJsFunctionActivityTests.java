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
		TouchUtils.clickView(this, callFunctionButton);

		// Run on UI thread
		// ---------------

		final TextView resultTextViewUi = (TextView) mActivity
				.findViewById(R.id.textViewCallFunctionUiThreadResult);

		assertTrue(View.VISIBLE == resultTextViewUi.getVisibility());
		final String expectedResult = "UI thread result: Hello, World";
		assertEquals(expectedResult, resultTextViewUi.getText().toString());

		// Run on background thread
		// ---------------

		final TextView resultTextViewBackground = (TextView) mActivity
				.findViewById(R.id.textViewCallFunctionBackgroundThreadResult);

		assertTrue(View.VISIBLE == resultTextViewBackground.getVisibility());
		final String expectedResultBackgroundThread = "Background thread result: Hello, World";
		assertEquals(expectedResultBackgroundThread, resultTextViewBackground.getText().toString());


		// Block UI thread and run function
		// ---------------

		final TextView resultTextViewBlockUiThread = (TextView) mActivity
				.findViewById(R.id.textViewCallFunctionBlockUiThreadResult);

		assertTrue(View.VISIBLE == resultTextViewBlockUiThread.getVisibility());
		final String expectedResultBlockUiThread = "Block UI thread result: Hello, World";
		assertEquals(expectedResultBlockUiThread, resultTextViewBlockUiThread.getText().toString());
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
