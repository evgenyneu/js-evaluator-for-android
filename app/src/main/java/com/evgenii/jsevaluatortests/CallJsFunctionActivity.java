package com.evgenii.jsevaluatortests;

import android.app.Activity;
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
	JsEvaluator mJsEvaluator;

	public void onCallFuncitonClicked(View view) {
		final EditText functionText = (EditText) findViewById(R.id.js_function_edit_text);
		final String jsCode = functionText.getText().toString();

		final EditText parameterText = (EditText) findViewById(R.id.editTextParameter);
		mJsEvaluator.callFunctionAndRespondOnUiThread(jsCode, new JsCallback() {
			@Override
			public void onResult(final String resultValue) {
				final TextView jsResultTextView = (TextView) findViewById(R.id.textViewCallFunctionUiThreadResult);
				jsResultTextView.setText(String.format("UI thread result: %s", resultValue));

                callFunctionOnBackgroundThread();
			}
		}, "greet", parameterText.getText().toString());
	}

	void callFunctionOnBackgroundThread() {
        final EditText functionText = (EditText) findViewById(R.id.js_function_edit_text);
        final String jsCode = functionText.getText().toString();

        final EditText parameterText = (EditText) findViewById(R.id.editTextParameter);
        mJsEvaluator.callFunctionAndRespondOnBackgroundThread(jsCode, new JsCallback() {
            @Override
            public void onResult(final String resultValue) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        final TextView jsResultTextView = (TextView) findViewById(R.id.textViewCallFunctionBackgroundThreadResult);
                        jsResultTextView.setText(String.format("Background thread result: %s", resultValue));
                    }
                });
            }
        }, "greet", parameterText.getText().toString());
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_js_function);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		mJsEvaluator = new JsEvaluator(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
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
