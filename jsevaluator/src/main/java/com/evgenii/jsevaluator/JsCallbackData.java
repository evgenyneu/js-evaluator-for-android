package com.evgenii.jsevaluator;

import com.evgenii.jsevaluator.interfaces.JsCallback;

/**
 * Data object that is used to store the callback for JavaScript evaluation result
 * and whether to call in the UI thread.
 */

public class JsCallbackData {

    /**
     * Create new callback data
     *
     * @param aCallback             will be passed the result of JavaScript evaluation.
     * @param shouldCallInUiThread  whether to execute the callback in the UI thread or background thread.
     */
    public JsCallbackData(JsCallback aCallback, Boolean shouldCallInUiThread) {
        callback = aCallback;
        callInUiThread = shouldCallInUiThread;
    }

    /**
     * Callback method that is used to pass the result of JavaScript evaluation.
     */
    public JsCallback callback;

    /**
     * When true the callback is executed in the UI thread.
     * When false the callback is executed in background thread.
     */
    public Boolean callInUiThread = true;
}
