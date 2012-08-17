package org.jboss.arquillian.graphene.spi.components.common;

import org.openqa.selenium.WebElement;

/**
 * RootReference which contains an reference to the root of the particular component. Can be changed at runtime.
 * 
 * @author jhuska
 * 
 */
public class RootReference {

    private WebElement root = null;

    public WebElement get() {
        return root;
    }

    public void set(WebElement root) {
        this.root = root;
    }
}
