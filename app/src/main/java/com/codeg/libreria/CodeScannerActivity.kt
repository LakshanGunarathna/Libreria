package com.codeg.libreria

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import java.util.Locale

class CodeScannerActivity : AppCompatActivity() {

    private var allowManualInput = false
    private var enableAutoZoom = false
    private var barcodeResultView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_codescanner)
        barcodeResultView = findViewById(R.id.barcode_result_view)
    }

    fun onAllowManualInputCheckboxClicked(view: View) {
        allowManualInput = (view as CheckBox).isChecked
    }

    fun onEnableAutoZoomCheckboxClicked(view: View) {
        enableAutoZoom = (view as CheckBox).isChecked
    }

    fun onScanButtonClicked(view: View) {
        // Check for camera permission
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Camera permission is already granted, start barcode scanning
            startBarcodeScanning()
        } else {
            // Request camera permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun startBarcodeScanning() {
        val optionsBuilder = GmsBarcodeScannerOptions.Builder()
        if (allowManualInput) {
            optionsBuilder.allowManualInput()
        }
        if (enableAutoZoom) {
            optionsBuilder.enableAutoZoom()
        }
        val gmsBarcodeScanner = GmsBarcodeScanning.getClient(this, optionsBuilder.build())
        gmsBarcodeScanner
            .startScan()
            .addOnSuccessListener { barcode: Barcode ->
                barcodeResultView!!.text = getSuccessfulMessage(barcode)
            }
            .addOnFailureListener { e: Exception -> barcodeResultView!!.text = getErrorMessage(e) }
            .addOnCanceledListener {
                barcodeResultView!!.text = getString(R.string.error_scanner_cancelled)
            }
    }

    // Override onRequestPermissionsResult to handle the result of the permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Camera permission granted, start barcode scanning
                    startBarcodeScanning()
                } else {
                    // Camera permission denied, handle accordingly (e.g., show a message)
                    // You might want to inform the user that the camera permission is required
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putBoolean(KEY_ALLOW_MANUAL_INPUT, allowManualInput)
        savedInstanceState.putBoolean(KEY_ENABLE_AUTO_ZOOM, enableAutoZoom)
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        allowManualInput = savedInstanceState.getBoolean(KEY_ALLOW_MANUAL_INPUT)
        enableAutoZoom = savedInstanceState.getBoolean(KEY_ENABLE_AUTO_ZOOM)
    }

    private fun getSuccessfulMessage(barcode: Barcode): String {
        val barcodeValue =
            String.format(
                Locale.US,
                "%s",
                barcode.displayValue,
                /*barcode.rawValue,
                barcode.format,
                barcode.valueType*/
            )
        return getString(R.string.barcode_result, barcodeValue)
    }

    private fun getErrorMessage(e: Exception): String? {
        return if (e is MlKitException) {
            when (e.errorCode) {
                MlKitException.CODE_SCANNER_CAMERA_PERMISSION_NOT_GRANTED ->
                    getString(R.string.error_camera_permission_not_granted)
                MlKitException.CODE_SCANNER_APP_NAME_UNAVAILABLE ->
                    getString(R.string.error_app_name_unavailable)
                else -> getString(R.string.error_default_message, e)
            }
        } else {
            e.message
        }
    }

    companion object {
        private const val KEY_ALLOW_MANUAL_INPUT = "allow_manual_input"
        private const val KEY_ENABLE_AUTO_ZOOM = "enable_auto_zoom"
        private const val CAMERA_PERMISSION_REQUEST_CODE = 123 // Any unique integer value
    }
}

