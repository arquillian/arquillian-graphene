package org.jboss.test.selenium.utils.text.simplifiedFormat;

import org.testng.annotations.Test;

import static org.jboss.test.selenium.locator.LocatorFactory.*;

public class TestFormattingLocators {

    @Test
    public void testBackReference() {
        "input[id$=mojeId]".equals(jq("input[id$=mojeId]").getAsString());
    }
}
