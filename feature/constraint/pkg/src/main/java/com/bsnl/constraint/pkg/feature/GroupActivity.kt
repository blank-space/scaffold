package com.bsnl.constraint.pkg.feature

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bsnl.constraint.pkg.R
import kotlinx.android.synthetic.main.feature_constraint_pkg_activity_group.*

class GroupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feature_constraint_pkg_activity_group)
        btn.setOnClickListener {
            group.visibility = View.GONE
        }
    }
}