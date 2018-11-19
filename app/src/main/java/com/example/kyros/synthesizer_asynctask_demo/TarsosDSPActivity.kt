package com.example.kyros.synthesizer_asynctask_demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import be.tarsos.dsp.io.android.AndroidFFMPEGLocator
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import kotlinx.coroutines.experimental.*
import kotlin.coroutines.experimental.CoroutineContext

class TarsosDSPActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarsos_dsp)
        job = Job()

        launch {
            AndroidFFMPEGLocator(this@TarsosDSPActivity)
            val adp = AudioDispatcherFactory.fromPipe("android.resources://$packageName/scalea.wav", 44100, 4096, 0)
        }

        //val gain = GainProcessor(1.0)
        //val params = WaveformSimilarityBasedOverlapAdd.Parameters.slowdownDefaults(0.5, file.)
        //val wsola = WaveformSimilarityBasedOverlapAdd(WaveformSimilarityBasedOverlapAdd.Parameters.slowdownDefaults(0.5))
    }
}
