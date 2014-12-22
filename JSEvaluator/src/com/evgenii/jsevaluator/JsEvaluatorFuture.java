package com.evgenii.jsevaluator;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.evgenii.jsevaluator.interfaces.HandlerWrapperInterface;
import com.evgenii.jsevaluator.interfaces.JsCallback;
import com.evgenii.jsevaluator.interfaces.JsEvaluatorInterface;

public class JsEvaluatorFuture extends JsEvaluator {

	public JsEvaluatorFuture(Context context) {
		super(context);
	}

	public JsEvaluatorFuture(Context context, HandlerWrapperInterface callbackHandler) {
		super(context, callbackHandler);
	}

	public JsEvaluatorFuture(Context context, HandlerWrapperInterface callbackHandler, HandlerWrapperInterface mainThreadHandler) {
		super(context, callbackHandler, mainThreadHandler);
	}


	public FutureTask<String> callFunctionFutureTask(final String jsCode, final String name, final Object... args) {
		return new JsFutureTask(new JsCallFunctionTask(this, jsCode, name, args));
	}

	public FutureTask<String> evaluateFutureTask(String jsCode) {
		return new JsFutureTask(new JsEvaluateTask(this, jsCode));
	}


	private interface JsTask {
		void run(JsCallback callback);
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
			debugLog(new Throwable());
			synchronized (mMutex) {
				mResult[0] = value;
				mMutex.notifyAll();
			}
			debugLog(new Throwable());
		}
	}

	private static class JsCallFunctionTask implements JsTask {

		private final String mCode;
		private final String mFunctionName;
		private final Object[] mArgs;
		private JsEvaluatorInterface mJsEvaluator;

		private JsCallFunctionTask(JsEvaluatorInterface jsEvaluator, String code, String mFunctionName, Object[] args) {
			mCode = code;
			this.mFunctionName = mFunctionName;
			mArgs = args;
			this.mJsEvaluator = jsEvaluator;
		}

		@Override
		public void run(JsCallback callback) {
			mJsEvaluator.callFunction(mCode, callback, mFunctionName, mArgs);
		}
	}

	private static class JsEvaluateTask implements JsTask {

		private final String mCode;
		private JsEvaluatorInterface mJsEvaluator;

		private JsEvaluateTask(JsEvaluatorInterface jsEvaluator, String code) {
			mCode = code;
			this.mJsEvaluator = jsEvaluator;
		}

		@Override
		public void run(JsCallback callback) {
			mJsEvaluator.evaluate(mCode, callback);
		}
	}

	private class JsFutureTask extends FutureTask<String> {

		private JsFutureTask(final JsTask jsTask) {
			super(new Callable<String>(){

				@Override
				public String call() throws Exception {
					final Object mtx = new Object();
					final String[] result = new String[]{null};
					debugLog(new Throwable());
					jsTask.run(new BlockingJsCallBack(mtx, result));

					synchronized (mtx) {
						while (result[0] == null)
							mtx.wait();
					}

					debugLog(new Throwable());
					return result[0];
				}
			});
		}
	}

	public static void debugLog(Throwable throwable){
		final StackTraceElement element = throwable.getStackTrace()[0];
		Log.d("TEST", element.getClass().getSimpleName() + "." + element.getMethodName() + ":" + element.getLineNumber());
	}
}
