package android.tvz.hr.pedometer

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.IBinder
import android.util.Log
import java.util.*

class StepCounterService : Service() {

    companion object {
        val BROADCAST_ACTION: String = "android.tvz.hr.pedometer.displaySteps"
    }

    private var MagnitudePrevious = 0.0
    private var stepCount: Int = 0

    lateinit var intent: Intent

    var handler: Handler = Handler()

    override fun onCreate() {
        super.onCreate()

        intent = Intent(BROADCAST_ACTION)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        handler.removeCallbacks(sendUpdatesToUI)
        handler.postDelayed(sendUpdatesToUI, 1000)

        return super.onStartCommand(intent, flags, startId)
    }

    private var sendUpdatesToUI: Runnable = (object : Runnable {
        override fun run() {
            DisplayLoggingInfo()

            handler.postDelayed(this, 1000)
        }

    })

    private fun DisplayLoggingInfo() {
        Log.d("StepsCount", stepCount.toString())

        intent.putExtra("time", Date().toLocaleString())
        intent.putExtra("counter", stepCount)
        sendBroadcast(intent)
    }

    override fun onBind(intent: Intent): IBinder? {
        val sensorManager =
            getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor =
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val stepDetector: SensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(sensorEvent: SensorEvent) {
                if (sensorEvent != null) {
                    val x_acceleration = sensorEvent.values[0]
                    val y_acceleration = sensorEvent.values[1]
                    val z_acceleration = sensorEvent.values[2]
                    val Magnitude =
                        Math.sqrt(x_acceleration * x_acceleration + y_acceleration * y_acceleration + (z_acceleration * z_acceleration).toDouble())
                    val MagnitudeDelta = Magnitude - MagnitudePrevious
                    MagnitudePrevious = Magnitude
                    if (MagnitudeDelta > 6) {
                        stepCount++

                    }
                }
            }

            override fun onAccuracyChanged(
                sensor: Sensor,
                i: Int
            ) {
            }
        }

        sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        return null
    }

    override fun onDestroy() {
        handler.removeCallbacks(sendUpdatesToUI)
        super.onDestroy()
    }
}
