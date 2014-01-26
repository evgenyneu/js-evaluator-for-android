package com.evgenii.jsevaluatortests;

import android.test.AndroidTestCase;

import com.evgenii.jsevaluator.JsFunctionCallFormatter;

public class JsFunctionCallFormatterTests extends AndroidTestCase {
	public void testParamToString_float() {
		assertEquals("123.22", JsFunctionCallFormatter.paramToString(123.22));
	}

	public void testParamToString_integer() {
		assertEquals("123", JsFunctionCallFormatter.paramToString(123));
	}

	public void testParamToString_string() {
		assertEquals("\"Boy's bike\"",
				JsFunctionCallFormatter.paramToString("Boy's bike"));
	}

	public void testParamToString_stringEscapesDoubleQuotes() {
		assertEquals("\"\\\\\"clever\\\\\"\"",
				JsFunctionCallFormatter.paramToString("\"clever\""));
	}

	public void testToString() {
		final String js = JsFunctionCallFormatter.toString("drink",
				"Cow's milk", 5);

		assertEquals("drink(\"Cow's milk\", 5)", js);

	}
}
