# JsEvaluator library for running JavaScript in Android apps

[![JitPack](https://img.shields.io/github/tag/evgenyneu/js-evaluator-for-android.svg?label=JitPack)](https://jitpack.io/#evgenyneu/js-evaluator-for-android)

JsEvaluator may help you run JavaScript in an Android app and get the results. This is an alternative to `evaluateJavascript` method of the WebView. Supports Android version 4.0 (Ice Cream Sandwich) and newer.


## Setup

There are two ways your can add JsEvaluator to your project:

1. From a remote Maven repositorty.
1. From a local .aar or .jar file.

### 1. Setup from Maven repository in Android Studio

1) Add `maven { url "https://jitpack.io" }` into **allprojects/repositories** section of your **project** build.gradle file. For example:

```gradle
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

2) Add `compile 'com.github.evgenyneu:js-evaluator-for-android:v1.0.3'` into **dependencies** section of your **module** build.gradle file. For example:

```gradle
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    // Keep you existing dependencies here
    compile 'com.github.evgenyneu:js-evaluator-for-android:v1.0.3'
}
```

For less information, see Gradle/Maven [setup instructions](https://jitpack.io/#evgenyneu/js-evaluator-for-android/v1.0.3) on JsEvaluator jitpack.io page.

### 2. Setup from local .aar file

Download [jsevaluator-1.0.aar](https://github.com/evgenyneu/js-evaluator-for-android/blob/master/jsevaluator/build/outputs/aar/jsevaluator-1.0.aar?raw=true). You can also build it yourself into *jsevaluator/build/outputs/aar/* directory in Android Studio with this command:

```
./gradlew :jsevaluator:aR
```

#### Add aar file to Android Studio project

To add JsEvaluator to your app in Android Studio:

1. Copy the `jsevaluator-1.0.aar` to your **app/libs** folder.
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

```Java
JsEvaluator jsEvaluator = new JsEvaluator(this);
```

`this` is a reference to your activity.

## Evaluate JavaScript

The following functions evaluate JavaScript and return the result. Please note that all functions can only be called **from UI thread**.

#### Respond in UI thread

```Java
// Call only from UI thread
jsEvaluator.evaluateAndRespondInUiThread("2 * 17", new JsCallback() {
  @Override
  public void onResult(final String result) {
    // Result of JavaScript evaluation is returned here in UI thread.
    // It is safe to update your UI here.
  }
});
```

#### Respond in background thread

```Java
// Call only from UI thread
jsEvaluator.evaluateAndRespondInBackgroundThread("2 * 17", new JsCallback() {
  @Override
  public void onResult(final String result) {
    // Result of JavaScript evaluation is returned here in background thread.
    // Avoid accessing UI here.
  }
});
```

#### Block UI thread and wait for result

```Java
// Call only from UI thread
String result = jsEvaluator.blockUIThreadAndEvaluate(1_000, "2 * 17");
```

The function will wait for result from JavaScript evaluation. It receives two arguments.

1. Wait time *in milliseconds*. The function will return `null` if it fails to evaluate JavaScript within the given time period.
1. JavaScript code for evaluation.

**Warning:** UI thread will be blocked during JavaScript evaluation and the app will appear frozen to the user. Moreover, if JavaScript evaluation takes more than a few seconds the "application not responding" dialog will be presented to the user.


## Call a JavaScript function

The following methods can be used to call a JavaScript function and pass any number of string, integer or double arguments.

#### Respond in UI thread

```Java
// Call only from UI thread
jsEvaluator.callFunctionAndRespondInUiThread("function myFunction(a, b, c, a) { return 'result'; }",
  new JsCallback() {

  @Override
  public void onResult(final String result) {
    // Result of JavaScript function is returned here in UI thread.
    // It is safe to update your UI here.
  }
}, "myFunction", "parameter 1", "parameter 2", 912, 101.3);
```

#### Respond in background thread

```Java
// Call only from UI thread
jsEvaluator.callFunctionAndRespondInBackgroundThread("function myFunction(a, b, c, a) { return 'result'; }",
  new JsCallback() {

  @Override
  public void onResult(final String result) {
    // Result of JavaScript function is returned here in background thread.
    // Avoid accessing UI here.
  }
}, "myFunction", "parameter 1", "parameter 2", 912, 101.3);
```

#### Block UI thread and wait for result

```Java
// Call only from UI thread
String result = jsEvaluator.blockUIThreadAndCallFunction(1_000, "function myFunction(a, b, c, a) { return 'result'; }",
  "myFunction", "parameter 1", "parameter 2", 912, 101.3);
```

The function will wait for result from JavaScript evaluation. It receives the following arguments.

1. Wait time *in milliseconds*. The function will return `null` if it fails to evaluate JavaScript within the given time period.
1. JavaScript code for evaluation.
1. Name of the JavaScript function.
1. Any number of string, integer or double arguments that will be passed to the JavaScript function.

**Warning:** UI thread will be blocked during JavaScript evaluation and the app will appear frozen to the user. Moreover, if JavaScript evaluation takes more than a few seconds the "application not responding" dialog will be presented to the user.

## How it works

Behind the scenes it creates a `WebView` and feeds it JavaScript code for evaluation:

```Java
mWebView = new WebView(context);
String javascript = "<script>myObj.returnResult('Hello World')</script>";
String base64 = Base64.encodeToString(data, Base64.DEFAULT);
mWebView.loadUrl("data:text/html;charset=utf-8;base64," + base64);
```

The result of evaluation is sent back into Android activity:

```Java
public class JavaScriptInterface {
	public void returnResult(String result) {
		// result from JavaScript
	}
}

mWebView.addJavascriptInterface(new JavaScriptInterface(), "myObj");
```

## JavaScript is evaluated in new context

Each time the JavaScript is evaluated in the new context. It can not access the result of a previous evaluation. It means, for example, that one can not evaluate a large JavaScript library and then use it in subsequent calls. Please concatenate all your JavaScript to one string and evaluate it in one go.

For example, if you need to load jQuery libary and then use it:

```Java
String jQuery = "/*! jQuery JavaScript Library v2.1.1 ...";
jsEvaluator.evaluate(jQuery + "; $.isNumeric(123)", new JsCallback() { ...
```


## Tests

Tests are located in `app` module of this project. The app can be run for manual testing as well.

<img src='https://raw.githubusercontent.com/evgenyneu/js-evaluator-for-android/master/js_evaluator_screenshot_1_2014_08_30.png' width='270' alt='JsEvaluator library for Android screenshot 1'>

<img src='https://raw.github.com/evgenyneu/js-evaluator-for-android/master/js_evaluator_screenshot_2.png' width='270' alt='JsEvaluator library for Android screenshot 2'>


Or run as `Android JUnit Test` for unit testing.

Android versions tested:

* 4.0.3 (Ice Cream Sandwich)
* 4.1.2, 4.2.2, 4.3 (Jelly Bean)
* 4.4.2 (KitKat)
* 5.0, 5.1 (Lollipop)
* 6.0 Android (Marshmallow)


## Feedback is welcome

If you have any issues or need help please do not hesitate to create an issue ticket.

