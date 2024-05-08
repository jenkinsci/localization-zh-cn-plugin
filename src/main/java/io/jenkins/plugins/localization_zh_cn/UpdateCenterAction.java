package io.jenkins.plugins.localization_zh_cn;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.Extension;
import hudson.model.RootAction;
import java.nio.charset.StandardCharsets;
import jenkins.model.Jenkins;
import org.apache.commons.io.IOUtils;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.interceptor.RequirePOST;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.logging.Logger;

@Extension
public class UpdateCenterAction implements RootAction {
    private final String CRT = "mirror-adapter.crt";

    @RequirePOST
    @SuppressFBWarnings(value = {"NP_LOAD_OF_KNOWN_NULL_VALUE", "RCN_REDUNDANT_NULLCHECK_OF_NULL_VALUE"}, justification = "Spotbugs doesn't grok try-with-resources")
    public void doUse(StaplerResponse response) throws IOException {
        if (!Jenkins.get().hasPermission(Jenkins.ADMINISTER)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        ServletContext context = Jenkins.get().servletContext;
        if (context == null) {
            LOGGER.warning("cannot get the servlet context when use the mirror certificate");
            return;
        }

        URL caRoot = context.getResource("/WEB-INF/update-center-rootCAs");
        String crtPath = caRoot.getFile() + CRT;
        String decodedCrtPath = URLDecoder.decode(crtPath, StandardCharsets.UTF_8);
        try (InputStream input = this.getClass().getResourceAsStream("/" + CRT);
             OutputStream output = new FileOutputStream(decodedCrtPath)) {
            if (input == null) {
                LOGGER.warning("no mirror certificate found");
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
            if(!crtFile.delete()) {
                LOGGER.warning("Failed to delete file " + crtFile.getAbsolutePath());
            }
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

    private static final Logger LOGGER = Logger.getLogger(UpdateCenterAction.class.getName());
}
