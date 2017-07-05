package org.jboss.arquillian.graphene.assertions;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebElement;

public class GrapheneAssert extends Assertions {

    public static WebElementAssert assertThat(WebElement webElement) {
        return new WebElementAssert(webElement);
    }

}
