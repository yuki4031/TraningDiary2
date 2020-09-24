package com.yuuki.kawashima.trainingdiary

import io.realm.RealmObject
import java.io.Serializable
import kotlin.collections.ArrayList

class Sum(val logs: ArrayList<Log>): RealmObject(), Serializable {
    val sum: Int = 0 //合計
    var taskId: Int = 0 //
}