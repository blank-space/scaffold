package com.dawn.base.utils

import java.io.File

/**
 * @author : LeeZhaoXing
 * @date   : 2020/10/13
 * @desc   :
 */
object FileUtil {
    /**
     * 创建未存在的文件夹
     *
     * @param file
     * @return
     */
    fun makeDirs(file: File): File? {
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }

    /**
     * 使用递归获取目录文件大小
     *
     * @param dir
     * @return
     */
    fun getDirSize(dir: File?): Long {
        if (dir == null) {
            return 0
        }
        if (!dir.isDirectory) {
            return 0
        }
        var dirSize: Long = 0
        val files = dir.listFiles()
        for (file in files) {
            if (file.isFile) {
                dirSize += file.length()
            } else if (file.isDirectory) {
                dirSize += file.length()
                dirSize += getDirSize(file) // 递归调用继续统计
            }
        }
        return dirSize
    }

    /**
     * 使用递归删除文件夹
     *
     * @param dir
     * @return
     */
    fun deleteDir(dir: File?): Boolean {
        if (dir == null) {
            return false
        }
        if (!dir.isDirectory) {
            return false
        }
        val files = dir.listFiles()
        for (file in files) {
            if (file.isFile) {
                file.delete()
            } else if (file.isDirectory) {
                deleteDir(file) // 递归调用继续删除
            }
        }
        return true
    }


}