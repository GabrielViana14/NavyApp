package com.fatec_gab_viana.navy

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentContainer
import com.fatec_gab_viana.navy.models.Filial
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class Mapa : Fragment() , OnMapReadyCallback {

    private lateinit var mapView: MapView
    private var mMap: GoogleMap? = null
    private lateinit var databaseReference: DatabaseReference
    private val REQUEST_CODE_LOCATION = 123
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?):View? {
        val rootView = inflater.inflate(R.layout.fragment_mapa, container, false)
        mapView = rootView.findViewById(R.id.mapview)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        return rootView
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        if (checkLocationPermission()){
            obterLocalizacaoAtual()
        } else{
            val fatec = LatLng(-23.663372, -46.459810)
            mMap?.addMarker(MarkerOptions().position(fatec).title("Marcador na fatec"))
            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(fatec,15f))
        }


        mMap?.uiSettings?.apply {
            isZoomControlsEnabled = true
            isZoomGesturesEnabled = true
            isScrollGesturesEnabled = true
            isMyLocationButtonEnabled = true

        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Filial")
        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                p0.clear()

                for (snapshot in snapshot.children){
                    val local = snapshot.getValue(Filial::class.java)
                    val icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_map)
                    if (local!=null){
                        val markerOptions = MarkerOptions()
                            .position(LatLng(local.latitude,local.longitude))
                            .title(local.nome)
                            .icon(icon)
                            .snippet("${local.rua}, ${local.numero} - ${local.bairro} -" +
                                    " ${local.cidade} ")
                            .infoWindowAnchor(0.5f, 0.5f)
                        val marker = p0.addMarker(markerOptions)

                        marker?.tag = local


                    }

                }
                mMap?.setOnInfoWindowClickListener { marker ->
                    val local = marker.tag as? Filial
                    val intent = Intent(requireContext(), PesquisaFiltrada::class.java)
                    intent.putExtra("filtro", local?.nome.toString())
                    startActivity(intent)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    private fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Se a permissão não foi concedida, solicite-a
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION
            )
            return false
        } else {
            // A permissão já foi concedida, obtenha a localização atual
            requestLocation()
            return true
        }
    }

    private fun requestLocation() {
        // Verificar novamente a permissão, mesmo que já tenha sido solicitada antes
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Obter a localização atual
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    // A localização foi obtida com sucesso
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        // Faça o que quiser com as coordenadas (latitude e longitude) aqui
                    }
                }
        }
    }

    // Chamado quando o resultado da solicitação de permissão é disponibilizado
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão concedida, obtenha a localização atual
                requestLocation()
            } else {
                // Permissão negada, trate de acordo
            }
        }
    }

    private fun obterLocalizacaoAtual() {
        // Verifica novamente a permissão, mesmo que já tenha sido solicitada antes
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Obter a localização atual
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    // A localização foi obtida com sucesso
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude

                        // Adiciona um marcador para a localização atual
                        val localAtual = LatLng(latitude, longitude)
                        //mMap?.addMarker(MarkerOptions().position(localAtual).title("Minha Localização").icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map)))
                        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(localAtual, 15f))
                        val markerOptions = MarkerOptions()
                            .position(localAtual)
                            .title("Local atual")
                        mMap?.addMarker(markerOptions)
                    }
                }
        }
    }

}