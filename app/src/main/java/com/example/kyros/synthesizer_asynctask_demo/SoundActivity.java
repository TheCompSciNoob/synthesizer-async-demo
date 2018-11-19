package com.example.kyros.synthesizer_asynctask_demo;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SoundActivity extends AppCompatActivity implements View.OnClickListener {
    private SoundPool soundPool;
    //widgets
    private Button button1;
    private Button button2;
    private Button togetherButton;
    //notes
    private int noteC, noteD, noteE, noteF, noteG;
    //handler that schedules the notes
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();
        loadSounds();
        wireWidgets();
        setListeners();
    }

    private void loadSounds() {
        soundPool = new SoundPool(50, AudioManager.STREAM_MUSIC, 0);
        noteC = soundPool.load(this, R.raw.scalec, 1);
        noteD = soundPool.load(this, R.raw.scaled, 1);
        noteE = soundPool.load(this, R.raw.scalee, 1);
        noteF = soundPool.load(this, R.raw.scalef, 1);
        noteG = soundPool.load(this, R.raw.scaleg, 1);
    }

    private void wireWidgets() {
        button1 = findViewById(R.id.buttonMainSong1);
        button2 = findViewById(R.id.buttonMainSong2);
        togetherButton = findViewById(R.id.buttonMainTogether);
    }

    private void setListeners() {
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        togetherButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonMainSong1:
                scheduleSongs(500, getSong1());
                break;
            case R.id.buttonMainSong2:
                scheduleSongs(500, getSong2());
                break;
            case R.id.buttonMainTogether:
                //Handler plays two songs simultaneously
                scheduleSongs(500, getSong1(), getSong2());
                break;
        }
    }

    private Note[] getSong1() {
        int[] noteIds = {noteC, noteD, noteE, noteF, noteG};
        Note[] song = new Note[noteIds.length];
        for (int i = 0; i < noteIds.length; i++) {
            song[i] = new Note(noteIds[i], 500);
        }
        return song;
    }

    private Note[] getSong2() {
        int[] noteIds = {noteG, noteF, noteE, noteD, noteC};
        Note[] song = new Note[noteIds.length];
        for (int i = 0; i < noteIds.length; i++) {
            song[i] = new Note(noteIds[i], 500);
        }
        return song;
    }

    private void scheduleSongs(long startDelay, Note[]... songs) {
        long base = SystemClock.uptimeMillis() + startDelay;
        for (Note[] song: songs) {
            long delay = 0;
            for (final Note note: song) {
                handler.postAtTime(new Runnable() {
                    @Override
                    public void run() {
                        soundPool.play(note.getNoteId(), 1f, 1f, 1, 0, 1f);
                    }
                }, base + delay);
                delay+=note.getDelay();
            }
        }
    }

    //The PlaySongAsyncTask class extends AsyncTask class
    //and handles processes that may block the UI thread
    //Use static to prevent memory leaks
    private static class PlaySongAsyncTask extends AsyncTask<Note, Void, Void> {
        private SoundPool soundPool;

        private PlaySongAsyncTask(SoundPool soundPool) {
            this.soundPool = soundPool;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            //loops through all notes and play them in the background
            for (Note note: notes) {
                soundPool.play(note.getNoteId(), 1f, 1f, 1, 0, 1f);
                try {
                    Thread.sleep(note.getDelay());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //cancel all Handler callbacks to avoid lifecycle errors
        handler.removeCallbacksAndMessages(null);
    }
}
