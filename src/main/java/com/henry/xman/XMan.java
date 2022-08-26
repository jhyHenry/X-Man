package com.henry.xman;

import com.henry.xman.ui.dev.DevPanel;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.henry.xman.util.Utils;


public class XMan extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        Utils.INSTANCE.initProject(project);
        new DevPanel(project).show();
    }

}
