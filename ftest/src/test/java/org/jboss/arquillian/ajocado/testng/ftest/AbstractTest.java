package org.jboss.arquillian.ajocado.testng.ftest;

import org.jboss.arquillian.ajocado.testng.AbstractAjocadoTest;
import org.jboss.arquillian.ajocado.utils.URLUtils;
import org.testng.annotations.BeforeMethod;

public class AbstractTest extends AbstractAjocadoTest {

    @BeforeMethod(alwaysRun = true)
    public void openContext() {
        selenium.open(URLUtils.buildUrl(contextPath, "/" + this.getClass().getSimpleName() + ".jsp"));
    }
}
