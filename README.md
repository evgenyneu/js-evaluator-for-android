# JsEvaluator library for running JavaScript in Android apps

[![JitPack](https://img.shields.io/github/tag/evgenyneu/js-evaluator-for-android.svg?label=JitPack)](https://jitpack.io/#evgenyneu/js-evaluator-for-android)

JsEvaluator may help you run JavaScript in an Android app and get the results. This is an alternative to `evaluateJavascript` method of the WebView. Supports Android version 4.0 (Ice Cream Sandwich) and newer.

## Setup

There are two ways your can add JsEvaluator to your project:

1. From a remote Maven repository.
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

2) Add `compile 'com.github.evgenyneu:js-evaluator-for-android:v5.0.0'` into **dependencies** section of your **module** build.gradle file. For example:

```gradle
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    // Keep you existing dependencies here
    implementation 'com.github.evgenyneu:js-evaluator-for-android:v5.0.0'
}
```

For less information, see Gradle/Maven [setup instructions](https://jitpack.io/#evgenyneu/js-evaluator-for-android/v5.0.0) on JsEvaluator jitpack.io page.

### 2. Setup from local .aar file

Download [jsevaluator-1.0.aar](https://github.com/evgenyneu/js-evaluator-for-android/blob/master/jsevaluator/build/outputs/aar/jsevaluator-1.0.aar?raw=true). You can also build it yourself into *jsevaluator/build/outputs/aar/* directory in Android Studio with this command:

```
./gradlew :jsevaluator:aR
```

#### Add aar file to Android Studio project

To add JsEvaluator to your app in Android Studio:

1. Copy the `jsevaluator-1.0.aar` to your **app/libs** folder.
1. Add `compile(name:'jsevaluator-1.0', ext:'aar')` to **dependencies** block of your **module** build.gradle file.


```Gradle
dependencies {
    compile(name:'jsevaluator-1.0', ext:'aar')
}
```

1. Add the following code to the **allprojects/repositories** block of your **project** build.gradle file.

```Gradle
allprojects {
    repositories {
        jcenter()
        flatDir {
            dirs 'libs'
        }
    }
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

Create evaluator instance variable in your activity:

```Java
public class MainActivity extends AppCompatActivity {
    JsEvaluator jsEvaluator = new JsEvaluator(this);
```

`this` is a reference to your activity.

## Evaluate JavaScript

```Java
jsEvaluator.evaluate("2 * 17", new JsCallback() {
  @Override
  public void onResult(String result) {
    // Process result here.
    // This method is called in the UI thread.
  }

  @Override
  public void onError(String errorMessage) {
    // Process JavaScript error here.
    // This method is called in the UI thread.
  }
});
```

Note: make sure to call `evaluate` method in UI thread.

## Call a JavaScript function

```Java
jsEvaluator.callFunction("function myFunction(a, b, c, d) { return 'result'; }",
  new JsCallback() {

  @Override
  public void onResult(String result) {
    // Process result here.
    // This method is called in the UI thread.
  }

  @Override
  public void onError(String errorMessage) {
    // Process JavaScript error here.
    // This method is called in the UI thread.
  }
}, "myFunction", "parameter 1", "parameter 2", 912, 101.3);
```

Any number of string, int or double parameters can be supplied.

Note: make sure to call `callFunction` method in UI thread.

## JavaScript is evaluated asynchronously

JavaScript is evaluated asynchronously without blocking UI thread. Result is returned in the UI thread. It is required to call `evaluate` and `callFunction` in UI thread.

## JavaScript is evaluated in new context

Each time the JavaScript is evaluated in the new context. It can not access the result of a previous evaluation.
Please concatenate all your JavaScript to one string and evaluate it in one go.

For example, if you need to load jQuery libary and then use it:

```Java
String jQuery = "/*! jQuery JavaScript Library v2.1.1 ...";
jsEvaluator.evaluate(jQuery + "; $.isNumeric(123)", new JsCallback() { ...
```

## Advanced functionality

### Destroying the evaluator

Calling the `destroy()` method will destroy the Web View used by JsEvaluator and clear the memory. JsEvaluator can not be used after it is destroyed.

```Java
jsEvaluator.destroy();
```

### Accessing the WebView

Here is how to get the instance to the web view used by the JsEvaluator.

```Java
WebView webView = jsEvaluator.getWebView();
```


## Known limitations

This library is suitable for evaluating only small amounts of JavaScript within hundreds of KB. [It has been reported](https://github.com/evgenyneu/js-evaluator-for-android/issues/24) that the library can not evaluate a megabyte of JavaScript. If you run into similar problems you can try [ericwlange/AndroidJSCore](https://github.com/ericwlange/AndroidJSCore) library instead.

## How it works

Behind the scenes it creates a `WebView` and feeds it JavaScript code for evaluation:

```Java
mWebView = new WebView(context);
String javascript = "<script>myObj.returnResult('Hello World')</script>";
byte[] data = javascript.getBytes("UTF-8");
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

## Catching JavaScript errors

This library catches errors by wrapping the whole JavaScript code in a try-catch block. The errors are then returned to Java in the `onError` method of the callback object.

```Java
jsEvaluator.evaluate("4 * octapod", new JsCallback() {
   @Override
   public void onResult(String result) { }

   @Override
   public void onError(String errorMessage) {
      // errorMessage => "ReferenceError: octapod is not defined"
   }
});
```

Please note that this method only catches runtime JavaScript errors, like undefined variables or properties. It will not, however, catch errors resulted from malformed JavaScript code, like missing a `}` bracket.


## Using with ProGuard

If you are using ProGuard (`minifyEnabled true`) you can add these rules to your *proguard-rules.pro* file.

```
# js-evaluator-for-android
-keepattributes JavascriptInterface
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
```

You will most likely not need to modify your proguard file because JSEvaluator uses [consumer proguard rules](https://github.com/evgenyneu/js-evaluator-for-android/issues/30).


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
* 6.0 (Marshmallow)
* 8.0 (Oreo)

## Thanks 👍

* [codebymikey](https://github.com/codebymikey) for adding error handling.
* [xbao](https://github.com/xbao) for adding a Gradle file.
* [clydzik](https://github.com/clydzik) for fixing a [memory leak](https://github.com/evgenyneu/js-evaluator-for-android/pull/40) and simplifying the code.


## Feedback is welcome

If you have any issues or need help please do not hesitate to create an issue ticket.

