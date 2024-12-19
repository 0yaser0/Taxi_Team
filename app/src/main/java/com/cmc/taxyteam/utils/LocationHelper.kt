package com.cmc.taxyteam.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.*

class LocationHelper(context: Context) {
    private val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val locationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = 5000
        fastestInterval = 2000
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { onLocationReceived?.invoke(it) }
        }
    }

    private var onLocationReceived: ((Location?) -> Unit)? = null

    /**
     * Gets the last known location or starts location updates if null.
     */
    @SuppressLint("MissingPermission")
    fun getCurrentLocation(onLocationReceived: (Location?) -> Unit) {
        this.onLocationReceived = onLocationReceived

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                onLocationReceived(location)
            } else {
                startLocationUpdates()
            }
            Log.d("LocationHelper", "Location received: $location")
        }.addOnFailureListener {
            onLocationReceived(null)
            Log.e("LocationHelper", "Error getting location: ${it.message}")
        }
    }

    /**
     * Starts location updates to fetch the current location.
     */
    @SuppressLint("MissingPermission")
     fun startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    /**
     * Stops location updates when no longer needed.
     */
    fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}