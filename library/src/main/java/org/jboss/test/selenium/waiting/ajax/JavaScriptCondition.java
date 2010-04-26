package org.jboss.test.selenium.waiting.ajax;

import org.jboss.test.selenium.encapsulated.JavaScript;

/**
 * Condition using JavaScript to decide when it is already satisfied.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public interface JavaScriptCondition {
    /**
     * Gets JavaScript which define condition what it should be waited for satisfaction.
     * 
     * @return JavaScript defining condition
     */
    JavaScript getJavaScriptCondition();
}
