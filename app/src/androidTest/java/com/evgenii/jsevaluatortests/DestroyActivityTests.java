package com.evgenii.jsevaluatortests;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.MediumTest;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class DestroyActivityTests extends
        ActivityInstrumentationTestCase2<DestroyActivity> {

    private DestroyActivity mActivity;

    public DestroyActivityTests() {
        super(DestroyActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
    }

    @MediumTest
    public void testEvaluateButton_clickButtonAndShowResult() {
        final Button evaluateButton = (Button) mActivity.findViewById(R.id.button_evaluate_on_destroy);

        final TextView resultTextView = (TextView) mActivity.findViewById(R.id.js_result_destroy_text_view);

        TouchUtils.clickView(this, evaluateButton);
        assertTrue(View.VISIBLE == resultTextView.getVisibility());
        assertEquals("Result: 4", resultTextView.getText().toString());

        final Button destroyButton = (Button) mActivity.findViewById(R.id.button_destroy);
        TouchUtils.clickView(this, destroyButton);
        assertEquals("Web view destroyed", resultTextView.getText().toString());
    }

    public void testPreconditions() {
        assertNotNull("mActivity is null", mActivity);
    }
}
