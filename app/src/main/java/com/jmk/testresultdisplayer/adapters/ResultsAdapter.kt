package com.jmk.testresultdisplayer.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.jmk.testresultdisplayer.R
import com.jmk.testresultdisplayer.model.TestCase

class ResultsAdapter(val context: Context, val tests: List<TestCase>): RecyclerView.Adapter<ResultsAdapter.ResultHolder>() {

    inner class ResultHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val statusImageView = itemView?.findViewById<ImageView>(R.id.statusImageView)

        fun bindTest(test: TestCase, context: Context) {
            var imageName = "neutralicon"
            if(test.status.toLowerCase().contains("pass")) {
                imageName = "passicon"
            } else if(test.status.toLowerCase().contains("fail")) {
                imageName = "failicon"
            }
            val resourceId = context.resources.getIdentifier(imageName, "drawable", context.packageName)
            statusImageView.setImageResource(resourceId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.status_list_item, parent, false)
        return ResultHolder(view)
    }

    override fun getItemCount(): Int {
        return tests.count()
    }

    override fun onBindViewHolder(holder: ResultHolder, position: Int) {
        holder?.bindTest(tests[position], context)
    }
}