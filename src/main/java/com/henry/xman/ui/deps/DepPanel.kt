package com.henry.xman.ui.deps

import com.henry.xman.XMan
import com.henry.xman.bean.DepModuleInfo
import com.henry.xman.util.DepUtil
import com.henry.xman.util.JsonUtil
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import org.jdesktop.swingx.JXTree
import java.awt.Component
import javax.swing.*
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeCellRenderer

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
        val rootNode = DefaultMutableTreeNode("${XMan.project.name}")
        val tree = JXTree(rootNode).apply {
            showsRootHandles = true
            isEditable = false
        }

        tree.cellRenderer = DepTreeCell()

        val nodes = mutableListOf<DefaultMutableTreeNode>()
        //level 1 node
        val levelNode = mutableListOf<DepModuleInfo>()
        DepUtil.getDepsModuleInfo().forEach {
            val node = DefaultMutableTreeNode(it)
            val childNode = DefaultMutableTreeNode("${it.version}")

            //exist node.add child node
            if (levelNode.contains(it)) {
                val index = levelNode.indexOf(it)
                if (index >= 0) {
                    nodes[index].add(childNode)
                }
            } else {
                //not exist,add node to level 1
                levelNode.add(it)
                node.add(childNode)
                nodes.add(node)
            }
        }
        nodes.forEach {
            rootNode.add(it)
        }

        val scrollPane = JBScrollPane(tree).apply {
            verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
            isWheelScrollingEnabled = true
        }


        add(scrollPane)
    }
}