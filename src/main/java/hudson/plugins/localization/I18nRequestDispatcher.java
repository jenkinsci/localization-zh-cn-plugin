package hudson.plugins.localization;

import org.kohsuke.stapler.RequestImpl;
import org.kohsuke.stapler.lang.Klass;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class I18nRequestDispatcher implements RequestDispatcher {
    private RequestImpl request;
    private Klass<?> type;
    private Object it;
    private String viewName;
    public I18nRequestDispatcher(RequestImpl request, Klass<?> type, Object it, String viewName) {
        this.request = request;
        this.type = type;
        this.it = it;
        this.viewName = viewName;
    }

    @Override
    public void forward(ServletRequest req, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("forward");
        String path = this.request.getOriginalRestOfPath();
        String uri = this.request.getOriginalRequestURI();

//        this.getClass().getResource("/hudson/model/FreeStyleProject/help_zh_CN.html").openStream() == null
        servletResponse.getWriter().println("sff");
//        /jenkins/descriptor/hudson.model.FreeStyleProject/help/concurrentBuild
    }

    @Override
    public void include(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("include");
    }
}