# JavaScript evaluator library for Android

Library for evaluating JavaScript in Android apps. Supports Android version 3 and newer.

## Usage example

Create evaluator instance:

    JsEvaluator jsEvaluator = new JsEvaluator(this);

`this` is a reference to your activity.

Evaluate JavaScript:

    jsEvaluator.evaluate("2 * 17", new JsCallback() {
      @Override
      public void onResult(final String result) {
        // get result here (optional)
      }
    });

Call a JS function:

    jsEvaluator.callFunction(new JsCallback() {
      @Override
      public void onResult(final String result) {
        // get result here
      }
    }, "functionName", "parameter 1", "parameter 2", 912, 101.3);

Any number of string, int or double parameters can be supplied.

## Tests

Run Android application in `JsEvaluatorTests` project for manual testing.

<img src='https://raw.github.com/evgenyneu/js-evaluator-for-android/master/js_evaluator_screenshot_1.png' alt='JSEvaluator library for Android screenshot 1'>

<img src='https://raw.github.com/evgenyneu/js-evaluator-for-android/master/js_evaluator_screenshot_2.png' alt='JSEvaluator library for Android screenshot 2'>