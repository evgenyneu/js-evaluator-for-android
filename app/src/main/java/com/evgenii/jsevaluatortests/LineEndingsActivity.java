package com.evgenii.jsevaluatortests;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.evgenii.jsevaluator.JsEvaluator;
import com.evgenii.jsevaluator.interfaces.JsCallback;

public class LineEndingsActivity extends Activity {
	JsEvaluator mJsEvaluator;

	protected void evaluate() {
		// Use CR, LF characters and their combinations
		mJsEvaluator.evaluate("1;\r\n2;\n3;\r4;\n\r5;", new JsCallback() {
			@Override
			public void onResult(final String resultValue) {
				final TextView jsResultTextView = (TextView) findViewById(R.id.lineEndingsViewResult);
				String resultStr = "failed";
				if (resultValue.equals("5")) {
					resultStr = "success!";
				}

				jsResultTextView.setText(String.format("Result: %s", resultStr));
			}

			@Override
			public void onError(String errorMessage) {
				final TextView jsResultTextView = (TextView) findViewById(R.id.lineEndingsViewResult);
				jsResultTextView.setText(JSUtils.getErrorSpan(errorMessage));
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_line_endings);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		mJsEvaluator = new JsEvaluator(this);

		evaluate();
	}
}