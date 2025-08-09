package com.example.kioskapp.settings

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.example.kioskapp.core.Prefs
import com.example.kioskapp.databinding.ActivitySettingsBinding
import kotlinx.coroutines.launch

/**
 * Simple settings screen that allows the user to configure video/image files,
 * trigger type and behaviour flags.  Uses the Storage Access Framework to
 * select files and Jetpack DataStore to persist preferences.  Values are
 * loaded asynchronously and the UI is updated accordingly.
 */
import android.widget.CheckBox
import android.widget.EditText
class SettingsActivity : ComponentActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private val prefs = Prefs
    private val openVideo = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        if (uri != null) {
            lifecycleScope.launch { prefs.setValue(this@SettingsActivity, Prefs.VIDEO_URI, uri.toString()) }
            binding.btnSelectVideo.text = uri.lastPathSegment
        }
    }
    private val openImage = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        if (uri != null) {
            lifecycleScope.launch { prefs.setValue(this@SettingsActivity, Prefs.IMAGE_URI, uri.toString()) }
            binding.btnSelectImage.text = uri.lastPathSegment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Populate spinner with TriggerType values
        val types = Prefs.TriggerType.values().map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinnerTrigger.adapter = adapter

        // Load current values into UI
        lifecycleScope.launch {
            prefs.videoUriFlow(this@SettingsActivity).collect { uri ->
                if (uri != null) binding.btnSelectVideo.text = Uri.parse(uri).lastPathSegment
            }
        }
        lifecycleScope.launch {
            prefs.imageUriFlow(this@SettingsActivity).collect { uri ->
                if (uri != null) binding.btnSelectImage.text = Uri.parse(uri).lastPathSegment
            }
        }
        lifecycleScope.launch {
            prefs.loopVideoFlow(this@SettingsActivity).collect { binding.switchLoopVideo.isChecked = it }
        }
        lifecycleScope.launch {
            prefs.muteFlow(this@SettingsActivity).collect { binding.switchMuteVideo.isChecked = it }
        }
        lifecycleScope.launch {
            prefs.autostartFlow(this@SettingsActivity).collect { binding.switchAutostart.isChecked = it }
        }
        lifecycleScope.launch {
            prefs.kioskFlow(this@SettingsActivity).collect { binding.switchKiosk.isChecked = it }
        }
        lifecycleScope.launch {
            prefs.debounceMsFlow(this@SettingsActivity).collect { binding.editDebounce.setText(it.toString()) }
        }
        lifecycleScope.launch {
            prefs.triggerTypeFlow(this@SettingsActivity).collect { type ->
                val position = types.indexOf(type)
                if (position >= 0) binding.spinnerTrigger.setSelection(position)
            }
        }

        // Register button listeners
        binding.btnSelectVideo.setOnClickListener {
            openVideo.launch(arrayOf("video/*"))
        }
        binding.btnSelectImage.setOnClickListener {
            openImage.launch(arrayOf("image/*"))
        }
        binding.btnSave.setOnClickListener {
            savePreferences()
        }
    }

    private fun savePreferences() {
        saveExtraPrefs()
        // PATCH: save HTTP/BLE prefs
        lifecycleScope.launch {
            prefs.setValue(this@SettingsActivity, Prefs.LOOP_VIDEO, binding.switchLoopVideo.isChecked)
            prefs.setValue(this@SettingsActivity, Prefs.MUTE, binding.switchMuteVideo.isChecked)
            prefs.setValue(this@SettingsActivity, Prefs.AUTOSTART, binding.switchAutostart.isChecked)
            prefs.setValue(this@SettingsActivity, Prefs.KIOSK, binding.switchKiosk.isChecked)
            val debounce = binding.editDebounce.text.toString().toIntOrNull() ?: 200
            prefs.setValue(this@SettingsActivity, Prefs.DEBOUNCE_MS, debounce)
            val trigger = binding.spinnerTrigger.selectedItem as String
            prefs.setValue(this@SettingsActivity, Prefs.TRIGGER_TYPE, trigger)
            finish()
        }
    }
}

// PATCH: ask user to disable battery optimizations and start TriggerService
override fun onStart() {
    super.onStart()
    com.example.kioskapp.util.BatteryUtils.requestIgnoreBatteryOptimizations(this)
    try {
        val intent = android.content.Intent(this, com.example.kioskapp.service.TriggerService::class.java)
        if (android.os.Build.VERSION.SDK_INT >= 26) startForegroundService(intent) else startService(intent)
    } catch (_: Throwable) {}
}


// PATCH helpers
private fun bindExtraViews() {
    // nothing to do when using view binding, fields already available
}

private fun loadExtraPrefs() {
    lifecycleScope.launch {
        val port = prefs.getInt(this@SettingsActivity, Prefs.HTTP_PORT, 8765)
        binding.editHttpPort.setText(port.toString())
        binding.checkEnableHttp.isChecked = prefs.getBool(this@SettingsActivity, Prefs.ENABLE_HTTP, true)
        binding.checkEnableBle.isChecked = prefs.getBool(this@SettingsActivity, Prefs.ENABLE_BLE, false)
        binding.editBleName.setText(prefs.getString(this@SettingsActivity, Prefs.BLE_DEVICE_NAME, ""))
        binding.editBleService.setText(prefs.getString(this@SettingsActivity, Prefs.BLE_SERVICE_UUID, ""))
        binding.editBleChar.setText(prefs.getString(this@SettingsActivity, Prefs.BLE_CHAR_UUID, ""))
    }
}

private fun saveExtraPrefs() {
    lifecycleScope.launch {
        val port = binding.editHttpPort.text.toString().toIntOrNull() ?: 8765
        prefs.setValue(this@SettingsActivity, Prefs.HTTP_PORT, port)
        prefs.setValue(this@SettingsActivity, Prefs.ENABLE_HTTP, binding.checkEnableHttp.isChecked)
        prefs.setValue(this@SettingsActivity, Prefs.ENABLE_BLE, binding.checkEnableBle.isChecked)
        prefs.setValue(this@SettingsActivity, Prefs.BLE_DEVICE_NAME, binding.editBleName.text.toString())
        prefs.setValue(this@SettingsActivity, Prefs.BLE_SERVICE_UUID, binding.editBleService.text.toString())
        prefs.setValue(this@SettingsActivity, Prefs.BLE_CHAR_UUID, binding.editBleChar.text.toString())
    }
}
