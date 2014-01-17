package com.evgenii.jsevaluatortests;

import android.test.AndroidTestCase;

import com.evgenii.jsevaluator.WebViewWrapper;

public class WebViewWrapperTest extends AndroidTestCase {
	public void testLoadUrl() {
		final WebViewWrapper wrapper = new WebViewWrapper(mContext, null);
		wrapper.loadUrl("javascript: 12");
	}
}
