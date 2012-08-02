package org.jboss.arquillian.graphene.condition;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class ElementTextEquals extends AbstractElementAndTextBooleanCondition {

    public ElementTextEquals(WebElement element, String text) {
        super(element, text);
    }

    public ElementTextEquals(WebElement element, String text, boolean negation) {
        super(element, text, negation);
    }

    @Override
    protected Boolean check(WebDriver driver) {
        return getElement().getText().equals(getText());
    }

}
