package com.empreendapp.collev.listeners

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.os.Looper
import android.location.LocationManager
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import androidx.core.app.ActivityCompat
import com.empreendapp.collev.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception


class MyLocalizationListener(val ctx: Context, var googleMap: GoogleMap): LocationListener {
    private val lm: LocationManager
    private var location: Location? = null

    companion object {
        protected val TAG: String? = null
        private const val UM_SEGUNDO = 1000
    }

    init {
        lm = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    @Volatile
    private var stop = false
    private val tempoTotalBusca = 10
    protected var progressDialog: ProgressDialog? = null
    fun estado(): Boolean {
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun capturarCoordenadaGPS(): Location? {
        try {
            Thread {
                Looper.myLooper()
                Looper.prepare()
                progressDialog = ProgressDialog.show(
                    ctx, null,
                    ctx.getString(R.string.aguarde),
                    true
                )
                ativaGPS()
                Looper.loop()
            }.start()
            // Thread.sleep(10*1000);
            var tempoBusca = 0
            while (!stop) {
                if (tempoTotalBusca == tempoBusca) {
                    break
                }
                Thread.sleep(UM_SEGUNDO.toLong())
                tempoBusca++
            }
            return location
        } catch (e: Exception) {
            // TODO - Trate a exceção;
        } finally {
            desativaGPS()
            if (progressDialog != null) progressDialog!!.dismiss()
        }
        return null
    }

    private fun ativaGPS() {
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat
                .requestPermissions(
                    ctx as Activity,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    5)
            return
        }
        lm.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, 0, 0f, this,
            Looper.myLooper()
        )
        // Looper.loop();
    }

    private fun desativaGPS() {
        lm.removeUpdates(this)
    }

    override fun onLocationChanged(p0: Location) {
        this.location = location
        stop = true

        val latLng= LatLng(location!!.latitude, location!!.longitude)
        val markerOptions: MarkerOptions = MarkerOptions().position(latLng).title("Localização Atual")

        // moving camera and zoom map
        val zoomLevel = 19.0f //This goes up to 21

        googleMap.let {
            it!!.addMarker(markerOptions)
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))
        }
    }

    override fun onProviderDisabled(provider: String) {
        // Provider desabilitado
    }

    override fun onProviderEnabled(provider: String) {
        // Provider habilitado
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        // Status do provider alterado
    }
}