package com.example.agarimo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewParent
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.agarimo.databinding.ActivitySignUpBinding

class ActivitySignUp : AppCompatActivity() {

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
        }else{
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, RegistroProfesional())
                .commit()
        }
    }



}