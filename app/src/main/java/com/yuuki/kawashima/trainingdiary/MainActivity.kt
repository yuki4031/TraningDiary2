package com.yuuki.kawashima.trainingdiary

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import io.realm.RealmChangeListener
import io.realm.Sort
import java.util.*
import android.content.Intent
import androidx.appcompat.app.AlertDialog

const val EXTRA_TASK = "com.yuuki.kawashima.trainingdiary.TASK"

class MainActivity : AppCompatActivity() {

    private lateinit var mRealm: Realm
    private val mRealmListener = object : RealmChangeListener<Realm>{
        override fun onChange(element: Realm){
            reloadListView()
        }
    }

    private lateinit var mTaskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        fab.setOnClickListener{ view->
            val intent = Intent(this@MainActivity, InputActivity::class.java)
            startActivity(intent)
        }

        //Realmの設定
        mRealm = Realm.getDefaultInstance()
        mRealm.addChangeListener(mRealmListener)

        //ListViewの設定
        mTaskAdapter = TaskAdapter(this@MainActivity)

        //ListViewをタップした時の処理
        listView1.setOnItemClickListener{parent, _, position, _ ->
            //入力・編集する画面に遷移させる
            val task = parent.adapter.getItem(position) as Task
            val intent = Intent(this@MainActivity,LogpageActivity::class.java)
            intent.putExtra(EXTRA_TASK,task.id)
            startActivity(intent)
        }

        //ListViewを長押しした時の処理
        listView1.setOnItemLongClickListener{parent, _, position, _ ->
            //タスクを削除する
            val task = parent.adapter.getItem(position) as Task

            //ダイアログを表示する
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("削除")
            builder.setMessage(task.trainingName + "を削除しますか？")

            builder.setPositiveButton("OK"){_,_ ->
                val results = mRealm.where(Task::class.java).equalTo("id",task.id).findAll()

                mRealm.beginTransaction()
                results.deleteAllFromRealm()
                mRealm.commitTransaction()

                reloadListView()
            }

            builder.setNegativeButton("CANCEL",null)

            val dialog = builder.create()
            dialog.show()

            true
        }

        reloadListView()
    }

    private fun reloadListView(){

        val taskRealmResults = mRealm.where(Task::class.java).findAll()
        mTaskAdapter.taskList = mRealm.copyFromRealm(taskRealmResults)
        listView1.adapter = mTaskAdapter
        mTaskAdapter.notifyDataSetChanged()
    }

    override fun onDestroy(){
        super.onDestroy()

        mRealm.close()
    }
}