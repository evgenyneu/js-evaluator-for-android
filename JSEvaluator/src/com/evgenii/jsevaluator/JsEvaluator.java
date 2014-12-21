package com.evgenii.jsevaluator;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

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

	private HandlerWrapperInterface mCallbackHandler;

	private final HandlerWrapperInterface mMainThreadHandler;

	public JsEvaluator(Context context) {
		this(context,new HandlerWrapper());
	}

	public JsEvaluator(Context context, HandlerWrapperInterface callbackHandler) {
		this(context, callbackHandler, new MainThreadHandlerWrapper());
	}

	public JsEvaluator(Context context, HandlerWrapperInterface callbackHandler, HandlerWrapperInterface mainThreadHandler) {
		mContext = context;
		mCallbackHandler = callbackHandler;
		mMainThreadHandler = mainThreadHandler;
	}


	@Override
	public void callFunction(String jsCode, JsCallback resultCallback, String name, Object... args) {
		jsCode += "; " + JsFunctionCallFormatter.toString(name, args);
		evaluate(jsCode, resultCallback);
	}

    private static class BlockingRunnable implements Runnable {

        private final Object mMutex;
        private final String mJavaScriptCode;
        private final String[] mResult;

        private BlockingRunnable(Object mMutex, String jsCode, String[] result) {
            this.mMutex = mMutex;
            this.mJavaScriptCode = jsCode;
            this.mResult = result;
        }

        @Override
        public void run() {

        }
    }

    private static class BlockingJsCallBack implements JsCallback {

        private final Object mMutex;
        private final String[] mResult;

        private BlockingJsCallBack(Object mutex, String[] result) {
            mMutex = mutex;
            mResult = result;
        }

        @Override
        public void onResult(String value) {
            synchronized (mMutex) {
                mResult[0] = value;
                mMutex.notifyAll();
            }
        }
    }

    private interface JsTask {
        void run(JsCallback callback);
    }

    private class JsCallFunctionTask implements JsTask {

        private final String mCode;
        private final String mFunctionName;
        private final Object[] mArgs;

        private JsCallFunctionTask(String code, String mFunctionName, Object[] args) {
            mCode = code;
            this.mFunctionName = mFunctionName;
            mArgs = args;
        }

        @Override
        public void run(JsCallback callback) {
            callFunction(mCode, callback, mFunctionName, mArgs);
        }
    }

    private class JsCallable  extends FutureTask<String> {

        private JsCallable(final JsTask jsTask) {
            super(new Callable<String>(){

                @Override
                public String call() throws Exception {
                    final Object mtx = new Object();
                    final String[] result = new String[]{null};

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            jsTask.run(new BlockingJsCallBack(mtx, result));
                        }
                    });

                    synchronized (mtx) {
                        while (result[0] == null)
                            mtx.wait();
                    }

                    return result[0];
                }
            });
        }
    }


    public FutureTask<String> callFunctionFutureTask(final String jsCode, final String name, final int timeout, final Object... args) {
        JsCallable futureTask = new JsCallable(new JsCallFunctionTask(jsCode, name, args));
        return futureTask;
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

	private static class MainThreadHandlerWrapper implements HandlerWrapperInterface {

		Handler mHandler = new Handler(Looper.getMainLooper());
		@Override
		public void post(Runnable r) {
			mHandler.post(r);
		}
	}
}
