package org.jboss.arquillian.ajocado;

import static org.jboss.arquillian.ajocado.SystemProperties.getSeleniumTimeout;

import org.jboss.arquillian.ajocado.SystemProperties.SeleniumTimeoutType;
import org.jboss.arquillian.ajocado.waiting.Wait;
import org.jboss.arquillian.ajocado.waiting.ajax.AjaxWaiting;
import org.jboss.arquillian.ajocado.waiting.conditions.AlertEquals;
import org.jboss.arquillian.ajocado.waiting.conditions.AlertPresent;
import org.jboss.arquillian.ajocado.waiting.conditions.AttributeEquals;
import org.jboss.arquillian.ajocado.waiting.conditions.AttributePresent;
import org.jboss.arquillian.ajocado.waiting.conditions.CountEquals;
import org.jboss.arquillian.ajocado.waiting.conditions.ElementPresent;
import org.jboss.arquillian.ajocado.waiting.conditions.IsDisplayed;
import org.jboss.arquillian.ajocado.waiting.conditions.StyleEquals;
import org.jboss.arquillian.ajocado.waiting.conditions.TextEquals;
import org.jboss.arquillian.ajocado.waiting.retrievers.AttributeRetriever;
import org.jboss.arquillian.ajocado.waiting.retrievers.TextRetriever;
import org.jboss.arquillian.ajocado.waiting.selenium.SeleniumWaiting;

public class Ajocado {
    public static final int WAIT_GUI_INTERVAL = 100;
    public static final int WAIT_AJAX_INTERVAL = 500;
    public static final int WAIT_MODEL_INTERVAL = 1500;

    /*
     * Waitings
     */
    public static final AjaxWaiting waitGui = Wait.waitAjax.interval(WAIT_GUI_INTERVAL).timeout(
        getSeleniumTimeout(SeleniumTimeoutType.GUI));

    public static final AjaxWaiting waitAjax = Wait.waitAjax.interval(WAIT_AJAX_INTERVAL).timeout(
        getSeleniumTimeout(SeleniumTimeoutType.AJAX));

    public static final SeleniumWaiting waitModel = Wait.waitSelenium.interval(WAIT_MODEL_INTERVAL).timeout(
        getSeleniumTimeout(SeleniumTimeoutType.MODEL));

    /*
     * Wait Conditions
     */
    public static final ElementPresent elementPresent = ElementPresent.getInstance();
    public static final TextEquals textEquals = TextEquals.getInstance();
    public static final StyleEquals styleEquals = StyleEquals.getInstance();
    public static final AttributePresent attributePresent = AttributePresent.getInstance();
    public static final AttributeEquals attributeEquals = AttributeEquals.getInstance();
    public static final AlertPresent alertPresent = AlertPresent.getInstance();
    public static final AlertEquals alertEquals = AlertEquals.getInstance();
    public static final CountEquals countEquals = CountEquals.getInstance();
    public static final IsDisplayed isDisplayed = IsDisplayed.getInstance();

    /*
     * Retrievers
     */
    public static final TextRetriever retrieveText = TextRetriever.getInstance();
    public static final AttributeRetriever retrieveAttribute = AttributeRetriever.getInstance();
}
