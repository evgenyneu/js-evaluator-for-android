package com.evgenii.jsevaluator;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
     * Evaluates JavaScript code and passes result on UI thread.
     *
     * @param  jsCode           JavaScript code to evaluate.
     * @param  resultCallback   callback to receive the result form JavaScript function. It is called on UI thread.
     */
    public void evaluateAndRespondOnUiThread(String jsCode, JsCallback resultCallback) {
        evaluate(
                jsCode,
                resultCallback,
                true // execute callback on UI thread
        );
    }

    @Override
    /**
     * Evaluates JavaScript code and passes result on background thread.
     *
     * @param  jsCode           JavaScript code to evaluate.
     * @param  resultCallback   callback to receive the result form JavaScript function. It is called on background thread.
     */
    public void evaluateAndRespondOnBackgroundThread(String jsCode, JsCallback resultCallback) {
        evaluate(
                jsCode,
                resultCallback,
                false // execute callback on background thread
        );
    }

    @Override
    /**
     * Evaluates JavaScript code and returns the result. UI thread will be blocked during JavaScript evaluation and the app will appear frozen to the user.
     *
     * @param  waitTimeoutMilliseconds  wait time in milliseconds. The function will return null if it fails to evaluate JavaScript within the given time period.
     * @param  jsCode                   JavaScript code to evaluate.
     * @return                          result of JavaScript evaluation. The function will return null if it fails to evaluate JavaScript within the given time period.
     */
    public String blockUIThreadAndEvaluate(long waitTimeoutMilliseconds, String jsCode) {
        return blockUiThreadAndEvaluateShared(waitTimeoutMilliseconds, jsCode);
    }

    private String blockUiThreadAndEvaluateShared(long waitTimeoutMilliseconds, String jsCode) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final JsResultContainer jsResultContainer = new JsResultContainer();

        evaluateAndRespondOnBackgroundThread(jsCode, new JsCallback() {
            @Override
            public void onResult(final String result) {
                jsResultContainer.result = result;
                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await(waitTimeoutMilliseconds, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) { }

        return jsResultContainer.result;
    }

    private void evaluate(String jsCode, JsCallback resultCallback, Boolean executeCallbackInUiThread) {
        int callbackIndex = mResultCallbacks.size();
        if (resultCallback == null) { callbackIndex = -1; }
        final String js = JsEvaluator.getJsForEval(jsCode, callbackIndex);
        JsCallbackData callbackData = new JsCallbackData(resultCallback, executeCallbackInUiThread);
        if (resultCallback != null) { mResultCallbacks.add(callbackData); }
        getWebViewWrapper().loadJavaScript(js);
    }

	@Override
    /**
     * Calls a JavaScript function and pass arguments to it. Result of evaluation is passed on UI thread.
     *
     * @param  jsCode           JavaScript code to evaluate.
     * @param  resultCallback   callback to receive the result form JavaScript function. It is called on UI thread.
     * @param  functionName     name of the JavaScript function to be called.
     * @param  args             any number of string, integer or double arguments that will be passed to the JavaScript function.
     */
	public void callFunctionAndRespondOnUiThread(String jsCode, JsCallback resultCallback, String functionName, Object... args) {
        callFunction(
                true, // execute callback on UI thread
                jsCode,
                resultCallback,
                functionName,
                args
        );
	}

    @Override
    /**
     * Calls a JavaScript function and pass arguments to it. Result of evaluation is passed on background thread.
     *
     * @param  jsCode           JavaScript code to evaluate.
     * @param  resultCallback   callback to receive the result form JavaScript function. It is called on background thread.
     * @param  functionName     name of the JavaScript function to be called.
     * @param  args             any number of string, integer or double arguments that will be passed to the JavaScript function.
     */
    public void callFunctionAndRespondOnBackgroundThread(String jsCode, JsCallback resultCallback, String functionName, Object... args) {
        callFunction(
                false, // execute callback on background thread
                jsCode,
                resultCallback,
                functionName,
                args
        );
    }

    @Override
    /**
     * Calls a JavaScript function and returns the result. UI thread will be blocked during JavaScript evaluation and the app will appear frozen to the user.
     *
     * @param  waitTimeoutMilliseconds  wait time in milliseconds. The function will return null if it fails to evaluate JavaScript within the given time period.
     * @param  jsCode                   JavaScript code to evaluate.
     * @param  functionName             name of the JavaScript function to be called.
     * @param  args                     any number of string, integer or double arguments that will be passed to the JavaScript function.
     */
    public String blockUIThreadAndCallFunction(long waitTimeoutMilliseconds, String jsCode, String functionName, Object... args) {
        jsCode += "; " + JsFunctionCallFormatter.toString(functionName, args);
        return blockUiThreadAndEvaluateShared(waitTimeoutMilliseconds, jsCode);
    }

    private void callFunction(Boolean executeCallbackInUiThread, String jsCode, JsCallback resultCallback, String functionName, Object... args) {
        jsCode += "; " + JsFunctionCallFormatter.toString(functionName, args);
        evaluate(jsCode, resultCallback, executeCallbackInUiThread);
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

        if (callbackData.callOnUiThread) {
            // Execute on UI thread
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
