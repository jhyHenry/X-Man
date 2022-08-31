package com.henry.xman.util

import com.henry.xman.XMan
import com.henry.xman.bean.KitConfig
import com.intellij.openapi.project.Project
import org.apache.commons.lang.StringEscapeUtils
import java.io.File


object DevUtil {
    private const val ProjectConfigFileName = "kit_config.json"
    private const val DepsConfigGradleName = "config.gradle"
    private const val ConfigPlugin = "apply from : \"config.gradle\""



    @JvmStatic
    fun updateConfigJson(config: KitConfig? = null) {
        XMan.project?.let {
            getConfigFile(it).writeText(JsonUtil.toJson(config ?: KitConfig().apply { projectName = it.name }))
        }
    }

    fun checkConfigGradle(): Boolean {
        XMan.project?.let { p ->
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
                FileUtil.openFile(p, config)
                return true
            }
            return false
        }
        return false
    }

    /**
     * pack your dev dependencies config info(dev flag must open),and update file
     */
    /*
configurations.all {
    resolutionStrategy {
        dependencySubstitution {
            substitute module("androidx.core:core-ktx") with project(":XXX")
        }
    }
}
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
     * 配置setting文件，只针对其他工程项目依赖时配置
     */
    private fun configSettingGradle(content: String) {
        XMan.project?.let {
            val dir = it.basePath + File.separator + "settings.gradle"
            val file = File(dir)
            if (file.exists()) {
                file.appendText(content)
            }
        }
    }

    /**
     * 检查配置文件
     */
    @JvmStatic
    fun getConfigContent(): String {
        XMan.project ?: return ""
        val file = getConfigFile(XMan.project!!)
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
        return JsonUtil.toObj(getConfigContent(), KitConfig::class.java)
    }


}