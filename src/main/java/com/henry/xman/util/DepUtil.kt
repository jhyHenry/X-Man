package com.henry.xman.utilimport com.henry.xman.XManimport com.henry.xman.bean.DepModuleInfoimport java.io.File/** * Date  2022/8/30. * Author henry.jia * Description */object DepUtil {    private const val DepsKtsGradleName = "Deps.gradle.kts"    private const val DepsPlugin = "apply from : \"Deps.gradle.kts\""    private const val PathDeps = "build/deps.txt"    /*val outFile: File by lazy { file("../build/deps.txt") }project.configurations.all {    if (!outFile.exists()) {        outFile.createNewFile()    }    resolutionStrategy.eachDependency {        val depInfo = requested.group + ":" + requested.name + ":" + requested.version        if (!outFile.readText().contains(depInfo)) {            outFile.appendText(depInfo + "\n")        }    }}    */    fun printDeps() {        XMan.project?.let { p ->            val dir = p.basePath + File.separator + "app"            val path = dir + File.separator + "build.gradle"            val file = File(path)            if (!file.exists()) {                return            }            val str = file.readText()            //check apply from            if (!str.contains(DepsPlugin)) {                file.writeText(StringBuilder(str).append(DepsPlugin).toString())            }            //check config.gradle            val config = File(dir + File.separator + DepsKtsGradleName)            if (!config.exists()) {                config.createNewFile()            }            config.writeText(                """val outFile: File by lazy { file("../build/deps.txt") }project.configurations.all {    if (!outFile.exists()) {        outFile.createNewFile()    }    resolutionStrategy.eachDependency {        val depInfo = requested.group + ":" + requested.name + ":" + requested.version        if (!outFile.readText().contains(depInfo)) {            outFile.appendText(depInfo + "\n")        }    }}"""            )            FileUtil.openFile(XMan.project, config)            return        }    }    fun getDepsModuleInfo(): MutableList<DepModuleInfo> {        val list = mutableListOf<DepModuleInfo>()        getDepsFile().split("\n").forEach {            //group + ":" + name + ":" +version            val info = it.split(":")            if (info.size >= 3) {                list.add(DepModuleInfo().apply {                    group = info[0]                    moduleName = info[1]                    version = info[2]                })            }        }        return list    }    fun getDepsFile(): String {        val file = File(XMan.project.basePath + File.separator + PathDeps)        if (!file.exists()) {            return "请先输出依赖，使用Print Dependencies即可"        }        return file.readText()    }}