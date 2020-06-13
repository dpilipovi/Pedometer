package android.tvz.hr.pedometer.adapters

import android.tvz.hr.pedometer.R
import android.tvz.hr.pedometer.Step
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.history_screen_item_card.view.*
import java.util.*

class HistoryRecyclerViewAdapter(private val historyList: List<Step>): RecyclerView.Adapter<HistoryRecyclerViewAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val historyDate: TextView = itemView.history_date
        val historySteps: TextView = itemView.history_step_count
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.history_screen_item_card, parent, false)

        return HistoryViewHolder(itemView)
    }

    override fun getItemCount(): Int = historyList.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentItem = historyList[position]

        holder.historyDate.text = convertDateToString(currentItem.date)
        holder.historySteps.text = currentItem.stepCount.toString()
    }

    private fun convertDateToString(date: Date): String {

        val calendar: Calendar = Calendar.getInstance()
        calendar.time = date

        return "${calendar.get(Calendar.DAY_OF_MONTH)}.${calendar.get(Calendar.MONTH).plus(1)}.${calendar.get(Calendar.YEAR)}."
    }
}