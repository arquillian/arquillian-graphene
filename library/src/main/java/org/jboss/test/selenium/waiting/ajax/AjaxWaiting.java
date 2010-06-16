package org.jboss.test.selenium.waiting.ajax;

import static org.jboss.test.selenium.framework.AjaxSelenium.getCurrentSelenium;
import static org.jboss.test.selenium.utils.text.SimplifiedFormat.format;

import org.jboss.test.selenium.encapsulated.JavaScript;
import org.jboss.test.selenium.waiting.DefaultWaiting;

/**
 * <p>
 * Implementation of waiting for satisfaction of conditions on page after the Ajax request.
 * </p>
 * 
 * <p>
 * It uses custom JavaScript and {@link com.thoughtworks.selenium.Selenium.Selenium#waitForCondition(String, String)} to
 * wait for satisfying given condition.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class AjaxWaiting extends DefaultWaiting<AjaxWaiting> {

    /**
     * Stars loop waiting to satisfy condition.
     * 
     * @param condition
     *            what wait for to be satisfied
     */
    public void until(JavaScriptCondition condition) {
        getCurrentSelenium().waitForCondition(condition.getJavaScriptCondition(), this.getTimeout());
    }

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
    public <T> void waitForChange(T oldValue, JavaScriptRetriever<T> retrieve) {
        JavaScript waitCondition =
            new JavaScript(format("{0} != '{1}'", retrieve.getJavaScriptRetrieve().getAsString(), oldValue));
        getCurrentSelenium().waitForCondition(waitCondition, this.getTimeout());
    }

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
    public <T> T waitForChangeAndReturn(T oldValue, JavaScriptRetriever<T> retrieve) {
        final String oldValueString = retrieve.getConvertor().forwardConversion(oldValue);
        JavaScript waitingRetriever =
            new JavaScript(format("selenium.waitForCondition({0} != '{1}'); {0}", retrieve.getJavaScriptRetrieve()
                .getAsString(), oldValueString));
        String retrieved = getCurrentSelenium().getEval(waitingRetriever);
        return retrieve.getConvertor().backwardConversion(retrieved);
    }
}