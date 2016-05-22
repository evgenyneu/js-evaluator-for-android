package com.evgenii.jsevaluatortests.mocks;

import java.util.ArrayList;

import com.evgenii.jsevaluator.interfaces.WebViewWrapperInterface;
import android.webkit.WebView;


public class WebViewWrapperMock implements WebViewWrapperInterface {
	public ArrayList<String> mLoadedJavaScript = new ArrayList<String>();

	@Override
	public void loadJavaScript(String javascript) {
		mLoadedJavaScript.add(javascript);
	}

	@Override
	public void destroy() { }

	@Override
	public WebView getWebView() { return null; }
}
