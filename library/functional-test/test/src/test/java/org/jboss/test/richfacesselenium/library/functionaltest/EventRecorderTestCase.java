package org.jboss.test.richfacesselenium.library.functionaltest;

import java.net.MalformedURLException;

import org.jboss.test.selenium.AbstractTestCase;
import org.jboss.test.selenium.waiting.Wait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests the capability of event recoding using PageSpeed automation extension called EventRecorder.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$ * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public class EventRecorderTestCase extends AbstractTestCase {

    @Override
    public void setupEventRecorder() {
        eventRecorder.setEnabled(true);
    }

    @BeforeMethod
    public void openPage() throws MalformedURLException {
        selenium.open(contextPath);
        selenium.waitForPageToLoad(Wait.DEFAULT_TIMEOUT);
    }

    @Test
    public void test() {
        if (!eventRecorder.isExtensionInstalled()) {
            throw new SkipException("EventRecorder extension isn't installed");
        }
    }

    @AfterMethod(dependsOnMethods = "flushRecordedData")
    public void verifyRecordedData() {
        if (!eventRecorder.isExtensionInstalled()) {
            return;
        }
        Assert.assertNotNull(eventRecorder.getRecorderData());
        Assert.assertNotNull(eventRecorder.getRecordedDataHumanReadable());
    }
}
