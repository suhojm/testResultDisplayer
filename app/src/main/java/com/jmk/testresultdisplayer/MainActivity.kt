package com.jmk.testresultdisplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun mainSearchResultButtonClicked(view: View) {
        val searchTestActivityIntent = Intent(this, SearchTestActivity::class.java)
        startActivity(searchTestActivityIntent);
    }

    fun failingTestsClicked(view: View) {
        val failingTestsActivityIntent = Intent(this, FailingTestsActivity::class.java)
        startActivity(failingTestsActivityIntent)
    }

    fun mainSearchTestsByTitleButtonClicked(view: View) {
        val searchTestsByTitleActivityIntent = Intent(this, SearchTestsByTitleActivity::class.java)
        startActivity(searchTestsByTitleActivityIntent)
    }
}
