package org.jboss.test.richfacesselenium.library.functionaltest;

import org.jboss.test.selenium.AbstractTestCase;
import org.jboss.test.selenium.waiting.Wait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

/**
 * Tests the capability of event recoding using PageSpeed automation extension called EventRecorder.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$ * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public class EventRecorderTestCase extends AbstractTestCase {

    Boolean isCacheEnabled = null;

    @Override
    public void setupEventRecorder() {
        eventRecorder.setEnabled(true);
        eventRecorder.setSaveHumanReadable(true);
    }

    public void openPage() {
        selenium.open(contextPath);
        selenium.waitForPageToLoad(Wait.DEFAULT_TIMEOUT);
        isCacheEnabled = null;
    }

    @Test
    public void testInitial() {
        if (!eventRecorder.isExtensionInstalled()) {
            throw new SkipException("EventRecorder extension isn't installed");
        }
        openPage();
    }

    @Test(dependsOnMethods = "testInitial")
    public void testClearBrowserCache() {
        if (!eventRecorder.isExtensionInstalled()) {
            throw new SkipException("EventRecorder extension isn't installed");
        }
        eventRecorder.clearBrowserCache();
        openPage();
        isCacheEnabled = false;
    }

    @Test(dependsOnMethods = "testClearBrowserCache")
    public void testCacheHits() {
        if (!eventRecorder.isExtensionInstalled()) {
            throw new SkipException("EventRecorder extension isn't installed");
        }
        openPage();
        isCacheEnabled = true;
    }

    @AfterMethod(dependsOnMethods = "flushRecordedData")
    public void verifyRecordedData() {
        if (!eventRecorder.isExtensionInstalled()) {
            return;
        }
        
        Assert.assertNotNull(eventRecorder.getRecorderData());
        Assert.assertNotNull(eventRecorder.getRecordedDataHumanReadable());

        if (isCacheEnabled != null) {
            if (!isCacheEnabled) {
                Assert.assertFalse(eventRecorder.getRecordedDataHumanReadable().contains("CACHE_HIT"));
            }
        }

    }
}
