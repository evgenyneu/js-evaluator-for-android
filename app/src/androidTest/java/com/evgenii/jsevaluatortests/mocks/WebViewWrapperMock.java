package com.evgenii.jsevaluatortests.mocks;

import java.util.ArrayList;

import com.evgenii.jsevaluator.interfaces.WebViewWrapperInterface;

public class WebViewWrapperMock implements WebViewWrapperInterface {
	public ArrayList<String> mLoadedJavaScript = new ArrayList<String>();

	@Override
	public void loadJavaScript(String javascript) {
		mLoadedJavaScript.add(javascript);
	}
}
