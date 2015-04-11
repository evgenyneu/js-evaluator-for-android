package com.evgenii.jsevaluatortests;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.TextView;

import com.evgenii.jsevaluator.JsEvaluator;
import com.evgenii.jsevaluator.interfaces.JsCallback;

public class LineEndingsActivity extends Activity {
	JsEvaluator mJsEvaluator;
	private Scanner scanner;
	String jsCode;

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
		});
	}

	protected void evaluateAndDisplay() {
		mJsEvaluator.evaluate(jsCode, new JsCallback() {
			@Override
			public void onResult(final String resultValue) {
				final TextView jsResultTextView = (TextView) findViewById(R.id.realLibraryResult);
				jsResultTextView.setText(String.format("Result:%s", resultValue));
			}
		});
	}

	private String loadJs(String fileName) {
		try {
			return ReadFile(fileName);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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

	protected String ReadFile(String fileName) throws IOException {
		final AssetManager am = getAssets();
		final InputStream inputStream = am.open(fileName);

		scanner = new Scanner(inputStream, "UTF-8");
		return scanner.useDelimiter("\\A").next();
	}
}