package com.evgenii.jsevaluatortests;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.evgenii.jsevaluator.JsEvaluator;

public class RealLibrary extends Activity {
	JsEvaluator mJsEvaluator;

	protected void loadAndRunLibrary() {
		String library_source = null;
		try {
			library_source = ReadFile("real_library/jquery-2.1.0.js");
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mJsEvaluator = new JsEvaluator(this);
		mJsEvaluator.evaluate(library_source);

		// final String code = "$('<div iiatr=\"26\" />').attr('iiatr')";
		//
		// mJsEvaluator.evaluate(code, new JsCallback() {
		// @Override
		// public void onResult(final String resultValue) {
		// final TextView jsResultTextView = (TextView)
		// findViewById(R.id.realLibraryResultView);
		// jsResultTextView.setText(String.format("Result: %s", resultValue));
		// }
		// });
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_real_library);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		loadAndRunLibrary();
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

		return new Scanner(inputStream, "UTF-8").useDelimiter("\\A").next();
	}
}
