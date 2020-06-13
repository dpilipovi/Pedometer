package android.tvz.hr.pedometer.fragments

import android.os.Bundle
import android.tvz.hr.pedometer.Achievement
import android.tvz.hr.pedometer.MainActivity
import android.tvz.hr.pedometer.R
import android.tvz.hr.pedometer.adapters.AchievementRecyclerViewAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager

import kotlinx.android.synthetic.main.fragment_achievements.*

class AchievementsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_achievements, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        updateAchievements()
    }

    override fun onResume() {
        super.onResume()

        updateAchievements()
    }

    private fun updateAchievements() {
        val achievementList: List<Achievement> = MainActivity.achievements

        achievement_recycler_view.adapter = AchievementRecyclerViewAdapter(achievementList)
        achievement_recycler_view.layoutManager = LinearLayoutManager(context)
        achievement_recycler_view.setHasFixedSize(true)
    }

}