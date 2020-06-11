package android.tvz.hr.pedometer

import android.os.Parcelable
import com.dbflow5.annotation.Column
import com.dbflow5.annotation.PrimaryKey
import com.dbflow5.annotation.Table
import kotlinx.android.parcel.Parcelize
import java.util.*


@Table(name = "Steps", database = StepDatabase::class)
@Parcelize
data class Step(@PrimaryKey var id: Int = 0, @Column var stepCount: Int = 0,@Column var date: Date = Date()): Parcelable


