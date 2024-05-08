package io.jenkins.plugins.localization_zh_cn;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.PluginWrapper;
import io.jenkins.plugins.localization.support.LocalizationContributor;

import edu.umd.cs.findbugs.annotations.CheckForNull;
import java.net.URL;

@Extension
public class LocalizationContributorImpl extends LocalizationContributor {
    @CheckForNull
    @Override
    public URL getResource(@NonNull String s) {
        if (s.startsWith("/")) {
            s = s.substring(1);
        }
        return getClass().getClassLoader().getResource(s);
    }

    @CheckForNull
    @Override
    public URL getPluginResource(@NonNull String s, @NonNull PluginWrapper pluginWrapper) {
        return getClass().getClassLoader().getResource("webapp/" + s);
    }
}
