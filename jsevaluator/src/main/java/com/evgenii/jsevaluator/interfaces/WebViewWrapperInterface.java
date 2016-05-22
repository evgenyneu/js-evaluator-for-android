package com.evgenii.jsevaluator.interfaces;

public interface WebViewWrapperInterface {
	public void loadJavaScript(String javascript);

	// Destroys the web view in order to free the memory.
	// The web view can not be accessed after is has been destroyed.
	public void destroy();
}
