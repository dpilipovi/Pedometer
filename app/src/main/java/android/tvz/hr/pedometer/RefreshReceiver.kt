package android.tvz.hr.pedometer

import android.app.PendingIntent.getActivity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import com.dbflow5.config.databaseForTable
import com.dbflow5.structure.save
import java.util.*

class RefreshReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val db = databaseForTable<Step>()

        var data: Step? = intent?.getParcelableExtra("Step")

        if (data != null) {
            data.save(db)
            StepCounterService.step.id++
            StepCounterService.step.stepCount=0
            StepCounterService.step.date = Date()
        }
    }

}