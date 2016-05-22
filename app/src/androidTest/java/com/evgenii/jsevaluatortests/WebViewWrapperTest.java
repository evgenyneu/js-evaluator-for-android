package com.evgenii.jsevaluatortests;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Base64;

import com.evgenii.jsevaluator.WebViewWrapper;

import java.io.UnsupportedEncodingException;

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

	public void testDestroyWebView() {
		final Instrumentation mInstrumentation = getInstrumentation();
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				final WebViewWrapper wrapper = new WebViewWrapper(mActivity, null);
				wrapper.loadJavaScript("2 + 3");

				assertNotNull(wrapper.getWebView());
				wrapper.destroy();
				Boolean exceptionRaised = false;

				try {
					wrapper.loadJavaScript("2 + 3");
					// Must fail because the web view has been destroyed.
				} catch (final NullPointerException e) {
					exceptionRaised = true;
				}

				assertTrue(exceptionRaised);
				assertNull(wrapper.getWebView());
			}
		});

		mInstrumentation.waitForIdleSync();
	}
}
