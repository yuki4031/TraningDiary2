package com.yuuki.kawashima.trainingdiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import io.realm.Realm
import kotlinx.android.synthetic.main.content_input.*

class InputActivity : AppCompatActivity() {

    private var mTask: Task? = null

    private val mOnDoneClickListener = View.OnClickListener {
        addTask()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        add_button.setOnClickListener(mOnDoneClickListener)

        val intent = intent
        val taskId = intent.getIntExtra(EXTRA_TASK,-1)
        val realm = Realm.getDefaultInstance()
        mTask = realm.where(Task::class.java).equalTo("id",taskId).findFirst()
        realm.close()
        if(mTask != null){
            edittext_training_name.setText(mTask!!.trainingName)
        }
    }

    private fun addTask(){
        val realm = Realm.getDefaultInstance()

        realm.beginTransaction()

        if(mTask == null){
            //新規作成の場合
            mTask = Task()

            val taskRealmResults = realm.where(Task::class.java).findAll()

            val identifier: Int = if(taskRealmResults.max("id") != null){
                taskRealmResults.max("id")!!.toInt() + 1
            }else{
                0
            }
            mTask!!.id = identifier
        }

        val trainingName = edittext_training_name.text.toString()
        mTask!!.trainingName = trainingName

        realm.copyToRealmOrUpdate(mTask!!)
        realm.commitTransaction()

        realm.close()
    }
}