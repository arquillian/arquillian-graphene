package org.jboss.test.selenium;

import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.waiting.SeleniumWaiting;
import org.jboss.test.selenium.waiting.Wait;
import org.jboss.test.selenium.waiting.ajax.*;
import org.jboss.test.selenium.waiting.conditions.*;
import org.jboss.test.selenium.waiting.retrievers.*;

public class AbstractTestCase {
    AjaxSelenium selenium;
    
    ElementPresent conditionElementPresent = ElementPresent.getInstance();
    TextEquals conditionTextEquals = TextEquals.getInstance();
    
    AttributeRetriever retrieverAttribute = AttributeRetriever.getInstance();
    TextRetriever retrieverText = TextRetriever.getInstance();
    
    SeleniumWaiting waitModelUpdate = Wait.interval(500).timeout(30000);
    AjaxWaiting waitGuiInteraction = Wait.interval(100).timeout(5000);
}
