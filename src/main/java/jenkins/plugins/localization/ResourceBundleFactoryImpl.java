package jenkins.plugins.localization;

import org.kohsuke.stapler.jelly.ResourceBundle;
import org.kohsuke.stapler.jelly.ResourceBundleFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
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

                    Set<URI> uriSet = new HashSet<>();
                    URL locRes = this.getClass().getResource(localizationResource);
                    if(locRes != null) {
                        try {
                            uriSet.add(locRes.toURI());
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                    Enumeration<URL> resList = clsLoader.getResources(localizationResource);
                    while(resList.hasMoreElements()) {
                        URL item = resList.nextElement();

                        try {
                            URI itemURI = item.toURI();
                            if(itemURI.equals(url.toURI())) {
                                continue;
                            }

                            uriSet.add(itemURI);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    for(URI item : uriSet) {
                        try(InputStream input = item.toURL().openStream()) {
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

    public ResourceBundleFactory getParentFactory() {
        return parentFactory;
    }

    /**
     * Used to force the reloading of a cache.
     */
    private final AtomicInteger reloadModCount = new AtomicInteger();

    public void clearCache() {
        reloadModCount.incrementAndGet();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
