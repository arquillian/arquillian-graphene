package org.jboss.test.selenium.utils.testng;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.ITestResult;

/**
 * Obtains informations about status of test, obtains method names from test result.
 * 
 * @author lfryc
 * 
 */
public final class TestInfo {

    /**
     * Mapping of the status ids to string equivalents
     */
    public static final Map<Integer, String> STATUSES = Collections.unmodifiableMap(new TreeMap<Integer, String>() {
        private static final long serialVersionUID = 1L;

        {
            put(ITestResult.FAILURE, "Failure");
            put(ITestResult.SKIP, "Skip");
            put(ITestResult.STARTED, "Started");
            put(ITestResult.SUCCESS, "Success");
            put(ITestResult.SUCCESS_PERCENTAGE_FAILURE, "FailurePercentage");
        }
    });

    private TestInfo() {
    }

    /**
     * Get class + method name from ITestResult
     * 
     * @param result
     *            from the fine-grained listener's method such as onTestFailure(ITestResult)
     * @return the method name in current context
     */
    public static String getMethodName(ITestResult result) {
        String methodName = result.getMethod().toString();

        Matcher matcher = Pattern.compile("(?:.*\\.)?(.*\\..*)\\(.*\\)").matcher(methodName);
        if (matcher.lookingAt()) {
            methodName = matcher.group(1);
        }
        return methodName;
    }
}
