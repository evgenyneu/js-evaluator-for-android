package com.evgenii.jsevaluatortests;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.Button;
import android.widget.TextView;

public class CharacterEscapeTests extends ActivityInstrumentationTestCase2<CharacterEscape> {

	private CharacterEscape mActivity;

	public CharacterEscapeTests() {
		super(CharacterEscape.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
	}

	@MediumTest
	public void testCallFunctionButton_clickButtonAndShowResult() {
		final Button callFunctionButton = (Button) mActivity
				.findViewById(R.id.buttonCharacterEscapeCallFunction);

		final TextView resultTextView = (TextView) mActivity
				.findViewById(R.id.textViewCharacterEscapeResult);

		TouchUtils.clickView(this, callFunctionButton);
		assertEquals(
				"Result: \nmultiline:  newline: \n apostrophe: \' quote: \" slashes: /\\/ closing tag: </script> "
						+ "unicode: 日本 characters: !@#$%^&*()-=_+`~<>? \n#####\n"
						+ "multiline: \n newline: \\n apostrophe: \' quote: \" slashes: \\/\\\\/ closing tag: </script> "
						+ "unicode: 日本 characters: !@#$%^&*()-=_+`~<>?", resultTextView.getText()
						.toString());
	}

	public void testPreconditions() {
		assertNotNull("mActivity is null", mActivity);
	}
}
