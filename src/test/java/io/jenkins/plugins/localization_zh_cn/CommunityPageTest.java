package io.jenkins.plugins.localization_zh_cn;

import org.htmlunit.ElementNotFoundException;
import org.htmlunit.WebRequest;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.util.UrlUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

import java.net.URL;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@WithJenkins
class CommunityPageTest {

    private JenkinsRule rule;

    @BeforeEach
    void beforeEach(JenkinsRule rule) {
        this.rule = rule;
    }

    @Test
    void indexPage() throws Exception {
        try (JenkinsRule.WebClient client = rule.createWebClient()) {
            client.goTo("chinese");
        }
    }

    @Test
    void noLinkToTheClosedCommunityWebsite() throws Exception {
        try (JenkinsRule.WebClient client = rule.createWebClient()) {
            HtmlPage page = client.goTo("chinese");
            assertFalse(page.getWebResponse().getContentAsString().contains("jenkins-zh.cn/about"));
        }
    }

    @Test
    void locale() throws Exception {
        try (JenkinsRule.WebClient client = rule.createWebClient()) {
            URL rootURL = UrlUtils.toUrlUnsafe(client.getContextPath());

            WebRequest webRequest = new WebRequest(rootURL);
            webRequest.setAdditionalHeader("Accept-Language", Locale.ENGLISH.toString());

            // for English locale
            HtmlPage englishPage = client.getPage(webRequest);
            // should throw an exception here
            assertThrows(ElementNotFoundException.class,
                () -> englishPage.getAnchorByText("Jenkins 中文社区"));

            // for Chinese locale
            webRequest.removeAdditionalHeader("Accept-Language");
            webRequest.setAdditionalHeader("Accept-Language", Locale.SIMPLIFIED_CHINESE.toString());
            HtmlPage chinesePage = client.getPage(webRequest);
            HtmlAnchor anchor = chinesePage.getAnchorByText("Jenkins 中文社区");
            assertNotNull(anchor);
        }
    }
}
