package org.jboss.arquillian.graphene.ftest.enricher;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.InitialPage;
import org.jboss.arquillian.graphene.page.Location;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(Arquillian.class)
@RunAsClient
public class TestPageObjectsLocationWithoutDeployment {

    @Drone
    private WebDriver browser;

    @Test
    public void testGoto() {
        Graphene.goTo(SeleniumHub.class);
    }

    @Test
    public void testInitialPage(@InitialPage SeleniumHub hub) {
    }

    @Location("http://127.0.0.1:4444/")
    public static class SeleniumHub {
    }
}
