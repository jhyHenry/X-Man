package com.henry.xman.ui.datastore

import com.henry.xman.util.CommonUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import org.apache.commons.lang.StringEscapeUtils
import java.awt.BorderLayout
import java.awt.Dimension
import java.nio.charset.Charset
import javax.swing.*

/**
 * Date  2022/11/29.
 * Author henry.jia
 * Description
 */
class DataStorePanel(private val project: Project) : DialogWrapper(project) {
    private var path = ""
    private val command get() = """protoc --decode_raw < ${StringEscapeUtils.escapeJava(path)}"""
    private var jList: JBList<String>? = null

    init {
        //一定要先调用init()
        init()
        title = "DataStore预览"
        isModal = true
        pack()
        setSize(800, 600)
    }

    override fun createNorthPanel(): JComponent {
        return JPanel().apply {
            add(JButton().apply {
                text = "设置DataStore路径"
                addActionListener {
                    setDataStorePath()
                }
            })
        }
    }

    override fun createCenterPanel(): JComponent? {
        jList = JBList<String>().apply {
            setBounds(120, 50, 100, 50)
        }
        val scrollPane = JBScrollPane(jList).apply {
            preferredSize = Dimension(720, 480)
            verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
            isWheelScrollingEnabled = true
        }
        return JPanel().apply {
            add(scrollPane, BorderLayout.CENTER)
        }
    }

    /**
     * 设置路径
     */
    private fun setDataStorePath() {
        val chooser = JFileChooser().apply {
            fileSelectionMode = JFileChooser.FILES_ONLY
        }
        val flag = chooser.showOpenDialog(null)
        val file = chooser.selectedFile
        if (file.isFile) {
            path = file.absolutePath
        }
        if (flag == JFileChooser.APPROVE_OPTION) {
            decodeDataStore()
        }
    }

    /**
     * 解析DataStore pb文件
     * todo 解析key-value  tree
     */
    private fun decodeDataStore() {
        if (path.isEmpty()) {
            CommonUtil.showMsgTip("DataStore path is null", "Error")
            return
        }
        try {
            //Linux("Linux"),Mac_OS("Mac OS"),Mac_OS_X("Mac OS X"),Windows("Windows")
            val cmd = if (System.getProperty("os.name").contains("Linux")) {
                //linux
                arrayOf("bash", "-c", command)
            } else if (System.getProperty("os.name").contains("Mac")) {
                //mac
                arrayOf("/bin/bash", "-c", command)
            } else {
                //windows
                arrayOf("cmd", "/c", command)
            }

            val process = Runtime.getRuntime().exec(cmd)

            val inStream = process.inputStream.readAllBytes().toString(Charset.defaultCharset())
            val errorStream = process.errorStream.readAllBytes().toString(Charset.defaultCharset())

            if (errorStream.isNotEmpty()) {
                CommonUtil.showMsgTip(errorStream, "Error")
            }
            //  1: "agree_state"  格式：1：key
            //  2 {                    2{  不定值:value }
            //    1: 1
            //  }
            val keyValues = inStream.split("1 {").filter {
                it.isNotEmpty()
            }.map {
                //去掉前后空格、}
                it.substring(1, it.length - 2)
            }.map {
                val array = it.split("2 {")
                val key = array[0].substring(array[0].indexOfFirst { it == '"' }).trim()
                val value = array[1].substring(array[1].indexOf(":") + 1).trim().dropLast(1).trim()
                "K-V:$key - $value"
            }
            jList?.setListData(keyValues.toTypedArray())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}