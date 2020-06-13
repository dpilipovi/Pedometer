package android.tvz.hr.pedometer.adapters

import android.graphics.Color
import android.tvz.hr.pedometer.Achievement
import android.tvz.hr.pedometer.R
import android.tvz.hr.pedometer.Step
import android.tvz.hr.pedometer.StepCounterService
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.achievement_screen_item_card.view.*
import kotlinx.android.synthetic.main.history_screen_item_card.view.*
import java.util.*

class AchievementRecyclerViewAdapter(private val achievementList: List<Achievement>): RecyclerView.Adapter<AchievementRecyclerViewAdapter.AchievementViewHolder>() {

    class AchievementViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val achievementName: TextView = itemView.achievement_name
        val achievementSteps: TextView = itemView.achievement_step_count
        val achievementBackground : LinearLayout = itemView.achievement_background
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):AchievementViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.achievement_screen_item_card, parent, false)

        return AchievementViewHolder(itemView)
    }

    override fun getItemCount(): Int = achievementList.size

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        val currentItem = achievementList[position]

        holder.achievementName.text =currentItem.name
        holder.achievementSteps.text = currentItem.stepCount.toString()

        if(currentItem.stepCount >= StepCounterService.stepCount) holder.achievementBackground.setBackgroundColor(
            Color.parseColor("#777777"))
    }

}