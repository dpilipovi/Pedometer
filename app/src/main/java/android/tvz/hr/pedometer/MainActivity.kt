package android.tvz.hr.pedometer


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity() {

    private val TAG: String = "MainActivityTag"

    private lateinit var textView: TextView
    private var MagnitudePrevious = 0.0
    private var stepCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val intent = Intent(this, StepCounterService::class.java)

        startService(intent)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) run {
            val channel = NotificationChannel(
                "MYCHANNEL",
                "Notification channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Channel Description"
            val man = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            man.createNotificationChannel(channel)

        }

        notification()

        /*
        textView = findViewById(R.id.steps_count)
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
                    textView.setText(stepCount.toString())
                }
            }
            override fun onAccuracyChanged(
                sensor: Sensor,
                i: Int
            ) {
            }
        }
        sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL)
         */
    }

    var broadcastReceiver: BroadcastReceiver = (object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateUI(intent!!)
            notification()
        }

    })

    override fun onResume() {
        super.onResume()
        startService(intent)
        registerReceiver(broadcastReceiver, IntentFilter(StepCounterService.BROADCAST_ACTION))

    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(broadcastReceiver)
        stopService(intent)
    }

    private fun updateUI(intent: Intent) {
        val counter: Int = intent.getIntExtra("counter",  0)
        val time: String = intent.getStringExtra("time")

        stepCount = counter;

        Log.d(TAG, counter.toString())
        Log.d(TAG, time)

        steps_count.text = counter.toString()
    }

    private fun notification()
    {


        val notification = NotificationCompat.Builder(this, "MYCHANNEL")
            .setContentTitle("Pedometer")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentText("Steps: "+ stepCount)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOnlyAlertOnce(true)
            .setOngoing(true)

        with(NotificationManagerCompat.from(this)) {
            notify(172, notification.build())
        }

    }
}
