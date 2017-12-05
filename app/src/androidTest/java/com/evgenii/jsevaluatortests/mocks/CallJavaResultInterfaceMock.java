package com.evgenii.jsevaluatortests.mocks;

import com.evgenii.jsevaluator.interfaces.CallJavaResultInterface;

public class CallJavaResultInterfaceMock implements CallJavaResultInterface {
	public String jsCallFinished_paramValue;

	@Override
	public void jsCallFinished(String value) {
		jsCallFinished_paramValue = value;
	}
}
