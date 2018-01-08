package com.nilhcem.androidthings.homeautomation.device.data

import android.arch.lifecycle.LiveData
import android.util.Log
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.nilhcem.androidthings.homeautomation.device.data.model.Device
import com.nilhcem.androidthings.homeautomation.device.data.model.Lightbulb
import com.nilhcem.androidthings.homeautomation.device.data.model.Unknown

class FirestoreLiveData : LiveData<Device>() {

    private lateinit var listener: ListenerRegistration

    override fun onActive() {
        super.onActive()

        val ref = FirebaseFirestore.getInstance().collection("devices")
        ref.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException == null) {
                querySnapshot.documentChanges.forEach { documentChange ->
                    if (documentChange.type == DocumentChange.Type.MODIFIED) {
                        with(documentChange.document) {
                            val device = when (id) {
                                "lightbulb" -> Lightbulb(getBoolean("on"), getLong("spectrumRGB").toInt())
                                else -> Unknown
                            }

                            postValue(device)
                        }
                    }
                }
            } else {
                Log.e(FirestoreLiveData::class.java.simpleName!!, "Firestore error", firebaseFirestoreException)
            }
        }
    }

    override fun onInactive() {
        super.onInactive()
        listener.remove()
    }
}
