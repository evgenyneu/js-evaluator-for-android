package com.evgenii.jsevaluatortests;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

class JSUtils {

  static SpannableString getErrorSpan(String errorMessage){
    SpannableString span = new SpannableString("Error: " + errorMessage);
    span.setSpan(new StyleSpan(Typeface.BOLD), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    span.setSpan(new ForegroundColorSpan(Color.RED), 0, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    return span;
  }
}
