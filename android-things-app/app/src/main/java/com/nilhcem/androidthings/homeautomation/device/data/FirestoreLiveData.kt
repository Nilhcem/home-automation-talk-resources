package com.nilhcem.androidthings.homeautomation.device.data

import android.arch.lifecycle.LiveData
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.nilhcem.androidthings.homeautomation.BuildConfig
import com.nilhcem.androidthings.homeautomation.device.data.model.*

class FirestoreLiveData : LiveData<Device>() {

    companion object {
        private val TAG = FirestoreLiveData::class.java.simpleName!!
    }

    private var listener: ListenerRegistration? = null

    override fun onActive() {
        super.onActive()
        authenticate { user ->
            listenForFirestoreChanges(user)
        }
    }

    override fun onInactive() {
        super.onInactive()
        listener?.remove().also { listener = null }
    }

    private fun authenticate(onceAuthenticated: (FirebaseUser) -> Unit) {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Log.d(TAG, "User is not authenticated")
            auth.signInWithEmailAndPassword(BuildConfig.FIREBASE_EMAIL, BuildConfig.FIREBASE_PASSWORD)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "signInWithEmail:success")
                            onceAuthenticated(task.result.user)
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                        }
                    }
        } else {
            Log.d(TAG, "User is already authenticated")
            onceAuthenticated(currentUser)
        }
    }

    private fun listenForFirestoreChanges(user: FirebaseUser) {
        val ref = FirebaseFirestore.getInstance().collection("users").document(user.uid).collection("devices")
        listener = ref.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException == null) {
                querySnapshot.documentChanges.forEach { documentChange ->
                    if (documentChange.type == DocumentChange.Type.MODIFIED) {
                        with(documentChange.document) {
                            val device = when (id) {
                                "lightbulb" -> Lightbulb(getBoolean("on"), getLong("spectrumRGB").toInt())
                                "fan" -> Outlet(getBoolean("on"))
                                "3dlamp" -> Lamp3d(getBoolean("on"), getLong("spectrumRGB").toInt())
                                else -> Unknown
                            }

                            postValue(device)
                        }
                    }
                }
            } else {
                Log.e(TAG, "Firestore error", firebaseFirestoreException)
            }
        }
    }
}
