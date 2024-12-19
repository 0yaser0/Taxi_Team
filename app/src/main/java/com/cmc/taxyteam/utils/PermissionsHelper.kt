package com.cmc.taxyteam.utils

import android.content.Context
import android.content.Intent

import android.net.Uri
import android.provider.Settings
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import pub.devrel.easypermissions.EasyPermissions


object PermissionsHelper {
    private const val LOCATION_PERMISSION_CODE = 100
    const val LOCATION_REQUEST_CODE = 101

    fun hasLocationPermission(context: Context): Boolean {
        return EasyPermissions.hasPermissions(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    fun requestLocationPermission(activity: FragmentActivity) {
        EasyPermissions.requestPermissions(
            activity,
            "This app needs access to your location to track rides.",
            LOCATION_PERMISSION_CODE,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    fun showSettingsDialog(context: Context) {
        android.app.AlertDialog.Builder(context)
            .setTitle("Permissions Required")
            .setMessage("Location permission is necessary for the app to function. Please enable it in the app settings.")
            .setPositiveButton("Go to Settings") { _, _ ->
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", context.packageName, null)
                )
                context.startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    fun checkAndPromptLocationServices(
        context: Context,
        onLocationSettingsSatisfied: () -> Unit,
        onResolutionRequired: (ResolvableApiException) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val settingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()

        val settingsClient = LocationServices.getSettingsClient(context)
        settingsClient.checkLocationSettings(settingsRequest)
            .addOnSuccessListener {
                onLocationSettingsSatisfied()
            }
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    onResolutionRequired(exception)
                } else {
                    onFailure(exception)
                }
            }
    }
}
