package com.evgenii.jsevaluatortests;

import android.app.Activity;
import android.os.Bundle;
import androidx.core.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.evgenii.jsevaluator.JsEvaluator;
import com.evgenii.jsevaluator.interfaces.JsCallback;

public class PercentageActivity extends Activity {
	JsEvaluator mJsEvaluator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_percentage);

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

	public void onEvaluatePercentageClicked(View view) {
		final EditText editText = (EditText) findViewById(R.id.percentageCodeLabel);
		mJsEvaluator.evaluate(editText.getText().toString(), new JsCallback() {
			@Override
			public void onResult(final String resultValue) {
				final TextView resultTextView = (TextView) findViewById(R.id.percentageResultLabel);
				resultTextView.setText(String.format("Result: %s", resultValue));
			}

			@Override
			public void onError(String errorMessage) {
				final TextView jsResultTextView = (TextView) findViewById(R.id.percentageResultLabel);
				jsResultTextView.setText(JSUtils.getErrorSpan(errorMessage));
			}
		});
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
