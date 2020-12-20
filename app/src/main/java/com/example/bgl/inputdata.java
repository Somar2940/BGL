package com.example.bgl;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import android.widget.Button;
import java.util.Date;
import java.text.DateFormat;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.Log;

public class inputdata extends AppCompatActivity {
    private SoundPool soundPool;
    private int soundOne, soundTwo;
    private Handler mHandler;
    private Timer mTimer;
    private EditText editTextValue;
    private MyOpenHelper helper;
    private SQLiteDatabase db;
    // 時刻表示のフォーマット
    private static SimpleDateFormat mSimpleDataFormat = new SimpleDateFormat("yyyy/MM/dd" + "\n" + "  HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                // USAGE_MEDIA
                // USAGE_GAME
                .setUsage(AudioAttributes.USAGE_GAME)
                // CONTENT_TYPE_MUSIC
                // CONTENT_TYPE_SPEECH, etc.
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                // ストリーム数に応じて
                .setMaxStreams(2)
                .build();

        // wav をロードしておく
        soundOne = soundPool.load(this, R.raw.girl, 1);
        soundTwo = soundPool.load(this, R.raw.swit, 1);

        // load が終わったか確認する場合
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Log.d("debug", "sampleId=" + sampleId);
                Log.d("debug", "status=" + status);
            }
        });

        editTextValue = findViewById(R.id.edit_text_value);

        mHandler = new Handler(getMainLooper());
        mTimer = new Timer();
        // 一秒ごとに定期的に実行します。
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    public void run() {
                        Calendar calendar = Calendar.getInstance();
                        String nowDate = mSimpleDataFormat.format(calendar.getTime());
                        // 時刻表示をするTextView
                        ((TextView) findViewById(R.id.text_time1)).setText(nowDate);
                    }
                });
            }
        }, 0, 1000);

        ImageButton insertButton = findViewById(R.id.button_insert);
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // one.wav の再生
                // play(ロードしたID, 左音量, 右音量, 優先度, ループ,再生速度)
                soundPool.play(soundOne, 1.0f, 1.0f, 0, 0, 1);

                if (helper == null) {
                    helper = new MyOpenHelper(getApplicationContext());
                }

                if (db == null) {
                    db = helper.getWritableDatabase();
                }
                Calendar cal1 = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH時mm分ss秒");
                String str1 = sdf.format(cal1.getTime());
                String value = editTextValue.getText().toString();

                // 血糖値は整数を想定
                insertData(db, str1, Integer.valueOf(value));

                SimpleDateFormat ld = new SimpleDateFormat("yyyyMMdd");

                Intent intentstart = new Intent(getApplication(), praise.class);
                startActivity(intentstart);
            }
        });

        ImageButton backButton = findViewById(R.id.backbutton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // one.wav の再生
                // play(ロードしたID, 左音量, 右音量, 優先度, ループ,再生速度)
                soundPool.play(soundTwo, 1.0f, 1.0f, 0, 0, 1);
                Intent intentView = new Intent(getApplication(), start.class);
                startActivity(intentView);
            }
        });
    }

    private void insertData(SQLiteDatabase db, String com, int price){

        ContentValues values = new ContentValues();
        values.put("company", com);
        values.put("stockprice", price);

        db.insert("testdb", null, values);
    }

}