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
                    1,
                    1000,
                    "Grandma level"
                )
            )

            list.add(
                Achievement(
                    1,
                    2500,
                    "You`re still pathetic"
                )
            )
            list.add(
                Achievement(
                    1,
                    5000,
                    "Halfway done, hope you`re not tired"
                )
            )
            list.add(
                Achievement(
                    1,
                    10000,
                    "You can stop if you must, wimp "
                )
            )
            list.add(
                Achievement(
                    1,
                    100000,
                    "Oficially insane | cheater"
                )
            )
            return list
        }
    }
}