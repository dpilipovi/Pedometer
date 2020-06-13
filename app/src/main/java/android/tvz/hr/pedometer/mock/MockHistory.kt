package android.tvz.hr.pedometer.mock

import android.os.Build
import android.tvz.hr.pedometer.Step
import android.tvz.hr.pedometer.StepCounterService
import androidx.annotation.RequiresApi
import com.dbflow5.structure.save
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random
import kotlin.random.nextInt

class MockHistory {
    @RequiresApi(Build.VERSION_CODES.O)
    fun generateMockSteps(numSteps: Int) {
        var id_counter = StepCounterService.id_counter

        val steps: ArrayList<Step> = ArrayList()

        for (i in 1..numSteps) {
            steps.add(Step(id_counter, Random.nextInt(1000, 5000), Date.from(LocalDate.now().atStartOfDay(
                ZoneId.systemDefault()).minusDays(i.toLong()).toInstant())))
            StepCounterService.id_counter++
            id_counter = StepCounterService.id_counter
        }
        steps.forEach { step ->
            step.save()
        }
    }
}