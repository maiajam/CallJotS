package com.maiajam.calljots.helper;

import android.content.Context;
import android.drm.DrmStore;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import com.maiajam.calljots.R;
import com.maiajam.calljots.ui.fragment.AllContactFrag;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_SWIPE;
import static android.support.v7.widget.helper.ItemTouchHelper.END;
import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;
import static android.support.v7.widget.helper.ItemTouchHelper.RIGHT;
import static android.support.v7.widget.helper.ItemTouchHelper.START;

enum ButtonsState {
    GONE,
    LEFT_VISIBLE,
    RIGHT_VISIBLE
}

public class SwipeControler extends ItemTouchHelper.Callback implements ViewTreeObserver.OnDrawListener {

    private final SwipeContrlloerActions actions;
    private boolean swipeBack;
    private ButtonsState buttonShowedState = ButtonsState.GONE;
    private static final float buttonWidth = 200;
    private RectF buttonInstance;
    private int hint ;
    private RecyclerView.ViewHolder currentItemViewHolder = null;
    private Context context ;
    private AllContactFrag mListener = new AllContactFrag();

    public SwipeControler(Context mContext, SwipeContrlloerActions swipeContrlloerActions, int mhint) {
        this.actions = swipeContrlloerActions;
        hint = mhint ;
        context = mContext ;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, START | END);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = false;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onChildDraw(Canvas c,
                            RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder,
                            float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        drawButton(c,viewHolder);
        if (currentItemViewHolder != null && currentItemViewHolder != viewHolder) {
            mListener.revertSwipe(currentItemViewHolder.getAdapterPosition());
        }
        currentItemViewHolder = viewHolder;
        if (actionState == ACTION_STATE_SWIPE) {
            if (buttonShowedState != ButtonsState.GONE) {
                if (buttonShowedState == ButtonsState.LEFT_VISIBLE) dX = Math.max(dX, buttonWidth);
                if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) dX = Math.min(dX, -buttonWidth);
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
            else {
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
        if (buttonShowedState == ButtonsState.GONE) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

    }
    private void drawButton(Canvas c, RecyclerView.ViewHolder viewHolder) {

        float buttonWidthWithoutPadding = buttonWidth - 20;
        float corners = 0;

        View itemView = viewHolder.itemView;
        Paint p = new Paint();


        buttonInstance = null;
        if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
            RectF leftButton = new RectF(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + buttonWidthWithoutPadding, itemView.getBottom());
            p.setColor(context.getResources().getColor(R.color.colorSecondAccent));
            c.drawRoundRect(leftButton, corners, corners, p);
            if(hint == 1)
            {
                drawText("Call", c, leftButton, p);
                buttonInstance = leftButton;
            }else
            {
                drawText("Edit", c, leftButton, p);
                buttonInstance = leftButton;
            }
        }
        else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
            RectF rightButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
            p.setColor(Color.RED);
            c.drawRoundRect(rightButton, corners, corners, p);
            drawText("Delete", c, rightButton, p);
            buttonInstance = rightButton;
        }
    }

    private void drawText(String edit, Canvas c, RectF button, Paint p) {

        float textSize = 40;
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        p.setTextSize(textSize);
        float textWidth = p.measureText(edit);
        c.drawText(edit, button.centerX()-(textWidth/2), button.centerY()+(textSize/2), p);
    }
    private void setTouchListener(final Canvas c,
                                  final RecyclerView recyclerView,
                                  final RecyclerView.ViewHolder viewHolder,
                                  final float dX, final float dY,
                                  final int actionState, final boolean isCurrentlyActive) {

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
                if (swipeBack) {
                    if (dX < -buttonWidth) buttonShowedState = ButtonsState.RIGHT_VISIBLE;
                    else if (dX > buttonWidth) buttonShowedState  = ButtonsState.LEFT_VISIBLE;

                    if (buttonShowedState != ButtonsState.GONE) {
                        setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        setItemsClickable(recyclerView, false);
                    }
                }
                return false;
            }

        });
    }
    private void setTouchDownListener(final Canvas c,
                                      final RecyclerView recyclerView,
                                      final RecyclerView.ViewHolder viewHolder,
                                      final float dX, final float dY,
                                      final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
                return false;
            }
        });
    }
    private void setTouchUpListener(final Canvas c,
                                    final RecyclerView recyclerView,
                                    final RecyclerView.ViewHolder viewHolder,
                                    final float dX, final float dY,
                                    final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (actions != null && buttonInstance != null && buttonInstance.contains(event.getX(), event.getY())) {
                        if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
                            actions.onLeftClicked(viewHolder.getAdapterPosition());
                        }
                        else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
                            actions.onRightClicked(viewHolder.getAdapterPosition());
                        }
                    }
                    SwipeControler.super.onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);
                    recyclerView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });
                    setItemsClickable(recyclerView, true);
                    swipeBack = false;
                    buttonShowedState = ButtonsState.GONE;
                }
                return false;
            }
        });
    }
    private void setItemsClickable(RecyclerView recyclerView,
                                   boolean isClickable) {
        for (int i = 0; i < recyclerView.getChildCount(); ++i) {
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    }

    @Override
    public void onDraw() {

    }
}
