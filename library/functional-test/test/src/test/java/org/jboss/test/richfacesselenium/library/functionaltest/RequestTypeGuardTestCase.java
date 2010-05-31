package org.jboss.test.richfacesselenium.library.functionaltest;

import java.net.MalformedURLException;
import java.net.URL;

import org.jboss.test.selenium.AbstractTestCase;
import org.jboss.test.selenium.guard.request.RequestGuardException;
import org.jboss.test.selenium.locator.ElementLocator;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.jboss.test.selenium.locator.LocatorFactory.*;
import static org.jboss.test.selenium.guard.request.RequestTypeGuardFactory.*;

public class RequestTypeGuardTestCase extends AbstractTestCase {

    ElementLocator linkNoRequest = id("none");
    ElementLocator linkAjaxRequest = id("xhr");
    ElementLocator linkHttpRequest = id("regular");

    @BeforeMethod
    public void openContext() throws MalformedURLException {
        selenium.open(new URL(contextPath, "./reguest-type-guards.jsf"));
    }

    @Test
    public void testNoRequest() {
        guardNoRequest(selenium).click(linkNoRequest);
    }

    @Test
    public void testNoRequestWrong() {
        try {
            guardNoRequest(selenium).click(linkHttpRequest);
            Assert.fail("The NO request was observed, however HTTP request was expected");
        } catch (RequestGuardException e) {
            // should catch exception
        }

        try {
            guardNoRequest(selenium).click(linkAjaxRequest);
            Assert.fail("The NO request was observed, however XHR request was expected");
        } catch (RequestGuardException e) {
            // should catch exception
        }
    }

    @Test
    public void testHttpRequest() {
        guardHttp(selenium).click(linkHttpRequest);
    }

    @Test
    public void testHttpRequestWrong() {
        try {
            guardHttp(selenium).click(linkNoRequest);
            Assert.fail("The HTTP request was observed, however NO request was expected");
        } catch (RequestGuardException e) {
            // should catch exception
        }

        try {
            guardHttp(selenium).click(linkAjaxRequest);
            Assert.fail("The HTTP request was observed, however XHR request was expected");
        } catch (RequestGuardException e) {
            // should catch exception
        }
    }

    @Test
    public void testXhrRequest() {
        guardXhr(selenium).click(linkAjaxRequest);
    }

    @Test
    public void testXhrRequestWrong() {
        try {
            guardXhr(selenium).click(linkNoRequest);
            Assert.fail("The XHR request was observed, however NO request was expected");
        } catch (RequestGuardException e) {
            // should catch exception
        }

        try {
            guardXhr(selenium).click(linkHttpRequest);
            Assert.fail("The XHR request was observed, however HTTP request was expected");
        } catch (RequestGuardException e) {
            // should catch exception
        }
    }
}
