package com.evgenii.jsevaluator;

import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.webkit.WebView;

import com.evgenii.jsevaluator.interfaces.CallJavaResultInterface;
import com.evgenii.jsevaluator.interfaces.HandlerWrapperInterface;
import com.evgenii.jsevaluator.interfaces.JsCallback;
import com.evgenii.jsevaluator.interfaces.JsEvaluatorInterface;
import com.evgenii.jsevaluator.interfaces.WebViewWrapperInterface;

import java.util.concurrent.atomic.AtomicReference;

public class JsEvaluator implements CallJavaResultInterface, JsEvaluatorInterface {
	public final static String JS_NAMESPACE = "evgeniiJsEvaluator";
	private final static String JS_ERROR_PREFIX = "evgeniiJsEvaluatorException";

	public static String escapeCarriageReturn(String str) {
		return str.replace("\r", "\\r");
	}

	public static String escapeClosingScript(String str) {
		return str.replace("</", "<\\/");
	}

	public static String escapeNewLines(String str) {
		return str.replace("\n", "\\n");
	}

	public static String escapeSingleQuotes(String str) {
		return str.replace("'", "\\'");
	}

	public static String escapeSlash(String str) {
		return str.replace("\\", "\\\\");
	}

	public static String getJsForEval(String jsCode) {
		jsCode = escapeSlash(jsCode);
		jsCode = escapeSingleQuotes(jsCode);
		jsCode = escapeClosingScript(jsCode);
		jsCode = escapeNewLines(jsCode);
		jsCode = escapeCarriageReturn(jsCode);

		return String.format("%s.returnResultToJava(eval('try{%s}catch(e){\"%s\"+e}'));",
						JS_NAMESPACE, jsCode, JS_ERROR_PREFIX);
	}

	protected WebViewWrapperInterface mWebViewWrapper;

	private final Context mContext;

	private AtomicReference<JsCallback> callback = new AtomicReference<>(null);

	private HandlerWrapperInterface mHandler = new HandlerWrapper();

	public JsEvaluator(Context context) {
		mContext = context;
	}

	@Override
	public void callFunction(String jsCode, JsCallback resultCallback, String name, Object... args) {
		jsCode += "; " + JsFunctionCallFormatter.toString(name, args);
		evaluate(jsCode, resultCallback);
	}

	@Override
	public void evaluate(String jsCode) {
		evaluate(jsCode, null);
	}

	@Override
	public void evaluate(String jsCode, JsCallback resultCallback) {
		final String js = JsEvaluator.getJsForEval(jsCode);
        this.callback.set(resultCallback);
		getWebViewWrapper().loadJavaScript(js);
	}

	// Destroys the web view in order to free the memory.
	// The web view can not be accessed after is has been destroyed.
	// To check open the page in Chrome: chrome://inspect/#devices
	public void destroy() {
		getWebViewWrapper().destroy();
	}

	// Returns the WebView object
	public WebView getWebView() {
		return getWebViewWrapper().getWebView();
	}

	public WebViewWrapperInterface getWebViewWrapper() {
		if (mWebViewWrapper == null) {
			mWebViewWrapper = new WebViewWrapper(mContext, this);
		}
		return mWebViewWrapper;
	}

	@Override
	public void jsCallFinished(final String value) {
		final JsCallback callbackLocal = callback.getAndSet(null);
		if (callbackLocal == null) {
			return;
		}

		mHandler.post(new Runnable() {
			@Override
			public void run() {
				if(value!=null && value.startsWith(JS_ERROR_PREFIX)) {
					callbackLocal.onError(value.substring(JS_ERROR_PREFIX.length()));
				} else {
					callbackLocal.onResult(value);
				}
			}
		});
	}

	@VisibleForTesting
	public void setHandler(HandlerWrapperInterface handlerWrapperInterface) {
		mHandler = handlerWrapperInterface;
	}

	@VisibleForTesting
	public void setWebViewWrapper(WebViewWrapperInterface webViewWrapper) {
		mWebViewWrapper = webViewWrapper;
	}

	@VisibleForTesting
	public JsCallback getCallback() {
		return callback.get();
	}

}
