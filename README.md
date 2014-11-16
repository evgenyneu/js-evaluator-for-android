# Run JavaScript in Android apps (library)

Evaluates JavaScript and gets results. This is an alternative to `evaluateJavascript` method of the WebView. Supports Android version 3.0 (Honeycomb) and newer.

## Get JAR file

[Download jsevaluator.jar](https://github.com/evgenyneu/js-evaluator-for-android/raw/master/JSEvaluator/bin/jsevaluator.jar). You can also build it yourself with Eclipse. It will be built into JSEvaluator/bin/jsevaluator.jar.

## Setup

To add JSEvaluator to your app in Eclipse:

1. File > Import `JSEvaluator` project.
1. Open properties for **your** app project.
1. Select `Libraries` tab in `Java Build Path`.
1. Click `Add JARs...` button and select `jsevaluator.jar`.
1. In your project properties click `Order and export` tab.
1. Tick `jsevaluator.jar`.

## Usage

Create evaluator instance:

    JsEvaluator jsEvaluator = new JsEvaluator(this);

`this` is a reference to your activity.

## Evaluate JavaScript

    jsEvaluator.evaluate("2 * 17", new JsCallback() {
      @Override
      public void onResult(final String result) {
        // get result here (optional)
      }
    });

## Call a JavaScript function

    jsEvaluator.callFunction("function myFunction(a, b, c, a) { return 'result'; }",
      new JsCallback() {

      @Override
      public void onResult(final String result) {
        // get result here
      }
    }, "myFunction", "parameter 1", "parameter 2", 912, 101.3);

Any number of string, int or double parameters can be supplied.

## How it works

Behind the scenes it creates a `WebView` and feeds it JavaScript code for evaluation:

    mWebView = new WebView(context);
    String javascript = "<script>myObj.returnResult('Hello World')</script>";
    String base64 = Base64.encodeToString(data, Base64.DEFAULT);
    mWebView.loadUrl("data:text/html;charset=utf-8;base64," + base64);
    
The result of evaluation is sent back into Android activity:

    public class JavaScriptInterface {
    	public void returnResult(String result) {
    		// result from JavaScript
    	}
    }

    mWebView.addJavascriptInterface(new JavaScriptInterface(), "myObj");

## Tests

Tests are located in `JsEvaluatorTests` project.

Run as `Android Application` for manual testing.

<img src='https://raw.githubusercontent.com/evgenyneu/js-evaluator-for-android/master/js_evaluator_screenshot_1_2014_08_30.png' width='270' alt='JSEvaluator library for Android screenshot 1'>

<img src='https://raw.github.com/evgenyneu/js-evaluator-for-android/master/js_evaluator_screenshot_2.png' width='270' alt='JSEvaluator library for Android screenshot 2'>


Or run as `Android JUnit Test` for unit testing.

Android versions tested:

* 3.0 (Honeycomb)
* 4.0.3 (Ice Cream Sandwich)
* 4.1.2 (Jelly Bean)
* 4.2.2 (Jelly Bean)
* 4.3 (Jelly Bean)
* 4.4.2 (KitKat)
* 5.0 (Lollipop)

## Result is returned asynchronously

The result from JavaScript is returned asynchronously in the UI thread. It is recommended to evaluate in the UI thread as well.

    jsEvaluator.evaluate("2 * 17", new JsCallback() {
      @Override
      public void onResult(final String result) {
        // Result is returned here asynchronously
      }
    });

## JavaScript is evaluated in new context

Each time the JavaScript is evaluated in the new context. It can not access the result of a previous evaluation.
Please concatenate all your JavaScript to one string to evaluate it at one go.

For example, consider if you need to load jQuery libary and then use it:

    String jQuery = "/*! jQuery JavaScript Library v2.1.1 ...";
    jsEvaluator.evaluate(jQuery + "; $.isNumeric(123)", new JsCallback() { ...
    

## Feedback

If you have any issues or need help please do not hesitate to create an issue ticket.

