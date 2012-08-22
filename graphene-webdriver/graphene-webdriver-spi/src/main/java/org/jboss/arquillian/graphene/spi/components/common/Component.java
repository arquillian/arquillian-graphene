package org.jboss.arquillian.graphene.spi.components.common;

import org.openqa.selenium.WebElement;

/**
 * Interface for any web component
 * 
 * @author jhuska
 * 
 */
public interface Component {

    void setRoot(WebElement root);

    WebElement getRoot();
}
