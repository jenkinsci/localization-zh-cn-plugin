package io.jenkins.plugins.localization_zh_cn;

import hudson.Extension;
import hudson.model.PageDecorator;
import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.StaplerRequest;

import java.util.Locale;

@Extension
public class CommunityDecorator extends PageDecorator {
    public boolean isCurrentLanguage() {
        StaplerRequest req = Stapler.getCurrentRequest();
        if(req == null) {
            return false;
        }

        String languages = req.getHeader("Accept-Language");
        if(languages != null) {
            return languages.startsWith(Locale.SIMPLIFIED_CHINESE.toString());
        }

        return false;
    }
}
