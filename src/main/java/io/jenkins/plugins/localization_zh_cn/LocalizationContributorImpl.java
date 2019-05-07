package io.jenkins.plugins.localization_zh_cn;

import hudson.Extension;
import hudson.PluginWrapper;
import io.jenkins.plugins.localization.support.LocalizationContributor;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.net.URL;

@Extension
public class LocalizationContributorImpl extends LocalizationContributor {
    @CheckForNull
    @Override
    public URL getResource(@Nonnull String s) {
        if (s.startsWith("/")) {
            s = s.substring(1);
        }
        return getClass().getClassLoader().getResource(s);
    }

    @CheckForNull
    @Override
    public URL getPluginResource(@Nonnull String s, @Nonnull PluginWrapper pluginWrapper) {
        return getClass().getClassLoader().getResource("/webapp/" + s);
    }
}
