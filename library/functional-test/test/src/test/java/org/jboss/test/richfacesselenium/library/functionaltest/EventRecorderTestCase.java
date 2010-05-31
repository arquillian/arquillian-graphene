package org.jboss.test.richfacesselenium.library.functionaltest;

import static org.jboss.test.selenium.utils.testng.TestInfo.getMethodName;

import java.net.MalformedURLException;

import org.jboss.test.selenium.AbstractTestCase;
import org.jboss.test.selenium.waiting.Wait;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class EventRecorderTestCase extends AbstractTestCase {

    @BeforeClass(dependsOnMethods = {"initializeEventRecorder", "initializeExtensions"})
    public void startEventRecording() {
        if (!eventRecorder.isExtensionInstalled()) {
            throw new SkipException("EventRecorder extension isn't installed");
        }
        eventRecorder.open();
    }

    @BeforeMethod
    public void openPage() throws MalformedURLException {
        selenium.open(contextPath);
        selenium.waitForPageToLoad(Wait.DEFAULT_TIMEOUT);
    }

    @Test
    public void test() {
    }

    @AfterMethod
    public void flushRecordedData(ITestResult result) {
        String methodName = getMethodName(result);
        eventRecorder.stopProfiler();
        eventRecorder.flushEvents(methodName);
    }

    @AfterClass
    public void finishEventRecording() {
        eventRecorder.close();
    }
}
