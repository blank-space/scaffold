package com.bsnl.constraint.pkg.feature

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bsnl.constraint.pkg.R

class LayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feature_constraint_pkg_activity_layer)

       /* btn.setOnClickListener {
            layer.translationY = 500f
            layer.alpha = 0.5f
        }*/
    }
}