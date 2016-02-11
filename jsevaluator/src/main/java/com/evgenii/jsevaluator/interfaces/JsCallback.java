package com.evgenii.jsevaluator.interfaces;

/**
 * Interface for passing code that will be executed after the JS has finished
 */

public interface JsCallback {
    /**
     * Called by JsEvaluator to pass the result of JavaScript evaluation.
     *
     * @param  value result of JavaScript evaluation.
     */
	public abstract void onResult(String value);
}
