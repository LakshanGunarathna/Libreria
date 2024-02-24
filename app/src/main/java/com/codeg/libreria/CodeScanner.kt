package com.codeg.libreria

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import java.util.Locale

class CodeScanner(private val activity: Activity) {

    private var callback: ((String) -> Unit)? = null
    private var errorCallback: ((String) -> Unit)? = null

    fun startScanning(callback: (String) -> Unit, errorCallback: (String) -> Unit) {
        this.callback = callback
        this.errorCallback = errorCallback

        // Check for camera permission
        if (ContextCompat.checkSelfPermission(
                activity,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Camera permission is already granted, start barcode scanning
            startBarcodeScanning()
        } else {
            // Request camera permission
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        }
    }

    // Add this method to handle the result of the permission request
    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Camera permission granted, start barcode scanning
                    startBarcodeScanning()
                } else {
                    // Camera permission denied, handle accordingly (e.g., show a message)
                    errorCallback?.invoke("Camera permission denied.")
                }
            }
        }
    }

    // Modify startBarcodeScanning to be private
    private fun startBarcodeScanning() {
        val optionsBuilder = GmsBarcodeScannerOptions.Builder()
            .enableAutoZoom() // Auto zoom as the default option

        val gmsBarcodeScanner =
            GmsBarcodeScanning.getClient(activity, optionsBuilder.build())
        gmsBarcodeScanner
            .startScan()
            .addOnSuccessListener { barcode: Barcode ->
                callback?.invoke(getSuccessfulMessage(barcode))
            }
            .addOnFailureListener { e: Exception -> errorCallback?.invoke(getErrorMessage(e)) }
            .addOnCanceledListener {
                errorCallback?.invoke("Scanner cancelled.")
            }
    }


    private fun getSuccessfulMessage(barcode: Barcode): String {
        return String.format(
            Locale.US,
            "%s",
            barcode.displayValue
        )
    }

    private fun getErrorMessage(e: Exception): String {
        return if (e is MlKitException) {
            when (e.errorCode) {
                MlKitException.CODE_SCANNER_CAMERA_PERMISSION_NOT_GRANTED ->
                    "Camera permission not granted."
                MlKitException.CODE_SCANNER_APP_NAME_UNAVAILABLE ->
                    "App name unavailable."
                else -> "Error: $e"
            }
        } else {
            "Error: ${e.message}"
        }
    }

    companion object {
        const val CAMERA_PERMISSION_REQUEST_CODE = 123 // Any unique integer value
    }
}
