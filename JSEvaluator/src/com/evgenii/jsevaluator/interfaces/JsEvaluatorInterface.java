package com.evgenii.jsevaluator.interfaces;

public interface JsEvaluatorInterface {
	public void callFunction(JsCallback resultCallback, String name,
			Object... args);

	public void evaluate(String jsCode);

	public void evaluate(String jsCode, JsCallback resultCallback);
}
