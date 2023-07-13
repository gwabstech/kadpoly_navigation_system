package com.gwabs.kadpoly_navigation_system

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.lang.ref.WeakReference

class LocationPermissionHelper(activity: WeakReference<AppCompatActivity>) {

    private val activityRef: WeakReference<AppCompatActivity> = activity

    fun checkPermissions(onPermissionGranted: () -> Unit) {
        val activity = activityRef.get() ?: return

        val context = activity.applicationContext
        val fineLocationPermission = Manifest.permission.ACCESS_FINE_LOCATION

        if (ContextCompat.checkSelfPermission(context, fineLocationPermission) == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted
            onPermissionGranted.invoke()
        } else {
            // Request permission using Dexter
            Dexter.withActivity(activity)
                .withPermissions(fineLocationPermission)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            // All permissions granted
                            onPermissionGranted.invoke()
                        } else {
                            // Some or all permissions denied
                            // Handle the permission denied scenario
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?
                    ) {
                        // Handle the permission rationale scenario
                        token?.continuePermissionRequest()
                    }
                })
                .check()
        }
    }
}
