package com.henry.xman.ui.dev

import com.henry.xman.bean.KitConfig
import com.henry.xman.callback.ConfigCallback
import com.henry.xman.util.Utils.getConfigContent
import com.henry.xman.util.Utils.showMsgTip
import com.henry.xman.util.Utils.toObj
import com.henry.xman.util.Utils.updateConfigJson
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.components.JBTextField
import org.apache.http.util.TextUtils
import java.awt.Dimension
import java.awt.GridLayout
import java.awt.event.ActionEvent
import java.io.File
import javax.swing.*


/**
 * Date  2022/8/24.
 * Author henry.jia
 * Description
 */
class DevInfoDialog(private val callback: ConfigCallback? = null) : JDialog() {
    private var sameProject = true
    private var config: KitConfig? = null
    private val moduleInfo = KitConfig.ModuleInfo()
    private val rbLocal by lazy {
        JBRadioButton().apply {
            text = "本项目"
        }
    }
    private val rbOther by lazy {
        JBRadioButton().apply {
            text = "其他项目"
        }
    }
    private val tvDeps by lazy { JBTextField() }
    private val tvVersion by lazy { JBTextField() }
    private val tvDesc by lazy { JBTextField() }
    private val btnSetModule by lazy {
        JButton().apply {
            text = "设置调试模块"
            addActionListener {
                setModulePath()
            }
        }
    }

    init {
        contentPane = contentPane
        isModal = true
        pack()
        preferredSize = Dimension(600, 200)
        title = "配置你的模块依赖信息"

        initPanel()
    }

    private fun initPanel() {
        val panel = JPanel(GridLayout(6, 2))

        val lbSetDeps = JBLabel().apply {
            text = "设置依赖"
        }

        val btnGroup = ButtonGroup()
        btnGroup.add(rbOther)
        btnGroup.add(rbLocal)
        rbLocal.isSelected = sameProject
        rbOther.addActionListener { e -> onRadioChange(e) }
        rbLocal.addActionListener { e -> onRadioChange(e) }

        val lbSetVersion = JBLabel().apply {
            text = "设置版本号"
        }

        val lbsetDesc = JBLabel().apply {
            text = "备注"
        }

        val btnOk = JButton().apply {
            text = "OK"
            addActionListener {
                checkOkInfo()
            }
        }

        panel.apply {
            add(lbSetDeps)
            add(tvDeps)
            add(rbLocal)
            add(rbOther)
            add(lbSetVersion)
            add(tvVersion)
            add(lbsetDesc)
            add(tvDesc)
            add(btnSetModule)
            add(btnOk)
        }
        add(panel)
    }

    /**
     * 设置模块依赖
     */
    private fun setModulePath() {
        val chooser = JFileChooser()
        chooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        chooser.showOpenDialog(this)
        val file = chooser.selectedFile
        if (file.isDirectory) {
            val path = file.absolutePath
            btnSetModule.text = path
            val name = path.substring(path.lastIndexOf(File.separator) + 1)
            moduleInfo.moduleName = name
            moduleInfo.path = path
        }
    }

    private fun checkOkInfo() {
        val configFile = getConfigContent()
        //
        if (TextUtils.isEmpty(configFile)) {
            showMsgTip("configFile is empty", "Error")
            updateConfigJson(null)
            return
        }
        config = toObj(configFile, KitConfig::class.java)
        //
        if (tvDeps.getText().isEmpty()) {
            showMsgTip("your deps info is null", "Error")
            return
        }
        if (moduleInfo.moduleName?.isEmpty() == true) {
            showMsgTip("your ModuleName is null", "Error")
            return
        }
        if (tvVersion.getText().isEmpty()) {
            showMsgTip("your version info is null", "Error")
            return
        }
        moduleInfo.deps = tvDeps.getText()
        moduleInfo.version = tvVersion.getText()
        moduleInfo.desc = tvDesc.getText()
        moduleInfo.sameProject = sameProject
        config?.setModuleDep(moduleInfo)
        //
        updateConfigJson(config)
        callback?.onComplete()
        dispose()
    }

    private fun onRadioChange(e: ActionEvent) {
        sameProject = e.source !== rbOther
    }


}