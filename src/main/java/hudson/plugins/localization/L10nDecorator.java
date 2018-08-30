package hudson.plugins.localization;

import hudson.Extension;
import hudson.model.Hudson;
import hudson.model.PageDecorator;
import org.kohsuke.stapler.WebApp;
import org.kohsuke.stapler.jelly.JellyFacet;

/**
 * {@link PageDecorator} that loading Chinese Localization from current plugin.
 *
 * @author suren
 */
@Extension
public class L10nDecorator extends PageDecorator {
    private final ResourceBundleFactoryImpl bundleFactory = new ResourceBundleFactoryImpl();
    private final Hudson hudson;

    public L10nDecorator() {
        super(L10nDecorator.class);

        // hook into Stapler to activate contributed l10n
        hudson = Hudson.getInstance();
        WebApp webContext = WebApp.get(hudson.servletContext);
        JellyFacet facet = webContext.getFacet(JellyFacet.class);
        facet.resourceBundleFactory = bundleFactory;

        webContext.facets.add(new LocalizationFacet());
    }
}
