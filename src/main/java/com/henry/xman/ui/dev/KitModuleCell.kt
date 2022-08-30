package com.henry.xman.ui.dev

import com.henry.xman.bean.KitConfig
import com.intellij.ui.components.JBTextField
import java.awt.Component
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

        return panel
    }
}