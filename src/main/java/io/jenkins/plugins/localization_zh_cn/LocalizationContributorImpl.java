package io.jenkins.plugins.localization_zh_cn;

import hudson.Extension;
import io.jenkins.plugins.localization.support.LocalizationContributor;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.net.URL;

@Extension
public class LocalizationContributorImpl extends LocalizationContributor {
    @CheckForNull
    @Override
    public URL getResource(@Nonnull String s) {
        return getClass().getClassLoader().getResource(s);
    }
}
