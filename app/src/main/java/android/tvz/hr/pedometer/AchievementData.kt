package android.tvz.hr.pedometer

class AchievementData {

    companion object{

        fun createDataSet(): ArrayList<Achievement>{
            val list = ArrayList<Achievement>()
            list.add(
                Achievement(
                    1,
                    100,
                    "Baby steps"
                )
            )
            list.add(
                Achievement(
                    2,
                    1000,
                    "Grandma level"
                )
            )

            list.add(
                Achievement(
                    3,
                    2500,
                    "You`re still pathetic"
                )
            )
            list.add(
                Achievement(
                    4,
                    5000,
                    "Less pathetic"
                )
            )
            list.add(
                Achievement(
                    5,
                    10000,
                    "AverageMan"
                )
            )
            list.add(
                Achievement(
                    6,
                    100000,
                    "Officially insane | cheater"
                )
            )
            return list
        }
    }
}