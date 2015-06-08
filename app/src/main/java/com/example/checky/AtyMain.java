package com.example.checky;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.TextView;


public class AtyMain extends Activity{

    private ViewBarChart viewBarChart;
    private TextView yesterdayTimes;
    private  ViewProgressArc viewProgressArc;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
//        setupActionBar();
        startService(new Intent(AtyMain.this, ScreenService.class));
        Config.configDays();
        createCountData();
        viewBarChart = (ViewBarChart) findViewById(R.id.scorePillars);
        viewBarChart.setOnTouchListener(viewBarChart);
        viewProgressArc = (ViewProgressArc) findViewById(R.id.viewProgressArc);
        yesterdayTimes = (TextView) findViewById(R.id.alt_count);
        SharedPreferences sp = getSharedPreferences("count", Context.MODE_PRIVATE);
        yesterdayTimes.setText("Yesterday: "+sp.getInt(Config.YESTERDAY,0)+" times");

        findViewById(R.id.settings_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AtyMain.this,AtySetting.class));
            }
        });
        findViewById(R.id.share_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sendText = "So far today I've checked my phone " + viewProgressArc.getScore()+" times. How about you? http://www.checkyapp.com";
                Intent intent2 = new Intent(); intent2.setAction(Intent.ACTION_SEND);
                intent2.setType("text/plain");
                intent2.putExtra(Intent.EXTRA_TEXT, sendText );
                startActivity(intent2);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    private void createCountData() {
        SharedPreferences sp = getSharedPreferences("count", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        for(int i = 0; i<7;i++){
            String date = Config.DAYS[i];
            editor.putInt(date,getRandom(0,100));
        }
        editor.apply();
    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            SharedPreferences sp = getSharedPreferences("count", Context.MODE_PRIVATE);
            int curScore = sp.getInt(Config.TODAY, 0);
            if (viewProgressArc.getScore()!=curScore) {
                viewProgressArc.setFirstTime(true, viewProgressArc.getScore());
                viewProgressArc.setScore(curScore);
                viewProgressArc.postInvalidate();
                viewBarChart.getScores();
                viewBarChart.postInvalidate();
            }
        }
    }

    private int getRandom(int min,int max){
		return (int) Math.round(Math.random()*(max-min)+min);
	}
        
}

// TODO 每天24点定时更新Config
