package com.evgenii.jsevaluatortests.mocks;

import com.evgenii.jsevaluator.interfaces.HandlerWrapperInterface;

public class HandlerWrapperMock implements HandlerWrapperInterface {
	@Override
	public void post(Runnable runnable) {
		runnable.run();
	}
}
