package com.henry.xman.ui.dev

import com.intellij.ui.components.JBLabel
import com.henry.xman.bean.KitConfig
import com.henry.xman.util.Utils
import java.awt.Color
import java.awt.Dimension
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JPanel
import javax.swing.JToggleButton

class DevCtrlDialog(private val info: KitConfig.ModuleInfo) : JDialog() {

    init {
        contentPane = contentPane
        isModal = true
        pack()
        setSize(250, 120)
        title = "${info.deps}"

        add(JPanel().apply {
            add(JBLabel().apply {
                text = "是否调试本模块"
            })
            val devStateBtn = JToggleButton().apply {
                isSelected = info.dev
                isBorderPainted = false
                preferredSize = Dimension(40, 20)
                text = if (isSelected) "on" else "off"
                background = if (isSelected) Color.GREEN else Color.GRAY
                addChangeListener {
                    text = if (isSelected) "on" else "off"
                    background = if (isSelected) Color.GREEN else Color.GRAY
                }
//            icon = ImageIcon("D:\\ideaworkspace\\SkyProjectKit\\images\\icon_off.png")
//            selectedIcon = ImageIcon("D:\\ideaworkspace\\SkyProjectKit\\images\\icon_on.png")
            }
            add(devStateBtn)
            add(JButton().apply {
                text = "OK"
                info.dev = !devStateBtn.isSelected
                addActionListener {
                    Utils.updateConfigJson(Utils.getProjectConfig()?.apply {
                        setModuleDep(info)
                    })
                    dispose()
                }
            })
        })
    }


}