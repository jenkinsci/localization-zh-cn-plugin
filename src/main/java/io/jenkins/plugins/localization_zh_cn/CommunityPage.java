package io.jenkins.plugins.localization_zh_cn;

import hudson.Extension;
import hudson.model.UnprotectedRootAction;
import org.jenkinsci.Symbol;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

@Extension
@Symbol("chinese")
public class CommunityPage implements UnprotectedRootAction {
    @CheckForNull
    @Override
    public String getIconFileName() {
        return null;
    }

    @Nonnull
    @Override
    public String getDisplayName() {
        return "Jenkins Chinese Community";
    }

    @CheckForNull
    @Override
    public String getUrlName() {
        return "chinese";
    }
}
