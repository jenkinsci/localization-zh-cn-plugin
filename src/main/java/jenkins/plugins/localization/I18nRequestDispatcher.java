package jenkins.plugins.localization;

import hudson.Util;
import org.apache.commons.io.IOUtils;
import org.kohsuke.stapler.RequestImpl;
import org.kohsuke.stapler.lang.Klass;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;

public class I18nRequestDispatcher implements RequestDispatcher {
    private RequestImpl request;
    private Klass<?> type;
    private Object it;
    private String viewName;
    private URL targetResource;
    public I18nRequestDispatcher(RequestImpl request, Klass<?> type, Object it, String viewName, URL targetResource) {
        this.request = request;
        this.type = type;
        this.it = it;
        this.viewName = viewName;
        this.targetResource = targetResource;
    }

    @Override
    public void forward(ServletRequest req, ServletResponse rsp) throws ServletException, IOException {
        rsp.setContentType("text/html;charset=UTF-8");
        try (InputStream in = targetResource.openStream()) {
            String literal = IOUtils.toString(in,"UTF-8");
            rsp.getWriter().println(Util.replaceMacro(literal,
                    Collections.singletonMap("rootURL", this.request.getContextPath())));
        }
    }

    @Override
    public void include(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
    }
}