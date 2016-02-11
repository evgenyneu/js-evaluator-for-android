package com.evgenii.jsevaluatortests.mocks;

import com.evgenii.jsevaluator.interfaces.HandlerWrapperInterface;

public class UiThreadHandlerWrapperMock implements HandlerWrapperInterface {
    /**
     * True if the post method has been executed. Will be checked in unit tests.
     */
    public Boolean didRunMock = false;

	@Override
	public void post(Runnable runnable) {
        didRunMock = true;
		runnable.run();
	}
}
