package com.example.dellpc.sorting2;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by DELL PC on 11.07.2016.
 */
public class CustomTextView extends TextView {
    public CustomTextView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/waltographUI.ttf"));
    }
}
