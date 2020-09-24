package com.yuuki.kawashima.trainingdiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import io.realm.Realm
import java.util.*
import kotlinx.android.synthetic.main.activity_log_input.*
import kotlinx.android.synthetic.main.content_input.*
import kotlinx.android.synthetic.main.content_log_input.*

class LogInputActivity : AppCompatActivity() {

    private var mLog: Log? = null

    private val mDoneClickListener = View.OnClickListener {
        addLog()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_input)

        save_button.setOnClickListener(mDoneClickListener)

        val intent = intent
        val taskId = intent.getIntExtra(EXTRA_LOG,-1)
        val realm = Realm.getDefaultInstance()
        mLog = realm.where(Log::class.java).equalTo("id",taskId).findFirst()
        realm.close()
        if(mLog != null){
            editText_number.setText(mLog!!.numberOfTraining)
        }

    }

    private fun addLog(){
        val realm = Realm.getDefaultInstance()

        realm.beginTransaction()

        if(mLog == null) {
            //新規作成の場合
            mLog = Log()

            val logRealmResults = realm.where(Log::class.java).findAll()


            val identifier: Int =
                if (logRealmResults.max("id") != null) {
                    logRealmResults.max("id")!!.toInt() + 1
                } else {
                    0
                }
            mLog!!.id = identifier
        }

        val number = editText_number.text.toString()
        val date: Date = Date()
        mLog!!.numberOfTraining = number
        mLog!!.date = date

        realm.copyToRealmOrUpdate(mLog!!)
        realm.commitTransaction()

        realm.close()

        }
    }
