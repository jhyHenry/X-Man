package com.henry.xman.util

import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import java.io.File

/**
 * Date  2022/8/29.
 * Author henry.jia
 * Description
 */
object FileUtil {

    /**
     * 打开文件
     */
    fun openFile(project: Project, file: File) {
        val target = LocalFileSystem.getInstance().findFileByIoFile(file)
        if (target != null) {
            OpenFileDescriptor(project, target).navigate(true)
        }
    }

    /**
     * 刷新目录结构，参数：是否异步，是否递归，完成后的回调。不建议获取项目的baseDir，建议针对性的刷新指定目录
     */
    @JvmStatic
    fun refreshDir(file: File) {
        LocalFileSystem.getInstance().findFileByIoFile(file)?.refresh(true, true) {
//
        }
    }
}