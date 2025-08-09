# Kiosk App

This project implements a **kiosk‑style tablet application** that toggles
between displaying a looping video and a static image/GIF in response to
external triggers.  It is designed for use on a 7″ Android tablet and runs
in fullscreen immersive mode.  The app is written in **Kotlin** with a
minimum SDK of 24 and uses modern Jetpack components, including
ExoPlayer for video playback, Glide for image loading and DataStore for
settings persistence.  When the trigger is ON the configured MP4 video is
played either once or in a loop; when the trigger is OFF a static image or
GIF is shown.  Multiple trigger sources are supported: USB or Bluetooth HID
devices, wired headset media buttons, an embedded HTTP webhook, BLE GATT
notifications and volume buttons as a fallback.

## Getting started

The project is organised as a standard Android Studio project using the
Gradle Kotlin DSL.  To open it in Android Studio:

1. Install the latest stable version of **Android Studio**.
2. Clone or copy the `kiosk_app` directory to your machine.
3. Open Android Studio, choose **File > Open** and select the `kiosk_app`
   folder.  Gradle will download dependencies on the first sync.
4. Connect a 7″ Android device running API 24 or higher and click **Run**.

### Permissions

On first launch the application will request permission to read external
storage so that you can pick video and image files.  If you intend to use
Bluetooth or BLE triggers additional runtime permissions may be requested by
Android depending on the API level.  Make sure to grant these so the app can
communicate with your devices.

## Usage

### Configuring media files

Tap the **gear icon** in the top‑right corner of the main screen to open the
settings.  From there you can choose a local MP4 for the splash video and
an image or GIF for the idle screen.  The file picker uses the Storage
Access Framework so you can browse the device or attached storage.  Enable
“Loop video” to repeat the video indefinitely while the trigger is ON.

### Selecting a trigger source

The **Trigger source** dropdown lets you pick which input method should
control the active/idle state.  Supported options are:

| Trigger | Behaviour |
|--------|-----------|
| `USB_HID` | Listens for volume‑up/down key codes from a keyboard connected via OTG.  Volume up sets the trigger ON, volume down sets it OFF. |
| `BT_HID` | Same as USB but reacts to keys sent by a paired Bluetooth keyboard or selfie remote. |
| `HEADSET` | Responds to media button presses on a wired headset.  Play/pause or play toggles ON, pause toggles OFF. |
| `HTTP` | Starts an embedded HTTP server on port **8765**.  POST `{"state":"on"}` to `/trigger` to turn the video on and `{"state":"off"}` to turn it off.  GET `/health` returns `200 OK`. |
| `BLE_GATT` | Acts as a BLE GATT client.  It connects to a peripheral advertising a service with UUID `00001234‑0000‑1000‑8000‑00805f9b34fb` and subscribes to notifications on characteristic `00005678‑0000‑1000‑8000‑00805f9b34fb`.  A value of `1` means ON, `0` means OFF. |
| `VOLUME` | Long‑press the device’s volume keys (up for ON, down for OFF).  Useful for testing without external hardware. |

You can also adjust the **debounce** time (in milliseconds) to filter out
rapid toggling and enforce a minimum dwell time.  Enabling **Autostart on
boot** causes the app to relaunch automatically after the device boots.
**Kiosk mode** pins the activity and prevents users from leaving the app
without a special key combination.  Finally, **Mute video** silences audio
playback while still displaying the video.

### Diagnostic screen

A basic diagnostic view is shown on the main screen when the settings
button is visible.  It displays the currently selected trigger type, the
last trigger event time and a manual toggle.  This view can be expanded
further to include logs or telemetry.

## Wiring guides

### USB HID via Pro Micro

You can simulate the trigger using an Arduino Pro Micro configured as a
USB HID keyboard.  Connect a momentary push‑button to digital pin 2 and
ground.  Upload the following sketch using the Arduino IDE:

