package android.tvz.hr.pedometer


import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.tvz.hr.pedometer.fragments.AchievementsFragment
import android.tvz.hr.pedometer.fragments.HistoryFragment
import android.tvz.hr.pedometer.fragments.HomeFragment
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.dbflow5.config.FlowConfig
import com.dbflow5.config.FlowManager
import com.dbflow5.config.FlowManager.context
import kotlinx.android.synthetic.main.main_activity.*
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object {
        var active = false
    }

    private val TAG: String = "MainActivityTag"
    private lateinit var textView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()

        bottom_navigation.setOnNavigationItemSelectedListener { item ->

            var selectedFragment: Fragment? = null

             when(item.itemId) {
                 R.id.menu_main -> {
                     selectedFragment = HomeFragment()
                     true
                 }
                 R.id.menu_achievements -> {
                     selectedFragment = AchievementsFragment()
                     true
                 }
                 R.id.menu_history -> {
                     selectedFragment = HistoryFragment()
                     true
                 }
                 else -> false
             }

            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment!!).commit()

            return@setOnNavigationItemSelectedListener true
        }

        // Initialize DBFlow
        FlowManager.init(
            FlowConfig.builder(this)
            .openDatabasesOnInit(true)
            .build())

        val intent = Intent(this, StepCounterService::class.java)

        startService(intent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) run {
            val channel = NotificationChannel(
                "MYCHANNEL",
                "Notification channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Channel Description"
            val man = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            man.createNotificationChannel(channel)




        }

        notification()


    }

    override fun onResume() {
        super.onResume()
        active = true

        //startService(intent)
        //

        if(!StepCounterService.active) {
            startService(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        active = false
    }

    override fun onStop() {
        super.onStop()
        active = false

        // Maybe do something
    }

    override fun onDestroy() {
        super.onDestroy()
        active = false

        //unregisterReceiver(broadcastReceiver)
        //stopService(intent)
    }

    private fun notification()
    {
        val steps = StepCounterService.step.stepCount

        val notification = NotificationCompat.Builder(this, "MYCHANNEL")
            .setContentTitle("Pedometer")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentText("Steps:" + steps.toString())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOnlyAlertOnce(true)
            .setOngoing(true)

        with(NotificationManagerCompat.from(this)) {
            notify(172, notification.build())
        }

    }
}
