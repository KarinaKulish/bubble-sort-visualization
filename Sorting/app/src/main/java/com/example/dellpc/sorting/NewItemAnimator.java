package com.example.dellpc.sorting;

import android.support.v4.animation.AnimatorCompatHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import java.util.ArrayList;

/**
 * Created by DELL PC on 07.07.2016.
 */
public class NewItemAnimator extends RecyclerView.ItemAnimator {
    private static final int AnimDuration = 500;
    private static final int ANIMATION_DURATION = 400;
    private ArrayList<RecyclerView.ViewHolder> mPendingRemovals = new ArrayList<ViewHolder>();
    private ArrayList<RecyclerView.ViewHolder> mPendingAdditions = new ArrayList<ViewHolder>();
    private ArrayList<MoveInfo> mPendingMoves = new ArrayList<MoveInfo>();
    private ArrayList<ChangeInfo> mPendingChanges = new ArrayList<ChangeInfo>();
    private ArrayList<ArrayList<RecyclerView.ViewHolder>> mAdditionsList =
            new ArrayList<ArrayList<ViewHolder>>();
    private ArrayList<ArrayList<MoveInfo>> mMovesList = new ArrayList<ArrayList<MoveInfo>>();
    private ArrayList<ArrayList<ChangeInfo>> mChangesList = new ArrayList<ArrayList<ChangeInfo>>();
    private ArrayList<RecyclerView.ViewHolder> mAddAnimations = new ArrayList<ViewHolder>();
    private ArrayList<RecyclerView.ViewHolder> mMoveAnimations = new ArrayList<ViewHolder>();
    private ArrayList<RecyclerView.ViewHolder> mRemoveAnimations = new ArrayList<ViewHolder>();
    private ArrayList<RecyclerView.ViewHolder> mChangeAnimations = new ArrayList<ViewHolder>();
    private AccelerateInterpolator interpolator = new AccelerateInterpolator();

    private void resetAnimation(RecyclerView.ViewHolder holder) {
        AnimatorCompatHelper.clearInterpolator(holder.itemView);
        endAnimation(holder);
    }

    @Override
    public void runPendingAnimations() {
        boolean removalsPending = !mPendingRemovals.isEmpty();
        boolean additionsPending = !mPendingAdditions.isEmpty();
        boolean movesPending = !mPendingMoves.isEmpty();

        if (!removalsPending && !additionsPending) {
            // nothing to animate
            return;
        }
        if (movesPending) {
            final ArrayList<MoveInfo> moves = new ArrayList<>();
            moves.addAll(mPendingMoves);
            mMovesList.add(moves);
            mPendingMoves.clear();
            Runnable mover = new Runnable() {
                @Override
                public void run() {
                    for (MoveInfo moveInfo : moves) {
                        animateMoveImpl(moveInfo.holder, moveInfo.fromX, moveInfo.fromY,
                                moveInfo.toX, moveInfo.toY);
                    }
                    moves.clear();
                    mMovesList.remove(moves);
                }
            };
            if (removalsPending) {
                View view = moves.get(0).holder.itemView;
                ViewCompat.postOnAnimationDelayed(view, mover, ANIMATION_DURATION);
            } else {
                mover.run();
            }
        }
        // First, remove stuff
        for (RecyclerView.ViewHolder holder : mPendingRemovals) {
            // animateToLeft(holder);
        }
        mPendingRemovals.clear();

        // Next, add stuff
        if (additionsPending) {
            final ArrayList<RecyclerView.ViewHolder> additions = new ArrayList<>();
            additions.addAll(mPendingAdditions);
            mAdditionsList.add(additions);
            mPendingAdditions.clear();
            Runnable adder = new Runnable() {
                public void run() {
                    for (RecyclerView.ViewHolder holder : additions) {
                        animateDownToUp(holder);

                    }
                    additions.clear();
                    mAdditionsList.remove(additions);

                }

            };
            View view = additions.get(0).itemView;
            ViewCompat.postOnAnimationDelayed(view, adder, ANIMATION_DURATION);
        }
    }

    private void animateToLeft(final RecyclerView.ViewHolder oldHolder) {
        final View view = oldHolder != null ? oldHolder.itemView : null;
        if (view != null) {
            mRemoveAnimations.add(oldHolder);

            final ViewPropertyAnimatorCompat animOut = ViewCompat.animate(view)
                    .setDuration(ANIMATION_DURATION)
                    .setInterpolator(interpolator)
                    .translationY(300);
            ;

            animOut.setListener(new VpaListenerAdapter() {
                @Override
                public void onAnimationStart(View view) {
                    dispatchRemoveStarting(oldHolder);
                }

                @Override
                public void onAnimationEnd(View view) {
                    animOut.setListener(null);
                    ViewCompat.setTranslationX(view, 0);
                    dispatchRemoveFinished(oldHolder);
                    mRemoveAnimations.remove(oldHolder);

                    dispatchFinishedWhenDone();
                }
            }).start();
        }
    }

    private void animateDownToUp(final RecyclerView.ViewHolder newHolder) {
        final View newView = newHolder != null ? newHolder.itemView : null;
        if (newView != null) {
            // setting starting animation params for view
            ViewCompat.setTranslationY(newView, 0);

            mAddAnimations.add(newHolder);

            final ViewPropertyAnimatorCompat animIn = ViewCompat.animate(newView)
                    .setDuration(ANIMATION_DURATION)
                    .translationY(0)
                    .alpha(1);

            animIn.setListener(new VpaListenerAdapter() {
                @Override
                public void onAnimationStart(View view) {
                    dispatchAddStarting(newHolder);
                }

                @Override
                public void onAnimationEnd(View view) {
                    animIn.setListener(null);
                    ViewCompat.setTranslationY(newView, 300);
                    dispatchAddFinished(newHolder);
                    mAddAnimations.remove(newHolder);
                    dispatchFinishedWhenDone();
                }
            }).start();
        }
    }