```cpp
#include <Keyboard.h>

const int onPin = 2;
bool lastState = false;

void setup() {
    pinMode(onPin, INPUT_PULLUP);
    Keyboard.begin();
}

void loop() {
    bool pressed = digitalRead(onPin) == LOW;
    if (pressed != lastState) {
        if (pressed) {
            Keyboard.press(KEY_UP_ARROW);   // maps to volume up key
            delay(10);
            Keyboard.release(KEY_UP_ARROW);
        } else {
            Keyboard.press(KEY_DOWN_ARROW); // maps to volume down key
            delay(10);
            Keyboard.release(KEY_DOWN_ARROW);
        }
        lastState = pressed;
        delay(200);
    }
}
```

When the button is pressed the sketch sends a `KEY_UP_ARROW` (volume up) to
the tablet to activate the video.  Releasing the button sends
`KEY_DOWN_ARROW` (volume down) to deactivate it.

### ESP32 BLE HID

An ESP32 can emulate a BLE HID device or a custom BLE GATT server.  The
following Arduino sketch shows how to send notifications with value `1` or
`0` over a custom characteristic.  This corresponds to the `BLE_GATT` trigger.

```cpp
#include <BLEDevice.h>
#include <BLEServer.h>

// Replace with the same UUIDs used in the app
static BLEUUID serviceUUID("00001234-0000-1000-8000-00805f9b34fb");
static BLEUUID charUUID("00005678-0000-1000-8000-00805f9b34fb");

BLECharacteristic *pCharacteristic;
bool deviceConnected = false;

class MyServerCallbacks : public BLEServerCallbacks {
    void onConnect(BLEServer* pServer) { deviceConnected = true; }
    void onDisconnect(BLEServer* pServer) { deviceConnected = false; }
};

void setup() {
    BLEDevice::init("KioskTrigger");
    BLEServer *pServer = BLEDevice::createServer();
    pServer->setCallbacks(new MyServerCallbacks());
    BLEService *pService = pServer->createService(serviceUUID);
    pCharacteristic = pService->createCharacteristic(
        charUUID,
        BLECharacteristic::PROPERTY_NOTIFY
    );
    pCharacteristic->addDescriptor(new BLE2902());
    pService->start();
    pServer->getAdvertising()->start();
}

void loop() {
    if (deviceConnected) {
        // Example: toggle ON then OFF every two seconds
        pCharacteristic->setValue((uint8_t*)"\x01", 1);
        pCharacteristic->notify();
        delay(2000);
        pCharacteristic->setValue((uint8_t*)"\x00", 1);
        pCharacteristic->notify();
        delay(2000);
    }
}
```

Flash this sketch to the ESP32 using the Arduino IDE.  When connected,
notifications with a single byte of `1` turn the video on and `0` turn it
off.

### Headset remote caveat

Most modern phones and tablets have **digital audio ports** that do not
support analogue TRRS remotes.  As a result the media button on a wired
headset may not generate key events on these devices.  Only devices with
analogue headphone jacks will support the `HEADSET` trigger.  When in
doubt test with the `VOLUME` fallback trigger.

### HTTP examples

When using the HTTP trigger the tablet exposes a small web server on
`http://localhost:8765`.  You can test it locally with `curl`:

```sh
# Turn the trigger on
curl -X POST http://localhost:8765/trigger -d '{"state": "on"}'

# Turn the trigger off
curl -X POST http://localhost:8765/trigger -d '{"state": "off"}'

# Check the health endpoint
curl http://localhost:8765/health
```

If the device is on a network you can replace `localhost` with its IP
address to control the trigger remotely.

## QA checklist & test plan

The following checklist can be used to verify the application meets the
requirements:

1. **Installation & Permissions**
   - [ ] App installs on a device with API 24+.
   - [ ] On first launch the app requests permission to read external storage and functions correctly once granted.

2. **Media configuration**
   - [ ] Video file picker selects a local MP4 and displays its filename in settings.
   - [ ] Image/GIF picker selects a local image or GIF and displays its filename.
   - [ ] Loop and mute switches persist their state across app restarts.

