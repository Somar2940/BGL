package com.example.bgl;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import java.util.Timer;
import java.util.TimerTask;
import android.widget.ImageButton;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.util.Log;
import android.database.Cursor;
import java.text.SimpleDateFormat;
import android.widget.TextView;
import android.os.Bundle;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.Log;

import org.w3c.dom.Text;


public class start extends AppCompatActivity {
    private SoundPool soundPool;
    private int soundOne;
    private Handler mHandler;
    private Timer mTimer;
    public int hour,spDay;
    // 時刻表示のフォーマット
    private static SimpleDateFormat mSimpleDataFormat1 = new SimpleDateFormat("yyyy/MM/dd");
    private static SimpleDateFormat mSimpleDataFormat2 = new SimpleDateFormat("HH");
    private static SimpleDateFormat mSimpleDataFormat3 = new SimpleDateFormat("MMdd");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        TextView greeting = findViewById(R.id.greeting);
        TextView message = findViewById(R.id.message);
        int i,j;

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

        // one.wav をロードしておく
        soundOne = soundPool.load(this, R.raw.swit, 1);

        // load が終わったか確認する場合
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Log.d("debug","sampleId="+sampleId);
                Log.d("debug","status="+status);
            }
        });

        ImageButton sendButton = findViewById(R.id.menubutton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // one.wav の再生
                // play(ロードしたID, 左音量, 右音量, 優先度, ループ,再生速度)
                soundPool.play(soundOne, 1.0f, 1.0f, 0, 0, 1);
                Intent intentinput = new Intent(getApplication(),inputdata.class);
                startActivity(intentinput);
            }
        });

        ImageButton sendButton2 = findViewById(R.id.viewbutton);
        sendButton2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // one.wav の再生
                // play(ロードしたID, 左音量, 右音量, 優先度, ループ,再生速度)
                soundPool.play(soundOne, 1.0f, 1.0f, 0, 0, 1);
                Intent intentView = new Intent(getApplication(),ShowDataBase.class);
                startActivity(intentView);
            }
        });

        ImageButton backButton = findViewById(R.id.backbutton);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // one.wav の再生
                // play(ロードしたID, 左音量, 右音量, 優先度, ループ,再生速度)
                soundPool.play(soundOne, 1.0f, 1.0f, 0, 0, 1);
                Intent intentView = new Intent(getApplication(),Title.class);
                startActivity(intentView);
            }
        });

        mHandler = new Handler(getMainLooper());
        mTimer = new Timer();
        // 一秒ごとに定期的に実行します。
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    public void run() {
                        Calendar calendar1 = Calendar.getInstance();
                        String nowDate1 = mSimpleDataFormat1.format(calendar1.getTime());
                        // 時刻表示をするTextView
                        ((TextView) findViewById(R.id.text_time)).setText(nowDate1);
                    }
                });}
        },0,1000);


        //時間によってメッセージを変更
        Calendar calendar2 = Calendar.getInstance();
        Calendar calendar3 = Calendar.getInstance();
        String nowDate2 = mSimpleDataFormat2.format(calendar2.getTime());
        String nowDate3 = mSimpleDataFormat3.format(calendar3.getTime());
        hour = Integer.parseInt(nowDate2);
        spDay = Integer.parseInt(nowDate3);
        //朝昼晩の挨拶
        if(4<=hour && hour<=9){
            greeting.setText(R.string.morning);
            TypedArray typedArray1 = getResources().obtainTypedArray(R.array.mesmo1);
            i = (int) (Math.floor(Math.random() * (3)) );
            String str1 = typedArray1.getString(i);
            TypedArray typedArray2 = getResources().obtainTypedArray(R.array.mesmo2);
            j = (int) (Math.floor(Math.random() * (3)) );
            String str2 = typedArray2.getString(j);
            String str = (str1+"\n"+str2);
            message.setText(str);
        }
        if(10<=hour && hour<=17){
            greeting.setText(R.string.hello);
            TypedArray typedArray1 = getResources().obtainTypedArray(R.array.meshe1);
            i = (int) (Math.floor(Math.random() * (3)) );
            String str1 = typedArray1.getString(i);
            TypedArray typedArray2 = getResources().obtainTypedArray(R.array.meshe2);
            j = (int) (Math.floor(Math.random() * (3)) );
            String str2 = typedArray2.getString(j);
            String str = (str1+"\n"+str2);
            message.setText(str);
        }
        if(18<=hour && hour<24){
            greeting.setText(R.string.evening);
            TypedArray typedArray1 = getResources().obtainTypedArray(R.array.mesev1);
            i = (int) (Math.floor(Math.random() * (3)) );
            String str1 = typedArray1.getString(i);
            TypedArray typedArray2 = getResources().obtainTypedArray(R.array.mesev2);
            j = (int) (Math.floor(Math.random() * (3)) );
            String str2 = typedArray2.getString(j);
            String str = (str1+"\n"+str2);
            message.setText(str);
        }
        if(0<=hour && hour<=3){
            greeting.setText(R.string.midnight);
            message.setText(R.string.mesmid);
        }
        //行事ごとの特殊なメッセージ
        if(spDay==101){
            greeting.setText(R.string.grnewyear);
            message.setText(R.string.mesnewyear);
        }
        if(spDay==203){
            greeting.setText(R.string.grsetubunn);
            message.setText(R.string.messetubunn);
        }
        if(spDay==214){
            greeting.setText(R.string.grvalen);
            message.setText(R.string.mesvalen);
        }
        if(spDay==314) {
            greeting.setText(R.string.grwhite);
        }
        if(spDay==401) {
            greeting.setText(R.string.grnewdate);
            message.setText(R.string.mesnewdate);
        }
        if(spDay==505) {
            greeting.setText(R.string.grchild);
        }
        if(spDay==707) {
            greeting.setText(R.string.grtanabata);
            message.setText(R.string.mestanabata);
        }
        if(spDay==810) {
            greeting.setText(R.string.grmount);
        }
        if(spDay==1031) {
            greeting.setText(R.string.grhalloween);
            message.setText(R.string.meshalloween);
        }
        if(spDay==1225){
            greeting.setText(R.string.grxmas);
            message.setText(R.string.mesxmas);
        }
        if(spDay==1231) {
            greeting.setText(R.string.grfinalday);
            message.setText(R.string.mesfinalday);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 定期実行をcancelする
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }


}