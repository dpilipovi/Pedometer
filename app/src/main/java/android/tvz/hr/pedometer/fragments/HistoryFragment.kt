package android.tvz.hr.pedometer.fragments

import android.os.Bundle
import android.tvz.hr.pedometer.R
import android.tvz.hr.pedometer.Step
import android.tvz.hr.pedometer.adapters.HistoryRecyclerViewAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dbflow5.query.NameAlias
import com.dbflow5.query.list
import com.dbflow5.query.select
import com.dbflow5.query.update
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        updateHistory()
    }

    override fun onResume() {
        super.onResume()

        updateHistory()
    }

    private fun updateHistory() {
        val historyList: List<Step> = (select from Step::class.java).orderBy(nameAlias = NameAlias("date"), ascending = false).list

        history_recycler_view.adapter = HistoryRecyclerViewAdapter(historyList)
        history_recycler_view.layoutManager = LinearLayoutManager(context)
        history_recycler_view.setHasFixedSize(true)
    }

}
