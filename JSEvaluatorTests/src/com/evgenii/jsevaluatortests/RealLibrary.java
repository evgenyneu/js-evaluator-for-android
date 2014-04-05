package com.evgenii.jsevaluatortests;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.evgenii.jsevaluator.JsEvaluator;
import com.evgenii.jsevaluator.interfaces.JsCallback;

public class RealLibrary extends Activity {
	JsEvaluator mJsEvaluator;
	private Scanner scanner;

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
		// testJQuery();
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
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		final int id = item.getItemId();
		if (id == R.id.home)
			return true;
		return super.onOptionsItemSelected(item);
	}

	protected String ReadFile(String fileName) throws IOException {
		final AssetManager am = getAssets();
		final InputStream inputStream = am.open(fileName);

		scanner = new Scanner(inputStream, "UTF-8");
		return scanner.useDelimiter("\\A").next();
	}

	protected void testCryptoJs() {
		final String librarySrouce = loadJs("real_library/aes.js");
		mJsEvaluator.evaluate(librarySrouce);

		// final String code =
		// "var encrypted = CryptoJS.AES.encrypt('CryptoJs is working!', 'Secret Passphrase');"
		// +
		// "var decrypted = CryptoJS.AES.decrypt(encrypted, 'Secret Passphrase');"
		// + "decrypted = decrypted.toString(CryptoJS.enc.Utf8);" + "decrypted";

		final String code = "iiTest();";

		mJsEvaluator.evaluate(code, new JsCallback() {
			@Override
			public void onResult(final String resultValue) {
				final TextView jsResultTextView = (TextView) findViewById(R.id.realLibraryResultView);
				jsResultTextView.setText(String.format("Result: %s", resultValue));
			}
		});
	}

	protected void testJQuery() {
		final String library_source = null;

		final String librarySrouce = loadJs("real_library/jquery-2.1.0.js");
		mJsEvaluator.evaluate(librarySrouce);

		final String code = "$('<div><div class=\"ii\">jQuery is working!</div></div>').find('.ii').text()";

		mJsEvaluator.evaluate(code, new JsCallback() {
			@Override
			public void onResult(final String resultValue) {
				final TextView jsResultTextView = (TextView) findViewById(R.id.realLibraryResultView);
				jsResultTextView.setText(String.format("Result: %s", resultValue));
			}
		});
	}
}
