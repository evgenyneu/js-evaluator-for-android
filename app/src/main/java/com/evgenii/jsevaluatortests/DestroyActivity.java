package com.evgenii.jsevaluatortests;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.evgenii.jsevaluator.JsEvaluator;
import com.evgenii.jsevaluator.interfaces.JsCallback;

public class DestroyActivity extends Activity {
    JsEvaluator mJsEvaluator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destroy);

        mJsEvaluator = new JsEvaluator(this);
    }

    public void onEvaluateClicked(View view) {
        mJsEvaluator.evaluate("2 + 2", new JsCallback() {
            @Override
            public void onResult(final String resultValue) {
                final TextView jsResultTextView = (TextView) findViewById(R.id.js_result_destroy_text_view);
                jsResultTextView.setText(String.format("Result: %s",
                        resultValue));
            }

            @Override
            public void onError(String errorMessage) {
                final TextView jsResultTextView = (TextView) findViewById(R.id.js_result_destroy_text_view);
                jsResultTextView.setText(JSUtils.getErrorSpan(errorMessage));
            }
        });
    }

    public void onDestroyClicked(View view) {
        mJsEvaluator.destroy();

        if (mJsEvaluator.getWebView() == null) {
            final TextView jsResultTextView = (TextView) findViewById(R.id.js_result_destroy_text_view);
            jsResultTextView.setText("Web view destroyed");
        }
    }
}
