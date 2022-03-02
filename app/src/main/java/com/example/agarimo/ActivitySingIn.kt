package com.example.agarimo

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ActivitySingIn : AppCompatActivity() , InicioSesion.CallBack{
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singin)
        auth = Firebase.auth
        loadFragment(InicioSesion())


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
    fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer2, fragment)
            .commit()

    }
    private fun updateUI(user: FirebaseUser?, userType: String) {
        Log.d("estado", "" + auth.currentUser?.uid)
        if (userType == "Cliente"){
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)


        }else{
           loadFragment(ProfesionalRegistrado())

        }
    }

    /*
     Un companion object es un objeto que es común a todas las instancias de esa clase. Vendría a ser similar a los campos estáticos en Java.
     En este caso creamos el objeto TAG al que llamaremos en los Log de control para saber si se ha iniciado sesión correctamente
     */
    companion object {
        private const val TAG = "EmailPassword"
    }

    override fun onClickButton() {

        var email = findViewById<EditText>(R.id.editTextTextEmailAddress)
        var password = findViewById<EditText>(R.id.editTextTextPassword)
        val bundle = intent.extras
        val userType = bundle?.get("USUARIO")
        val usuario : String = userType.toString()

        signIn(email.text.toString(),password.text.toString(),usuario)
    }


}
