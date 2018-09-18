package hudson.plugins.localization;

import org.kohsuke.stapler.jelly.ResourceBundle;
import org.kohsuke.stapler.jelly.ResourceBundleFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * {@link ResourceBundleFactory} to inject contributed localization.
 * 
 * @author suren
 */
final class ResourceBundleFactoryImpl extends ResourceBundleFactory {
    private ResourceBundleFactory parentFactory;

    public ResourceBundleFactoryImpl(ResourceBundleFactory parentFactory) {
        this.parentFactory = parentFactory;
    }

    @Override
    public ResourceBundle create(String baseName) {
        final ResourceBundle resourceBundle = this.parentFactory.create(baseName);

        return new ResourceBundle(baseName) {
            private int modCount = reloadModCount.get();
            @Override
            protected Properties get(String key) {
                int mc = reloadModCount.get();
                if (modCount!=mc) {
                    ResourceBundleFactoryImpl.this.clearCache();
                    modCount = mc;
                }

                Properties originPro = super.get(key);

                ClassLoader clsLoader = this.getClass().getClassLoader();

                try {
                    URL url = new URL(this.getBaseName());
                    String localizationResource = null;
                    if("jar".equals(url.getProtocol())) {
                        String resource = this.getBaseName().replaceFirst(".*jar!", "");
                        localizationResource = resource + key + ".properties";
                    } else if("file".equals(url.getProtocol())) {
                        // for local testing
                        String resource = this.getBaseName().replaceFirst(".*src/main/resources/", "");
                        localizationResource = resource + key + ".properties";
                    }

                    if(localizationResource == null) {
                        return originPro;
                    }

                    Set<URL> urls = new HashSet<>();
                    URL locRes = this.getClass().getResource(localizationResource);
                    if(locRes != null) {
                        urls.add(locRes);
                    }
                    Enumeration<URL> resList = clsLoader.getResources(localizationResource);
                    while(resList.hasMoreElements()) {
                        URL item = resList.nextElement();
                        if(item.equals(url)) {
                            continue;
                        }

                        urls.add(item);
                    }

                    for(URL item : urls) {
                        try(InputStream input = item.openStream()) {
                            if(input != null) {
                                originPro.load(input);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return originPro;
            }

            @Override
            public String getFormatString(Locale locale, String key) {
                String text =  super.getFormatString(locale, key);
                if(text == null) {
                    text = resourceBundle.getFormatString(locale, key);
                }
                return text;
            }

            @Override
            public String getFormatStringWithoutDefaulting(Locale locale, String key) {
                String text = super.getFormatStringWithoutDefaulting(locale, key);
                if(text == null) {
                    text = resourceBundle.getFormatStringWithoutDefaulting(locale, key);
                }
                return text;
            }
        };
    }

    /**
     * Used to force the reloading of a cache.
     */
    private final AtomicInteger reloadModCount = new AtomicInteger();

    public void clearCache() {
        reloadModCount.incrementAndGet();
    }
}
