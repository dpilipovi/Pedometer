package android.tvz.hr.pedometer.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.tvz.hr.pedometer.R
import android.tvz.hr.pedometer.StepCounterService
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private var stepCount: Int = StepCounterService.step.stepCount

    private var broadcastReceiver: BroadcastReceiver = (object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateUI(intent!!)
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        broadcastReceiver = (object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                updateUI(intent!!)
            }
        })

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onResume() {
        super.onResume()
        context!!.registerReceiver(broadcastReceiver, IntentFilter(StepCounterService.BROADCAST_ACTION))

        circular_steps_progress.points = stepCount
    }

    override fun onPause() {
        super.onPause()
        context?.unregisterReceiver(broadcastReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    // method called from broadcast receiver
    // gets called when accelerometer detects a step
    private fun updateUI(intent: Intent) {
        val counter: Int = intent.getIntExtra("counter",  0)

        stepCount = counter;

        circular_steps_progress.points = stepCount
    }
}