package com.bsnl.constraint.pkg.feature

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bsnl.base.utils.startActivity
import com.bsnl.constraint.pkg.R
import kotlinx.android.synthetic.main.feature_constraint_pkg_activity_main.*


class ConstraintDemoActivity : AppCompatActivity() {

    companion object {
        fun startAction(context: Context) {
            startActivity<ConstraintDemoActivity>(context)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feature_constraint_pkg_activity_main)
        initListener()
    }

    private fun initListener() {
            tv_center.setOnClickListener {
                startActivity(
                    Intent(
                        this@ConstraintDemoActivity,
                        CenterActivity::class.java
                    )
                )
            }


            tv_weight.setOnClickListener {
                startActivity(
                    Intent(
                        this@ConstraintDemoActivity,
                        WeightActivity::class.java
                    )
                )
            }


            tv_baseline.setOnClickListener {
                startActivity(
                    Intent(
                        this@ConstraintDemoActivity,
                        BaseLineActivity::class.java
                    )
                )
            }


            tv_constraint_limit.setOnClickListener {
                startActivity(
                    Intent(
                        this@ConstraintDemoActivity,
                        ConstraintLimitActivity::class.java
                    )
                )
            }

            tv_gone_margin.setOnClickListener {
                startActivity(
                    Intent(
                        this@ConstraintDemoActivity,
                        GoneMarginActivity::class.java
                    )
                )
            }


            tv_bias.setOnClickListener {
                startActivity(
                    Intent(
                        this@ConstraintDemoActivity,
                        BiasActivity::class.java
                    )
                )
            }


            tv_chain.setOnClickListener {
                startActivity(
                    Intent(
                        this@ConstraintDemoActivity,
                        ChainActivity::class.java
                    )
                )
            }


            tv_w_h.setOnClickListener {
                startActivity(
                    Intent(
                        this@ConstraintDemoActivity,
                        WHActivity::class.java
                    )
                )
            }


            tv_percent.setOnClickListener {
                startActivity(
                    Intent(
                        this@ConstraintDemoActivity,
                        PercentActivity::class.java
                    )
                )
            }

            tv_guide_line.setOnClickListener {
                startActivity(
                    Intent(
                        this@ConstraintDemoActivity,
                        GuideLineActivity::class.java
                    )
                )
            }

            tv_group.setOnClickListener {
                startActivity(
                    Intent(
                        this@ConstraintDemoActivity,
                        GroupActivity::class.java
                    )
                )
            }

            tv_layer.setOnClickListener {
                startActivity(
                    Intent(
                        this@ConstraintDemoActivity,
                        LayerActivity::class.java
                    )
                )
            }

            tv_barrier.setOnClickListener {
                startActivity(
                    Intent(
                        this@ConstraintDemoActivity,
                        BarrierActivity::class.java
                    )
                )
            }

            tv_flow.setOnClickListener {
                startActivity(
                    Intent(
                        this@ConstraintDemoActivity,
                        FlowActivity::class.java
                    )
                )
            }

            tv_constraintSet.setOnClickListener {
                startActivity(
                    Intent(
                        this@ConstraintDemoActivity,
                        ConstraintSetActivity::class.java
                    )
                )
            }
        }
}