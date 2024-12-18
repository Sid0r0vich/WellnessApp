package com.example.wellness.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import javax.inject.Inject

class UserDatabase @Inject constructor() {
    private val instance: FirebaseFirestore = Firebase.firestore

    fun getDatabase(): FirebaseFirestore = instance
}
