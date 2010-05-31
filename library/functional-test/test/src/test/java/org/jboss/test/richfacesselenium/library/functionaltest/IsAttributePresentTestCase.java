package org.jboss.test.richfacesselenium.library.functionaltest;
import java.net.MalformedURLException;
import java.net.URL;

import org.jboss.test.selenium.AbstractTestCase;

import org.jboss.test.selenium.locator.Attribute;
import org.jboss.test.selenium.locator.AttributeLocator;
import org.jboss.test.selenium.locator.IdLocator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.Assert;

import com.thoughtworks.selenium.SeleniumException;
import static org.jboss.test.selenium.utils.text.SimplifiedFormat.format;

import static org.jboss.test.selenium.locator.LocatorFactory.*;

public class IsAttributePresentTestCase extends AbstractTestCase {

    final IdLocator noRequest = id("none");
    final AttributeLocator noRequestHref = noRequest.getAttribute(Attribute.HREF);
    final AttributeLocator noRequestStyle = noRequest.getAttribute(Attribute.STYLE);

    final IdLocator notExists = id("no-such-element");
    final AttributeLocator notExistsClass = notExists.getAttribute(Attribute.CLASS);

    final String expectedMessage = "ERROR: element is not found";
        
    @BeforeMethod
    public void openContext() throws MalformedURLException {
        selenium.open(new URL(contextPath, "./reguest-type-guards.jsf"));
    }
    
    @Test
    public void testAttributePresent() {
        Assert.assertTrue(selenium.isAttributePresent(noRequestHref));
    }

    @Test
    public void testAttributeNotPresent() {
        Assert.assertFalse(selenium.isAttributePresent(noRequestStyle));
    }

    @Test
    public void testElementNotPresent() {
        try {
            selenium.isAttributePresent(notExistsClass);
            Assert.fail("should raise a exception pointing that there is not such element");
        } catch (SeleniumException e) {
            if (!expectedMessage.equals(e.getMessage())) {
                Assert.fail(format("message should be '{0}'", expectedMessage));
            }
        }
    }
    
    @Test
    public void testExposedMember() {
        Assert.assertTrue(attributePresent.locator(noRequestHref).isTrue());
        Assert.assertFalse(attributePresent.locator(noRequestStyle).isTrue());
        
        try {
            attributePresent.locator(notExistsClass).isTrue();
            Assert.fail("should raise a exception pointing that there is not such element");
        } catch (SeleniumException e) {
            if (!expectedMessage.equals(e.getMessage())) {
                Assert.fail(format("message should be '{0}'", expectedMessage));
            }
        }
    }
}
