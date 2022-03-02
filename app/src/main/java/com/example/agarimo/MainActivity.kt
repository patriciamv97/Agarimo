package com.example.agarimo

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.agarimo.databinding.ActivityMainBinding
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agarimo.adapter.ProfesionalAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var tipoUsuario: String
    private lateinit var database: DatabaseReference
    private val TAG = "ReadAndWriteSnippets"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = Firebase.database("https://agarimo-24b7c-default-rtdb.firebaseio.com/").reference

        addPostEventListener(database)
        val intent = Intent(this, ActivitySeleccion::class.java)

        binding.btnCliente.setOnClickListener {
            tipoUsuario = "Cliente"
            intent.putExtra("TIPO_USUARIO", tipoUsuario)
            startActivity(intent)

        }

        binding.btnProfesional.setOnClickListener {
            tipoUsuario = "Profesional"
            intent.putExtra("TIPO_USUARIO", tipoUsuario)
            startActivity(intent)

        }


    }

    private fun addPostEventListener(postReference: DatabaseReference) {
        // [START post_value_event_listener]
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                dataSnapshot.children.forEach { child ->
                    val profesional: ProfesionalesBd? = ProfesionalesBd(
                        child.child("nombre").getValue() as String?,
                        child.child("lt").getValue() as Double?,
                        child.child("lg").getValue() as Double?,

                        )
                    profesional?.let { ListaProfesionales.listaProfesionales.add(it) }

                    //var nombre : String = Profesional?.
                    Log.d(TAG, "Profesional: " + ListaProfesionales.listaProfesionales.toString())
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "loadPost:onCancelled")

            }

        }
        database.child("/profesionales/").addValueEventListener(postListener)
    }


}