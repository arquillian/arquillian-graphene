package org.jboss.arquillian.graphene.javascript;

import org.openqa.selenium.WebDriver;

public interface ExecutionResolver {

    Object execute(WebDriver driver, JSCall call);

}
