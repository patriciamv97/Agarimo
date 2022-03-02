package com.example.agarimo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.agarimo.databinding.ActivityMainBinding
import android.content.Intent
import android.util.Log
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var tipoUsuario : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, ActivitySeleccion::class.java)


        binding.btnCliente.setOnClickListener {
            tipoUsuario= "Cliente"
            intent.putExtra("TIPO_USUARIO", tipoUsuario)
            startActivity(intent)

        }

        binding.btnProfesional.setOnClickListener {
            tipoUsuario= "Profesional"
            Toast.makeText(this,"QUE PASA", Toast.LENGTH_SHORT).show()
            intent.putExtra("TIPO_USUARIO", tipoUsuario)
            startActivity(intent)

        }
    }
}