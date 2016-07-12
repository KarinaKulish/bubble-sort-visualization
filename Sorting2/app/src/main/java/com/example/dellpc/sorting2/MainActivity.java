package com.example.dellpc.sorting2;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    List<Sheep> sheep = new ArrayList<>();
    private RecyclerView rv;
    private RVAdapter adapter;
    private LinearLayoutManager layoutManager;
    private View ufoView;
    private Animation animOne, animThree;
    private boolean isSwapped;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        rv = (RecyclerView) findViewById(R.id.rv);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.getItemAnimator().setMoveDuration(700);

        sheep.add(new Sheep(1, "1"));
        sheep.add(new Sheep(2, "2"));
        sheep.add(new Sheep(3, "3"));
        sheep.add(new Sheep(4, "4"));
        sheep.add(new Sheep(5, "5"));
        sheep.add(new Sheep(6, "6"));
        sheep.add(new Sheep(7, "7"));
        sheep.add(new Sheep(8, "8"));

        adapter = new RVAdapter(sheep);
        rv.setAdapter(adapter);

        ufoView = findViewById(R.id.imageView_ufo);
        animOne = AnimationUtils.loadAnimation(this, R.anim.animation_one);
        animThree = AnimationUtils.loadAnimation(this, R.anim.animation_three);
        isSwapped = false;

    }

    /**
     * Starts sorting animation
     */
    public void onAnimationSort(final View v) {

        sortValuesStep(sheep.size() - 1, 0, isSwapped);

    }

    private void sortValuesStep(int i, int j, boolean isSwapped) {
        if (i == 0) {
            return;
        }
        Sheep left = sheep.get(j);
        Sheep right = sheep.get(j + 1);

        int swapLeft = -1, swapRight = -1;

        if (left.number > right.number) {
            Collections.swap(sheep, j, j + 1);
            swapLeft = j;
            swapRight = j + 1;
            isSwapped = true;

        }
        if ((j == sheep.size() - 2) && (i > 0)) {
            j = 0;
            i--;
            if (!isSwapped) {
                i = 0;
            } else {
                isSwapped = false;
            }
        } else if (j < sheep.size() - 2) {
            j++;
        }
        animateSortStep(swapLeft, swapRight, i, j, isSwapped);
    }


    private void animateSortStep(final int swapLeft, final int swapRight, final int i, final int j, final boolean isSwapped) {
        View leftView = layoutManager.findViewByPosition(j);
        View rightView = layoutManager.findViewByPosition(j + 1);

        if (leftView == null) {
            rv.smoothScrollToPosition(j);
            animateUfoMove(0);
        } else {
            if (rightView == null || rightView.getLeft() > rv.getWidth()) {
                rv.smoothScrollToPosition(j + 1);
                rv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animateSortStep(swapLeft, swapRight, i, j, isSwapped);
                    }
                }, 600);

                return;
            } else {
                int leftX = leftView.getLeft();
                animateUfoMove(leftX);
            }
        }

        if (swapLeft != -1 && swapRight != -1) {
            adapter.notifyItemMoved(swapLeft, swapRight);
        }
        rv.postDelayed(new Runnable() {
            public void run() {
                sortValuesStep(i, j, isSwapped);
            }
        }, 700);

    }

    private void animateUfoMove(int leftX) {
        ufoView.animate()
                .translationX(leftX)
                .setDuration(250)
                .start();
    }

    private void shuffle() {
        Collections.shuffle(sheep);
        rv.startAnimation(animThree);
        rv.getAdapter().notifyDataSetChanged();
        ImageView imageView = (ImageView) findViewById(R.id.imageView_ufo);
        if (imageView != null) {
            imageView.startAnimation(animOne);
        }
    }

    public void onAnimationShuffle(final View v) {
        shuffle();
    }

}
