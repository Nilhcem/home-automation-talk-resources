package com.nilhcem.androidthings.homeautomation.device

import android.arch.lifecycle.ViewModel
import com.nilhcem.androidthings.homeautomation.device.data.FirestoreLiveData

class MainViewModel : ViewModel() {

    val firestoreLiveData = FirestoreLiveData()
}
