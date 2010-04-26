package org.jboss.test.selenium.waiting.ajax;

/**
 * <p>
 * Interface for waiting for satisfaction of conditions on page after the Ajax request.
 * </p>
 * 
 * <p>
 * It uses custom JavaScript and {@link com.thoughtworks.selenium.Selenium.Selenium#waitForCondition(String, String)} to
 * wait for satysfing given condition.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public interface AjaxWaiting {

    /**
     * Stars loop waiting to satisfy condition.
     * 
     * @param condition
     *            what wait for to be satisfied
     */
    void until(JavaScriptCondition condition);

    /**
     * Waits until Retrieve's implementation doesn't retrieve value other than oldValue.
     * 
     * @param <T>
     *            type of value what we are waiting for change
     * @param oldValue
     *            value that we are waiting for change
     * @param retrieve
     *            implementation of retrieving actual value
     */
    <T> void waitForChange(T oldValue, JavaScriptRetriever<T> retrieve);

    /**
     * Waits until Retrieve's implementation doesn't retrieve value other than oldValue and this new value returns.
     * 
     * @param <T>
     *            type of value what we are waiting for change
     * @param oldValue
     *            value that we are waiting for change
     * @param retrieve
     *            implementation of retrieving actual value
     * @return new retrieved value
     */
    <T> T waitForChangeAndReturn(final T oldValue, final JavaScriptRetriever<T> retrieve);
}