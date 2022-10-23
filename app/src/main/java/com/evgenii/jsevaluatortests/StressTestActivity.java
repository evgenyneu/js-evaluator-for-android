package com.evgenii.jsevaluatortests;

import android.app.Activity;
import android.os.Bundle;
import androidx.core.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.evgenii.jsevaluator.JsEvaluator;
import com.evgenii.jsevaluator.interfaces.JsCallback;

public class StressTestActivity extends Activity {
	JsEvaluator mJsEvaluator;
	int stressJsLength = 20 * 100;

	// Constructs a very large JS function for stress test
	// returns: stressJsLength * (number + 1)
	private String addOneJs() {
		final String jsStart = "function addOne(number) { var i=0, m=0;";
		final String jsEnd = "return i;}";
		final StringBuilder jsRepeat = new StringBuilder("");
		for (int i = 0; i < stressJsLength; i += 1) {
			jsRepeat.append("m = (new Date()).getMilliseconds()+1;i = i + m/m + number;");
		}
		return jsStart + jsRepeat.toString() + jsEnd;
	}

	// Constructs a very large JS function for stress test.
	// calls addOne;
	// returns: 2 * stressJsLength * (number + 1)
	private String addTwoJs() {
		final String jsStart = "function addTwo(number) { var rnd=0, i=0;";
		final String jsEnd = "return i + addOne(number);}";
		final StringBuilder jsRepeat = new StringBuilder("");
		for (int i = 0; i < stressJsLength; i += 1) {
			jsRepeat.append("rnd = Math.random()+1;i = i + rnd/rnd + number;");
		}
		return jsStart + jsRepeat.toString() + jsEnd;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stress_test);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		mJsEvaluator = new JsEvaluator(this);

		String jsCode = addOneJs();
		jsCode += addTwoJs();

		mJsEvaluator.callFunction(jsCode, new JsCallback() {
			@Override
			public void onResult(final String resultValue) {
				final TextView jsResultTextView = (TextView) findViewById(R.id.js_stress_result_text_view);
				jsResultTextView.setText(String.format("Result: %s", resultValue));
			}

			@Override
			public void onError(String errorMessage) {
				final TextView jsResultTextView = (TextView) findViewById(R.id.js_stress_result_text_view);
				jsResultTextView.setText(JSUtils.getErrorSpan(errorMessage));
			}
		}, "addTwo", 10);
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

	public void onStressTestClicked(View view) {

	}
}
