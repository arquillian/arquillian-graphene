package org.jboss.arquillian.graphene.page.extension;

import org.jboss.arquillian.graphene.spi.javascript.JavaScript;
import org.jboss.arquillian.graphene.spi.page.PageExtension;
import org.junit.Before;
import org.mockito.Mock;
import java.util.Collections;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import static org.mockito.Mockito.when;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class RemotePageExtensionInstallatorProviderTestCase {

    @Mock(extraInterfaces={JavascriptExecutor.class})
    private WebDriver driver;

    @Before
    public void prepareDriver() {
        when(((JavascriptExecutor) driver).executeScript("install")).thenReturn(null);
        when(((JavascriptExecutor) driver).executeScript("check")).thenReturn(false, true, true);
    }

    @Test
    public void testInstallation() {
        // page extension construction
        PageExtension pageExtensionMock = Mockito.mock(PageExtension.class);
        when(pageExtensionMock.getExtensionScript()).thenReturn(JavaScript.fromString("install"));
        when(pageExtensionMock.getInstallationDetectionScript()).thenReturn(JavaScript.fromString("check"));
        when(pageExtensionMock.getRequired()).thenReturn(Collections.EMPTY_LIST);
        when(pageExtensionMock.getName()).thenReturn("mock");
        // registry
        PageExtensionRegistry registry = new PageExtensionRegistryImpl();
        registry.register(pageExtensionMock);
        // tests
        PageExtensionInstallatorProvider provider = new RemotePageExtensionInstallatorProvider(registry, driver);
        Assert.assertFalse(provider.installator(pageExtensionMock.getName()).isInstalled());
        provider.installator(pageExtensionMock.getName()).install();
        Assert.assertTrue(provider.installator(pageExtensionMock.getName()).isInstalled());
    }

}
