package android.tvz.hr.pedometer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.dbflow5.config.database
import com.dbflow5.config.databaseForTable
import com.dbflow5.query.list
import com.dbflow5.query.select
import com.dbflow5.structure.save
import java.util.*

class RefreshReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

            Step(StepCounterService.id_counter, StepCounterService.stepCount, StepCounterService.date).save()
            StepCounterService.id_counter++
            StepCounterService.stepCount=0
            StepCounterService.date = Date()

            val results = (select from Step::class.java).list

            for(result in results)
            {
               println(result.toString())
            }

    }

}