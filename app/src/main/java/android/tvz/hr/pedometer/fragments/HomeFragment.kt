package android.tvz.hr.pedometer.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.tvz.hr.pedometer.R
import android.tvz.hr.pedometer.StepCounterService
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private var stepCount: Int = StepCounterService.stepCount

    private var broadcastReceiver: BroadcastReceiver = (object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateUI(intent!!)
            //notification()
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
                //notification()
            }
        })

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private fun updateUI(intent: Intent) {
        val counter: Int = intent.getIntExtra("counter",  0)

        stepCount = counter;

        circular_steps_progress.points = stepCount
    }
}