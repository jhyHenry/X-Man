package com.henry.xman.util

import com.google.gson.GsonBuilder

/**
 * Date  2022/8/29.
 * Author henry.jia
 * Description
 */
object JsonUtil {
    private val gson by lazy { GsonBuilder().create() }

    @JvmStatic
    fun <T> toObj(str: String?, cls: Class<T>): T {
        return gson.fromJson(str, cls)
    }

    @JvmStatic
    fun toJson(obj: Any): String {
        return gson.toJson(obj) ?: ""
    }
}