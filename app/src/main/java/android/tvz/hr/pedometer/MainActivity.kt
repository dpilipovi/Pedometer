package android.tvz.hr.pedometer

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    private var MagnitudePrevious = 0.0
    private var stepCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
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
    }

    override fun onPause() {
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
    }
}