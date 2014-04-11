package com.evgenii.jsevaluatortests;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.evgenii.jsevaluator.JsEvaluator;
import com.evgenii.jsevaluator.interfaces.JsCallback;

public class RealLibrary extends Activity {
	JsEvaluator mJsEvaluator;
	private Scanner scanner;

	protected void evaluateAndDisplay(String code, final int viewId) {

		mJsEvaluator.evaluate(code, new JsCallback() {
			@Override
			public void onResult(final String resultValue) {
				final TextView jsResultTextView = (TextView) findViewById(viewId);
				jsResultTextView.setText(String.format("Result: %s", resultValue));
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

		mJsEvaluator = new JsEvaluator(this);
		testJQuery();
		testCryptoJs();
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
		final String librarySrouce = loadJs("real_library/cryptojs.js");
		mJsEvaluator.evaluate(librarySrouce);

		final String code = "var encrypted = CryptoJS.AES.encrypt('CryptoJs is working!', 'Secret Passphrase');"
				+ "var decrypted = CryptoJS.AES.decrypt(encrypted, 'Secret Passphrase');"
				+ "decrypted.toString(CryptoJS.enc.Utf8);";

		evaluateAndDisplay(code, R.id.realLibraryResultCryptoJsView);
	}

	protected void testJQuery() {
		final String librarySrouce = loadJs("real_library/jquery-2.1.0.js");
		mJsEvaluator.evaluate(librarySrouce);

		final String code = "$('<div><div class=\"ii\">jQuery is working!</div></div>').find('.ii').text()";

		evaluateAndDisplay(code, R.id.realLibraryResultJqueryView);
	}
}
