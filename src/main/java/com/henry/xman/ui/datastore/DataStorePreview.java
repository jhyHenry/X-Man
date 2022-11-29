package com.henry.xman.ui.datastore;

import com.henry.xman.XMan;
import com.henry.xman.ui.deps.DepPanel;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;

/**
 * Date  2022/8/30.
 * Author henry.jia
 * Description
 */
public class DataStorePreview extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        XMan.init(project);
        new DepPanel();
    }
}
