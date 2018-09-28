package jenkins.plugins.localization;

import org.kohsuke.stapler.*;
import org.kohsuke.stapler.lang.Klass;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

public class LocalizationFacet extends Facet {
    @Override
    public void buildViewDispatchers(MetaClass owner, List<Dispatcher> dispatchers) {
    }

    @Override
    public RequestDispatcher createRequestDispatcher(RequestImpl request, Klass<?> type, Object it, String viewName) throws IOException {
        RequestDispatcher dispatcher = super.createRequestDispatcher(request, type, it, viewName);
        if(dispatcher == null) {
            Locale locale = Stapler.getCurrentRequest().getLocale();
            if(!"zh".equals(locale.getLanguage())) {
                return null;
            }

            String uri = request.getOriginalRequestURI();
            if(uri.contains("help")) {
                String name = ((Class) type.clazz).getName();
                String path = name.replace(".", "/");

                String fullPath = path + "/" + viewName + "_zh_CN.html";
                Enumeration<URL> resources = this.getClass().getClassLoader().getResources(fullPath);
                URL targetResource = null;
                while(resources.hasMoreElements()) {
                    URL resource = resources.nextElement();
                    if(resource.getPath().contains("jenkins-core")) {
                        continue;
                    }
                    targetResource = resource;
                    break;
                }

                if(targetResource != null) {
                    dispatcher = new I18nRequestDispatcher(request, targetResource);
                }
            }
        }

        return dispatcher;
    }


    @Override
    public boolean handleIndexRequest(RequestImpl req, ResponseImpl rsp, Object node, MetaClass nodeMetaClass) throws IOException, ServletException {
        return false;
    }
}