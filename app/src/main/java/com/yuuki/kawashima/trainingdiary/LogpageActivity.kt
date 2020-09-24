package com.yuuki.kawashima.trainingdiary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_logpage.*

const val EXTRA_LOG = "com.yuuki.kawashima.trainingdiary.LOG"

class LogpageActivity : AppCompatActivity() {

    private lateinit var mRealm: Realm
    private val mRealmListener = object : RealmChangeListener<Realm>{
        override fun onChange(element: Realm) {
            reloadListView()
        }
    }
    private lateinit var mLogAdapter: LogAdapter
    private var mSum: Sum? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logpage)



        fab2.setOnClickListener { view ->
            val intent = Intent(this@LogpageActivity, LogInputActivity::class.java)
            startActivity(intent)
        }

        //Realmの設定
        mRealm = Realm.getDefaultInstance()
        mRealm.addChangeListener(mRealmListener)

        val taskId = intent.getIntExtra(EXTRA_TASK,-1)
        mSum = mRealm.where(Sum::class.java).equalTo("id",taskId).findFirst()

        //ListViewの設定
        mLogAdapter = LogAdapter(this@LogpageActivity,mSum)

        //ListViewをタップした時の処理
        listView2.setOnItemClickListener { parent, _, position, _ ->
            //入力・編集する画面に遷移させる
            val log = parent.adapter.getItem(position) as Log
            val intent = Intent(this@LogpageActivity, LogInputActivity::class.java)
            intent.putExtra(EXTRA_LOG, log.id)
            startActivity(intent)
        }

        listView2.setOnItemLongClickListener { parent, _, position, _ ->
            //タスクを削除する
            val log = parent.adapter.getItem(position) as Log

            // ダイアログを表示する
            val builder = AlertDialog.Builder(this@LogpageActivity)

            builder.setTitle("削除")
            builder.setMessage("記録を削除しますか")

            builder.setPositiveButton("OK") { _, _ ->
                val results = mRealm.where(Log::class.java).equalTo("id", log.id).findAll()

                mRealm.beginTransaction()
                results.deleteAllFromRealm()
                mRealm.commitTransaction()

                reloadListView()
            }
            builder.setNegativeButton("CANCEL", null)

            val dialog = builder.create()
            dialog.show()

            true
        }
        reloadListView()
    }

    private fun reloadListView(){
        val logRealmResults = mRealm.where(Log::class.java).findAll().sort("date",Sort.DESCENDING) //where条件を追加

    }

    override fun onDestroy() {
        super.onDestroy()

        mRealm.close()
    }
}