package com.example.checky;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

/**
 * 圆弧计分
 * @author Administrator
 *
 */
public class ViewProgressArc extends View {

	private Paint paint;
    private Paint timesPaint;
	private RectF helperRectF;
	private float baseSize;
	private int score=0;

    boolean firstTime = true;

    /***
     * when drawing, currentScore is increasing from 0 to score
     * while arcDegree is increasing from 0 to the actual degree slowly
     */
	private int currentScore;

    public ViewProgressArc(Context context){
        super(context);
        init();
    }
    public ViewProgressArc(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        init();
    }

	public void init() {
		Resources res = getResources();
		baseSize = res.getDimension(R.dimen.history_score_base_size);

		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
        paint.setColor(Color.WHITE);

        timesPaint = new Paint();
        timesPaint.setAntiAlias(true);
        timesPaint.setStyle(Style.FILL);
        timesPaint.setTextAlign(Paint.Align.CENTER);
        timesPaint.setTextSize(baseSize*2.0f);
        timesPaint.setColor(0xff329800);

		helperRectF = new RectF();
		helperRectF.set(baseSize * 0.5f, baseSize * 0.5f, baseSize * 18.5f, baseSize * 18.5f);

		setLayoutParams(new LayoutParams((int) (baseSize * 19.5f), (int) (baseSize * 19.5f)));
	}

	protected void onDraw(Canvas c) {
        super.onDraw(c);
        if (firstTime){
            new ScoreThread().start();
            firstTime = false;
        }
        c.drawCircle(baseSize * 9.5f, baseSize * 9.5f, baseSize * 9f, paint);
        timesPaint.setTextSize(baseSize * 6.0f);
        c.drawText("" + currentScore, baseSize * 9.7f, baseSize * 11.0f, timesPaint);
        timesPaint.setTextSize(baseSize * 2.0f);
        c.drawText("TIMES",baseSize * 9.7f,baseSize * 16.0f,timesPaint);
	}

    /**
     * @see android.view.View#measure(int, int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

    /**
     * Determines the width of this view
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private int measureWidth(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);


        result = (int) baseSize * 20 + getPaddingLeft() + getPaddingRight();
        if (specMode == MeasureSpec.AT_MOST) {
            // Respect AT_MOST value if that was what is called for by measureSpec
            result = Math.min(result, specSize);
        }
        return result;
    }

    /**
     * Determines the height of this view
     * @param measureSpec A measureSpec packed into an int
     * @return The height of the view, honoring constraints from measureSpec
     */
    private int measureHeight(int measureSpec) {
        int result ;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        result = (int) baseSize * 20 + getPaddingTop()+ getPaddingBottom();
        if (specMode == MeasureSpec.AT_MOST) {
            // Respect AT_MOST value if that was what is called for by measureSpec
            result = Math.min(result, specSize);
        }
        return result;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setFirstTime(boolean firstTime, int score) {
        this.firstTime = firstTime;
        currentScore = score;
    }

    private class ScoreThread extends  Thread{
        @Override
        public void run()
        {
            while (!Thread.currentThread().isInterrupted()&&currentScore < score){
                currentScore++;
                try{
                    Thread.sleep(300);
                    postInvalidate();
                }catch (InterruptedException e){
                    System.err.println("InterruptedException");
                    this.interrupt();
                }
            }
            this.interrupt();
        }
    }

}
