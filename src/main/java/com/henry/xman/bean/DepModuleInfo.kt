package com.henry.xman.bean

import java.io.Serializable

/**
 * Date  2022/8/31.
 * Author henry.jia
 * Description   group + ":" + name + ":" +version
 */
class DepModuleInfo : Serializable {
    var group: String? = ""
    var moduleName: String? = ""
    var version: String? = ""

    override fun equals(other: Any?): Boolean {
        return other is DepModuleInfo && group == other.group && moduleName == other.moduleName
    }
}