package com.henry.xman.util

import com.google.gson.GsonBuilder
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.LocalFileSystem
import com.henry.xman.bean.KitConfig
import org.apache.commons.lang.StringEscapeUtils
import java.io.File


object Utils {
    private const val ProjectConfigFileName = "kit_config.json"
    private const val DepsConfigGradleName = "config.gradle"
    private const val ConfigPlugin = "apply from : \"config.gradle\""
    private val gson by lazy { GsonBuilder().create() }
    private var project: Project? = null

    fun initProject(project: Project) {
        Utils.project = project
    }

    @JvmStatic
    fun showMsgTip(msg: String, title: String? = null) {
        Messages.showMessageDialog(
            String(msg.toByteArray(), Charsets.UTF_8), title ?: "提示", Messages.getInformationIcon()
        )
    }

    @JvmStatic
    fun updateConfigJson(config: KitConfig? = null) {
        project ?: return
        project?.let {
            getConfigFile(it).writeText(toJson(config ?: KitConfig().apply { projectName = it.name }))
        }
    }

    fun checkConfigGradle(): Boolean {
        project?.let { p ->
            val dir = p.basePath + File.separator + "app"
            val path = dir + File.separator + "build.gradle"
            val file = File(path)
            if (!file.exists()) {
                return false
            }
            val str = file.readText()
            //check apply from : "config.gradle"
            if (!str.contains(ConfigPlugin)) {
                file.writeText(StringBuilder(str).append(ConfigPlugin).toString())
            }

            //check config.gradle
            val config = File(dir + File.separator + DepsConfigGradleName)
            if (!config.exists()) {
                config.createNewFile()
            }
            getProjectConfig()?.modules?.let {
                if (it.isEmpty()) {
                    return false
                }
                config.writeText(packConfigGradle(it))
                openFile(p, config)
                return true
            }
            return false
        }
        return false
    }

    /**
     * pack your dev dependencies config info(dev flag must open),and update file
     */
    private fun packConfigGradle(modules: MutableList<KitConfig.ModuleInfo>): String {
        val sb = StringBuilder()
        val settingSb = StringBuilder()
        modules.forEachIndexed { index, info ->
            if (info.dev) {
                //config settings.gradle
                if (!info.sameProject) {
                    settingSb.append(
                        """

include ":${info.moduleName}"
project(":${info.moduleName}").projectDir = new File("${StringEscapeUtils.escapeJava(info.path)}")
""".trimIndent()
                    )
                }
                //add tab space(format file)
                if (index > 0) {
                    sb.append('\t')
                    sb.append('\t')
                    sb.append('\t')
                }
                sb.append("substitute module(\"${info.deps}\") with project(\":${info.moduleName}\")")
                if (index < modules.size - 1) {
                    sb.appendLine()
                }
            }
        }
        //config settings file for other project dev
        if (settingSb.isNotEmpty()) {
            configSettingGradle(settingSb.toString())
        }
        if (sb.isEmpty()) {
            return ""
        }
        return """
configurations.all {
    resolutionStrategy {
        dependencySubstitution {
            $sb
        }
    }
}
""".trimIndent()
    }

    /**
     *
     */
    private fun configSettingGradle(content: String) {
        project?.let {
            val dir = it.basePath + File.separator + "settings.gradle"
            val file = File(dir)
            if (file.exists()) {
                file.writeText(file.readText() + content)
            }
        }
    }

    /**
     * 检查配置文件
     */
    @JvmStatic
    fun getConfigContent(): String {
        project ?: return ""
        val file = getConfigFile(project!!)
        return file.readText()
    }

    @JvmStatic
    fun getConfigFile(project: Project): File {
        val file = File(project.basePath + File.separator + ProjectConfigFileName)
        if (!file.exists()) {
            file.createNewFile()
        }
        return file
    }

    fun getProjectConfig(): KitConfig? {
        return toObj(getConfigContent(), KitConfig::class.java)
    }

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

    @JvmStatic
    fun <T> toObj(str: String?, cls: Class<T>): T {
        return gson.fromJson(str, cls)
    }

    @JvmStatic
    fun toJson(obj: Any): String {
        return gson.toJson(obj) ?: ""
    }

}