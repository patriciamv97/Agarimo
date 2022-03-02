package com.example.agarimo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.agarimo.databases.Clientes
import com.example.agarimo.databases.Profesionales
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ActivitySignUp : AppCompatActivity(), Registro.CallBack {
    // Para obtener la localización
    //TODO() Obtener la localizacion en la ActivtyMain
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    var PERMISION_ID = 1000

    // Para la base de datos
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private val TAG = "Real Time"
    lateinit var email: String
    lateinit var password: String
    lateinit var latitud : String
    lateinit var longitud : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        loadFragment(Registro())
        auth = Firebase.auth
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()



    }

    private fun createAccount(email: String, password: String) {
        /*
            Si el registro del usuario  se realiza correctamente
            el listener del estado de autenticación será notificado y la lógica para manejar el
            el usuario que se ha registrado se puede resuelve en el listener.
        */
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //Logs de control
                    Log.d(TAG, "createUserWithEmail:success")
                    Log.d("estado", "usuario registrado")
                    //Asignas a la variable user el usuario actual  para después pasarselo a método updateUI
                    val user = auth.currentUser
                } else {
                    //  Si el registro falla, muestra un mensaje al usuario
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    //Logs de control
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Log.d("estado", "usuario NO registrado")

                }
            }
    }

    fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()

    }

    fun writeNewDataProfesional(uid: String, nombre: String, lt: Double, lg: Double) {
        Log.d(TAG, "Escribiendo datos")
        val profesional = Profesionales(nombre, lt, lg)
        database.child(uid).setValue(profesional)
    }
    fun writeNewDataCliente(uid: String, nombre: String, email: String, password: String) {
        Log.d(TAG, "Escribiendo datos")
        val cliente = Clientes(nombre, email, password)
        database.child(uid).setValue(cliente)
    }

    override fun onClickButton() {
        val bundle = intent.extras
        val userType = bundle?.get("USUARIO")


        Log.d("tipo", "" + userType.toString())
        val nombre = findViewById<EditText>(R.id.etCubrirNombre)
        val email = findViewById<EditText>(R.id.etCubrirEmail)
        val password = findViewById<EditText>(R.id.etCubirPassword)

        Toast.makeText(this, "El usuario ha sido registrado", Toast.LENGTH_SHORT).show()
        loadFragment(ProfesionalRegistrado())
        if (userType == "Cliente") {
            createAccount(email.text.toString(), password.text.toString())
            val uid : String? = auth.currentUser?.uid
            database = Firebase.database.getReference("clientes")
            if(uid!=null) {
                writeNewDataCliente(uid, nombre.text.toString(),email.text.toString(),password.text.toString())
                Toast.makeText(this,"Cliente registrado en la bd", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"No se registro el cliente",Toast.LENGTH_SHORT).show()
            }
            /*
               Hay que crear la activity principal que mostrará el mapa y los profesionales y así dotar de sentido y funcionalidad a la app
               e implementarla aquí.

            */
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)

        } else {
            createAccount(email.text.toString(), password.text.toString())
            val uid : String? = auth.currentUser?.uid
            database = Firebase.database.getReference("profesionales")
            Log.d("Coordenadas","lg"+longitud+" lt"+latitud)
            Log.d("ui",""+uid)
            if (uid != null && latitud !=null && longitud !=null) {
                writeNewDataProfesional(uid, nombre.text.toString(), latitud.toDouble(), longitud.toDouble())
                Toast.makeText(this,"Profesional registrado en la bd", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"No se registro el profesional",Toast.LENGTH_SHORT).show()
            }
            loadFragment(ProfesionalRegistrado())


        }

    }
    @SuppressLint("MissingPermission")
    private fun getLastLocation(){
        if (checkPermissions()){
            if (isLocationEnable()){
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    var location : Location? = task.result
                    if (location==null){
                        getNewLoaction()

                    }else{
                        // localizacion.text="Tus coordenadas actuales son:\nLat:"+location.latitude+";Log: "+location.longitude
                        latitud = location.latitude.toString()
                        longitud = location.longitude.toString()
                    }
                }

            }else{
                Toast.makeText(this,"Tienes que dar el permiso de ubicacion", Toast.LENGTH_SHORT).show()
            }

        }else{
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getNewLoaction(){
        locationRequest = com.google.android.gms.location.LocationRequest()
        locationRequest.priority= LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval=0
        locationRequest.numUpdates=2
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest,locationCallback, Looper.myLooper()
        )

    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            var lastLocation: Location = p0.lastLocation
            //localizacion.text="Tus coordenadas actuales son:\nLat:"+lastLocation.latitude+";Log: "+lastLocation.longitude
            latitud = lastLocation.latitude.toString()
            longitud = lastLocation.longitude.toString()
        }
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            PERMISION_ID
        )
    }

    private fun isLocationEnable(): Boolean {

        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Debug: ", "tienes el permiso")
            }
        }
    }




}