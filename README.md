# Run JavaScript in Android apps (library)

![JitPack](https://img.shields.io/github/tag/evgenyneu/js-evaluator-for-android.svg?label=JitPack)

Evaluates JavaScript and gets results. This is an alternative to `evaluateJavascript` method of the WebView. Supports Android version 4.0 (Ice Cream Sandwich) and newer.


## Setup

There are two ways your can add JsEvaluator to your project:

1. From a remote Maven repositorty.
1. From a local .aar or .jar file.

### 1. Remove Maven repository

Read Gradle/Maven setup instructions on JsEvaluator [jitpack.io page](https://jitpack.io/#evgenyneu/js-evaluator-for-android/).
	
	
### 2. Setup from local .aar file

Download [jsevaluator-1.0.aar](https://github.com/evgenyneu/js-evaluator-for-android/blob/master/jsevaluator/build/outputs/aar/jsevaluator-1.0.aar?raw=true). You can also build it yourself with this command:

```
./gradlew :jsevaluator:aR
```

#### Add aar file to Android Studio project 

To add JSEvaluator to your app in Android Studio:

1. Copy the `jsevaluator-1.0.aar` to your **app/libs** folder..
1. Add `compile(name:'jsevaluator-1.0', ext:'aar')` to **dependencies** block of your **module** build.gradle file.
1. Add the following code to the **repositories** block of your **project** build.gradle file.

```
flatDir{
    dirs 'libs'
}
```

#### Add jar file to Eclipse project

1. Unzip the `jsevaluator-1.0.aar` and get the classes.jar from it. You may want to rename it to jsevaluator.jar.
1. Open properties for **your** app project.
1. Select `Libraries` tab in `Java Build Path`.
1. Click `Add JARs...` button and select the .jar file.
1. In your project properties click `Order and export` tab.
1. Tick the .jar file.

## Usage

Create evaluator instance:

    JsEvaluator jsEvaluator = new JsEvaluator(this);

`this` is a reference to your activity.

## Evaluate JavaScript

    jsEvaluator.evaluate("2 * 17", new JsCallback() {
      @Override
      public void onResult(final String result) {
        // get result here
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

* 4.0.3 (Ice Cream Sandwich)
* 4.1.2, 4.2.2, 4.3 (Jelly Bean)
* 4.4.2 (KitKat)
* 5.0, 5.1 (Lollipop)
* 5.2 Android M preview (Muffin) 😉

## Result is returned asynchronously

The result from JavaScript is returned asynchronously in the UI thread. It is recommended to evaluate in the UI thread as well.

    // somewhere in UI thread ...
    jsEvaluator.evaluate("2 * 17", new JsCallback() {
      @Override
      public void onResult(final String result) {
        // Result is returned here asynchronously in UI thread
      }
    });

## JavaScript is evaluated in new context

Each time the JavaScript is evaluated in the new context. It can not access the result of a previous evaluation.
Please concatenate all your JavaScript to one string and evaluate it in one go.

For example, if you need to load jQuery libary and then use it:

    String jQuery = "/*! jQuery JavaScript Library v2.1.1 ...";
    jsEvaluator.evaluate(jQuery + "; $.isNumeric(123)", new JsCallback() { ...


## Feedback

If you have any issues or need help please do not hesitate to create an issue ticket.

