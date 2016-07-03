package com.example.dellpc.sorting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.content.pm.ActivityInfo;

public class MainActivity extends AppCompatActivity {

    private Animation animOne, animTwo, animThree, animFour, animShuffle,animUfo;
    private ImageView imageView;
    List<Integer> solution = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        imageView = (ImageView) findViewById(R.id.imageView);
        animOne = AnimationUtils.loadAnimation(this, R.anim.animation_one);
        animTwo = AnimationUtils.loadAnimation(this, R.anim.animation_two);
        animThree = AnimationUtils.loadAnimation(this, R.anim.animation_three);
        animFour = AnimationUtils.loadAnimation(this, R.anim.animation_four);
        animShuffle = AnimationUtils.loadAnimation(this, R.anim.animation_shuffle);
        animUfo= AnimationUtils.loadAnimation(this,R.anim.animation_ufo);
        for (int i = 2; i <= 8; i++)
        {
            solution.add(i);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }


    //Анимация №4 - пузырек

    public void onAnimationImageFour(View v) {

        Button button = (Button) findViewById((R.id.button));
        Button button1 = (Button) findViewById((R.id.button2));
        button.setEnabled(false);
        button1.setEnabled(false);

        AnimationFour(0,solution.size()-1,v);



    }

    private void AnimationFour(final int i,final int j, final View v) {
        Animation fadeout =  new TranslateAnimation(0f, 1f, 0f, 1f);
        fadeout.setDuration(2500);
        final Button button = (Button) findViewById((R.id.button));
        final  Button button1 = (Button) findViewById((R.id.button2));


        if ((i < solution.size()-1)&&(j>0)) {

            fadeout.setAnimationListener(new Animation.AnimationListener() {

                int[] picture_index = {R.id.imageView, R.id.imageView2, R.id.imageView3, R.id.imageView4,
                        R.id.imageView5, R.id.imageView6, R.id.imageView7};
                ImageView imageView, imageView1;
                ImageView imageView2;


                final IntContainer f_i = new IntContainer(i);
                final IntContainer f_j = new IntContainer(j);
                boolean if_switch=false;
                @Override
                public void onAnimationStart(Animation animation) {


                    imageView = (ImageView) findViewById(picture_index[f_i.value]);
                    imageView1 = (ImageView) findViewById(picture_index[f_i.value + 1]);
                    imageView2 = (ImageView) findViewById(R.id.imageView_ufo);
                    if(f_i.value==0)
                    {imageView2.startAnimation(animUfo);
                    }


                        if (solution.get(f_i.value) > solution.get(f_i.value+1)) {
                            imageView1.startAnimation(animTwo);
                            imageView.startAnimation(animFour);
                            if_switch=true;


                        } else {

                            imageView1.startAnimation(animShuffle);
                            imageView.startAnimation(animShuffle);
                            if_switch=false;
                        }

                    }


                @Override
                public void onAnimationRepeat(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if(if_switch==true)
                    {   int t=solution.get(f_i.value);
                        int t1=solution.get(f_i.value+1);
                        int temp=get_picture(t);
                        int temp1=get_picture(t1);
                        solution.set(f_i.value,solution.get(f_i.value+1) );
                        solution.set(f_i.value+1, t);

                        imageView.setImageResource(temp1);
                        imageView1.setImageResource(temp);

                }
                    if(f_i.value==solution.size()-2) {
                        List tmp = new ArrayList(solution);
                        Collections.sort(tmp);
                        final boolean sorted = tmp.equals(solution);
                        if(sorted==true) {
                            f_j.value = 0;
                        }
                        f_i.value = -1;
                        f_j.value--;

                    }

                    AnimationFour(f_i.value + 1, f_j.value,v);
                    f_i.value++;
                    if(f_j.value<=0)
                    { button.setEnabled(true);
                    button1.setEnabled(true);}

                }
            });

            v.startAnimation(fadeout);
        }
    }

    // Анимация №5 - shuffle
    public void onAnimationShuffle(View v) {

      shuffle();

    }
    //подбираем нужную картинку
    public int get_picture(int a){
        int result=0;
        if(a==2)
            result=R.drawable.sheep2;
        else if(a==3)
            result=R.drawable.sheep3;
        else if(a==4)
            result=R.drawable.sheep4;
        else if(a==5)
            result=R.drawable.sheep5;
        else if(a==6)
            result=R.drawable.sheep6;
        else if(a==7)
            result=R.drawable.sheep7;
        else if(a==8)
            result=R.drawable.sheep8;
        return result;

    }

    public List shuffle(){
        imageView.startAnimation(animShuffle);
        Collections.shuffle(solution);
        for(int i=0; i<solution.size();i++)
        {
            int t=solution.get(i);
            int temp=get_picture(t);

            ImageView imageView;
            if(i==0)
                imageView=(ImageView) findViewById(R.id.imageView);
            else if(i==1)
                imageView=(ImageView) findViewById(R.id.imageView2);
            else if(i==2)
                imageView=(ImageView) findViewById(R.id.imageView3);
            else if(i==3)
                imageView=(ImageView) findViewById(R.id.imageView4);
            else if(i==4)
                imageView=(ImageView) findViewById(R.id.imageView5);
            else if(i==5)
                imageView=(ImageView) findViewById(R.id.imageView6);
            else
                imageView=(ImageView) findViewById(R.id.imageView7);
            imageView.startAnimation(animThree);
            imageView.setImageResource(temp);

        }
        ImageView imageView=(ImageView) findViewById(R.id.imageView_ufo);
        imageView.startAnimation(animOne);
        return solution;
    }

}
