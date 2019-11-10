package io.jenkins.plugins.localization_zh_cn;

import hudson.Extension;
import hudson.model.RootAction;
import jenkins.model.Jenkins;
import org.apache.commons.io.IOUtils;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.interceptor.RequirePOST;

import javax.annotation.CheckForNull;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Extension
public class UpdateCenterAction implements RootAction {
    private final String CRT = "mirror-adapter.crt";

    @RequirePOST
    public void doUse(StaplerResponse response) throws IOException {
        if (!Jenkins.get().hasPermission(Jenkins.ADMINISTER)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try (InputStream input = this.getClass().getResourceAsStream("/" + CRT);
             OutputStream output = new FileOutputStream(new File(Jenkins.get().getRootDir(),
                     "/war/WEB-INF/update-center-rootCAs/" + CRT))) {
            if (input == null) {
                return;
            }

            IOUtils.copy(input, output);
        }
        response.sendRedirect(Jenkins.get().getRootUrl() + "/chinese");
    }

    @RequirePOST
    public void doRemove(StaplerResponse response) throws IOException {
        if (!Jenkins.get().hasPermission(Jenkins.ADMINISTER)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        File crtFile = new File(Jenkins.get().getRootDir(),
                "/war/WEB-INF/update-center-rootCAs/" + CRT);
        if(crtFile.isFile()) {
            crtFile.delete();
        }
        response.sendRedirect(Jenkins.get().getRootUrl() + "/chinese");
    }

    @CheckForNull
    @Override
    public String getIconFileName() {
        return null;
    }

    @CheckForNull
    @Override
    public String getDisplayName() {
        return "Use mirror of update center";
    }

    @CheckForNull
    @Override
    public String getUrlName() {
        return "/update-center-mirror";
    }
}
