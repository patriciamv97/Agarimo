package com.example.agarimo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agarimo.adapter.ProfesionalAdapter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.agarimo.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.CircleOptions
import kotlin.math.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding


    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val intent= Intent(this, MapsActivity::class.java)





    }

    override fun onRestart() {
        super.onRestart()
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap = googleMap
        //Hago visible los botones para apliar y desampliar el mapa
        /// mMap.uiSettings.isZoomControlsEnabled = true

        radioMaximo(ListaProfesionales.listaProfesionales)
        mMap.setOnMyLocationButtonClickListener(this)
        mMap.setOnMyLocationClickListener(this)
        //Cuando se  ha cargado el mapa le decimos que activa la localización
        enableLocation()


        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    Toast.makeText(this, "boton clikado", Toast.LENGTH_SHORT).show()
                    val recyclerView = findViewById<RecyclerView>(R.id.recyclerProfesional)
                    recyclerView.visibility = View.GONE
                    true
                }
                R.id.page_2 -> {
                    Toast.makeText(this, "boton clikado", Toast.LENGTH_SHORT).show()
                    val recyclerView = findViewById<RecyclerView>(R.id.recyclerProfesional)
                    recyclerView.layoutManager= LinearLayoutManager(this)
                    recyclerView.adapter= ProfesionalAdapter(ListaProfesionales.listaProfesionales)
                    //Crear un recicler view con la lista de profesionales de la base de datos
                    recyclerView.visibility = View.VISIBLE
                    true
                }
                else -> false
            }
        }



    }

    private fun createMarker() {
        val vigo = LatLng(42.23282, -8.72264)
        mMap.addMarker(MarkerOptions().position(vigo).title("Vigo"))
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(vigo, 18f),
            4000,
            null
        )
    }

    private fun marcaProfesional(listaProfesionales: ArrayList<ProfesionalesBd>) {
        listaProfesionales.forEach { profesional ->
            val lt = profesional.lt ?: 0.0
            val lg = profesional.lg ?: 0.0
            val nombre = profesional.nombre
            val ubicacion = LatLng(lt, lg)
            val marca = mMap.addCircle(
                CircleOptions().center(ubicacion).radius(500.0).strokeColor(Color.CYAN)
                    .fillColor(Color.TRANSPARENT).clickable(true)

            )
            mMap.setOnCircleClickListener {
                Toast.makeText(this, "$nombre", Toast.LENGTH_SHORT).show()
            }
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(ubicacion, 18f),
                4000,
                null
            )
        }
    }
    private fun radioMaximo(listaProfesionales: ArrayList<ProfesionalesBd>){
        val bundle = intent.extras
        val latitud = bundle?.getDouble("latitud") ?: 0.0
        val longitud = bundle?.getDouble("longitud") ?: 0.0
        listaProfesionales.forEach { profesional ->
            val lt = profesional.lt ?: 0.0
            val lg = profesional.lg ?: 0.0
            //Variación de longitud y latitud en radianes
            val ALT = radianes(lt.minus(latitud))
            val ALG = radianes(lg.minus(longitud))
            var a = sin(ALT/2).pow(2) + cos(radianes(latitud))* cos(radianes(lt))*sin(ALG/2).pow(2)
            var c = 2 * atan2(sqrt(a), sqrt(1-a))
            val distancia = 6378 * c // Radio ecuatorial de la tierra 6738
            // Formula de Harsevine para el calculo de distancia entre coordenadas geograficas
            Log.d("distancia", ""+distancia)
            if (distancia<20000){
                marcaProfesional(listaProfesionales)
            }

        }
    }
    private  fun radianes (valor : Double) = (PI/180)*valor


    /**
     * Método que comprueba que el permiso este activado, pidiendo el permiso y viendo si es igual a el PERMISSION_GRANTED
     */
    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED


    //SupressLint es una interfaz que indica que se deben ignoar las advertencias especificadas

    /**
     * Método que intenta activar la localización
     */
    @SuppressLint("MissingPermission")
    private fun enableLocation() {
        //Si el mapa no está inicializado vete
        if (!::mMap.isInitialized) return
        if (isLocationPermissionGranted()) {
            //si  ha aceptado los permisos permitimos la localización
            mMap.isMyLocationEnabled = true
        } else {
            //no está activado el permiso , se lo tenemos que pedir a través
            // del siguiente método
            requestLocationPermission()
        }
    }

    /**
     * Método para pedir al usuario que active los permisos
     */
    private fun requestLocationPermission() {
        //Si cumple el primer if significa que le hemos pedido ya al usuario los permisos y los ha rechazado
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            Toast.makeText(this, "Ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()

        } else {
            //Si entra por el else es la primera vez que le pedimos los permisos
            //Se le pasa el companion object para saber si ha aceptado los permisos
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION
            )
        }
    }

    /**
     * Método que captura la respuesta del usuario si acepta los permisos
     */
    @SuppressLint("MissingSuperCall", "MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        // cuando el requestcode se igual al companion object definido
        when (requestCode) {
            // Si grantResults no está vacio y el permiso 0 está aceptado signfica que ha acptado nuestros permisos
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.isMyLocationEnabled = true
            } else {
                Toast.makeText(
                    this,
                    "Para activar la localización ve a ajustes y acepta los permisos",
                    Toast.LENGTH_SHORT
                ).show()

            }
            //Por si ha aceptado otro permiso
            else -> {
            }
        }
    }

    /**
     * Método que sirve para comprobar si los permisos siguen activos caundo el usuario
     * se va de la aplicación y vuelve para que la aplicación no rompa
     */
    override fun onResumeFragments() {
        super.onResumeFragments()
        if (!::mMap.isInitialized) return
        if (!isLocationPermissionGranted()) {
            !mMap.isMyLocationEnabled
            Toast.makeText(this, "Ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()


        }
    }

    /**
     * Método que sirve para que cuando el usuario pulse el botón OnMyLocation
     * le lleve a la ubicación
     */
    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "Boton pulsado", Toast.LENGTH_SHORT).show()
        return false
    }

    /**
     * Método que muestra la latitud y longitud de nuestra ubicación cuando pulsamos sobre ella
     */
    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(this, "Estás en ${p0.latitude},${p0.longitude} ", Toast.LENGTH_SHORT).show()
    }
}