3. **Trigger sources**
   - [ ] USB HID: pressing volume up/down on a connected keyboard toggles the video within 200 ms.
   - [ ] Bluetooth HID: pressing buttons on a paired remote toggles the video similarly.
   - [ ] Headset: play/pause button starts video; pause stops it (only on devices with analogue jacks).
   - [ ] HTTP: POST requests with `{"state":"on"}` and `{"state":"off"}` start and stop the video.
   - [ ] BLE GATT: peripheral notifications with value `1` and `0` toggle the state.
   - [ ] Volume: long‑pressing the device volume keys toggles the state.
   - [ ] Debounce time filters out spurious rapid changes.

4. **Behaviour**
   - [ ] When the trigger becomes ON, the video starts within 200 ms.
   - [ ] When the trigger becomes OFF in loop mode the video stops immediately and the image appears.
   - [ ] In single‑play mode the video plays to completion and then returns to idle if the trigger remains ON.
   - [ ] No visible flicker when rapidly toggling the trigger thanks to the minimum dwell time.
   - [ ] The app remains in immersive full‑screen mode and keeps the screen on.

5. **Lifecycle & resilience**
   - [ ] Rotating or locking/unlocking the screen does not disrupt playback.
   - [ ] The app resumes the correct state after being killed by the system (process death).
   - [ ] Autostart launches the app automatically after boot when enabled.
   - [ ] Kiosk mode pins the app and prevents navigation away when enabled.

6. **Performance**
   - [ ] Video playback is smooth and properly scaled on a 7″ tablet.
   - [ ] GIFs loop continuously without stuttering.
   - [ ] HTTP and BLE listeners restart automatically after connection errors.

By following this plan you can verify that the kiosk app meets the goals
outlined in the specification.  Feel free to extend the diagnostic screen
and add telemetry logs if additional observability is required.

## Notes

*ExoPlayer* is used for video playback because it provides better quality
and flexibility than the built‑in VideoView【238771223691998†L81-L85】.  The
dependencies in `build.gradle.kts` illustrate how to include ExoPlayer in a
Kotlin module【238771223691998†L99-L126】.  Persistent settings are stored via
Jetpack **DataStore**, which uses Kotlin coroutines and flows to save data
asynchronously and transactionally【83036416110107†L172-L176】.

## Kiosk & Power Notes
- Add the app to **Auto-start** and **Ignore Battery Optimizations** (the app will prompt).
- A **ForegroundService** keeps HTTP/BLE listeners alive during Doze.
- For **BT HID (ESP32/selfie remote)**: Android cannot keep the remote awake; implement **keep-alive on the device** (e.g., BLE HID send no-op every 20–30s) and/or disable deep sleep on ESP32 while powered.

## Permissions (Android 13+)
- Replaced legacy `READ_EXTERNAL_STORAGE` with `READ_MEDIA_VIDEO` and `READ_MEDIA_IMAGES`. Use the system file picker (SAF) to choose media.
- Added `POST_NOTIFICATIONS` for the foreground service notification.



## BLE UUID Config
- Service/Characteristic UUID untuk BLE GATT **tidak di-hardcode**. Set langsung dari **Settings → BLE** di dalam aplikasi.
- Biarkan kosong bila tidak menggunakan BLE GATT.


## CI: Build APK in GitHub Actions
This repo includes `.github/workflows/android.yml` that:
- sets up JDK + Android SDK
- generates the Gradle wrapper (Gradle 8.7)
- builds `assembleDebug`
- uploads the debug APK as an artifact

### Local (optional)
If you want the wrapper locally, run:
```bash
./bootstrap_wrapper.sh
./gradlew assembleDebug
```


### Windows: Generate Gradle Wrapper (if missing)
If `gradlew.bat` is missing, run the PowerShell script to generate wrapper locally:

```powershell
Set-ExecutionPolicy -Scope Process Bypass -Force
.ootstrap_wrapper.ps1
.\gradlew.bat assembleDebug
```
