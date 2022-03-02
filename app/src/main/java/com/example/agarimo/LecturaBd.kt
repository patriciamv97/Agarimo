package com.example.agarimo

import android.content.ContentValues
import com.google.firebase.database.DatabaseError
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

abstract class LecturaBd {

    private lateinit var database: DatabaseReference
    private val TAG = "ReadAndWriteSnippets"

    fun initializeDbRef() {
        // [START initialize_database_ref]
        database = Firebase.database.reference
        // [END initialize_database_ref]
    }
    private fun addPostEventListener(postReference: DatabaseReference) {
        // [START post_value_event_listener]
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val profesional = dataSnapshot.getValue<ProfesionalesBd>()
               Log.d(TAG,""+profesional)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "loadPost:onCancelled")

            }

        }
        postReference.addValueEventListener(postListener)

    }

}



