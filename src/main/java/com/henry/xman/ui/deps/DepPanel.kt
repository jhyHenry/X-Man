package com.henry.xman.ui.deps

import com.henry.xman.util.DepUtil
import com.intellij.ui.components.JBList
import javax.swing.DefaultListModel
import javax.swing.JDialog
import javax.swing.JScrollPane
import javax.swing.ScrollPaneConstants

/**
 * Date  2022/8/30.
 * Author henry.jia
 * Description
 */
class DepPanel : JDialog() {

    init {
        setLocationRelativeTo(parent)
        title = "依赖列表"
        isModal = true
        pack()
        setSize(400, 400)
        loadDeps()
        isVisible = true
    }

    private fun loadDeps() {
        val jList = JBList<String>()
        val scrollPane = JScrollPane().apply {
            verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
            isWheelScrollingEnabled = true
        }
        val list = DefaultListModel<String>()
        DepUtil.getDepsFile().split("\n").forEach {
            list.addElement(it)
        }
        jList.model = list
        scrollPane.setViewportView(jList)

        add(scrollPane)
    }
}