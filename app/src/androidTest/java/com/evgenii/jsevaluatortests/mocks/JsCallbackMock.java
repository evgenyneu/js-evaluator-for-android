package com.evgenii.jsevaluatortests.mocks;

import com.evgenii.jsevaluator.interfaces.JsCallback;

public class JsCallbackMock implements JsCallback {
	public String resultValue;
	public String errorMessage;

	@Override
	public void onResult(String value) {
		resultValue = value;
	}

	@Override
	public void onError(String message) {
		errorMessage = message;
	}
}
