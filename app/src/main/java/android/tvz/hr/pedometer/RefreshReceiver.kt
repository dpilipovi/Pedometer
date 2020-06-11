package android.tvz.hr.pedometer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.dbflow5.config.databaseForTable
import com.dbflow5.structure.save
import java.util.*

class RefreshReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val db = databaseForTable<Step>()

        //var data: Step? = intent?.extras?.getParcelable("Step")

        val stepBundle = intent!!.getBundleExtra("Step")

        var data: Step? = stepBundle.getParcelable("Step")

        Log.d("refresh service", "refreshing")
        if (data != null) {
            Log.d("data", data.stepCount.toString())
        }
        else Log.d("data:", "is null")

        if (data != null) {
            data.save(db)
            StepCounterService.step.id++
            StepCounterService.step.stepCount=0
            StepCounterService.step.date = Date()

            Log.d("add to database", "and refreshing")
        }
    }

}