    @Override
    public boolean animateRemove(ViewHolder holder) {
        return false;
    }

    @Override
    public boolean animateAdd(ViewHolder holder) {
        return false;
    }

    @Override
    public boolean animateMove(final ViewHolder holder, int fromX, int fromY,
                               int toX, int toY) {
        final View view = holder.itemView;
        fromX += ViewCompat.getTranslationX(holder.itemView);
        fromY += ViewCompat.getTranslationY(holder.itemView);
        endAnimation(holder);
        int deltaX = toX - fromX;
        int deltaY = toY - fromY;
        if (deltaX == 0 && deltaY == 0) {
            dispatchMoveFinished(holder);
            return false;
        }
        if (deltaX != 0) {
            ViewCompat.setTranslationX(view, -deltaX);
        }
        if (deltaY != 0) {
            ViewCompat.setTranslationY(view, -deltaY);
        }
        mPendingMoves.add(new MoveInfo(holder, fromX, fromY, toX, toY));
        return true;
    }

    private void animateMoveImpl(final RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
        final View view = holder.itemView;
        final int deltaX = toX - fromX;
        final int deltaY = toY - fromY;
        if (deltaX != 0) {
            ViewCompat.animate(view).translationX(0);
        }
        if (deltaY != 0) {
            ViewCompat.animate(view).translationY(0);
        }
        // TODO: make EndActions end listeners instead, since end actions aren't called when
        // vpas are canceled (and can't end them. why?)
        // need listener functionality in VPACompat for this. Ick.
        final ViewPropertyAnimatorCompat animation = ViewCompat.animate(view);
        mMoveAnimations.add(holder);
        animation.setDuration(getMoveDuration()).setListener(new VpaListenerAdapter() {
            @Override
            public void onAnimationStart(View view) {
                dispatchMoveStarting(holder);
            }

            @Override
            public void onAnimationCancel(View view) {
                if (deltaX != 0) {
                    ViewCompat.setTranslationX(view, 0);
                }
                if (deltaY != 0) {
                    ViewCompat.setTranslationY(view, 0);
                }
            }

            @Override
            public void onAnimationEnd(View view) {
                animation.setListener(null);
                dispatchMoveFinished(holder);
                mMoveAnimations.remove(holder);
                dispatchFinishedWhenDone();
            }
        }).start();
    }

    @Override
    public boolean animateChange(ViewHolder oldHolder, ViewHolder newHolder,
                                 int fromX, int fromY, int toX, int toY) {
        final float prevTranslationX = ViewCompat.getTranslationX(oldHolder.itemView);
        final float prevTranslationY = ViewCompat.getTranslationY(oldHolder.itemView);
        final float prevAlpha = ViewCompat.getAlpha(oldHolder.itemView);
        endAnimation(oldHolder);
        int deltaX = (int) (toX - fromX - prevTranslationX);
        int deltaY = (int) (toY - fromY - prevTranslationY);
        // recover prev translation state after ending animation
        ViewCompat.setTranslationX(oldHolder.itemView, prevTranslationX);
        ViewCompat.setTranslationY(oldHolder.itemView, prevTranslationY);
        ViewCompat.setAlpha(oldHolder.itemView, prevAlpha);
        if (newHolder != null && newHolder.itemView != null) {
            // carry over translation values
            endAnimation(newHolder);
            ViewCompat.setTranslationX(newHolder.itemView, -deltaX);
            ViewCompat.setTranslationY(newHolder.itemView, -deltaY);
            ViewCompat.setAlpha(newHolder.itemView, 0);
        }
        mPendingChanges.add(new ChangeInfo(oldHolder, newHolder, fromX, fromY, toX, toY));
        return true;
    }

    @Override
    public void endAnimation(ViewHolder item) {

    }

    @Override
    public void endAnimations() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public long getAddDuration() {
        return AnimDuration;
    }

    @Override
    public long getRemoveDuration() {
        return AnimDuration;
    }

    @Override
    public long getChangeDuration() {
        return AnimDuration;
    }

    @Override
    public long getMoveDuration() {
        return AnimDuration;
    }

    private void dispatchFinishedWhenDone() {
        if (!isRunning()) {
            dispatchAnimationsFinished();
        }
    }

    private static class MoveInfo {
        public ViewHolder holder;
        public int fromX, fromY, toX, toY;

        private MoveInfo(ViewHolder holder, int fromX, int fromY, int toX, int toY) {
            this.holder = holder;
            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
        }
    }

    private static class ChangeInfo {
        public ViewHolder oldHolder, newHolder;
        public int fromX, fromY, toX, toY;

        private ChangeInfo(ViewHolder oldHolder, ViewHolder newHolder) {
            this.oldHolder = oldHolder;
            this.newHolder = newHolder;
        }

        private ChangeInfo(ViewHolder oldHolder, ViewHolder newHolder,
                           int fromX, int fromY, int toX, int toY) {
            this(oldHolder, newHolder);
            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
        }
    }

    private static class VpaListenerAdapter implements ViewPropertyAnimatorListener {
        @Override
        public void onAnimationStart(View view) {
        }

        @Override
        public void onAnimationEnd(View view) {
        }

        @Override
        public void onAnimationCancel(View view) {
        }
    }
}
