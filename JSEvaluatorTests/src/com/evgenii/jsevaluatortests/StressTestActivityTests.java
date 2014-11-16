package com.evgenii.jsevaluatortests;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;
import android.widget.TextView;

public class StressTestActivityTests extends ActivityInstrumentationTestCase2<StressTestActivity> {

	private StressTestActivity mActivity;

	public StressTestActivityTests() {
		super(StressTestActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
	}

	@MediumTest
	public void test_runsJSAndShowsResult() throws InterruptedException {
		final TextView resultTextView = (TextView) mActivity
				.findViewById(R.id.js_stress_result_text_view);

		assertTrue(View.VISIBLE == resultTextView.getVisibility());

		final String expectedResult = "Result: 44000";

		for (int i = 0; i < 100; i++) {
			Thread.sleep(100);
			if (resultTextView.getText().toString().equals(expectedResult)) {
				break;
			}
		}
		assertEquals(expectedResult, resultTextView.getText().toString());
	}
}
