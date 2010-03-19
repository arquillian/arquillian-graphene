package org.jboss.test.selenium.waiting.ajax;

public interface AjaxWaiting {

    /**
     * Stars loop waiting to satisfy condition.
     * 
     * @param condition
     *            what wait for to be satisfied
     */
    public abstract void until(JavaScriptCondition condition);

    /**
     * Waits until Retrieve's implementation doesn't retrieve value other
     * than oldValue.
     * 
     * @param <T>
     *            type of value what we are waiting for change
     * @param oldValue
     *            value that we are waiting for change
     * @param retrieve
     *            implementation of retrieving actual value
     */
    public abstract <T> void waitForChange(T oldValue, JavaScriptRetrieve<T> retrieve);

    /**
     * Waits until Retrieve's implementation doesn't retrieve value other
     * than oldValue and this new value returns.
     * 
     * @param <T>
     *            type of value what we are waiting for change
     * @param oldValue
     *            value that we are waiting for change
     * @param retrieve
     *            implementation of retrieving actual value
     * @return new retrieved value
     */
    public abstract <T> T waitForChangeAndReturn(final T oldValue, final JavaScriptRetrieve<T> retrieve);

}