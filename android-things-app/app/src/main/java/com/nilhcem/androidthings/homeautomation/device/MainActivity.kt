package com.nilhcem.androidthings.homeautomation.device

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.nilhcem.androidthings.homeautomation.device.components.MagicBlueRgbBulb

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName!!
    }

    private val lightbulb by lazy { MagicBlueRgbBulb(applicationContext) }
    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(lightbulb)

        viewModel.firestoreLiveData.observe({ lifecycle }) {
            it?.let { device ->
                Log.i(TAG, device.toString())
            }
        }
    }
}
