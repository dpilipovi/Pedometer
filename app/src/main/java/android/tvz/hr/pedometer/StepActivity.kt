package android.tvz.hr.pedometer

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class StepActivity : AppCompatActivity() {
    var stepCount: TextView? = null
    var detectCount: TextView? = null
    var startServiceBtn: Button? = null
    var stopServiceBtn: Button? = null
    var countedStep: String? = null
    var detectedStep: String? = null
    var isServiceStopped = false


    @TargetApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // ___ instantiate intent ___ \\
        //  Instantiate the intent declared globally - which will be passed to startService and stopService.
        intent = Intent(this, StepCountService::class.java)
        init() // Call view initialisation method.
    }

    // Initialise views.
    fun init() {
        isServiceStopped = true // variable for managing service state - required to invoke "stopService" only once to avoid Exception.


        // ________________ Service Management (Start & Stop Service). ________________ //
        // ___ start Service & register broadcast receiver ___ \\
        startServiceBtn =
            findViewById<View>(R.id.start_button) as Button
        startServiceBtn!!.setOnClickListener { // start Service.
            startService(Intent(baseContext, StepCountService::class.java))
            // register our BroadcastReceiver by passing in an IntentFilter. * identifying the message that is broadcasted by using static string "BROADCAST_ACTION".
            registerReceiver(
                broadcastReceiver,
                IntentFilter("android.tvz.hr.StepCountService") // ?????????????????? NEMAM POJMA STA TREBA BIO JE "com.example.yusuf,mybroadcast" a to ne postoji
            )
            isServiceStopped = false
        }

        // ___ unregister receiver & stop service ___ \\
        stopServiceBtn =
            findViewById<View>(R.id.stop_button) as Button
        stopServiceBtn!!.setOnClickListener {
            if (!isServiceStopped) {
                // call unregisterReceiver - to stop listening for broadcasts.
                unregisterReceiver(broadcastReceiver)
                // stop Service.
                stopService(Intent(baseContext, StepCountService::class.java))
                isServiceStopped = true
            }
        }
        // ___________________________________________________________________________ \\


        stepCount = findViewById<View>(R.id.steps_count) as TextView
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    // --------------------------------------------------------------------------- \\
    // ___ create Broadcast Receiver ___ \\
    // create a BroadcastReceiver - to receive the message that is going to be broadcast from the Service
    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
            // call updateUI passing in our intent which is holding the data to display.
            updateViews(intent)
        }
    }

    // --------------------------------------------------------------------------- \\
    // ___ retrieve data from intent & set data to textviews __ \\
    private fun updateViews(intent: Intent) {
        // retrieve data out of the intent.
        countedStep = intent.getStringExtra("Counted_Step")
        detectedStep = intent.getStringExtra("Detected_Step");
        stepCount!!.text = '"'.toString() + countedStep.toString() + '"' + " Steps Counted"
        detectCount!!.text = '"'.toString() + detectedStep.toString() + '"' + " Steps Counted"
    }

    companion object {
        const val State_Count = "Counter"
        const val State_Detect = "Detector"
        private const val TAG = "SensorEvent"
    }
}