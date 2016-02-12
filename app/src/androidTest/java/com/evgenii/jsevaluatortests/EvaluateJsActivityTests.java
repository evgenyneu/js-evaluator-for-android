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
	public void testEvaluateButton_clickButtonAndShowResult() throws InterruptedException {
		final Button evaluateButton = (Button) mActivity.findViewById(R.id.button_evaluate);

		TouchUtils.clickView(this, evaluateButton);

		// UI thread result
		// --------------

        final TextView resultUiThreadTextView = (TextView) mActivity.findViewById(R.id.js_result_ui_thread_text_view);
        assertTrue(View.VISIBLE == resultUiThreadTextView.getVisibility());
		assertEquals("UI thread result: 4", resultUiThreadTextView.getText().toString());

		// Background thread result
		// --------------

        final TextView resultBackgroundThreadTextView = (TextView) mActivity.findViewById(R.id.js_result_background_thread_text_view);
        assertTrue(View.VISIBLE == resultBackgroundThreadTextView.getVisibility());
		assertEquals("Background thread result: 4", resultBackgroundThreadTextView.getText().toString());

		// Block UI thread and evaluate
		// --------------

		final TextView resultBlockUiThreadTextView = (TextView) mActivity.findViewById(R.id.js_result_block_ui_thread_text_view);
		assertTrue(View.VISIBLE == resultBlockUiThreadTextView.getVisibility());
		assertEquals("Block UI thread result: 4", resultBlockUiThreadTextView.getText().toString());
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
