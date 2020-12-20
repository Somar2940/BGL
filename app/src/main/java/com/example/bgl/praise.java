package com.example.bgl;

import android.content.Intent;
import android.content.res.TypedArray;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class praise extends AppCompatActivity {

    private SoundPool soundPool;
    private int soundOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_praise);

        int i,j;
        TextView mes1 = findViewById(R.id.prmes1);
        TextView mes2 = findViewById(R.id.prmes2);
        TypedArray typedArray1 = getResources().obtainTypedArray(R.array.prmes1);
        i = (int) (Math.floor(Math.random() * (3)) );
        String str1 = typedArray1.getString(i);
        TypedArray typedArray2 = getResources().obtainTypedArray(R.array.prmes2);
        j = (int) (Math.floor(Math.random() * (3)) );
        String str2 = typedArray2.getString(j);
        mes1.setText(str1);
        mes2.setText(str2);

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
        soundOne = soundPool.load(this,R.raw.bt, 1);

        // load が終わったか確認する場合
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Log.d("debug","sampleId="+sampleId);
                Log.d("debug","status="+status);
            }
        });

        Button sendButton = findViewById(R.id.clearButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // one.wav の再生
                // play(ロードしたID, 左音量, 右音量, 優先度, ループ,再生速度)
                soundPool.play(soundOne, 1.0f, 1.0f, 0, 0, 1);
                Intent intentstart = new Intent(getApplication(),start.class);
                startActivity(intentstart);
            }
        });
    }
}
