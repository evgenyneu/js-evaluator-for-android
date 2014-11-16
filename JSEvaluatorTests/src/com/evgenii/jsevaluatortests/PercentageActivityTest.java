package com.evgenii.jsevaluatortests;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PercentageActivityTest extends ActivityInstrumentationTestCase2<PercentageActivity> {
	private PercentageActivity mActivity;

	public PercentageActivityTest() {
		super(PercentageActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
	}

	@MediumTest
	public void testEvaluateButton_clickButtonAndShowResult() {
		final Button evaluateButton = (Button) mActivity
				.findViewById(R.id.button_evaluate_percentage);

		final TextView resultTextView = (TextView) mActivity
				.findViewById(R.id.percentageResultLabel);

		TouchUtils.clickView(this, evaluateButton);
		assertTrue(View.VISIBLE == resultTextView.getVisibility());
		assertEquals("Result: 5%7E 12.5% growth", resultTextView.getText().toString());
	}

	@MediumTest
	public void testJsTextView_shouldHaveJsText() {
		final TextView editJsTextView = (TextView) mActivity.findViewById(R.id.percentageCodeLabel);

		final String expected = mActivity.getString(R.string.percetage_js_code);
		final String actual = editJsTextView.getText().toString();
		assertEquals(expected, actual);
	}

	public void testPreconditions() {
		assertNotNull("mActivity is null", mActivity);
	}

}
