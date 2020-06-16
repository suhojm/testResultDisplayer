package com.jmk.testresultdisplayer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jmk.testresultdisplayer.adapters.TestsListAdapter
import com.jmk.testresultdisplayer.model.TestCase
import com.jmk.testresultdisplayer.service.RequestService
import com.jmk.testresultdisplayer.utilities.EXTRA_TEST_ID
import kotlinx.android.synthetic.main.activity_search_tests_by_title.*

class SearchTestsByTitleActivity : AppCompatActivity() {

    lateinit var searchedTestListAdapter : TestsListAdapter

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_tests_by_title)

        if(supportActionBar != null) { //null check
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

    }

    private fun handleFoundTests() {
        val searchedTests = ArrayList<TestCase>()
        for((k, v) in RequestService.searchedTest) {
            // sort by id (first: earliest case, last: latest case)
            searchedTests.add(v[v.count()-1])
        }
        val sortedTests = searchedTests.sortedByDescending { case ->
            case.id
        }

        searchedTestListAdapter = TestsListAdapter(this, sortedTests) { testId ->
            var searchIntent = Intent(this, SearchTestActivity::class.java)
            searchIntent.putExtra(EXTRA_TEST_ID, testId)
            startActivity(searchIntent)
        }
        val layoutManager = LinearLayoutManager(this)
        searchByTitleRecyclerView.adapter = searchedTestListAdapter
        searchByTitleRecyclerView.layoutManager = layoutManager
        searchByTitleRecyclerView.setHasFixedSize(true)
    }

    fun searchByTitleSearchButtonClicked(view: View) {
        val title = searchByTitleTitleText.text.toString()
        RequestService.findTestByTitle(this, title) { complete ->
            if(complete) {
                handleFoundTests()
            } else {
                Log.d("ERROR", "Error occurred for searching test using $title")
            }
        }
    }
}
