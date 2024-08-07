package com.ram.zedmusic;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView textViewTrackName;
    private SeekBar seekBar;
    private Button buttonPlay, buttonPause, buttonStop;

    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTrackName = findViewById(R.id.textViewTrackName);
        seekBar = findViewById(R.id.seekBar);
        buttonPlay = findViewById(R.id.buttonPlay);
        buttonPause = findViewById(R.id.buttonPause);
        buttonStop = findViewById(R.id.buttonStop);

        // Initialize media player with the music file from res/raw directory
        mediaPlayer = MediaPlayer.create(this, R.raw.bye_bye_bye);

        if (mediaPlayer == null) {
            Log.e("MainActivity", "MediaPlayer creation failed. Check your raw resource file.");
            Toast.makeText(this, "Error initializing MediaPlayer", Toast.LENGTH_SHORT).show();
            return;
        }

        textViewTrackName.setText("Sample Music");

        buttonPlay.setOnClickListener(view -> {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                updateSeekBar();
            }
        });

        buttonPause.setOnClickListener(view -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        });

        buttonStop.setOnClickListener(view -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.bye_bye_bye);
                seekBar.setProgress(0);
            }
        });

        mediaPlayer.setOnCompletionListener(mp -> seekBar.setProgress(0));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void updateSeekBar() {
        seekBar.setMax(mediaPlayer.getDuration());
        runnable = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 500);
            }
        };
        handler.postDelayed(runnable, 500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacks(runnable);
    }
}
