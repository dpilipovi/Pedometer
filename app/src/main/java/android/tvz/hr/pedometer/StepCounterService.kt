package android.tvz.hr.pedometer

import android.app.NotificationManager
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
import androidx.core.app.NotificationCompat
import java.util.*

class StepCounterService : Service() {

    companion object {
        const val BROADCAST_ACTION: String = "android.tvz.hr.pedometer.displaySteps"
        var active = false
        var stepCount: Int = 0

    }

    // Init notification
    private var notification = NotificationCompat.Builder(this, "MYCHANNEL")

    private var magnitudePrevious = 0.0

    lateinit var intent: Intent

    var handler: Handler = Handler()

    override fun onCreate() {
        super.onCreate()

        intent = Intent(BROADCAST_ACTION)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        //initNotification()

        // Set as active so it doesn't get started again
        active = true

        initNotification()

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
                    val magnitude =
                        Math.sqrt(x_acceleration * x_acceleration + y_acceleration * y_acceleration + (z_acceleration * z_acceleration).toDouble())
                    val magnitudeDelta = magnitude - magnitudePrevious
                    magnitudePrevious = magnitude
                    if (magnitudeDelta > 6) {
                        stepCount++

                        initNotification()
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

        if(MainActivity.active) {
            handler.removeCallbacks(sendUpdatesToUI)
            handler.postDelayed(sendUpdatesToUI, 1000)
        }


       // return super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    private var sendUpdatesToUI: Runnable = (object : Runnable {
        override fun run() {
            displayLoggingInfo()

            handler.postDelayed(this, 1000)
        }

    })

    private fun displayLoggingInfo() {
        Log.d("StepsCount", stepCount.toString())

        intent.putExtra("counter", stepCount)
        sendBroadcast(intent)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        handler.removeCallbacks(sendUpdatesToUI)
        super.onDestroy()
    }

    private fun initNotification()
    {
        notification = NotificationCompat.Builder(this, "MYCHANNEL")
            .setContentTitle("Pedometer")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentText("Steps: $stepCount")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOnlyAlertOnce(true)
            .setOngoing(true)

        val notificationManager: NotificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(172, notification.build())

        /*
        with(NotificationManagerCompat.from(this)) {
            notify(172, notification.build())
        }

         */

    }

    override fun stopService(name: Intent?): Boolean {
        return super.stopService(name)
    }

}
