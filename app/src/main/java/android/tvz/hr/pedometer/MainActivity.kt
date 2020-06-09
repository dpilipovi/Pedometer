package android.tvz.hr.pedometer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import 	android.support.LocalBroadcastManager
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    private var MagnitudePrevious = 0.0
    private var stepCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

      textView = findViewById(R.id.steps_count)

      LocalBroadcastManager.getInstance(this).registerReceiver(
          object : BroadcastReceiver() {
              override fun onReceive(context: Context?, intent: Intent) {
                  val steps =
                      intent.getIntExtra(MainService.STEP_COUNT, 0)
                  textView.setText(stepCount.toString())
              }
          }, IntentFilter(MainService.ACTION_LOCATION_BROADCAST)
      )
        /*
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
        sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL)*/
    }

    override fun onResume() {
        super.onResume()
        startService(Intent(this, MainService::class.java))
    }

    override fun onPause() {
        super.onPause()
        stopService(Intent(this, MainService::class.java))
    }

  /*  override fun onPause() {
        super.onPause()
        val sharedPreferences =
            getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.putInt("stepCount", stepCount)
        editor.apply()
    }

    override fun onStop() {
        super.onStop()
        val sharedPreferences =
            getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.putInt("stepCount", stepCount)
        editor.apply()
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences =
            getPreferences(Context.MODE_PRIVATE)
        stepCount = sharedPreferences.getInt("stepCount", 0)
    }*/
}