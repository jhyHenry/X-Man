package com.henry.xman.bean

import java.io.Serializable

/**
 *
 */
class KitConfig : Serializable {
    var projectName: String? = ""
    var modules: MutableList<ModuleInfo>? = null

    fun setModuleDep(info: ModuleInfo) {
        if (modules == null) {
            modules = mutableListOf()
        }
        if (modules!!.contains(info)) {
            modules!!.remove(info)
        }
        modules!!.add(info)
    }

    class ModuleInfo {
        var deps: String? = ""//
        var moduleName: String? = ""
        var path: String? = ""
        var version: String? = ""
        var desc: String? = ""//
        var dev = false//
        var sameProject = false//

        override fun equals(other: Any?): Boolean {
            return other is ModuleInfo && deps == other.deps
        }
    }
}