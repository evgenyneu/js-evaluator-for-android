package com.evgenii.jsevaluator;

import android.os.Handler;

import com.evgenii.jsevaluator.interfaces.HandlerWrapperInterface;

public class UiThreadHandlerWrapper implements HandlerWrapperInterface {
	private final Handler mHandler;

	public UiThreadHandlerWrapper() {
		mHandler = new Handler();
	}

	@Override
	public void post(Runnable r) {
		mHandler.post(r);
	}
}
