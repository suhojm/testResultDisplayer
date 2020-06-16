package com.jmk.testresultdisplayer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jmk.testresultdisplayer.R
import com.jmk.testresultdisplayer.model.TestCase

class TestsListAdapter(val context: Context, val failingTests: List<TestCase>, var itemClick: (String) -> Unit): RecyclerView.Adapter<TestsListAdapter.FailingTestHolder>() {

    inner class FailingTestHolder(itemView: View, val itemClick: (String) -> Unit) : RecyclerView.ViewHolder(itemView) {
        val failingTestIdTextView = itemView?.findViewById<TextView>(R.id.failingTestId)
        val failingTestTitleTextView = itemView?.findViewById<TextView>(R.id.failingTestTitle)
        val failingTestSearchButtonImageButton = itemView?.findViewById<ImageButton>(R.id.failingTestSearchButton)

        fun bindFailingTest(test: TestCase, context: Context) {
            failingTestIdTextView.text = test.testrailId
            failingTestTitleTextView.text = test.title
            failingTestSearchButtonImageButton.setOnClickListener {
                itemClick(test.testrailId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FailingTestHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.test_list_item, parent, false)
        return FailingTestHolder(view, itemClick)
    }

    override fun getItemCount(): Int {
        return failingTests.count()
    }

    override fun onBindViewHolder(holder: FailingTestHolder, position: Int) {
        holder?.bindFailingTest(failingTests[position], context)
    }

}