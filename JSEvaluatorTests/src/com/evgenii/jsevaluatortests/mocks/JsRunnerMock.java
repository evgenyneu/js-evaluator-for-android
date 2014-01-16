package com.evgenii.jsevaluatortests.mocks;

import com.evgenii.jsevaluator.interfaces.JsRunnerCallbackInterface;

public class JsRunnerMock implements JsRunnerCallbackInterface {
	public boolean mInitalEvaluationHasFinished;

	public String jsCallFinished_paramValue;
	public Integer jsCallFinished_paramCallIndex;

	@Override
	public void initalJsEvaluationHasFinished() {
		mInitalEvaluationHasFinished = true;
	}

	@Override
	public void jsCallFinished(String value, Integer callIndex) {
		jsCallFinished_paramValue = value;
		jsCallFinished_paramCallIndex = callIndex;
	}
}