package com.bsnl.constraint.pkg.feature

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.bsnl.constraint.pkg.R

class ConstraintSetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feature_constraint_pkg_activity_start)

    }

    fun onClick(view: View) {
        val constraintLayout = view as ConstraintLayout
        val constraintSet = ConstraintSet().apply {
            isForceId =false
            clone(this@ConstraintSetActivity, R.layout.feature_constraint_pkg_activity_end)
        }
        constraintSet.applyTo(constraintLayout)

    }
}