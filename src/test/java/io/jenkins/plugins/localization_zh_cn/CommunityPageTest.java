package io.jenkins.plugins.localization_zh_cn;

import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.xml.sax.SAXException;

import java.io.IOException;

public class CommunityPageTest {
    @Rule
    public JenkinsRule rule = new JenkinsRule();

    @Test
    public void indexPage() throws IOException, SAXException {
        JenkinsRule.WebClient client = rule.createWebClient();
        client.goTo("/chinese");
    }
}
