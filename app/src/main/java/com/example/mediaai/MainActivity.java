package com.example.mediaai;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private int[] tracks = {R.raw.bili, R.raw.cheap, R.raw.danzo, R.raw.doutra, R.raw.ego, R.raw.onthefloor, R.raw.policeman, R.raw.purelove, R.raw.vostok, R.raw.ziruza};
    private int[] albumCovers = {R.drawable.nig, R.drawable.cheap, R.drawable.danza, R.drawable.dan, R.drawable.ego, R.drawable.stereo, R.drawable.mr, R.drawable.pure, R.drawable.vos, R.drawable.forma};

    private String[] songNames;
    private String[] trackArtists;

    private int currentTrackIndex = 0;
    private boolean isRepeatOne = false;

    private ImageButton btnPlayPause;
    private ImageButton btnMusic;
    private ImageButton btnRepeat;
    private TextView trackTitle;
    private TextView trackArtist;
    private ImageView albumCover;
    private SeekBar seekBar;

    private TextView currentTimeTextView;
    private TextView totalTimeTextView;

    private Handler handler = new Handler();
    private Runnable updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация массивов названий песен и исполнителей
        songNames = new String[]{
                getString(R.string.song_1),
                getString(R.string.song_2),
                getString(R.string.song_3),
                getString(R.string.song_4),
                getString(R.string.song_5),
                getString(R.string.song_6),
                getString(R.string.song_7),
                getString(R.string.song_8),
                getString(R.string.song_9),
                getString(R.string.song_10)
        };

        trackArtists = new String[]{
                getString(R.string.artist_1),
                getString(R.string.artist_2),
                getString(R.string.artist_3),
                getString(R.string.artist_4),
                getString(R.string.artist_5),
                getString(R.string.artist_6),
                getString(R.string.artist_7),
                getString(R.string.artist_8),
                getString(R.string.artist_9),
                getString(R.string.artist_10)
        };

        // Инициализация элементов интерфейса
        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnMusic = findViewById(R.id.btnMusic);
        btnRepeat = findViewById(R.id.btnRepeat);
        trackTitle = findViewById(R.id.SonqName);
        trackArtist = findViewById(R.id.trackArtist);
        albumCover = findViewById(R.id.albumCover);
        seekBar = findViewById(R.id.seekBar);

        currentTimeTextView = findViewById(R.id.currentTime);
        totalTimeTextView = findViewById(R.id.totalTime);

        initializeMediaPlayer();
        setupSeekBar();
        setupButtons();
    }

    private void initializeMediaPlayer() {
        mediaPlayer = MediaPlayer.create(this, tracks[currentTrackIndex]);
        seekBar.setMax(mediaPlayer.getDuration());
        totalTimeTextView.setText(formatTime(mediaPlayer.getDuration()));
        updateTrackInfo();
        btnPlayPause.setImageResource(R.drawable.play);
        seekBar.setProgress(0);

        mediaPlayer.setOnCompletionListener(mp -> {
            if (isRepeatOne) {
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
            } else {
                changeTrack(1);
            }
        });
    }

    private void setupSeekBar() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
                currentTimeTextView.setText(formatTime(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeCallbacks(updateSeekBar);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                handler.postDelayed(updateSeekBar, 0);
            }
        });

        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer.isPlaying()) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currentTimeTextView.setText(formatTime(mediaPlayer.getCurrentPosition()));
                    handler.postDelayed(this, 1000);
                }
            }
        };
    }

    private void setupButtons() {
        btnPlayPause.setOnClickListener(view -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                btnPlayPause.setImageResource(R.drawable.play);
            } else {
                mediaPlayer.start();
                btnPlayPause.setImageResource(R.drawable.pause);
                handler.postDelayed(updateSeekBar, 0);
            }
        });

        findViewById(R.id.btnNext).setOnClickListener(view -> changeTrack(1));
        findViewById(R.id.btnPrevious).setOnClickListener(view -> changeTrack(-1));

        btnMusic.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            startActivityForResult(intent, 1);
        });

        btnRepeat.setOnClickListener(view -> {
            isRepeatOne = !isRepeatOne;
            updateRepeatButtonIcon();
        });
    }

    private void changeTrack(int direction) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.reset();
        handler.removeCallbacks(updateSeekBar);

        currentTrackIndex = (currentTrackIndex + direction + tracks.length) % tracks.length;
        mediaPlayer = MediaPlayer.create(this, tracks[currentTrackIndex]);
        seekBar.setMax(mediaPlayer.getDuration());

        totalTimeTextView.setText(formatTime(mediaPlayer.getDuration()));
        currentTimeTextView.setText(formatTime(0));

        updateTrackInfo();
        mediaPlayer.start();
        btnPlayPause.setImageResource(R.drawable.pause);
        handler.postDelayed(updateSeekBar, 0);

        mediaPlayer.setOnCompletionListener(mp -> {
            if (isRepeatOne) {
                mediaPlayer.seekTo(0);
                mediaPlayer.start();
            } else {
                changeTrack(1);
            }
        });
    }

    private void updateTrackInfo() {
        trackTitle.setText(songNames[currentTrackIndex]);
        albumCover.setImageResource(albumCovers[currentTrackIndex]);
        trackArtist.setText(trackArtists[currentTrackIndex]);
    }

    private void updateRepeatButtonIcon() {
        if (isRepeatOne) {
            btnRepeat.setImageResource(R.drawable.repeat_one);
        } else {
            btnRepeat.setImageResource(R.drawable.repeat);
        }
    }

    private String formatTime(int milliseconds) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            handler.removeCallbacksAndMessages(null);
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                currentTrackIndex = data.getIntExtra("selectedTrackIndex", 0);
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }
                initializeMediaPlayer();
                mediaPlayer.start();
                btnPlayPause.setImageResource(R.drawable.pause);
            }
        }
    }
}
