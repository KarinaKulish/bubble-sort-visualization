package com.example.dellpc.sorting2;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by DELL PC on 11.07.2016.
 */
public class CardViewActivity extends Activity {
    TextView sheepText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardview_activity);
        sheepText = (TextView)findViewById(R.id.textView);

        sheepText.setText("1");
    }
}
