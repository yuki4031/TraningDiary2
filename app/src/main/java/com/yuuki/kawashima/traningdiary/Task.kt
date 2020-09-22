package com.yuuki.kawashima.traningdiary

import java.io.Serializable
import java.util.Date
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Task: RealmObject(),Serializable {
    var trainingName: String = "" //タイトル

    // idをプライマリーキーとして設定
    @PrimaryKey
    var id: Int = 0
}