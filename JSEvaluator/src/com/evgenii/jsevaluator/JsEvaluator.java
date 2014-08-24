package com.evgenii.jsevaluator;

import java.util.ArrayList;

import android.content.Context;

import com.evgenii.jsevaluator.interfaces.CallJavaResultInterface;
import com.evgenii.jsevaluator.interfaces.HandlerWrapperInterface;
import com.evgenii.jsevaluator.interfaces.JsCallback;
import com.evgenii.jsevaluator.interfaces.JsEvaluatorInterface;
import com.evgenii.jsevaluator.interfaces.WebViewWrapperInterface;

public class JsEvaluator implements CallJavaResultInterface, JsEvaluatorInterface {
	public final static String JS_NAMESPACE = "evgeniiJsEvaluator";

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

	public static String getJsForEval(String jsCode, int callbackIndex) {
		jsCode = escapeSlash(jsCode);
		jsCode = escapeSingleQuotes(jsCode);
		jsCode = escapeClosingScript(jsCode);
		jsCode = escapeNewLines(jsCode);

		return String.format("%s.returnResultToJava(eval('%s'), %s);", JS_NAMESPACE, jsCode,
				callbackIndex);
	}

	protected WebViewWrapperInterface mWebViewWrapper;

	private final Context mContext;

	private final ArrayList<JsCallback> mResultCallbacks = new ArrayList<JsCallback>();

	private HandlerWrapperInterface mHandler;

	public JsEvaluator(Context context) {
		mContext = context;
		mHandler = new HandlerWrapper();
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
		int callbackIndex = mResultCallbacks.size();
		if (resultCallback == null) {
			callbackIndex = -1;
		}

		final String js = JsEvaluator.getJsForEval(jsCode, callbackIndex);

		if (resultCallback != null) {
			mResultCallbacks.add(resultCallback);
		}
		getWebViewWrapper().loadJavaScript(js);
	}

	public ArrayList<JsCallback> getResultCallbacks() {
		return mResultCallbacks;
	}

	public WebViewWrapperInterface getWebViewWrapper() {
		if (mWebViewWrapper == null) {
			mWebViewWrapper = new WebViewWrapper(mContext, this);
		}
		return mWebViewWrapper;
	}

	@Override
	public void jsCallFinished(final String value, Integer callIndex) {
		if (callIndex == -1)
			return;

		final JsCallback callback = mResultCallbacks.get(callIndex);

		mHandler.post(new Runnable() {
			@Override
			public void run() {
				callback.onResult(value);
			}
		});
	}

	// Used in test only to replace mHandler with a mock
	public void setHandler(HandlerWrapperInterface handlerWrapperInterface) {
		mHandler = handlerWrapperInterface;
	}

	// Used in test only to replace webViewWrapper with a mock
	public void setWebViewWrapper(WebViewWrapperInterface webViewWrapper) {
		mWebViewWrapper = webViewWrapper;
	}
}
