package com.evgenii.jsevaluatortests;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.evgenii.jsevaluator.HandlerWrapper;
import com.evgenii.jsevaluator.JsEvaluator;
import com.evgenii.jsevaluator.JsEvaluatorFuture;
import com.evgenii.jsevaluator.interfaces.JsCallback;

public class RealLibrary extends Activity {
	JsEvaluator mJsEvaluator;
	private Scanner scanner;
	String jsCode;

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
		setContentView(R.layout.activity_real_library);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		mJsEvaluator = new JsEvaluator(this, new HandlerWrapper(Looper.getMainLooper()));
		jsCode = "var jsEvaluatorResult = ''; ";
		testJQuery();
		testCryptoJs();
		jsCode += "jsEvaluatorResult;";
		evaluateAndDisplay();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.real_library, menu);
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

	protected String ReadFile(String fileName) throws IOException {
		final AssetManager am = getAssets();
		final InputStream inputStream = am.open(fileName);

		scanner = new Scanner(inputStream, "UTF-8");
		return scanner.useDelimiter("\\A").next();
	}

	protected void testCryptoJs() {
		jsCode += loadJs("real_library/cryptojs.js");
		jsCode += "; ";
		jsCode += "var encrypted = CryptoJS.AES.encrypt('CryptoJs is working!', 'Secret Passphrase');"
				+ "var decrypted = CryptoJS.AES.decrypt(encrypted, 'Secret Passphrase');"
				+ "jsEvaluatorResult += ' ' + decrypted.toString(CryptoJS.enc.Utf8); ";

	}

	protected void testJQuery() {
		jsCode += loadJs("real_library/jquery-2.1.0.js");
		jsCode += "; ";
		jsCode += "jsEvaluatorResult += ' ' + $('<div><div class=\"ii\">jQuery is working!</div></div>').find('.ii').text(); ";
	}
}
