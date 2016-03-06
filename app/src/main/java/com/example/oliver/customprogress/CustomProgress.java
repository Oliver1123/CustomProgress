package com.example.oliver.customprogress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oliver on 06.03.16.
 */
public class CustomProgress extends View {
    private static final int UPDATE_MESSAGE = 0;
    private static final int MAX_PROGRESS_DEFAULT = 100;
    private static final int UPDATE_TIME_DEFAULT_MS = 10;
    private Paint mProgressPaint;
    private Paint mPausesPaint;

    private int mProgress;
    private int mMaxProgress = MAX_PROGRESS_DEFAULT;
    private int mProgressColor = Color.BLUE;
    private int mPauseColor = Color.WHITE;
    private int mUpdateTimeMS = UPDATE_TIME_DEFAULT_MS;
    private List<Integer> mPauseProgressValue;
    private OnProgressChangeListener mProgressChangeListener;

    private Handler mUpdateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPDATE_MESSAGE) {
                mProgress += mUpdateTimeMS;
                if (mProgress > mMaxProgress) {
                    end();
                } else {
                    if (mProgressChangeListener != null) {
                        mProgressChangeListener.onProgressChange(CustomProgress.this, mProgress);
                    }
                    invalidate();
                    sendEmptyMessageDelayed(UPDATE_MESSAGE, mUpdateTimeMS);
                }
            }
        }
    };

    public CustomProgress(Context context) {
        super(context);
        init();
    }


    public CustomProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setStyle(Paint.Style.FILL);
        mProgressPaint.setColor(mProgressColor);

        mPausesPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPausesPaint .setStyle(Paint.Style.FILL);
        mPausesPaint .setColor(mPauseColor);
        mPauseProgressValue = new ArrayList<>();

        mProgress = 0;
    }
    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {
        if (progress > mMaxProgress)
            throw new IllegalArgumentException("Progress must be < " + mMaxProgress);
        if (progress < 0)
            throw new IllegalArgumentException("Progress must be positive");

        mProgress = progress;
    }

    public int getMaxProgress() {
        return mMaxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        if (mMaxProgress < 0)
            throw new IllegalArgumentException("MaxProgress must be positive");

        mMaxProgress = maxProgress;
    }

    public int getProgressColor() {
        return mProgressColor;
    }

    public void setProgressColor(int progressColor) {
        mProgressColor = progressColor;
        mProgressPaint.setColor(mProgressColor);
    }

    public int getPauseColor() {
        return mPauseColor;
    }

    public void setPauseColor(int pauseColor) {
        mPauseColor = pauseColor;
        mProgressPaint.setColor(mPauseColor);
    }

    public int getUpdateTimeMS() {
        return mUpdateTimeMS;
    }

    public void setUpdateTimeMS(int updateTimeMS) {
        if (updateTimeMS <= 0)
            throw new IllegalArgumentException("Update time must be > 0");

        mUpdateTimeMS = updateTimeMS;
    }

    public OnProgressChangeListener getProgressChangeListener() {
        return mProgressChangeListener;
    }

    public void setProgressChangeListener(@NonNull OnProgressChangeListener progressChangeListener) {
        mProgressChangeListener = progressChangeListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawProgress(canvas);
        drawPauseParts(canvas);
    }

    private void drawPauseParts(Canvas canvas) {
        float partWidth = getWidth() / (float)mMaxProgress;
        for (Integer pauseProgress : mPauseProgressValue) {
            canvas.drawRect(pauseProgress * partWidth, 0,
                    (pauseProgress + 1) * partWidth, getWidth(),
                    mPausesPaint);
        }
    }

    private void drawProgress(Canvas canvas) {
        float partWidth = getWidth() / (float)mMaxProgress;
        canvas.drawRect(0, 0, mProgress * partWidth, getHeight(), mProgressPaint);
    }

    public void start() {
        mUpdateHandler.sendEmptyMessage(UPDATE_MESSAGE);
    }

    public void end() {
        mPauseProgressValue.add(mProgress);
        mUpdateHandler.removeMessages(UPDATE_MESSAGE);
    }

    public void clear() {
        mProgress = 0;
        mPauseProgressValue.clear();
        invalidate();
    }


    public interface OnProgressChangeListener {
        void onProgressChange(CustomProgress customProgress, int value);
    }
}
