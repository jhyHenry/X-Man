package com.henry.xman.ui.dev;

import com.intellij.openapi.project.Project;
import com.henry.xman.bean.KitConfig;
import com.henry.xman.util.Utils;
import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class ModuleSetDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JRadioButton rbOther;
    private JRadioButton rbLocal;
    private JButton btnSetModule;
    private JTextField tvVersion;
    private JLabel lbVersion;
    private JTextField tvDesc;
    private JLabel lbDesc;
    private JButton btnSetDeps;
    private JTextField tvDeps;
    private JLabel lbSetDpes;

    private Project project;
    private String modulePath = "";
    private Boolean sameProject = true;
    private KitConfig config = null;
    private KitConfig.ModuleInfo moduleInfo = new KitConfig.ModuleInfo();

    public ModuleSetDialog(@NotNull Project project) {
        this.project = project;
        setContentPane(contentPane);
        setModal(true);
        contentPane.setPreferredSize(new Dimension(600, 300));
        getRootPane().setDefaultButton(buttonOK);

        //
        ButtonGroup btnGroup = new ButtonGroup();
        btnGroup.add(rbOther);
        btnGroup.add(rbLocal);
        rbLocal.setSelected(sameProject);

        btnSetModule.addActionListener(e -> {
            setModulePath();
        });

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        rbOther.addActionListener(e -> {
            onRadioChange(e);
        });
        rbLocal.addActionListener(e -> {
            onRadioChange(e);
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void onRadioChange(ActionEvent e) {
        if (e.getSource() == rbOther) {
            sameProject = false;
        } else {
            sameProject = true;
        }
    }

    /**
     * 设置模块依赖
     */
    private void setModulePath() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.showOpenDialog(this);
        File file = chooser.getSelectedFile();
        if (file.isDirectory()) {
            String path = file.getAbsolutePath();
            btnSetModule.setText(path);
            String name = path.substring(path.lastIndexOf(File.separator) + 1);
            moduleInfo.setModuleName(name);
            moduleInfo.setPath(path);
        }
    }

    private void onOK() {
        if (project == null) {
            Utils.showMsgTip("project is null", "Error");
            return;
        }
        String configFile = Utils.getConfigContent();
        //
        if (TextUtils.isEmpty(configFile)) {
            Utils.showMsgTip("configFile is empty", "Error");
            Utils.updateConfigJson(null);
            return;
        }
        config = Utils.toObj(configFile, KitConfig.class);
        //
        if (tvDeps.getText().isEmpty()) {
            Utils.showMsgTip("your deps info is null", "Error");
            return;
        }
        if (moduleInfo.getModuleName().isEmpty()) {
            Utils.showMsgTip("your ModuleName is null", "Error");
            return;
        }
        if (tvVersion.getText().isEmpty()) {
            Utils.showMsgTip("your version info is null", "Error");
            return;
        }
        moduleInfo.setDeps(tvDeps.getText());
        moduleInfo.setVersion(tvVersion.getText());
        moduleInfo.setDesc(tvDesc.getText());
        moduleInfo.setSameProject(sameProject);
        config.setModuleDep(moduleInfo);
        //
        Utils.updateConfigJson(config);
        dispose();
    }

    private void onCancel() {
        dispose();
    }


}
