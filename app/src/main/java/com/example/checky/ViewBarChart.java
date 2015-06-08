package com.example.checky;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class ViewBarChart extends View implements  View.OnTouchListener {
    private Paint paint = new Paint();

    private int totalWidth;
    private int totalHeight;
    private int barWidth;
    private int gapWidth;

    private int[] scores = new int[7];

    private boolean touched = false;
    private int touchedId ;
    private int touchedHeight;


    public ViewBarChart(Context context) {
        super(context);
    }

    public ViewBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void getScores() {
        SharedPreferences sp = getContext().getSharedPreferences("count", Context.MODE_PRIVATE);
        for(int i = 0; i<7;i++){
            String date = Config.DAYS[i];
            scores[6-i] = sp.getInt(date, 0);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        getScores();
        paint.setColor(0x64cdffde);
        paint.setAntiAlias(true);                       //设置画笔为无锯齿
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(gapWidth*3);
        int maxScore = 2;
        for (int score :scores){
            if (score > maxScore){
                maxScore = score;
            }
        }
        float heightPerScore = totalHeight / maxScore;
        for(int i = 0; i<7; i++){
            float left = gapWidth + (gapWidth+barWidth)*i;
            float right = left+barWidth;
            float bottom = totalHeight;
            float top = bottom - (scores[i] *heightPerScore);
            canvas.drawRect(left,top,right,bottom,paint);
            if (touched && touchedId == i){
                paint.setColor(0xffe1e733);
                canvas.drawRect(left,top,right,top+touchedHeight,paint);
                paint.setColor(0xff329800);
                canvas.drawText(""+scores[i],left+barWidth/2,top+touchedHeight*2/3,paint);
                paint.setColor(0x64cdffde);
            }
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        totalWidth = w;
        totalHeight = h;
        barWidth = totalWidth / 8;
        gapWidth = barWidth / 8;
        touchedHeight= totalHeight / 4;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            // divide = index + riminder
            float divide = event.getX()/(gapWidth+barWidth);
            int index =(int) Math.floor(divide);
            float reminder = divide - index;
            if (reminder > 8/9f && touched){
                touched = false;
            } else if (reminder < 8/9f){
                touched = !touched;
                touchedId = index;
                Log.e("Tag",index +"  onTouched");
            } else {
                return false;
            }
            invalidate();
        }
        return false;
    }
}
