package com.example.kyros.synthesizer_asynctask_demo

import android.media.AudioManager
import android.media.SoundPool
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.*
import kotlin.coroutines.experimental.CoroutineContext

class SoundCoroutineActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job
    private val soundPool = SoundPool(50, AudioManager.STREAM_MUSIC, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        job = Job()

        //load sounds
        val notesAscending = arrayOf(R.raw.scalec, R.raw.scaled, R.raw.scalee, R.raw.scalef, R.raw.scaleg).map {
            Note(soundPool.load(this, it, 1), 1000)
        }.toTypedArray()
        val notesDescending = notesAscending.reversedArray()

        //set listeners
        buttonMainSong1.setOnClickListener {
            soundPool.playSong(notesAscending)
        }
        buttonMainSong2.setOnClickListener {
            soundPool.playSong(notesDescending)
        }
        buttonMainTogether.setOnClickListener {
            soundPool.playSong(notesAscending)
            soundPool.playSong(notesDescending)
        }
    }

    private fun SoundPool.playSong(notes: Array<Note>) = async {
        notes.forEach {
            play(it.noteId, 1f, 1f, 1, 0, 1f)
            delay(it.delay)
        }
    }

    override fun onStop() {
        super.onStop()
        job.cancel()
    }
}
