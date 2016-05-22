package com.evgenii.jsevaluator.interfaces;

import android.webkit.WebView;

public interface JsEvaluatorInterface {
	public void callFunction(String jsCode, JsCallback resultCallback, String name, Object... args);

	public void evaluate(String jsCode);

	public void evaluate(String jsCode, JsCallback resultCallback);

	// Destroys the web view in order to free the memory.
	// The web view can not be accessed after is has been destroyed.
	public void destroy();

	// Returns the WebView object
	public WebView getWebView();
}
