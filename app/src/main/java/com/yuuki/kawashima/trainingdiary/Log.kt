package com.yuuki.kawashima.trainingdiary

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

open class Log: RealmObject(), Serializable {
    var numberOfTraining: String = "" //トレーニング回数
    var date: Date = Date() //日時
    // idをプライマリーキーとして設定
    @PrimaryKey
    var id: Int = 0
}