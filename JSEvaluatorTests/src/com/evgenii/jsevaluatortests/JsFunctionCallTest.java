package com.evgenii.jsevaluatortests;

import java.util.ArrayList;

import android.test.AndroidTestCase;

import com.evgenii.jsevaluator.JsFunctionCall;

public class JsFunctionCallTest extends AndroidTestCase {

	public void testInit_shouldSetNameAndParams() {
		final ArrayList<Object> params = new ArrayList<Object>();
		params.add("milk");
		params.add(2);

		final JsFunctionCall jsFunctionCall = new JsFunctionCall("drink",
				params, 101);

		assertEquals("drink", jsFunctionCall.getName());

		final ArrayList<Object> jsParams = jsFunctionCall.getParams();
		assertEquals(2, jsParams.size());
		assertEquals("milk", jsParams.get(0));
		assertEquals(2, jsParams.get(1));

		assertEquals(Integer.valueOf(101), jsFunctionCall.getCallbackIndex());
	}

	public void testParamToString_float() {
		assertEquals("123.22", JsFunctionCall.paramToString(123.22));
	}

	public void testParamToString_integer() {
		assertEquals("123", JsFunctionCall.paramToString(123));
	}

	public void testParamToString_string() {
		assertEquals("'Boy\'s bike'",
				JsFunctionCall.paramToString("Boy's bike"));
	}

	public void testToString() {
		final ArrayList<Object> params = new ArrayList<Object>();
		params.add("Milk");
		params.add(2);

		final JsFunctionCall jsFunctionCall = new JsFunctionCall("drink",
				params, 5);

		assertEquals("drink('Milk', 2)", jsFunctionCall.toString());

	}
}