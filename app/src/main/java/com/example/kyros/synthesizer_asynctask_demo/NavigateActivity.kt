package com.example.kyros.synthesizer_asynctask_demo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_navigate.*

class NavigateActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigate)

        buttonMainJavaActivity.setOnClickListener(this)
        buttonMainKotlinActivity.setOnClickListener(this)
        buttonMainTarsosDSPActivity.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val intentClass = when(v.id) {
            R.id.buttonMainJavaActivity -> SoundActivity::class.java
            R.id.buttonMainKotlinActivity -> SoundCoroutineActivity::class.java
            R.id.buttonMainTarsosDSPActivity -> TarsosDSPActivity::class.java
            else -> throw IllegalStateException("Activity for button does not exist.")
        }
        startActivity(Intent(this, intentClass))
    }
}
