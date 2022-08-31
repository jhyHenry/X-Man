package com.henry.xman

import com.intellij.openapi.project.Project

/**
 * Date  2022/8/30.
 * Author henry.jia
 * Description
 */
object XMan {
    lateinit var project: Project

    @JvmStatic
    fun init(project: Project) {
        this.project = project
    }
}