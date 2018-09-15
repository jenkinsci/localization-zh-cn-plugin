package hudson.plugins.localization;

import org.kohsuke.stapler.*;
import org.kohsuke.stapler.lang.Klass;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

public class LocalizationFacet extends Facet {
    @Override
    public void buildViewDispatchers(MetaClass owner, List<Dispatcher> dispatchers) {
        dispatchers.add(new I18nDispatcher());
    }

    @Override
    public RequestDispatcher createRequestDispatcher(RequestImpl request, Klass<?> type, Object it, String viewName) throws IOException {
        RequestDispatcher dispatcher = super.createRequestDispatcher(request, type, it, viewName);
        if(dispatcher == null) {
//            dispatcher = new I18nRequestDispatcher(request, type, it, viewName);
        }

        return dispatcher;
    }

    @Override
    public RequestDispatcher createRequestDispatcher(RequestImpl request, Class type, Object it, String viewName) throws IOException {
        return super.createRequestDispatcher(request, type, it, viewName);
    }

    @Override
    public boolean handleIndexRequest(RequestImpl req, ResponseImpl rsp, Object node, MetaClass nodeMetaClass) throws IOException, ServletException {
        return false;
    }
}