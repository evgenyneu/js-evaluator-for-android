package com.evgenii.jsevaluatortests.mocks;

import com.evgenii.jsevaluator.interfaces.CallJavaResultInterface;

public class CallJavaResultInterfaceMock implements CallJavaResultInterface {
	public String jsCallFinished_paramValue;
	public Integer jsCallFinished_paramCallIndex;

	@Override
	public void jsCallFinished(String value, Integer callIndex) {
		jsCallFinished_paramValue = value;
		jsCallFinished_paramCallIndex = callIndex;
	}
}
