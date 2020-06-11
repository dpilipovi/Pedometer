package android.tvz.hr.pedometer

import android.app.Application
import com.dbflow5.config.FlowManager

class DbFlow: Application() {

    override fun onCreate() {
        super.onCreate()
        FlowManager.init(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        FlowManager.destroy()
    }

}