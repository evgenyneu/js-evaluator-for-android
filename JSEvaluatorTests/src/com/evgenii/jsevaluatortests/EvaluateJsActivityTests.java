package com.evgenii.jsevaluatortests;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EvaluateJsActivityTests extends
		ActivityInstrumentationTestCase2<EvaluateJsStringActivity> {

	private EvaluateJsStringActivity mActivity;

	public EvaluateJsActivityTests() {
		super(EvaluateJsStringActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
	}

	@MediumTest
	public void testEvaluateButton_clickButtonAndShowResult() {
		final Button evaluateButton = (Button) mActivity.findViewById(R.id.button_evaluate);

		final TextView resultTextView = (TextView) mActivity.findViewById(R.id.js_result_text_view);

		TouchUtils.clickView(this, evaluateButton);
		assertTrue(View.VISIBLE == resultTextView.getVisibility());
		assertEquals("Result: 4", resultTextView.getText().toString());
	}

	@MediumTest
	public void testJsTextView_shouldHaveJsText() {
		final TextView editJsTextView = (TextView) mActivity.findViewById(R.id.edit_java_script);

		final String expected = mActivity.getString(R.string.edit_js_text);
		final String actual = editJsTextView.getText().toString();
		assertEquals(expected, actual);
	}

	public void testPreconditions() {
		assertNotNull("mActivity is null", mActivity);
	}
}
