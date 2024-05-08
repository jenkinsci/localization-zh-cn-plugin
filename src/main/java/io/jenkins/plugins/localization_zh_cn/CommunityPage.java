package io.jenkins.plugins.localization_zh_cn;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.UnprotectedRootAction;
import org.jenkinsci.Symbol;

import edu.umd.cs.findbugs.annotations.CheckForNull;

@Extension
@Symbol("chinese")
public class CommunityPage implements UnprotectedRootAction {
    @CheckForNull
    @Override
    public String getIconFileName() {
        return null;
    }

    @NonNull
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
