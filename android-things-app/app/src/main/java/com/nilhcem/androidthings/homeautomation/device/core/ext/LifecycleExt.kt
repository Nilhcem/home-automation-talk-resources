package com.nilhcem.androidthings.homeautomation.device.core.ext

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver

fun Lifecycle.addObservers(vararg observers: LifecycleObserver) {
    observers.forEach {
        addObserver(it)
    }
}
