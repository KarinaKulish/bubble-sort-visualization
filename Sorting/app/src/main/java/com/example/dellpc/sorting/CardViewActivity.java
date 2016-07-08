package com.example.dellpc.sorting;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by DELL PC on 06.07.2016.
 */
public class CardViewActivity extends Activity {
    ImageView sheepImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardview_activity);
        sheepImage = (ImageView)findViewById(R.id.sheep_image);
        sheepImage.setImageResource(R.drawable.sheep2);
    }

}
