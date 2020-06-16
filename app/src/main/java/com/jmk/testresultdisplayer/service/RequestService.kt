package com.jmk.testresultdisplayer.service

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.jmk.testresultdisplayer.model.TestCase
import com.jmk.testresultdisplayer.utilities.URL_GET_TEST
import com.jmk.testresultdisplayer.utilities.URL_GET_TESTS
import com.jmk.testresultdisplayer.utilities.URL_GET_TEST_BY_TITLE
import org.json.JSONException
import kotlin.collections.arrayListOf as arrayListOf

object RequestService {

    val searchedTest = HashMap<String, ArrayList<TestCase>>()
    val allTests = HashMap<String, ArrayList<TestCase>>()

    fun findTestByTitle (context: Context, title: String, complete: (Boolean) -> Unit) {
        val findTestRequest = object: JsonArrayRequest(Method.GET, "${URL_GET_TEST_BY_TITLE}$title", null, Response.Listener { response ->
            searchedTest.clear()
            try {
                for(x in 0 until response.length()) {
                    val test = response.getJSONObject(x)
                    val testObject = TestCase(
                        test.getInt("id"),
                        test.getString("title"),
                        test.getString("testrailId"),
                        test.getString("status"),
                        test.getString("jiraInfo"),
                        test.getString("automationDefect"),
                        test.getString("comment")
                    )
                    val caseId = testObject.testrailId
                    if(searchedTest.containsKey(caseId)) {
                        // add to list
                        val list = searchedTest[caseId]
                        if (list != null) {
                            list.add(testObject)
                            searchedTest[caseId] = list
                        }
                    } else {
                        // create a list and add
                        val list = ArrayList<TestCase>()
                        list.add(testObject)
                        searchedTest[caseId] = list
                    }
                }
            } catch (e: JSONException) {
                Log.d("JSON", "EXC: " + e.localizedMessage)
            }

            complete(true)
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not find test case: ${error.localizedMessage}")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                return super.getHeaders()
            }
        }

        Volley.newRequestQueue(context).add(findTestRequest)
    }
    fun findTestByCaseId(context: Context, caseId: String, complete: (Boolean) -> Unit) {

        val findTestRequest = object: JsonArrayRequest(Method.GET, "${URL_GET_TEST}$caseId", null, Response.Listener { response ->
            searchedTest.clear()
            try {
                for(x in 0 until response.length()) {
                    val test = response.getJSONObject(x)
                    val testObject = TestCase(
                        test.getInt("id"),
                        test.getString("title"),
                        test.getString("testrailId"),
                        test.getString("status"),
                        test.getString("jiraInfo"),
                        test.getString("automationDefect"),
                        test.getString("comment")
                    )
                    val caseId = testObject.testrailId
                    if(searchedTest.containsKey(caseId)) {
                        // add to list
                        val list = searchedTest[caseId]
                        if (list != null) {
                            list.add(testObject)
                            searchedTest[caseId] = list
                        }
                    } else {
                        // create a list and add
                        val list = ArrayList<TestCase>()
                        list.add(testObject)
                        searchedTest[caseId] = list
                    }
                }
            } catch (e: JSONException) {
                Log.d("JSON", "EXC: " + e.localizedMessage)
            }

            complete(true)
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not find test case:  ${error.localizedMessage}")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                return super.getHeaders()
            }
        }

        Volley.newRequestQueue(context).add(findTestRequest)
    }

    fun loadAllTests(context: Context, complete: (Boolean) -> Unit) {
        val findTestRequest = object: JsonArrayRequest(Method.GET, URL_GET_TESTS, null, Response.Listener { response ->
            try {
                for(x in 0 until response.length()) {
                    val test = response.getJSONObject(x)
                    val testObject = TestCase(
                        test.getInt("id"),
                        test.getString("title"),
                        test.getString("testrailId"),
                        test.getString("status"),
                        test.getString("jiraInfo"),
                        test.getString("automationDefect"),
                        test.getString("comment")
                    )
                    val caseId = testObject.testrailId
                    if(allTests.containsKey(caseId)) {
                        // add to list
                        val list = allTests[caseId]
                        if (list != null) {
                            list.add(testObject)
                            allTests[caseId] = list
                        }
                    } else {
                        // create a list and add
                        val list = ArrayList<TestCase>()
                        list.add(testObject)
                        allTests[caseId] = list
                    }
                }
            } catch (e: JSONException) {
                Log.d("JSON", "EXC: " + e.localizedMessage)
            }
            complete(true)
        }, Response.ErrorListener { error ->
            allTests.clear()
            Log.d("ERROR", "Error occurred for getting response for all tests: ${error.localizedMessage}")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                return super.getHeaders()
            }
        }

        Volley.newRequestQueue(context).add(findTestRequest)
    }
}