package com.example.agarimo

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.agarimo.databinding.ActivitySinginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ActivitySingIn : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySinginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySinginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        val bundle = intent.extras
        val userType = bundle?.get("USUARIO")
        val usuario : String = userType.toString()

        binding.btnSingIn.setOnClickListener {

            signIn(binding.editTextTextEmailAddress.text.toString(), binding.editTextTextPassword.text.toString(), usuario )
        }
    }


    private fun signIn(email: String, password: String , userType: String) {
        /*
          Si el inicio de sesión del usuario  se realiza correctamente
          el listener del estado de autenticación será notificado y la lógica para manejar el
          el usuario que ha iniciado sesión se puede resuelve en el listener.
          */
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //Logs de control
                    Log.d(TAG, "signInWithEmail:success")
                    Log.d("estado", "inicio de sesión correcto")
                    //Asignas a la variable user el usuario actual para después pasarselo a método updateUI
                    val user = auth.currentUser
                    //Se llama al método que actualizará el layout cuando se haya iniciado sesión y se le pasa el usuario
                    updateUI(user, userType)
                    finish()
                } else {
                    //  Si el inicio de sesión falla, muestra un mensaje al usuario
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT)
                        .show()
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Log.d("estado", "No se puedo iniciar sesion")
                }
            }

    }
    /**
     * Método que sirve para actualizar el layout cuando inicias sesión o te registras
     * Para que funcione deberias crear otras activities y que se cargasen cuando el registro
     * o el inicio fuesen correctos y te permitiesen acceder a las funcionalidades de la app
     */

    private fun updateUI(user: FirebaseUser?, userType: String) {
        Log.d("estado", "" + auth.currentUser?.uid)
        if (userType == "Cliente"){
            // FrameCliente
        }else{
            // FrameProfesional

        }
    }

    /*
     Un companion object es un objeto que es común a todas las instancias de esa clase. Vendría a ser similar a los campos estáticos en Java.
     En este caso creamos el objeto TAG al que llamaremos en los Log de control para saber si se ha iniciado sesión correctamente
     */
    companion object {
        private const val TAG = "EmailPassword"
    }


}
