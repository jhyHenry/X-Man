package com.henry.xman.ui.dev

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBList
import com.henry.xman.bean.KitConfig
import com.henry.xman.callback.ConfigCallback
import com.henry.xman.util.Utils
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.*


/**
 * XDevelop panel
 */
class DevPanel(private val project: Project) : DialogWrapper(project) {
    private val listModule by lazy {
        JBList<KitConfig.ModuleInfo>().apply {
            cellRenderer = KitModuleCell()
            addListSelectionListener {
                DevCtrlDialog(model.getElementAt(leadSelectionIndex)).apply {
                    setLocationRelativeTo(this)
                    isVisible = true
                }
            }
        }
    }

    init {
        init()
        title = "依赖开发"
    }

    override fun createNorthPanel(): JComponent? {
        val north = JPanel()
        north.add(JButton().apply {
            text = "添加依赖模块"
            addActionListener {
                val dialog = DevInfoDialog(object : ConfigCallback {
                    override fun onComplete() {
                        loadKitConfig()
                        listModule.updateUI()
                    }
                }).apply {
                    pack()
                    setLocationRelativeTo(north)

                }
                dialog.isVisible = true
            }
        })
        return north
    }

    override fun createCenterPanel(): JComponent {
        val center = JPanel()

        loadKitConfig()
        val scrollPane = JScrollPane().apply {
            preferredSize = Dimension(200, 200)
            verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
            isWheelScrollingEnabled = true
        }
        scrollPane.setViewportView(listModule)
        center.add(scrollPane, BorderLayout.CENTER)
        return center
    }

    private fun loadKitConfig() {
        val kitConfig = Utils.getProjectConfig()
        kitConfig?.modules?.let {
            val list = DefaultListModel<KitConfig.ModuleInfo>()
            it.forEach { info ->
                list.addElement(info)
            }
            listModule.model = list
        }
    }

    override fun doOKAction() {
        if (!Utils.checkConfigGradle()) {
            return
        }
        super.doOKAction()
    }

    override fun doCancelAction() {
        super.doCancelAction()

    }

}