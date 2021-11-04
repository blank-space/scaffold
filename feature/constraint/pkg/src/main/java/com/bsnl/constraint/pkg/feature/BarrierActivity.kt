package com.bsnl.constraint.pkg.feature

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.bsnl.constraint.export.path.ConstraintPath
import com.bsnl.constraint.pkg.R

@Route(path = ConstraintPath.A_BARRIER_ACTIVITY)
class BarrierActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feature_constraint_pkg_activity_barrier)
    }
}