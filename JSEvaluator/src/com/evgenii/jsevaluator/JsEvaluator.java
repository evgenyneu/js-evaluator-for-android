package com.evgenii.jsevaluator;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.evgenii.jsevaluator.interfaces.CallJavaResultInterface;
import com.evgenii.jsevaluator.interfaces.HandlerWrapperInterface;
import com.evgenii.jsevaluator.interfaces.JsCallback;
import com.evgenii.jsevaluator.interfaces.JsEvaluatorInterface;
import com.evgenii.jsevaluator.interfaces.WebViewWrapperInterface;

import java.util.ArrayList;

public class JsEvaluator implements CallJavaResultInterface, JsEvaluatorInterface {
	public final static String JS_NAMESPACE = "evgeniiJsEvaluator";
	protected final Context mContext;
	protected final HandlerWrapperInterface mMainThreadHandler;
	private final ArrayList<JsCallback> mResultCallbacks = new ArrayList<JsCallback>();
	protected WebViewWrapperInterface mWebViewWrapper;
	protected HandlerWrapperInterface mCallbackHandler;

	public JsEvaluator(Context context) {
		this(context, new NewThreadHandlerWrapper());
	}

	public JsEvaluator(Context context, HandlerWrapperInterface callbackHandler) {
		this(context, callbackHandler, new HandlerWrapper(Looper.getMainLooper()));
	}

	public JsEvaluator(Context context, HandlerWrapperInterface callbackHandler, HandlerWrapperInterface mainThreadHandler) {
		mMainThreadHandler = mainThreadHandler;
		mContext = context;
		mCallbackHandler = callbackHandler;
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

	public static String getJsForEval(String jsCode, int callbackIndex) {
		jsCode = escapeSlash(jsCode);
		jsCode = escapeSingleQuotes(jsCode);
		jsCode = escapeClosingScript(jsCode);
		jsCode = escapeNewLines(jsCode);

		return String.format("%s.returnResultToJava(eval('%s'), %s);", JS_NAMESPACE, jsCode,
				callbackIndex);
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
			mWebViewWrapper = new WebViewWrapper(mContext, this, mMainThreadHandler);
		}
		return mWebViewWrapper;
	}

	@Override
	public void jsCallFinished(final String value, Integer callIndex) {
		if (callIndex == -1)
			return;

		final JsCallback callback = mResultCallbacks.get(callIndex);

		mCallbackHandler.post(new Runnable() {
			@Override
			public void run() {
				callback.onResult(value);
			}
		});
	}

	// Used in test only to replace mCallbackHandler with a mock
	public void setCallbackHandler(HandlerWrapperInterface handlerWrapperInterface) {
		mCallbackHandler = handlerWrapperInterface;
	}

	// Used in test only to replace webViewWrapper with a mock
	public void setWebViewWrapper(WebViewWrapperInterface webViewWrapper) {
		mWebViewWrapper = webViewWrapper;
	}

	/**
	 * Creates a new thread to run posted tasks
	 *
	 * Uses {@link com.evgenii.jsevaluator.HandlerWrapper} internally
	 */
	private static class NewThreadHandlerWrapper implements HandlerWrapperInterface{

		private final HandlerThread mHandlerThread;
		private final HandlerWrapper mHandlerWrapper;
		private static int sThreadCount = 0;
		NewThreadHandlerWrapper(){
			this("newthread_" + sThreadCount ++);

		}
		NewThreadHandlerWrapper(final String threadName){
			mHandlerThread = new HandlerThread(threadName);
			mHandlerThread.start();
			mHandlerWrapper = new HandlerWrapper(mHandlerThread.getLooper());

		}

		@Override
		public void post(Runnable r) {
			mHandlerWrapper.post(r);
		}

		@Override
		public void runOnHandlerThread(Runnable r) {
			mHandlerWrapper.runOnHandlerThread(r);
		}

	}
}
