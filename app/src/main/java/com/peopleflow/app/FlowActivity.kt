package com.peopleflow.app

import android.os.Bundle

class FlowActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow)

        if (savedInstanceState == null) {
            replaceByClass(FlowFragment::class.java)
        }
    }
}