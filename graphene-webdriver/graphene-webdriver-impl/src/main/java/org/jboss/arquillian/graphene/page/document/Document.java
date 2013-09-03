package org.jboss.arquillian.graphene.page.document;

import java.util.List;

import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.openqa.selenium.WebElement;

/**
 * The JavaScript document object and its methods
 *
 * @author Lukas Fryc
 */
@JavaScript("document")
public interface Document {

    String getReadyState();

    String getTitle();

    List<WebElement> getElementsByTagName(String tagName);
}
