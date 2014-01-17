package com.evgenii.jsevaluatortests.mocks;

import java.util.ArrayList;

import com.evgenii.jsevaluator.interfaces.WebViewWrapperInterface;

public class WebViewWrapperMock implements WebViewWrapperInterface {
	public ArrayList<String> mLoadedUrls = new ArrayList<String>();

	@Override
	public void loadUrl(String url) {
		mLoadedUrls.add(url);
	}
}
