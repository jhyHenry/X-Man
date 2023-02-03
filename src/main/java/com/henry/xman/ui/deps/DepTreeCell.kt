package com.henry.xman.ui.deps

import com.henry.xman.bean.DepModuleInfo
import com.intellij.ui.components.JBTextField
import java.awt.Component
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeCellRenderer

/**
 * Date  2022/8/31.
 * Author henry.jia
 * Description
 */
class DepTreeCell : DefaultTreeCellRenderer() {
    override fun getTreeCellRendererComponent(
        tree: JTree, value: Any?, sel: Boolean, expanded: Boolean,
        leaf: Boolean, row: Int, hasFocus: Boolean
    ): Component {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus)
        val node = value as DefaultMutableTreeNode
        if (node.isRoot) {
            tree.expandRow(0)
            return this
        }
        val info = value.userObject as? DepModuleInfo
        if (node.path.size != 2) {
            return this
        }
        return JBTextField().apply {
            text = "${info?.group}:${info?.moduleName}"
        }
    }
}