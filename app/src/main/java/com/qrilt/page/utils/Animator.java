package com.qrilt.page.utils;

import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.View;

public class Animator {
    public static int shortAnimationDuration;
    public static void init(Context context) {
        shortAnimationDuration = context.getResources().getInteger(android.R.integer.config_shortAnimTime);
    }
    public static void crossFade(final View from, final View to) {
        to.setAlpha(0f);
        to.setVisibility(View.VISIBLE);
        to.animate().alpha(1f).setDuration(shortAnimationDuration).setListener(null);

        from.animate().alpha(0f).setDuration(shortAnimationDuration).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                super.onAnimationEnd(animation);
                from.setVisibility(View.GONE);
            }
        });
    }
}
