package org.jboss.arquillian.graphene.enricher.fragment;

import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class WrongPageFragmentTooManyRoots {

    @Root
    private WebElement root1;
    
    @Root
    private WebElement root2;
    
    @FindBy(className="randomClassName")
    private WebElement foo;
}
