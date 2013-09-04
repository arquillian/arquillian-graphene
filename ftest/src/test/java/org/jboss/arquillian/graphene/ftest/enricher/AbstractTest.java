package org.jboss.arquillian.graphene.ftest.enricher;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.ftest.Resource;
import org.jboss.arquillian.graphene.ftest.Resources;
import org.jboss.arquillian.graphene.ftest.enricher.page.AbstractPage;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
@RunWith(Arquillian.class)
@RunAsClient
public abstract class AbstractTest<T extends AbstractPage, E> {

    @Page
    protected T pageWithGenericType;

    @Page
    protected E anotherPageWithGenericType;

    @Drone
    protected WebDriver selenium;

    @ArquillianResource
    private URL contextRoot;

    @Deployment
    public static WebArchive createTestArchive() {
        return Resources.inPackage("org.jboss.arquillian.graphene.ftest.pageFragmentsEnricher").all().buildWar("test.war");
    }

    @Before
    public void loadPage() {
        Resource.inPackage("org.jboss.arquillian.graphene.ftest.pageFragmentsEnricher").find("sample.html").loadPage(selenium, contextRoot);
    }

}
