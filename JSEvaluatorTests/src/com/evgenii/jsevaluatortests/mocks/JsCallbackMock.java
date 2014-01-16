package com.evgenii.jsevaluatortests.mocks;

import com.evgenii.jsevaluator.interfaces.JsCallback;

public class JsCallbackMock implements JsCallback {
	public String resultValue;

	@Override
	public void onResult(String value) {
		resultValue = value;
	}
}
