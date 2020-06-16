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
import kotlinx.android.synthetic.main.activity_failing_tests.*

class FailingTestsActivity : AppCompatActivity() {

    lateinit var failingTestsAdapter : TestsListAdapter

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_failing_tests)

        if(supportActionBar != null) { //null check
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        if(RequestService.allTests.count() > 0) {
            handleFailingTests(1)
        } else {
            RequestService.loadAllTests(this) { complete ->
                if (complete) {
                    handleFailingTests(1)
                } else {
                    Log.d("ERROR", "Error occurred for loading all tests.")
                    // show error message on app
                    RequestService.allTests.clear()
                    failingTestsTitle.text =
                        "Failing Tests - Something went wrong for loading tests"
                }
            }
        }
    }

    private fun handleFailingTests(count: Int) {
        Log.d("DEBUG", "Loading all tests complete.")
        failingTestsTitle.text = "Failing Tests"

        val failingTests = ArrayList<TestCase>()

        // iterate through all tests
        for((k, v) in RequestService.allTests) {
            // sort by id (first: earliest case, last: latest case)
            val sortedCase = v.sortedWith(compareBy { case ->
                case.id
            })
            // check if last - num index is failed,
            var addFailTest = true
            for(i in 0 until count) {
                val indexToCheck = sortedCase.count() - 1 - i
                if(!(indexToCheck >= 0 && sortedCase[indexToCheck].status.toLowerCase().contains("fail"))) {
                    addFailTest = false
                }
            }
            if(addFailTest) {
                failingTests.add(sortedCase[sortedCase.count() - 1])
            }
        }

        Log.d("DEBUG", "Failing tests size is ${failingTests.count()}")
        val sortedFailingTests = failingTests.sortedByDescending { case ->
            case.id
        }

        failingTestsAdapter = TestsListAdapter(this, sortedFailingTests) { testId ->
            var searchIntent = Intent(this, SearchTestActivity::class.java)
            searchIntent.putExtra(EXTRA_TEST_ID, testId)
            startActivity(searchIntent)
        }

        val layoutManager = LinearLayoutManager(this)
        failingTestsListRecyclerView.adapter = failingTestsAdapter
        failingTestsListRecyclerView.layoutManager = layoutManager
        failingTestsListRecyclerView.setHasFixedSize(true)
    }

    fun failOneButtonClicked(view: View) {
        handleFailingTests(1)
    }

    fun failTwoButtonClicked(view: View) {
        handleFailingTests(2)
    }

    fun failThreeButtonClicked(view: View) {
        handleFailingTests(3)
    }

    fun failFourButtonClicked(view: View) {
        handleFailingTests(4)
    }

    fun failFiveButtonClicked(view: View) {
        handleFailingTests(5)
    }
}
