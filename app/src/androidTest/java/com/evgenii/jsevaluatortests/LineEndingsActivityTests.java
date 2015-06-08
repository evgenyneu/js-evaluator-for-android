package com.evgenii.jsevaluatortests;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.TextView;

public class LineEndingsActivityTests extends ActivityInstrumentationTestCase2<LineEndingsActivity> {

	private LineEndingsActivity mActivity;

	public LineEndingsActivityTests() {
		super(LineEndingsActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
	}

	public void testPreconditions() {
		assertNotNull("mActivity is null", mActivity);
	}

	@MediumTest
	public void testTestLineEndings() throws InterruptedException {
		final TextView resultTextView = (TextView) mActivity
				.findViewById(R.id.lineEndingsViewResult);

		final String expectedResult = "Result: success!";

		for (int i = 0; i < 100; i++) {
			Thread.sleep(100);
			if (resultTextView.getText().toString().equals(expectedResult)) {
				break;
			}
		}

		assertEquals(expectedResult, resultTextView.getText().toString());
	}
}
