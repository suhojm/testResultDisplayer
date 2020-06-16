package com.jmk.testresultdisplayer

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.jmk.testresultdisplayer.adapters.ResultsAdapter
import com.jmk.testresultdisplayer.model.TestCase
import com.jmk.testresultdisplayer.service.RequestService
import com.jmk.testresultdisplayer.utilities.EXTRA_TEST_ID
import kotlinx.android.synthetic.main.activity_search_test.*

class SearchTestActivity : AppCompatActivity() {

    lateinit var resultAdapter : ResultsAdapter

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_test)

        if(supportActionBar != null) { //null check
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        val caseId = intent.getStringExtra(EXTRA_TEST_ID)
        if(caseId != null) {
            loadTestInfoById(caseId)
        }
    }

    fun searchBySwitchClicked(view: View) {
        if(searchBySwitch.isChecked) {
            searchIdText.setHint("Testrail Case Title")
        } else {
            searchIdText.setHint("Testrail Case ID")
        }
    }

    fun handleSearchedTest(by: String) {
        // as a start, just deal with first test case found.
        if(RequestService.searchedTest.isNotEmpty()) {
            val testCase = RequestService.searchedTest.entries.first().value
            if (testCase != null) {
                val sortedCase = testCase.sortedWith(compareBy { case ->
                    case.id
                })

                val lastIndex = sortedCase.size - 1
                titleTextView.text = "Title: ${sortedCase[lastIndex].title}"
                testIdTextView.text = "Testrail ID: ${sortedCase[lastIndex].testrailId}"

                var imageName = "neutralicon"
                if(sortedCase[lastIndex].status.toLowerCase().contains("pass")) {
                    imageName = "passicon"
                } else if(sortedCase[lastIndex].status.toLowerCase().contains("fail")) {
                    imageName = "failicon"
                }

                val latestResultResourceId = resources.getIdentifier(imageName, "drawable", packageName)
                latestResultImageView.setImageResource(latestResultResourceId)
                commentTextView.text = sortedCase[lastIndex].comment

                // set Trend values using recycler view..? list it vertically something like "- - x v x"
                val startIndex = if(sortedCase.count() > 5) {
                    sortedCase.count() - 5
                } else {
                    0
                }
                val endIndex = sortedCase.count()
                val latestFive = ArrayList<TestCase>()
                for(i in startIndex until endIndex) {
                    latestFive.add(sortedCase[i])
                }

                resultAdapter = ResultsAdapter(this, latestFive)
                var spanCount = 5
                var layoutManager = GridLayoutManager(this, spanCount)
                resultsRecyclerView.layoutManager = layoutManager
                resultsRecyclerView.adapter = resultAdapter

            } else {
                // test not found
                titleTextView.text = "Test Found is NULL for $by"
                testIdTextView.text = ""
                val latestResultResourceId = resources.getIdentifier("neutralicon", "drawable", packageName)
                latestResultImageView.setImageResource(latestResultResourceId)
                commentTextView.text = ""
                resultsRecyclerView.adapter = ResultsAdapter(this, ArrayList<TestCase>())
            }
        } else {
            Log.d("FOUND", "Test is not found for $by.")
            // test not found
            titleTextView.text = "Test Not Found for searching $by"
            testIdTextView.text = ""
            val latestResultResourceId = resources.getIdentifier("neutralicon", "drawable", packageName)
            latestResultImageView.setImageResource(latestResultResourceId)
            commentTextView.text = ""
            resultsRecyclerView.adapter = ResultsAdapter(this, ArrayList<TestCase>())
        }
    }

    fun loadTestInfoById(caseId: String) {
        RequestService.findTestByCaseId(this, caseId) { complete ->
            if(complete) {
                handleSearchedTest(caseId)
            } else {
                titleTextView.text = "Request Failed"
                testIdTextView.text = ""
                val latestResultResourceId = resources.getIdentifier("neutralicon", "drawable", packageName)
                latestResultImageView.setImageResource(latestResultResourceId)
                commentTextView.text = ""
                resultsRecyclerView.adapter = ResultsAdapter(this, ArrayList<TestCase>())
            }
        }
    }

    fun loadTestInfoByTitle(title: String) {
        RequestService.findTestByTitle(this, title) { complete ->
            if(complete) {
                handleSearchedTest(title)
            } else {
                titleTextView.text = "Request Failed"
                testIdTextView.text = ""
                val latestResultResourceId = resources.getIdentifier("neutralicon", "drawable", packageName)
                latestResultImageView.setImageResource(latestResultResourceId)
                commentTextView.text = ""
                resultsRecyclerView.adapter = ResultsAdapter(this, ArrayList<TestCase>())
            }
        }
    }

    fun searchButtonClicked(view: View) {
        val caseId = searchIdText.text
        Log.d("DEBUG", "$caseId is being searched")

        if(searchBySwitch.isChecked) {
            loadTestInfoByTitle("$caseId")
        } else {
            loadTestInfoById("$caseId")
        }
    }

}
