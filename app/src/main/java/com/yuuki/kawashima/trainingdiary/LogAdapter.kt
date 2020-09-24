package com.yuuki.kawashima.trainingdiary

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class LogAdapter(context: Context, private val mSum: Sum): BaseAdapter() {
    companion object{
        private val TYPE_SUM = 0
        private val TYPE_LOG = 1
    }

    private var mLayoutInflater: LayoutInflater? = null

    init{
        mLayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return 1 + mSum.logs.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0){
            TYPE_SUM
        }else{
            TYPE_LOG
        }
    }

    override fun getViewTypeCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Any {
        return mSum
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView

        if(getItemViewType(position) == TYPE_SUM) {
            if (convertView == null) {
                convertView = mLayoutInflater!!.inflate(R.layout.list_sum, parent, false)!!
            }
            val sum = mSum.sum

            val sumView = convertView.findViewById<View>(R.id.sum) as TextView
            sumView.text = sum.toString()
        }else{
            if(convertView == null){
                convertView = mLayoutInflater!!.inflate(R.layout.list_log,parent,false)!!
            }

            val log = mSum.logs[position - 1]
            val numberOfTraining = log.numberOfTraining
            val date = log.date

            val numberTextView = convertView.findViewById<View>(R.id.numberTextView) as TextView
            numberTextView.text = numberOfTraining

            val dateTextView = convertView.findViewById<View>(R.id.dateTextView) as TextView
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.JAPANESE)
            dateTextView.text = simpleDateFormat.format(date)
        }

        return convertView
    }
}