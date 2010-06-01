package org.jboss.test.richfacesselenium.library.functionaltest;

import org.jboss.test.selenium.AbstractTestCase;
import org.jboss.test.selenium.waiting.Wait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static org.jboss.test.selenium.utils.testng.TestInfo.getMethodName;

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
        eventRecorder.setSaveHumanReadable(true);
    }

    public void openPage() {
        selenium.open(contextPath);
        selenium.waitForPageToLoad(Wait.DEFAULT_TIMEOUT);
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
    }

    @Test(dependsOnMethods = "testClearBrowserCache")
    public void testCacheHits() {
        if (!eventRecorder.isExtensionInstalled()) {
            throw new SkipException("EventRecorder extension isn't installed");
        }
        openPage();
    }

    @AfterMethod(dependsOnMethods = "flushRecordedData")
    public void verifyRecordedData(ITestResult result) {
        final String methodName = getMethodName(result);
        final boolean isCacheEnabled = !"EventRecorderTestCase.testClearBrowserCache".equals(methodName);

        Assert.assertNotNull(eventRecorder.getRecorderData());
        Assert.assertNotNull(eventRecorder.getRecordedDataHumanReadable());

        Assert.assertEquals(isCacheEnabled, eventRecorder.getRecordedDataHumanReadable().contains("CACHE_HIT"));

    }
}
