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

	public void onCallFunctionClicked(View view) {
        callFunctionOnUiThread();
        blockUiThreadAndCallFunction();
    }

    void blockUiThreadAndCallFunction() {
        JsEvaluator jsEvaluator = new JsEvaluator(this);
        String resultValue = jsEvaluator.blockUIThreadAndCallFunction(1_000, getFunctionJsCode(), "greet", getFunctionParameterJsCode());
        final TextView jsResultTextView = (TextView) findViewById(R.id.textViewCallFunctionBlockUiThreadResult);
        jsResultTextView.setText(String.format("Block UI thread result: %s", resultValue));
    }

	void callFunctionOnUiThread() {
		mJsEvaluator.callFunctionAndRespondOnUiThread(getFunctionJsCode(), new JsCallback() {
			@Override
			public void onResult(final String resultValue) {
				final TextView jsResultTextView = (TextView) findViewById(R.id.textViewCallFunctionUiThreadResult);
				jsResultTextView.setText(String.format("UI thread result: %s", resultValue));

				callFunctionOnBackgroundThread();
			}
		}, "greet", getFunctionParameterJsCode());
	}

	void callFunctionOnBackgroundThread() {
        mJsEvaluator.callFunctionAndRespondOnBackgroundThread(getFunctionJsCode(), new JsCallback() {
			@Override
			public void onResult(final String resultValue) {
				runOnUiThread(new Runnable() {
					public void run() {
						final TextView jsResultTextView = (TextView) findViewById(R.id.textViewCallFunctionBackgroundThreadResult);
						jsResultTextView.setText(String.format("Background thread result: %s", resultValue));
					}
				});
			}
		}, "greet", getFunctionParameterJsCode());
	}

	private String getFunctionJsCode() {
		return ((EditText) findViewById(R.id.js_function_edit_text)).getText().toString();
	}

	private String getFunctionParameterJsCode() {
		return ((EditText) findViewById(R.id.editTextParameter)).getText().toString();
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
