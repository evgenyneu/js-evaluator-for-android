package com.evgenii.jsevaluatortests;

import android.test.ActivityInstrumentationTestCase2;

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

	public void testPreconditions() {
		assertNotNull("mActivity is null", mActivity);
	}
}
