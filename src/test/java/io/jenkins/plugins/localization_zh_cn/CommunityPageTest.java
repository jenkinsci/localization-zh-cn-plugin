package io.jenkins.plugins.localization_zh_cn;

import org.htmlunit.ElementNotFoundException;
import org.htmlunit.WebRequest;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.util.UrlUtils;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;

import static org.junit.Assert.*;

public class CommunityPageTest {
    @Rule
    public JenkinsRule rule = new JenkinsRule();

    @Test
    public void indexPage() throws IOException, SAXException {
        JenkinsRule.WebClient client = rule.createWebClient();

        client.goTo("chinese");
    }

    @Test
    public void locale() throws IOException, SAXException {
        JenkinsRule.WebClient client = rule.createWebClient();
        URL rootURL = UrlUtils.toUrlUnsafe(client.getContextPath());

        WebRequest webRequest = new WebRequest(rootURL);
        webRequest.setAdditionalHeader("Accept-Language", Locale.ENGLISH.toString());

        // for English locale
        HtmlPage page = client.getPage(webRequest);
        HtmlAnchor anchor = null;
        try {
            page.getAnchorByText("Jenkins 中文社区");
        } catch (ElementNotFoundException e) {
            // should throw an exception here
        }

        // for Chinese locale
        webRequest.removeAdditionalHeader("Accept-Language");
        webRequest.setAdditionalHeader("Accept-Language", Locale.SIMPLIFIED_CHINESE.toString());
        page = client.getPage(webRequest);
        anchor = page.getAnchorByText("Jenkins 中文社区");
        assertNotNull(anchor);
    }
}
