package com.nilhcem.androidthings.homeautomation.device

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.nilhcem.androidthings.homeautomation.device.components.Lamp3d
import com.nilhcem.androidthings.homeautomation.device.components.MagicBlueRgbBulb
import com.nilhcem.androidthings.homeautomation.device.components.PowerOutlet
import com.nilhcem.androidthings.homeautomation.device.core.ext.addObservers
import com.nilhcem.androidthings.homeautomation.device.data.model.Lightbulb
import com.nilhcem.androidthings.homeautomation.device.data.model.Outlet
import com.nilhcem.androidthings.homeautomation.device.data.model.Lamp3d as Lamp3dDevice

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName!!
    }

    private val lightbulb by lazy { MagicBlueRgbBulb(applicationContext) }
    private val fan by lazy { PowerOutlet() }
    private val lamp3d by lazy { Lamp3d() }
    private val viewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObservers(lightbulb, fan)

        viewModel.firestoreLiveData.observe({ lifecycle }) { device ->
            Log.i(TAG, "Update device: $device")

            when (device) {
                is Lightbulb -> lightbulb.onStateChanged(device)
                is Outlet -> fan.onStateChanged(device)
                is Lamp3dDevice -> lamp3d.onStateChanged(device)
            }
        }
    }
}
