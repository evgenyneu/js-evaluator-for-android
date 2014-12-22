package com.evgenii.jsevaluator;

import android.os.Handler;
import android.os.Looper;

import com.evgenii.jsevaluator.interfaces.HandlerWrapperInterface;

public class HandlerWrapper implements HandlerWrapperInterface {
	private final Handler mHandler;

	public HandlerWrapper() {
		this(new Handler());
	}

	public HandlerWrapper(Looper looperToUse) {
		this(new Handler(looperToUse));
	}

    public HandlerWrapper(Handler handlerToWrap) {
        mHandler = handlerToWrap;
    }

	@Override
	public void post(Runnable r) {
		mHandler.post(r);
	}


	@Override
	public void runOnHandlerThread(Runnable r) {
		if(Thread.currentThread() == mHandler.getLooper().getThread()){
			r.run();
		} else {
			post(r);
		}
	}
}
