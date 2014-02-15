package com.evgenii.jsevaluatortests;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.evgenii.jsevaluator.JsEvaluator;
import com.evgenii.jsevaluator.interfaces.JsCallback;

public class CallJsFunctionActivity extends Activity {

	public void onCallFuncitonClicked(View view) {
		final JsEvaluator jsEvaluator = new JsEvaluator(this);
		final EditText functionText = (EditText) findViewById(R.id.js_function_edit_text);
		jsEvaluator.evaluate(functionText.getText().toString());

		final EditText parameterText = (EditText) findViewById(R.id.editTextParameter);
		jsEvaluator.callFunction(new JsCallback() {
			@Override
			public void onResult(final String resultValue) {
				final TextView jsResultTextView = (TextView) findViewById(R.id.textViewCallFunctionResult);
				jsResultTextView.setText(String.format("Result: %s",
						resultValue));
			}
		}, "greet", parameterText.getText().toString());
	}

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_js_function);
		// Make sure we're running on Honeycomb or higher to use ActionBar APIs
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.call_js_function, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
