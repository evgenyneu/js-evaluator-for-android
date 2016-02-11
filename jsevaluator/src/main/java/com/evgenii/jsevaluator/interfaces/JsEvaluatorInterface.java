package com.evgenii.jsevaluator.interfaces;

/**
 * Interface for the JsEvaluator library.
 */

public interface JsEvaluatorInterface {
    /**
     * Evaluates JavaScript code and passes result in the UI thread.
     *
     * @param  jsCode           JavaScript code to evaluate.
     * @param  resultCallback   callback to receive the result form JavaScript function. It is called in the UI thread.
     */
    public void evaluateAndRespondInUiThread(String jsCode, JsCallback resultCallback);

    /**
     * Evaluates JavaScript code and passes result in the background thread.
     *
     * @param  jsCode           JavaScript code to evaluate.
     * @param  resultCallback   callback to receive the result form JavaScript function. It is called in the background thread.
     */
    public void evaluateAndRespondInBackgroundThread(String jsCode, JsCallback resultCallback);

    /**
     * Calls a JavaScript function and pass arguments to it. Result of evaluation is passed in the UI thread.
     *
     * @param  jsCode           JavaScript code to evaluate.
     * @param  resultCallback   callback to receive the result form JavaScript function. It is called in the UI thread.
     * @param  functionName     name of the JavaScript function to be called.
     * @param  args             any number of string, integer or double arguments that will be passed to the JavaScript function.
     */
	public void callFunctionAndRespondInUiThread(String jsCode, JsCallback resultCallback, String functionName, Object... args);
}
