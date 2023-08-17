package io.jenkins.plugins.localization_zh_cn;

import hudson.Extension;
import hudson.model.PageDecorator;
import hudson.model.User;
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
            return languages.startsWith(Locale.SIMPLIFIED_CHINESE.toString())
                    || languages.startsWith("zh-CN");
        }

        return false;
    }

    public boolean showCommunityLink() {
        boolean isCurrentLanguage = isCurrentLanguage();
        User user = User.current();
        if(user == null) {
            return isCurrentLanguage;
        }

        UserCommunityProperty communityProperty = user.getProperty(UserCommunityProperty.class);
        if(communityProperty == null) {
            return isCurrentLanguage;
        }

        String conditionName = communityProperty.getShowCondition();
        UserCommunityProperty.ShowConditions condition = UserCommunityProperty.ShowConditions.valueOf(conditionName);

        switch (condition) {
            case Always:
                return true;
            case Never:
                return false;
            case Chinese:
                return isCurrentLanguage;
        }

        return isCurrentLanguage;
    }
}
