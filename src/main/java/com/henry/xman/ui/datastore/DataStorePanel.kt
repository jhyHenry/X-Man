package com.henry.xman.ui.datastore

import com.henry.xman.util.CommonUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
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
    private var jText: JTextField? = null

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
            add(JButton().apply {
                text = "show"
                addActionListener {
                    decodeDataStore()
                }
            })
        }
    }

    override fun createCenterPanel(): JComponent? {
        jText = JTextField().apply {
            text = "result"
        }
        val scrollPane = JBScrollPane(jText).apply {
            preferredSize = Dimension(800, 480)
            verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
            horizontalScrollBarPolicy = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS
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
        val chooser = JFileChooser()
        chooser.fileSelectionMode = JFileChooser.FILES_ONLY
        chooser.showOpenDialog(null)
        val file = chooser.selectedFile
        if (file.isFile) {
            path = file.absolutePath
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
            //linux系统下执行命令,windows环境暂时没测试
            val process = Runtime.getRuntime().exec(arrayOf("bash", "-c", command))
            val inStream = process.inputStream.readAllBytes().toString(Charset.defaultCharset())
            val errorStream = process.errorStream.readAllBytes().toString(Charset.defaultCharset())

            if (errorStream.isNotEmpty()) {
                CommonUtil.showMsgTip(errorStream, "Error")
            }
            jText?.text = inStream
            println("DataStorePb:$inStream")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}