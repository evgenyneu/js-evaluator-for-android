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

public class CharacterEscape extends Activity {

	JsEvaluator mJsEvaluator;

	public void onCharacterEscapeCallFuncitonClicked(View view) {
		final TextView functionText = (TextView) findViewById(R.id.editTextCharacterEscapeJsCode);
		final String jsCode = functionText.getText().toString();

		final TextView parameterText = (TextView) findViewById(R.id.editTextCharacterEscapeParameter);
		mJsEvaluator.callFunction(jsCode, new JsCallback() {
			@Override
			public void onResult(final String resultValue) {
				final TextView jsResultTextView = (TextView) findViewById(R.id.textViewCharacterEscapeResult);
				jsResultTextView.setText(String.format("Result: \n%s", resultValue));
			}

			@Override
			public void onError(String errorMessage) {
				final TextView jsResultTextView = (TextView) findViewById(R.id.textViewCharacterEscapeResult);
				jsResultTextView.setText(JSUtils.getErrorSpan(errorMessage));
			}
		}, "greet", parameterText.getText().toString());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_character_escape);

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
