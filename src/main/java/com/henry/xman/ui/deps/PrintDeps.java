package com.henry.xman.ui.deps;

import com.henry.xman.XMan;
import com.henry.xman.util.DepUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;

/**
 * Date  2022/8/30.
 * Author henry.jia
 * Description
 */
public class PrintDeps extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);
        XMan.init(project);
        DepUtil.INSTANCE.printDeps();
    }
}
