# JavaScript evaluator library for Android

Library for evaluating JavaScript in Android apps. Supports Android version 3 and newer.

## Setup

To add JSEvaluator to your app in Eclipse:

1. File > Import `JSEvaluator` project.
1. Open properties for **your** app project.
1. Select `Libraries` tab in `Java Build Path`.
1. Click `Add JARs...` button and select `jsevaluator.jar` from `JSEvaluator/bin` directory.
1. In your project properties click `Order and export` tab.
1. Tick `jsevaluator.jar`.

## Usage

Create evaluator instance:

    JsEvaluator jsEvaluator = new JsEvaluator(this);

`this` is a reference to your activity. You can do it inside `onCreate` method, for example.

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

Tests are located in `JsEvaluatorTests` project.

Run as `Android Application` for manual testing.

<img src='https://raw.github.com/evgenyneu/js-evaluator-for-android/master/js_evaluator_screenshot_1.png' width='270' alt='JSEvaluator library for Android screenshot 1'> &nbsp;
<img src='https://raw.github.com/evgenyneu/js-evaluator-for-android/master/js_evaluator_screenshot_2.png' width='270' alt='JSEvaluator library for Android screenshot 2'>

Or run as `Android JUnit Test` for unit testing.

## Feedback

If you have any issues or need help, let me know - I will be happy to assist you.

