package com.henry.xman.util

import com.intellij.openapi.ui.Messages

/**
 * Date  2022/8/30.
 * Author henry.jia
 * Description
 */
object CommonUtil {

    @JvmStatic
    fun showMsgTip(msg: String, title: String? = null) {
        Messages.showMessageDialog(
            String(msg.toByteArray(), Charsets.UTF_8), title ?: "提示", Messages.getInformationIcon()
        )
    }
}