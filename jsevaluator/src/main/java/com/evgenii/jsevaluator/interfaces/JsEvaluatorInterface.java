package com.evgenii.jsevaluator.interfaces;

/**
 * Interface for the JsEvaluator library.
 */

public interface JsEvaluatorInterface {
    /**
     * Evaluates JavaScript code and passes result on UI thread.
     *
     * @param  jsCode           JavaScript code to evaluate.
     * @param  resultCallback   callback to receive the result form JavaScript function. It is called on UI thread.
     */
    public void evaluateAndRespondOnUiThread(String jsCode, JsCallback resultCallback);

    /**
     * Evaluates JavaScript code and passes result on background thread.
     *
     * @param  jsCode           JavaScript code to evaluate.
     * @param  resultCallback   callback to receive the result form JavaScript function. It is called on background thread.
     */
    public void evaluateAndRespondOnBackgroundThread(String jsCode, JsCallback resultCallback);

    /**
     * Calls a JavaScript function and pass arguments to it. Result of evaluation is passed on UI thread.
     *
     * @param  jsCode           JavaScript code to evaluate.
     * @param  resultCallback   callback to receive the result form JavaScript function. It is called on UI thread.
     * @param  functionName     name of the JavaScript function to be called.
     * @param  args             any number of string, integer or double arguments that will be passed to the JavaScript function.
     */
	public void callFunctionAndRespondOnUiThread(String jsCode, JsCallback resultCallback, String functionName, Object... args);

    /**
     * Calls a JavaScript function and pass arguments to it. Result of evaluation is passed on background thread.
     *
     * @param  jsCode           JavaScript code to evaluate.
     * @param  resultCallback   callback to receive the result form JavaScript function. It is called on background thread.
     * @param  functionName     name of the JavaScript function to be called.
     * @param  args             any number of string, integer or double arguments that will be passed to the JavaScript function.
     */
    public void callFunctionAndRespondOnBackgroundThread(String jsCode, JsCallback resultCallback, String functionName, Object... args);
}
