package com.henry.xman.util

/**
 * Date  2022/8/30.
 * Author henry.jia
 * Description
 */
object DepUtil {
    private const val DepsKtsGradleName = "Deps.gradle.kts"
    private const val DepsPlugin = "apply from : \"Deps.gradle.kts\""

    /*
val outFile: File by lazy { file("../build/deps.txt") }

project.configurations.all {
    if (!outFile.exists()) {
        outFile.createNewFile()
    }

    resolutionStrategy.eachDependency {
        val depInfo = requested.group + ":" + requested.name + ":" + requested.version
        if (!outFile.readText().contains(depInfo)) {
            outFile.appendText(depInfo + "\n")
        }

    }
}
    */

}