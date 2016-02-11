package com.evgenii.jsevaluator.interfaces;

/**
 * Interface for the JsEvaluator libary.
 */

public interface JsEvaluatorInterface {
    /**
     * Calls a JavaScript function and pass arguments to it.
     *
     * @param  jsCode           JavaScript code to evaluate.
     * @param  resultCallback   callback to receive the result form JavaScript function.
     * @param  functionName     name of the JavaScript function to be called.
     * @param  args             any number of string, integer or double arguments that will be passed to the JavaScript function.
     */
	public void callFunctionAndRespondInUiThread(String jsCode, JsCallback resultCallback, String functionName, Object... args);

	public void evaluate(String jsCode, JsCallback resultCallback);
}
