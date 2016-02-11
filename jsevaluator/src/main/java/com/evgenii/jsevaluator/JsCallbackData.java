package com.evgenii.jsevaluator;

import com.evgenii.jsevaluator.interfaces.JsCallback;

/**
 * Data object that is used to store the callback for JavaScript evaluation result
 * and whether to call on UI thread or background thread.
 */

public class JsCallbackData {

    /**
     * Create new callback data
     *
     * @param aCallback             will be passed the result of JavaScript evaluation.
     * @param shouldCallOnUiThread  whether to execute the callback on UI thread or background thread.
     */
    public JsCallbackData(JsCallback aCallback, Boolean shouldCallOnUiThread) {
        callback = aCallback;
        callOnUiThread = shouldCallOnUiThread;
    }

    /**
     * Callback method that is used to pass the result of JavaScript evaluation.
     */
    public JsCallback callback;

    /**
     * When true the callback is executed on UI thread.
     * When false the callback is executed on background thread.
     */
    public Boolean callOnUiThread = true;
}
