package com.deepak.mytaxi.data.remote

const val data = "DATA_1_1"
fun main() {
    var data2: CharSequence = ""
    data.substring(5, 6).toInt().inc().run {
        //print(this)
        data2 = this.toString()
        data.replaceRange(5, 6, data2).apply {
            print(data)
        }
    }


    var finalString: String = ""

    for (count in 1..10) {
        //count = i
        data.elementAt(6).inc().apply {
            finalString = data.replaceRange(5, 6, this.toString())
        }

        print("$finalString \n" )

    }
}
    //Output

//DATA_1_1
//DATA_2_1
//DATA_3_1
//DATA_4_1
//DATA_5_1
//DATA_6_1
//DATA_7_1
//DATA_8_1
//DATA_9_1
//DATA_10_1




