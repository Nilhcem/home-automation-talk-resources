package com.nilhcem.androidthings.homeautomation.device.components

import android.graphics.Color
import android.support.annotation.ColorInt
import android.util.Log
import com.nilhcem.androidthings.homeautomation.BuildConfig
import com.nilhcem.androidthings.homeautomation.device.data.model.Lamp3d
import okhttp3.*
import java.io.IOException

class Lamp3d : Component<Lamp3d>() {

    companion object {
        private val TAG = Lamp3d::class.java.simpleName!!

        private val SUPPORTED_COLORS = mapOf(
                0xffffff to 'W', // white
                0xff0000 to 'R', // red
                0x00ff00 to 'G', // green
                0x0000ff to 'B', // blue
                0xb77600 to 'O', // orange
                0x4cff4c to 'L', // lime
                0x6300ff to 'V', // violet
                0xe59400 to 'H', // peach
                0x4cffa9 to 'E', // emerald
                0x9600ff to 'U', // purple
                0xffff00 to 'D', // dandelion
                0x4cfff1 to 'T', // turquoise
                0xff00ff to 'M', // magenta
                0xd6ff00 to 'Y', // yellow
                0x4cc4ff to 'C', // cyan
                0xffc0cb to 'P' // pink
        )
    }

    private val client by lazy { OkHttpClient() }

    override fun onStateChanged(prevState: Lamp3d?, newState: Lamp3d) {
        if (newState.on != prevState?.on) {
            if (newState.on) on() else off()
        }

        if (newState.colorSpectrumRGB != prevState?.colorSpectrumRGB) {
            setColor(newState.colorSpectrumRGB)
        }
    }

    private fun on() = sendCode('N')

    private fun off() = sendCode('F')

    private fun setColor(@ColorInt color: Int) {
        sendCode(getNearestSupportedColorCode(color))
    }

    private fun getNearestSupportedColorCode(color: Int) = SUPPORTED_COLORS[getNearestSupportedColor(color)]!!

    private fun getNearestSupportedColor(color: Int): Int {
        return SUPPORTED_COLORS.keys
                .map {
                    it to
                            Math.abs(Color.red(color) - Color.red(it)) +
                            Math.abs(Color.green(color) - Color.green(it)) +
                            Math.abs(Color.blue(color) - Color.blue(it))
                }
                .sortedBy { it.second }
                .first()
                .first
    }

    private fun sendCode(code: Char) {
        val request = Request.Builder()
                .url("${BuildConfig.LAMP_3D_URL}color/$code")
                .get()
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Error calling url: ${call.request().url()}", e)
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d(TAG, "${call.request().url()} called successfully")
            }
        })
    }
}
