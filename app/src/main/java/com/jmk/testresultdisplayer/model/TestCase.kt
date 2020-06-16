package com.jmk.testresultdisplayer.model

class TestCase(val id: Int, val title: String, val testrailId: String, val status: String, val jiraInfo: String, val automationDefect: String, val comment: String) {
    override fun toString(): String {
        return "TestCase class with\nid: $id\ntitle: $title\ntestrailId: $testrailId\nstatus: $status\njiraInfo: $jiraInfo\nautomationDefect: $automationDefect\ncomment: $comment"
    }
}