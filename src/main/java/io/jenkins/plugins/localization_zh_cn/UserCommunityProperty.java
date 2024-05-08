package io.jenkins.plugins.localization_zh_cn;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.User;
import hudson.model.UserProperty;
import hudson.model.UserPropertyDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

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

        @NonNull
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
