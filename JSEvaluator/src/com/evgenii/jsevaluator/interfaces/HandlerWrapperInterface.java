package com.evgenii.jsevaluator.interfaces;

public interface HandlerWrapperInterface {
	public void post(Runnable r);

    /**
     * If the current thread is the handled thread, 'r' is executed
     * immediately. Otherwise 'r' is scheduled to run on the handled
     * thread.
     */
	public void runOnHandlerThread(Runnable r);
}
