package com.bsnl.constraint.pkg.feature

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.bsnl.constraint.pkg.R

class GoneMarginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feature_constraint_pkg_activity_gone_margin)
    }

    fun onClick(view: View) {
        findViewById<TextView>(R.id.tv_1).visibility =View.GONE
    }
}