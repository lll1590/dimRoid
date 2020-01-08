package com.dim.ke.framework.core.util;


import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class AnimationUtils
{
    public static Animation alphaAnimation(float fromAlpha, float toAlpha, int durationMillis)
    {
        AlphaAnimation alpha = new AlphaAnimation(fromAlpha, toAlpha);
        alpha.setDuration(durationMillis);
        alpha.setFillAfter(true);
        return alpha;
    }

}
