package hudson.plugins.localization;

import org.kohsuke.stapler.Dispatcher;
import org.kohsuke.stapler.RequestImpl;
import org.kohsuke.stapler.ResponseImpl;

import javax.servlet.ServletException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class I18nDispatcher extends Dispatcher {
    @Override
    public boolean dispatch(RequestImpl req, ResponseImpl rsp, Object node) throws IOException, ServletException, IllegalAccessException, InvocationTargetException {
        if(req.getOriginalRequestURI().endsWith("help")) {
//            new I18nRequestDispatcher().forward(req.getRequest(), rsp.getResponse());
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return null;
    }
}