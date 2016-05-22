package com.evgenii.jsevaluatortests;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;

import com.evgenii.jsevaluator.WebViewWrapper;

public class WebViewWrapperTest extends ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity mActivity;

	public WebViewWrapperTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
	}

	public void testLoadJavaScript() {
		final Instrumentation mInstrumentation = getInstrumentation();
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				final WebViewWrapper wrapper = new WebViewWrapper(mActivity, null);
				wrapper.loadJavaScript("2 + 3");
			}
		});

		mInstrumentation.waitForIdleSync();
	}
}
