package com.cmc.mytaxi

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cmc.mytaxi.utils.NotificationHelper
import com.cmc.mytaxi.utils.PermissionsHelper
import com.cmc.mytaxi.viewmodel.MainViewModel
import com.cmc.mytaxi.viewmodel.MainViewModelFactory
import com.vmadalin.easypermissions.EasyPermissions

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var notificationHelper: NotificationHelper
    private var isRideActive: Boolean = false
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval: Long = 5000 // 5 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val factory = MainViewModelFactory(this)
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
        notificationHelper = NotificationHelper(this)

        val btnToggleRide = findViewById<Button>(R.id.btnToggleRide)

        btnToggleRide.setOnClickListener {
            if (!PermissionsHelper.hasLocationPermission(this)) {
                PermissionsHelper.requestLocationPermission(this)
                return@setOnClickListener
            }
            if (!isRideActive) {
                startRide()
                btnToggleRide.text = "End Ride"
                isRideActive = true
            } else {
                endRide()
                btnToggleRide.text = "Start Ride"
                isRideActive = false
            }
        }

        viewModel.rideData.observe(this, Observer { ride ->
            // Update UI with the latest ride details
            findViewById<TextView>(R.id.tvDistance).text = "Distance: ${ride.distance} km"
            findViewById<TextView>(R.id.tvTimeElapsed).text = "Time: ${ride.timeElapsed} min"
            findViewById<TextView>(R.id.tvTotalFare).text = "Fare: ${ride.totalFare} DH"
        })
    }

    private fun startRide() {
        viewModel.startRide()
        handler.postDelayed(updateRideTask, updateInterval)
    }

    private fun endRide() {
        handler.removeCallbacks(updateRideTask)
        viewModel.rideData.value?.let { ride ->
            notificationHelper.sendFareNotification(
                ride.totalFare, ride.distance, ride.timeElapsed
            )
        }
    }

    private val updateRideTask = object : Runnable {
        override fun run() {
            if (isRideActive) {
                viewModel.updateLocation()
                handler.postDelayed(this, updateInterval)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (EasyPermissions.somePermissionPermanentlyDenied(this, permissions.toList())) {
            PermissionsHelper.showSettingsDialog(this)
        } else if (PermissionsHelper.hasLocationPermission(this)) {
            Toast.makeText(this, "Permission granted. You can now start the ride.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Permission denied. Cannot start the ride.", Toast.LENGTH_SHORT).show()
        }
    }
}