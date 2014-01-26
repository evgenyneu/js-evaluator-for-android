package com.evgenii.jsevaluatortests;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.evgenii.jsevaluator.JsEvaluator;
import com.evgenii.jsevaluator.interfaces.JsCallback;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onEvaluateClicked(View view) {
		final JsEvaluator jsEvaluator = new JsEvaluator(this);
		final EditText editText = (EditText) findViewById(R.id.editJavaScript);
		jsEvaluator.evaluate(editText.getText().toString(), new JsCallback() {
			@Override
			public void onResult(final String resultValue) {
				final TextView jsResultTextView = (TextView) findViewById(R.id.textJSResult);
				jsResultTextView.setText(String.format("Result: %s",
						resultValue));
			}
		});
	}
}
