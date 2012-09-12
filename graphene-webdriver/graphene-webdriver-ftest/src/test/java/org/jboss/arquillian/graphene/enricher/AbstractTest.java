package org.jboss.arquillian.graphene.enricher;

import java.net.URL;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.enricher.page.AbstractPage;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.WebDriver;

public abstract class AbstractTest<T extends AbstractPage, E> {

    @Page
    protected T pageWithGenericType;

    @Page
    protected E anotherPageWithGenericType;
    
    @Drone
    protected WebDriver selenium;
    
    public void loadPage() {
        URL page = this.getClass().getClassLoader()
            .getResource("org/jboss/arquillian/graphene/ftest/pageFragmentsEnricher/sample.html");

        selenium.get(page.toExternalForm());
    }
    
    
}
