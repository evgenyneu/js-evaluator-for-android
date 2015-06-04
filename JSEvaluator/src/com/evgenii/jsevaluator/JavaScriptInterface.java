package com.evgenii.jsevaluator;

import android.webkit.JavascriptInterface;

import com.evgenii.jsevaluator.interfaces.CallJavaResultInterface;

/**
 * Passed in addJavascriptInterface of WebView to allow web views's JS execute
 * Java code
 */
public class JavaScriptInterface {
	private final CallJavaResultInterface mCallJavaResultInterface;

	public JavaScriptInterface(CallJavaResultInterface callJavaResult) {
		mCallJavaResultInterface = callJavaResult;
	}

	@JavascriptInterface
	public void returnResultToJava(String value, int callIndex) {
		mCallJavaResultInterface.jsCallFinished(value, callIndex);
	}
	
	@JavascriptInterface
    public String requestFile(String link) throws IOException {
        URL url = new URL(link);
        URLConnection conn = url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String file = "";
        String inputLine;
        while ((inputLine = br.readLine()) != null) {
            file += inputLine;
        }
        br.close();
        return br.toString();
    }
}
