package com.example.agarimo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewParent
import android.widget.Button
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.agarimo.databinding.ActivitySignUpBinding

class ActivitySignUp : AppCompatActivity(), RegistroProfesional.CallBack {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val bundle = intent.extras
        val userType = bundle?.get("USUARIO")
        Log.d("tipo", "" + userType.toString())
        if(userType=="Cliente"){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, RegistroCliente())
                .commit()
            /*
            Hay que crear la activity principal que mostrará el mapa y los profesionales y así dotar de sentido y funcionalidad a la app
            e implementarla aquí.
             */
        }else{
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, RegistroProfesional())
                .commit()

        }
    }
    // Hacer metodo replaceFragment y sustituirlo en las funciones
    override fun onClickButton() {
        Toast.makeText(this,"El profesional ha sido registrado", Toast.LENGTH_SHORT).show()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, ProfesionalRegistrado())
            .addToBackStack(null)
            .commit()

    }


}