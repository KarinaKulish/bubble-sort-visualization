package com.example.dellpc.sorting;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    List<Integer> solution = new ArrayList<>();
    List<Sheep> sheep = new ArrayList<>();
    private Animation animOne, animTwo, animThree, animFour, animShuffle, animUfo;
    private RecyclerView rv;
    private RVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        animOne = AnimationUtils.loadAnimation(this, R.anim.animation_one);
        animTwo = AnimationUtils.loadAnimation(this, R.anim.animation_two);
        animThree = AnimationUtils.loadAnimation(this, R.anim.animation_three);
        animFour = AnimationUtils.loadAnimation(this, R.anim.animation_four);
        animShuffle = AnimationUtils.loadAnimation(this, R.anim.animation_shuffle);
        animUfo = AnimationUtils.loadAnimation(this, R.anim.animation_ufo);
        for (int i = 2; i <= 8; i++) {
            solution.add(i);
        }
        rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.getItemAnimator().setMoveDuration(700);
        //rv.setItemAnimator(new NewItemAnimator());

        sheep.add(new Sheep(2, R.drawable.sheep2));
        sheep.add(new Sheep(3, R.drawable.sheep3));
        sheep.add(new Sheep(4, R.drawable.sheep4));
        sheep.add(new Sheep(5, R.drawable.sheep5));
        sheep.add(new Sheep(6, R.drawable.sheep6));
        sheep.add(new Sheep(7, R.drawable.sheep7));
        sheep.add(new Sheep(8, R.drawable.sheep8));
        adapter = new RVAdapter(sheep);
        rv.setAdapter(adapter);
    }


    /**
     * Starts sorting animation
     */
    public void onAnimationImageFour(View v) {

        sortValuesStep(sheep.size() - 1, 0);
        if (BuildConfig.DEBUG) {
            return;
        }
        Button button = (Button) findViewById((R.id.button));
        Button button1 = (Button) findViewById((R.id.button2));
        if (button == null || button1 == null) {
            return;
        }
        button.setEnabled(false);
        button1.setEnabled(false);
        final ImageView imageView2 = (ImageView) findViewById(R.id.imageView_ufo);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final int width = metrics.widthPixels;
        final ObjectAnimator anim = ObjectAnimator.ofFloat(imageView2, "translationX", 0, width);
        final ObjectAnimator anim1 = ObjectAnimator.ofFloat(imageView2, "translationX", width, 1);
        anim.setDuration(5000);
        anim1.setDuration(700);
        final Handler handler = new Handler();
        final int k = 0;
        final int q = sheep.size() - 3;
        final AnimatorSet set = new AnimatorSet();
        set.playSequentially(anim, anim1);
        set.start();

        final RecyclerView.Adapter adapter = rv.getAdapter();
        handler.postDelayed(new Runnable() {
            int j = k;
            int i = q;
            int w = width;

            @Override
            public void run() {


                Sheep left = sheep.get(j);
                Sheep right = sheep.get(j + 1);


                if (left.number > right.number) {
                    rv.getItemAnimator().setMoveDuration(700);
                    Collections.swap(sheep, j, j + 1);
                    adapter.notifyItemMoved(j, j + 1);
                }
                if ((j == sheep.size() - 2) && (i > 0)) {
                    j = 0;
                    i--;
                    final AnimatorSet set = new AnimatorSet();
                    set.playSequentially(anim, anim1);
                    set.start();

                } else if (j < sheep.size() - 2) {
                    j++;
                }

                handler.postDelayed(this, 700);
            }
        }, 700);
        button.setEnabled(true);
        button1.setEnabled(true);
    }

    private void sortValuesStep(int i, int j) {
        if(i==0){
            return;
        }
        Sheep left = sheep.get(j);
        Sheep right = sheep.get(j + 1);

        int swapLeft = -1, swapRight = -1;

        if (left.number > right.number) {
            Collections.swap(sheep, j, j + 1);
            swapLeft = j;
            swapRight = j + 1;

        }
        if ((j == sheep.size() - 2) && (i > 0)) {
            j = 0;
            i--;
        } else if (j < sheep.size() - 2) {
            j++;
        }

        animateSortStep(swapLeft, swapRight, i, j);

    }

    private void animateSortStep(int swapLeft, int swapRight, final int i, final int j) {
        if (swapLeft != -1 && swapRight != -1) {
            adapter.notifyItemMoved(swapLeft, swapRight);
        }
        rv.postDelayed(new Runnable() {
            public void run() {
                sortValuesStep(i, j);
            }
        }, 700);

    }

    /**
     * Bubble sort realization
     */
    private void animationFour(final int i, final int j, final View v) {
        Animation translation = new TranslateAnimation(0f, 1f, 0f, 1f);
        translation.setDuration(2500);
        final Button button = (Button) findViewById((R.id.button));
        final Button button1 = (Button) findViewById((R.id.button2));
        final int[] picture_index = {sheep.get(0).id, sheep.get(1).id, sheep.get(2).id, sheep.get(3).id,
                sheep.get(4).id, sheep.get(5).id, sheep.get(6).id};
        if ((i < solution.size() - 1) && (j > 0)) {
            translation.setAnimationListener(new Animation.AnimationListener() {
                int inner_loop = i;
                int outer_loop = j;

                ImageView imageView, imageView1;
                ImageView imageView2;
                boolean if_switch = false;

                @Override
                public void onAnimationStart(Animation animation) {
                    imageView = (ImageView) findViewById(picture_index[inner_loop]);
                    imageView1 = (ImageView) findViewById(picture_index[inner_loop + 1]);
                    imageView2 = (ImageView) findViewById(R.id.imageView_ufo);
                    if (inner_loop == 0 && imageView2 != null) {
                        imageView2.startAnimation(animUfo);
                    }
                    if (solution.get(inner_loop) > solution.get(inner_loop + 1)) {
                        imageView1.startAnimation(animTwo);
                        imageView.startAnimation(animFour);
                        if_switch = true;
                    } else {

                        imageView1.startAnimation(animShuffle);
                        imageView.startAnimation(animShuffle);
                        if_switch = false;
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (if_switch) {
                        int t = solution.get(inner_loop);
                        int t1 = solution.get(inner_loop + 1);
                        int temp = get_picture(t);
                        int temp1 = get_picture(t1);
                        solution.set(inner_loop, solution.get(inner_loop + 1));
                        solution.set(inner_loop + 1, t);
                        imageView.setImageResource(temp1);
                        imageView1.setImageResource(temp);
                    }
                    if (inner_loop == solution.size() - 2) {
                        List<Integer> tmp = new ArrayList<Integer>(solution);
                        Collections.sort(tmp);
                        final boolean sorted = tmp.equals(solution);
                        if (sorted) {
                            outer_loop = 0;
                        }
                        inner_loop = -1;
                        outer_loop--;
                    }
                    animationFour(inner_loop + 1, outer_loop, v);
                    inner_loop++;
                    if (outer_loop <= 0) {
                        if (button != null && button1 != null) {
                            button.setEnabled(true);
                            button1.setEnabled(true);
                        }
                    }
                }
            });
            v.startAnimation(translation);
        }
    }

    public void onAnimationShuffle(View v) {
        shuffle();

    }

    /**
     * Returns ImageView with desired number
     */
    public int get_picture(int a) {
        int result = 0;
        if (a == 2)
            result = R.drawable.sheep2;
        else if (a == 3)
            result = R.drawable.sheep3;
        else if (a == 4)
            result = R.drawable.sheep4;
        else if (a == 5)
            result = R.drawable.sheep5;
        else if (a == 6)
            result = R.drawable.sheep6;
        else if (a == 7)
            result = R.drawable.sheep7;
        else if (a == 8)
            result = R.drawable.sheep8;
        return result;
    }

    /**
     * Shuffle List and corresponding ImageViews
     */
    public List shuffle() {
        //imageView.startAnimation(animShuffle);
        Collections.shuffle(sheep);
        rv.startAnimation(animThree);

        rv.getAdapter().notifyDataSetChanged();


        ImageView imageView = (ImageView) findViewById(R.id.imageView_ufo);
        if (imageView != null) {
            imageView.startAnimation(animOne);
        }
        return solution;
    }

}

