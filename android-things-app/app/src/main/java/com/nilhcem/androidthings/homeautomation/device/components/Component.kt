package com.nilhcem.androidthings.homeautomation.device.components

import com.nilhcem.androidthings.homeautomation.device.data.model.Device

abstract class Component<in T : Device> {

    private var prevState: T? = null

    fun onStateChanged(newState: T) {
        onStateChanged(prevState, newState)
        prevState = newState
    }

    protected abstract fun onStateChanged(prevState: T?, newState: T)
}
