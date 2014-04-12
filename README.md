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

## How it works

Behind the scenes it creats a `WebView` and feeds it your JavaScript code for evaluation:

    mWebView = new WebView(context);
    mWebView.loadUrl("javascript: myObj.returnResult('Hello World');");
    
The result of evaluation is sent back into your Android activity:

    public class JavaScriptInterface {
    	public void returnResult(String result) {
    		// result from JavaScript
    	}
    }

    mWebView.addJavascriptInterface(new JavaScriptInterface(), "myObj");

## Tests

Tests are located in `JsEvaluatorTests` project.

Run as `Android Application` for manual testing.

<img src='https://raw.github.com/evgenyneu/js-evaluator-for-android/master/js_evaluator_screenshot_1.png' width='270' alt='JSEvaluator library for Android screenshot 1'> &nbsp;
<img src='https://raw.github.com/evgenyneu/js-evaluator-for-android/master/js_evaluator_screenshot_2.png' width='270' alt='JSEvaluator library for Android screenshot 2'>

Or run as `Android JUnit Test` for unit testing.

## Synchronous loading

JavaScript code is loaded synchronously. For example, suppose you have two JavaScript libraries `jQuery` and `underscore`. You can load them like this, they will be evaluated synchronously, one after another.

    jsEvaluator.evaluate(jQuery);
    
    // jQuery is fully loaded at this point
    // So you can use it right away
    jsEvaluator.evaluate("$.isNumeric(123)", new JsCallback() { ...
    
This is handy, because you can load a library and then use it on the next line.
    
## Result is returned asynchronously

Unlike loading the result from JavaScript is returned asynchronously.

    jsEvaluator.evaluate("2 * 17", new JsCallback() {
      @Override
      public void onResult(final String result) {
        // Result is returned here asynchronously
      }
    });

## Single-line comments

JavaScript with single-line comments won't be evaluated. It happens because the library needs to remove new lines from code before evaluation. If your JavaScript code does not evaluate try checking it with [jshint](http://www.jshint.com/) and [minify](http://jscompress.com/).

Test application runs a jQuery library as an example.

## Feedback

If you have any issues or need help please do not hesitate to create an issue ticket.

