package com.henry.xman.ui.dev

import com.intellij.ui.components.JBTextField
import com.henry.xman.bean.KitConfig
import java.awt.Component
import javax.swing.JButton
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.ListCellRenderer

class KitModuleCell : ListCellRenderer<KitConfig.ModuleInfo> {

    override fun getListCellRendererComponent(
        list: JList<out KitConfig.ModuleInfo>?,
        item: KitConfig.ModuleInfo,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        val panel = JPanel()
        val name = JBTextField().apply {
            text = "${item.deps}"
        }
        panel.add(name)
//        panel.add(JButton().apply {
//            text = "delete"
//        })

        return panel
    }
}