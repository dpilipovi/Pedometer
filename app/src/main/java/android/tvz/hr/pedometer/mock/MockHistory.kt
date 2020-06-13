package android.tvz.hr.pedometer.mock

import android.tvz.hr.pedometer.Step
import android.tvz.hr.pedometer.StepCounterService
import com.dbflow5.structure.save
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class MockHistory {

    companion object {
        fun generateMockSteps(numSteps: Int) {
            var id_counter = StepCounterService.step.id

            val steps: ArrayList<Step> = ArrayList()

            for (i in 1..numSteps) {
                steps.add(Step(id_counter, Random.nextInt(1000, 5000), Date(Date().time - (i * 24 * 3600 * 1000).toLong())))
                StepCounterService.step.id++
                id_counter = StepCounterService.step.id
            }
            steps.forEach { step ->
                step.save()
            }
        }
    }

}