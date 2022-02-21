package com.example.agarimo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.agarimo.databinding.ActivitySeleccionBinding

class ActivitySeleccion : AppCompatActivity() {

    private lateinit var binding: ActivitySeleccionBinding
    private lateinit var intent2: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySeleccionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle = intent.extras
        val userType = bundle?.get("TIPO_USUARIO")






        binding.btnSingIn.setOnClickListener {
            intent2=Intent(this, ActivitySingIn::class.java)
            intent2.putExtra("USUARIO",userType.toString())
            startActivity(intent2)

        }

        binding.btnSingUp.setOnClickListener {
            intent2= Intent(this, ActivitySignUp::class.java)
            intent2.putExtra("USUARIO",userType.toString())
            startActivity(intent2)

        }
    }


}