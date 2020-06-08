package android.tvz.hr.pedometer

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class StepCountService : Service(), SensorEventListener {
    var sensorManager: SensorManager? = null
    var stepCounterSensor: Sensor? = null
    var stepDetectorSensor: Sensor? = null

    //int currentStepCount;
    var currentStepsDetected = 0
    var stepCounter = 0
    var newStepCounter = 0
    var serviceStopped : Boolean = false // Boolean variable to control the repeating timer. = false
    var notificationManager: NotificationManager? = null

    // --------------------------------------------------------------------------- \\
    // _ (1) declare broadcasting element variables _ \\
    // Declare an instance of the Intent class.
    var intent: Intent? = null

    // Create a handler - that will be used to broadcast our data, after a specified amount of time.
    private val handler = Handler()

    // Declare and initialise counter - for keeping a record of how many times the service carried out updates.
    var counter = 0
    // ___________________________________________________________________________ \\
    /** Called when the service is being created.  */
    override fun onCreate() {
        super.onCreate()

        // --------------------------------------------------------------------------- \\
        // ___ (2) create/instantiate intent. ___ \\
        // Instantiate the intent declared globally, and pass "BROADCAST_ACTION" to the constructor of the intent.
        intent = Intent("android.tvz.hr.pedometer.StepService")
        // ___________________________________________________________________________ \\
    }

    /** The service is starting, due to a call to startService()  */
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.v("Service", "Start")
        showNotification()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepCounterSensor =
            sensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        stepDetectorSensor =
            sensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        sensorManager!!.registerListener(this, stepCounterSensor, 0)
        sensorManager!!.registerListener(this, stepDetectorSensor, 0)

        //currentStepCount = 0;
        currentStepsDetected = 0
        stepCounter = 0
        newStepCounter = 0
        serviceStopped = false

        // --------------------------------------------------------------------------- \\
        // ___ (3) start handler ___ \\
        /////if (serviceStopped == false) {
        // remove any existing callbacks to the handler
        handler.removeCallbacks(updateBroadcastData)
        // call our handler with or without delay.
        handler.post(updateBroadcastData) // 0 seconds
        /////}
        // ___________________________________________________________________________ \\
        return START_STICKY
    }

    /** A client is binding to the service with bindService()  */
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    /** Called when The service is no longer used and is being destroyed  */
    override fun onDestroy() {
        super.onDestroy()
        Log.v("Service", "Stop")
        serviceStopped = true
        dismissNotification()
    }

    /** Called when the overall system is running low on memory, and actively running processes should trim their memory usage.  */
    override fun onLowMemory() {
        super.onLowMemory()
    }

    /////////////////__________________ Sensor Event. __________________//////////////////
    override fun onSensorChanged(event: SensorEvent) {
        // STEP_COUNTER Sensor.
        // *** Step Counting does not restart until the device is restarted - therefore, an algorithm for restarting the counting must be implemented.
        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            val countSteps = event.values[0].toInt()

            // -The long way of starting a new step counting sequence.-
            /**
             * int tempStepCount = countSteps;
             * int initialStepCount = countSteps - tempStepCount; // Nullify step count - so that the step cpuinting can restart.
             * currentStepCount += initialStepCount; // This variable will be initialised with (0), and will be incremented by itself for every Sensor step counted.
             * stepCountTxV.setText(String.valueOf(currentStepCount));
             * currentStepCount++; // Increment variable by 1 - so that the variable can increase for every Step_Counter event.
             */

            // -The efficient way of starting a new step counting sequence.-
            if (stepCounter == 0) { // If the stepCounter is in its initial value, then...
                stepCounter =
                    event.values[0].toInt() // Assign the StepCounter Sensor event value to it.
            }
            newStepCounter =
                countSteps - stepCounter // By subtracting the stepCounter variable from the Sensor event value - We start a new counting sequence from 0. Where the Sensor event value will increase, and stepCounter value will be only initialised once.
        }

        // STEP_DETECTOR Sensor.
        // *** Step Detector: When a step event is detect - "event.values[0]" becomes 1. And stays at 1!
        if (event.sensor.type == Sensor.TYPE_STEP_DETECTOR) {
            val detectSteps = event.values[0].toInt()
            currentStepsDetected += detectSteps //steps = steps + detectSteps; // This variable will be initialised with the STEP_DETECTOR event value (1), and will be incremented by itself (+1) for as long as steps are detected.
        }
        Log.v("Service Counter", newStepCounter.toString())
    }

    override fun onAccuracyChanged(
        sensor: Sensor,
        accuracy: Int
    ) {
    }

    // ___________________________________________________________________________ \\
    // --------------------------------------------------------------------------- \\
    // _ Manage notification. _
    private fun showNotification() {
        val notificationBuilder = NotificationCompat.Builder(this)
        notificationBuilder.setContentTitle("Pedometer")
        notificationBuilder.setContentText("Pedometer session is running in the background.")
        notificationBuilder.setSmallIcon(R.drawable.ic_launcher_background)
        notificationBuilder.color = Color.parseColor("#6600cc")
        val colorLED = Color.argb(255, 0, 255, 0)
        notificationBuilder.setLights(colorLED, 500, 500)
        // To  make sure that the Notification LED is triggered.
        notificationBuilder.priority = Notification.PRIORITY_HIGH
        notificationBuilder.setOngoing(true)

        //Intent resultIntent = new Intent(this, MainActivity.class);
        val resultPendingIntent = PendingIntent.getActivity(this, 0, Intent(), 0)
        notificationBuilder.setContentIntent(resultPendingIntent)
        notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager!!.notify(0, notificationBuilder.build())
    }

    private fun dismissNotification() {
        notificationManager!!.cancel(0)
    }

    // ______________________________________________________________________________________ \\
    // --------------------------------------------------------------------------- \\
    // ___ (4) repeating timer ___ \\
    private val updateBroadcastData: Runnable = object : Runnable {
        override fun run() {
            if (!serviceStopped) { // Only allow the repeating timer while service is running (once service is stopped the flag state will change and the code inside the conditional statement here will not execute).
                // Call the method that broadcasts the data to the Activity..
                broadcastSensorValue()
                // Call "handler.postDelayed" again, after a specified delay.
                handler.postDelayed(this, 1000)
            }
        }
    }

    // ___________________________________________________________________________ \\
    // --------------------------------------------------------------------------- \\
    // ___ (5) add  data to intent ___ \\
    private fun broadcastSensorValue() {
        Log.d(TAG, "Data to Activity")
        // add step counter to intent.
        intent!!.putExtra("Counted_Step_Int", newStepCounter)
        intent!!.putExtra("Counted_Step", newStepCounter.toString())
        // add step detector to intent.
        intent!!.putExtra("Detected_Step_Int", currentStepsDetected)
        intent!!.putExtra("Detected_Step", currentStepsDetected.toString())
        // call sendBroadcast with that intent  - which sends a message to whoever is registered to receive it.
        sendBroadcast(intent)
    } // ___________________________________________________________________________ \\

    companion object {
        // A string that identifies what kind of action is taking place.
        private const val TAG = "StepService"
        const val BROADCAST_ACTION = "android.tvz.hr.StepCountService" // com.example.yusuf.mybroadcast
    }
}