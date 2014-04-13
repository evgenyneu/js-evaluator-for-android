package com.evgenii.jsevaluator;

public class JsFunctionCallFormatter {
	public static String paramToString(Object param) {
		String str = "";
		if (param instanceof String) {
			str = (String) param;
			str = str.replace("\\", "\\\\");
			str = str.replace("\"", "\\\"");
			str = str.replace("\n", "\\n");
			str = String.format("\"%s\"", str);
		} else {
			try {
				@SuppressWarnings("unused")
				final double d = Double.parseDouble(param.toString());
				str = param.toString();
			} catch (final NumberFormatException nfe) {
			}
		}

		return str;
	}

	public static String toString(String functionName, Object... args) {
		final StringBuilder paramsStr = new StringBuilder();

		for (final Object param : args) {
			if (paramsStr.length() > 0) {
				paramsStr.append(", ");
			}

			paramsStr.append(paramToString(param));
		}

		return String.format("%s(%s)", functionName, paramsStr);
	}
}
