package com.dawn.sample.pkg.feature.view.coroutine

import android.content.Context
import com.dawn.base.log.L
import com.dawn.base.ui.page.base.BaseActivity
import com.dawn.base.utils.onClick
import com.dawn.base.utils.startActivity
import com.dawn.base.viewmodel.EmptyViewModel
import com.dawn.sample.pkg.databinding.FeatureSamplePkgActivityCoroutineBinding
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

/**
 * @author : LeeZhaoXing
 * @date   : 2022/2/10
 * @desc   :
 */
class CoroutineActivity : BaseActivity<EmptyViewModel, FeatureSamplePkgActivityCoroutineBinding>() {

    override fun initView() {
        getTitleView()?.setTitleText(TAG)

        binding.tvAsync.onClick = {
            asyncTest1()
        }

        binding.tvLaunch.onClick = {
            launchTest3()
        }

        binding.tvAsync2.onClick ={
            asyncTest2()
        }
    }

    private fun launchTest3() {
        L.d("start")
        GlobalScope.launch {
            delay(1000)
            L.d("CoroutineScope.launch")

            //在协程内创建子协程
            launch {
                delay(1500)//1.5秒无阻塞延迟（默认单位为毫秒）
                L.d("launch 子协程")
            }
        }
        L.d("end")
    }


    //获取返回值
    private fun asyncTest1() {
        L.d("start")
        GlobalScope.launch {
            val deferred: Deferred<String> = async {
                //协程将线程的执行权交出去，该线程继续干它要干的事情，到时间后会恢复至此继续向下执行
                delay(2000)//2秒无阻塞延迟（默认单位为毫秒）
                L.d("asyncOne")
                "HelloWord"//这里返回值为HelloWord
            }

            //等待async执行完成获取返回值,此处并不会阻塞线程,而是挂起,将线程的执行权交出去
            //等到async的协程体执行完毕后,会恢复协程继续往下执行
            val result = deferred.await()
            L.d("result == $result")
        }
        L.d("end")
    }

    fun asyncTest2() {
        L.d("start")
        GlobalScope.launch {
            val time = measureTimeMillis {//计算执行时间
                val deferredOne: Deferred<Int> = async {
                    delay(2000)
                    L.d("asyncOne")
                    100//这里返回值为100
                }

                val deferredTwo: Deferred<Int> = async {
                    delay(3000)
                    L.d("asyncTwo")
                    200//这里返回值为200
                }

                val deferredThr: Deferred<Int> = async {
                    delay(4000)
                    L.d("asyncThr")
                    300//这里返回值为300
                }

                //等待所有需要结果的协程完成获取执行结果
                val result = deferredOne.await() + deferredTwo.await() + deferredThr.await()
                L.d("result == $result")
            }
            L.d("耗时 $time ms")
        }
        L.d("end")
    }


    companion object {
        fun actionStart(context: Context) {
            startActivity<CoroutineActivity>(context)
        }
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()

    }

}