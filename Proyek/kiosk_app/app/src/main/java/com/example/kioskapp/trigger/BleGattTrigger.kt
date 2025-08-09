package com.example.kioskapp.trigger

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Build
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

/**
 * Trigger that connects as a BLE GATT client to a peripheral device.  The
 * peripheral should advertise a service identified by [serviceUuid] and a
 * characteristic [charUuid].  Notifications are subscribed to on that
 * characteristic and the trigger state becomes ON when the characteristic
 * value is `1` and OFF when it is `0`.  Scanning is restarted on error.
 */
class BleGattTrigger(
    private val context: Context,
    private val serviceUuid: UUID,
    private val charUuid: UUID
) : TriggerSource {
    private val _events = MutableStateFlow(false)
    override val events: StateFlow<Boolean> get() = _events.asStateFlow()

    private var gatt: BluetoothGatt? = null
    private var scanning = false

    override fun start() {
        // Start BLE scanning for the service.  Requires location permission on
        // Android 11 and earlier but this example does not handle permission
        // requests for brevity.
        if (scanning) return
        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val adapter = manager.adapter ?: return
        val scanner = adapter.bluetoothLeScanner ?: return
        scanning = true
        scanner.startScan(scanCallback)
    }

    override fun stop() {
        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val adapter = manager.adapter ?: return
        val scanner = adapter.bluetoothLeScanner ?: return
        scanning = false
        scanner.stopScan(scanCallback)
        gatt?.close()
        gatt = null
    }

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device = result.device
            // Attempt to connect once a device is found.  Production code
            // should verify the advertised services match serviceUuid.
            connect(device)
            stopScanning()
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e("BleGattTrigger", "BLE scan failed: $errorCode")
        }
    }

    private fun stopScanning() {
        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val adapter = manager.adapter ?: return
        val scanner = adapter.bluetoothLeScanner ?: return
        if (scanning) {
            scanning = false
            scanner.stopScan(scanCallback)
        }
    }

    private fun connect(device: BluetoothDevice) {
        gatt = device.connectGatt(context, false, object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                if (newState == BluetoothGatt.STATE_CONNECTED) {
                    Log.d("BleGattTrigger", "Connected to GATT server")
                    gatt.discoverServices()
                } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                    Log.d("BleGattTrigger", "Disconnected from GATT server")
                    this@BleGattTrigger.gatt?.close()
                    this@BleGattTrigger.gatt = null
                    // Restart scanning to reconnect
                    start()
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                val service: BluetoothGattService? = gatt.getService(serviceUuid)
                val characteristic: BluetoothGattCharacteristic? = service?.getCharacteristic(charUuid)
                if (characteristic != null) {
                    // Enable notifications on the characteristic
                    gatt.setCharacteristicNotification(characteristic, true)
                    val descriptor = characteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"))
                    descriptor?.let {
                        it.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        gatt.writeDescriptor(it)
                    }
                }
            }

            override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
                if (characteristic.uuid == charUuid) {
                    val value = characteristic.value
                    if (value != null && value.isNotEmpty()) {
                        _events.value = value[0].toInt() != 0
                    }
                }
            }
        })
    }
}