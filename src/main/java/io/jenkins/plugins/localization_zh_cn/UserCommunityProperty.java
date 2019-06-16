package io.jenkins.plugins.localization_zh_cn;

import hudson.Extension;
import hudson.model.User;
import hudson.model.UserProperty;
import hudson.model.UserPropertyDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import javax.annotation.Nonnull;

public class UserCommunityProperty extends UserProperty {
    private String showCondition = ShowConditions.Chinese.name();

    @DataBoundConstructor
    public UserCommunityProperty(){}

    public String getShowCondition() {
        return showCondition;
    }

    @DataBoundSetter
    public void setShowCondition(String showCondition) {
        this.showCondition = showCondition;
    }

    @Extension
    public static final class DescriptorImpl extends UserPropertyDescriptor {

        @Override
        public UserProperty newInstance(User user) {
            return new UserCommunityProperty();
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Chinese Community";
        }
    }

    public ShowConditions[] getAllConditions() {
        return ShowConditions.values();
    }

    public String getConditionDisplay(ShowConditions conditions) {
        return conditions.getDisplay();
    }

    enum ShowConditions {
        Chinese("Only in Chinese"), Always("Always"), Never("Never");
        private String display;
        ShowConditions(String display) {
            this.display = display;
        }

        public String getDisplay() {
            return display;
        }
    }
}
