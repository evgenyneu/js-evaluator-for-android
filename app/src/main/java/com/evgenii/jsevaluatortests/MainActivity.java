package com.evgenii.jsevaluatortests;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onEvaluateJsClicked(View view) {
        final Intent intent = new Intent(this, EvaluateJsStringActivity.class);
        startActivity(intent);
    }

    public void onShowCallJsFunctionActivityClicked(View view) {
        final Intent intent = new Intent(this, CallJsFunctionActivity.class);
        startActivity(intent);
    }

    public void onShowCharacterEscapeClicked(View view) {
        final Intent intent = new Intent(this, CharacterEscape.class);
        startActivity(intent);
    }

    public void onShowLineEndingsClicked(View view) {
        final Intent intent = new Intent(this, LineEndingsActivity.class);
        startActivity(intent);
    }

    public void onShowDestroyClicked(View view) {
        final Intent intent = new Intent(this, DestroyActivity.class);
        startActivity(intent);
    }

    public void onShowPercentCharacterClicked(View view) {
        final Intent intent = new Intent(this, PercentageActivity.class);
        startActivity(intent);
    }

    public void onShowRunRealLibraryClicked(View view) {
        final Intent intent = new Intent(this, RealLibrary.class);
        startActivity(intent);
    }

    public void onShowStressTestActivityClicked(View view) {
        final Intent intent = new Intent(this, StressTestActivity.class);
        startActivity(intent);
    }
}
