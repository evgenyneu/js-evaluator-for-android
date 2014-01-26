package com.evgenii.jsevaluatortests;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.evgenii.jsevaluator.JsEvaluator;
import com.evgenii.jsevaluator.interfaces.JsCallback;

public class EvaluateJsStringActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluate_js_string);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.evaluate_js_string, menu);
		return true;
	}

	public void onEvaluateClicked(View view) {
		final JsEvaluator jsEvaluator = new JsEvaluator(this);
		final EditText editText = (EditText) findViewById(R.id.edit_java_script);
		jsEvaluator.evaluate(editText.getText().toString(), new JsCallback() {
			@Override
			public void onResult(final String resultValue) {
				final TextView jsResultTextView = (TextView) findViewById(R.id.js_result_text_view);
				jsResultTextView.setText(String.format("Result: %s",
						resultValue));
			}
		});

		// jsEvaluator.callFunction(new JsCallback() {
		// @Override
		// public void onResult(final String resultValue) {
		// final TextView jsResultTextView = (TextView)
		// findViewById(R.id.js_result_text_view);
		// jsResultTextView.setText(String.format("Result: %s",
		// resultValue));
		// }
		// }, "greet", "\"Cow's\" milk'''\"\\\"");
	}

}
