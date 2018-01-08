package com.nilhcem.androidthings.homeautomation.device.data.model

sealed class Device

data class Lightbulb(val on: Boolean, val colorSpectrumRGB: Int) : Device()
object Unknown : Device()
