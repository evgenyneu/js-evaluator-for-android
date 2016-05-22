package com.evgenii.jsevaluator.interfaces;

import android.webkit.WebView;

public interface WebViewWrapperInterface {
	public void loadJavaScript(String javascript);

	// Destroys the web view in order to free the memory.
	// The web view can not be accessed after is has been destroyed.
	public void destroy();

	// Returns the WebView object
	public WebView getWebView();
}
