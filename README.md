# JavaScript evaluator library for Android

Library for evaluating JavaScript in Android apps.

Supports Android version 3 and newer.

## Usage example

    JsEvaluator jsEvaluator = new JsEvaluator(this);
    mJsEvaluator.evaluate("2 * 17", new JsCallback() {
      @Override
      public void onResult(final String result) {
        // get result here
      }
    });

