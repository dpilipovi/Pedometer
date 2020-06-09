package android.tvz.hr.pedometer

import android.os.Parcelable
import com.dbflow5.annotation.PrimaryKey
import com.dbflow5.annotation.Table
import kotlinx.android.parcel.Parcelize
import java.sql.Date


@Table(name = "Steps", database = StepDatabase::class)
@Parcelize
class Step(@PrimaryKey var id: Int = 0, var stepCount: Int = 0, var date: Date): Parcelable
