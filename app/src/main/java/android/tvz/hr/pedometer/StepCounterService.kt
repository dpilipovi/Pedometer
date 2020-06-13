package android.tvz.hr.pedometer

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.dbflow5.config.FlowManager.context
import java.util.*


class StepCounterService : Service() {

    companion object {
        const val BROADCAST_ACTION: String = "android.tvz.hr.pedometer.displaySteps"
        var active = false
      //  var stepCount: Int = 0
        var id_counter: Int = 1
        val step: Step = Step(id_counter,0, Date())


    }

    val calendar: Calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
        set(Calendar.HOUR_OF_DAY, 17)
        //set(Calendar.HOUR, 0) prolly i ovo radi
        set(Calendar.MINUTE,21)
    }

    private var magnitudePrevious = 0.0

    private lateinit var intent: Intent

    var handler: Handler = Handler()

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmPendingIntent: PendingIntent
    private lateinit var alarmIntent: Intent

    override fun onCreate() {
        super.onCreate()

        intent = Intent(BROADCAST_ACTION)


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        super.onStartCommand(intent, flags, startId)

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
                        step.stepCount++
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

        handler.removeCallbacks(sendUpdatesToUI)
        handler.postDelayed(sendUpdatesToUI, 1000)

        return START_STICKY
    }

    private var sendUpdatesToUI: Runnable = (object : Runnable {
        override fun run() {
            displayLoggingInfo()
            initNotification()

            handler.postDelayed(this, 1000)
        }

    })

    private fun displayLoggingInfo() {

        intent.putExtra("counter", step.stepCount)
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

        val notification = NotificationCompat.Builder(this, "MYCHANNEL")
            .setContentTitle("Pedometer")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentText("Steps:" + step.stepCount.toString())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .build()

        startForeground(172, notification)
    }


    override fun stopService(name: Intent?): Boolean {
        return false
    }

}
