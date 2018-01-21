package com.nilhcem.androidthings.homeautomation.device.data.model

sealed class Device

data class Lightbulb(val on: Boolean, val colorSpectrumRGB: Int) : Device()
data class Outlet(val on: Boolean) : Device()
data class Lamp3d(val on: Boolean, val colorSpectrumRGB: Int) : Device()
object Unknown : Device()
