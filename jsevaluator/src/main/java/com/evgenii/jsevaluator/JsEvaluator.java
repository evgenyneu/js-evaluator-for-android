package com.evgenii.jsevaluator;

import java.util.ArrayList;

import android.content.Context;

import com.evgenii.jsevaluator.interfaces.CallJavaResultInterface;
import com.evgenii.jsevaluator.interfaces.HandlerWrapperInterface;
import com.evgenii.jsevaluator.interfaces.JsCallback;
import com.evgenii.jsevaluator.interfaces.JsEvaluatorInterface;
import com.evgenii.jsevaluator.interfaces.WebViewWrapperInterface;

/**
 * Evaluates JavaScript.
 */

public class JsEvaluator implements CallJavaResultInterface, JsEvaluatorInterface {
	public final static String JS_NAMESPACE = "evgeniiJsEvaluator";

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

	public static String getJsForEval(String jsCode, int callbackIndex) {
		jsCode = escapeSlash(jsCode);
		jsCode = escapeSingleQuotes(jsCode);
		jsCode = escapeClosingScript(jsCode);
		jsCode = escapeNewLines(jsCode);
		jsCode = escapeCarriageReturn(jsCode);

		return String.format("%s.returnResultToJava(eval('%s'), %s);", JS_NAMESPACE, jsCode,
				callbackIndex);
	}

	protected WebViewWrapperInterface mWebViewWrapper;

	private final Context mContext;

	private final ArrayList<JsCallbackData> mResultCallbacks = new ArrayList<JsCallbackData>();

    /**
     * A handler used for passing JavaScript result to the UI thread.
     */
	private HandlerWrapperInterface mUiThreadHandler;

	public JsEvaluator(Context context) {
		mContext = context;
		mUiThreadHandler = new UiThreadHandlerWrapper();
	}

    @Override
    /**
     * Evaluates JavaScript code and passes result in the UI thread.
     *
     * @param  jsCode           JavaScript code to evaluate.
     * @param  resultCallback   callback to receive the result form JavaScript function. It is called in the UI thread.
     */
    public void evaluateAndRespondInUiThread(String jsCode, JsCallback resultCallback) {
        int callbackIndex = mResultCallbacks.size();
        if (resultCallback == null) { callbackIndex = -1; }
        final String js = JsEvaluator.getJsForEval(jsCode, callbackIndex);
        JsCallbackData callbackData = new JsCallbackData(resultCallback, true);
        if (resultCallback != null) { mResultCallbacks.add(callbackData); }
        getWebViewWrapper().loadJavaScript(js);
    }

    @Override
    /**
     * Evaluates JavaScript code and passes result in the background thread.
     *
     * @param  jsCode           JavaScript code to evaluate.
     * @param  resultCallback   callback to receive the result form JavaScript function. It is called in the background thread.
     */
    public void evaluateAndRespondInBackgroundThread(String jsCode, JsCallback resultCallback) {

    }

	@Override
    /**
     * Calls a JavaScript function and pass arguments to it. Result of evaluation is passed in the UI thread.
     *
     * @param  jsCode           JavaScript code to evaluate.
     * @param  resultCallback   callback to receive the result form JavaScript function. It is called in the UI thread.
     * @param  functionName     name of the JavaScript function to be called.
     * @param  args             any number of string, integer or double arguments that will be passed to the JavaScript function.
     */
	public void callFunctionAndRespondInUiThread(String jsCode, JsCallback resultCallback, String functionName, Object... args) {
		jsCode += "; " + JsFunctionCallFormatter.toString(functionName, args);
        evaluateAndRespondInUiThread(jsCode, resultCallback);
	}

	public ArrayList<JsCallbackData> getResultCallbacks() {
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

		final JsCallbackData callbackData = mResultCallbacks.get(callIndex);

        if (callbackData.callInUiThread) {
            mUiThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    callbackData.callback.onResult(value);
                }
            });
        } else {
            // Execute in current (background) thread
            callbackData.callback.onResult(value);
        }
	}

	// Used in test only to replace mUiThreadHandler with a mock
	public void setUiThreadHandler(HandlerWrapperInterface handlerWrapperInterface) {
		mUiThreadHandler = handlerWrapperInterface;
	}

	// Used in test only to replace webViewWrapper with a mock
	public void setWebViewWrapper(WebViewWrapperInterface webViewWrapper) {
		mWebViewWrapper = webViewWrapper;
	}
}
