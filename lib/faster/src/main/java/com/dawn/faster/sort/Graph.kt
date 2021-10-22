package com.dawn.faster.sort

import com.dawn.faster.utils.DispatcherLog
import java.util.*
import kotlin.collections.ArrayList

/**
 * 有向无环图的拓扑排序算法
 */
class Graph(//顶点数
    private val mVerticalCount: Int
) {

    //邻接表
    private val mAdj: Array<MutableList<Int>?>

    /**
     * 添加边
     *
     * @param u from
     * @param v to
     */
    fun addEdge(u: Int, v: Int) {
        mAdj[u]?.add(v)
    }

    /**
     * 拓扑排序
     */
    fun topologicalSort(): Vector<Int> {
        val indegree = IntArray(mVerticalCount)
        for (i in 0 until mVerticalCount) { //初始化所有点的入度数量
            val temp = mAdj[i] as ArrayList<Int>
            for (node in temp) {
                indegree[node]++
            }
        }
        val queue: Queue<Int> = LinkedList()
        for (i in 0 until mVerticalCount) { //找出所有入度为0的点
            if (indegree[i] == 0) {
                queue.add(i)
            }
        }
        var cnt = 0
        val topOrder = Vector<Int>()
        while (!queue.isEmpty()) {
            val u = queue.poll()
            topOrder.add(u)
            for (node in mAdj[u]!!) { //找到该点（入度为0）的所有邻接点
                if (--indegree[node] == 0) { //把这个点的入度减一，如果入度变成了0，那么添加到入度0的队列里
                    queue.add(node)
                }
            }
            cnt++
        }
        check(cnt == mVerticalCount) {  //检查是否有环，理论上拿出来的点的次数和点的数量应该一致，如果不一致，说明有环
            "Exists a cycle in the graph"
        }
        return topOrder
    }

    init {
        mAdj = arrayOfNulls<MutableList<Int>>(mVerticalCount)
        DispatcherLog.i("mVerticalCount:"+mAdj.size)
        for (i in 0 until mVerticalCount) {
            DispatcherLog.i("index:"+i)
            mAdj[i] = ArrayList()
        }
    }
}