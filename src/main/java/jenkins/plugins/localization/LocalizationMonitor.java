package jenkins.plugins.localization;

import hudson.PluginWrapper;
import hudson.util.HttpResponses;
import jenkins.model.Jenkins;
import org.jenkinsci.Symbol;
import hudson.Extension;
import hudson.model.AdministrativeMonitor;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.WebApp;
import org.kohsuke.stapler.interceptor.RequirePOST;
import org.kohsuke.stapler.jelly.JellyFacet;
import org.kohsuke.stapler.jelly.ResourceBundleFactory;

import java.io.IOException;
import java.lang.reflect.Method;

@Extension
@Symbol("ChineseLocalization")
public class LocalizationMonitor extends AdministrativeMonitor {
    private PluginWrapper plugin;

    public LocalizationMonitor() {
        super("ChineseLocalization");
    }

    @Override
    public boolean isActivated() {
        Jenkins jenkins = Jenkins.getInstance();
        if(jenkins == null) {
            return false;
        }

        WebApp webContext = WebApp.get(jenkins.servletContext);
        JellyFacet facet = webContext.getFacet(JellyFacet.class);
        ResourceBundleFactory factory = facet.resourceBundleFactory;

        Class<?> factoryClazz = factory.getClass();
        plugin = jenkins.getPluginManager().whichPlugin(factoryClazz);

        return !isOk(factory, factoryClazz);
    }

    private boolean isOk(Object obj, Class<?> clazz) {
        if(obj.getClass().equals(ResourceBundleFactoryImpl.class)) {
            return true;
        }

        try {
            Method method = clazz.getMethod("getParentFactory");
            if(method != null) {
                Object result = method.invoke(obj);
                if(result.getClass().equals(ResourceBundleFactoryImpl.class)) {
                    return true;
                } else if(result instanceof ResourceBundleFactory){
                    return isOk(result, obj.getClass().getSuperclass());
                }
            }
        } catch (NoSuchMethodException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @RequirePOST
    public HttpResponse doAct(StaplerRequest req, StaplerResponse rsp) throws IOException {
        if (req.hasParameter("no")) {
            disable(true);
            return HttpResponses.redirectViaContextPath("/manage");
        } else {
            return HttpResponses.redirectViaContextPath("/configure");
        }
    }

    public PluginWrapper getPlugin() {
        return plugin;
    }
}
