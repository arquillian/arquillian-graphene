/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.arquillian.graphene.enricher.findby;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.context.GraphenePageExtensionsContext;
import org.jboss.arquillian.graphene.page.extension.JQueryPageExtension;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class ByJQuery extends By {

    private String jquerySelector;

    private JavascriptExecutor executor = GrapheneContext.getProxyForInterfaces(JavascriptExecutor.class);

    public ByJQuery(String jquerySelector) {
        this.jquerySelector = jquerySelector.replaceAll("\\\"", "\\\\\"");
    }

    public static ByJQuery jquerySelector(String selector) {
        if (selector == null) {
            throw new IllegalArgumentException("Cannot find elements when jquerySelector is null!");
        }
        return new ByJQuery(selector);
    }

    @Override
    public String toString() {
        return "By.jquerySelector " + jquerySelector;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<WebElement> findElements(SearchContext context) {
        installJQueryExtension();

        List<WebElement> elements = new ArrayList<WebElement>();
        try {
            // the element is referenced from parent web element
            if (context instanceof WebElement) {
                elements = (List<WebElement>) executor.executeScript("return Graphene.jQuery(\"" + jquerySelector
                    + "\", arguments[0]).get()", (WebElement) context);
            } else if (context instanceof WebDriver) { // element is not referenced from parent
                elements = (List<WebElement>) executor
                    .executeScript("return Graphene.jQuery(\"" + jquerySelector + "\").get()");
            } else { // other unknown case
                Logger
                    .getLogger(this.getClass().getName())
                    .log(
                        Level.SEVERE,
                        "Cannot determine the SearchContext you are passing to the findBy/s method! It is not instance of WebDriver nor WebElement! It is: "
                            + context);
            }
        } catch (Exception ex) {
            throw new WebDriverException("Can not locate element using selector " + jquerySelector
                + " Check out whether it is correct!", ex);
        }
        return elements;
    }

    @Override
    public WebElement findElement(SearchContext context) {
        installJQueryExtension();

        List<WebElement> elements = findElements(context);
        if (elements == null || elements.size() == 0) {
            throw new NoSuchElementException("Cannot locate element using: " + jquerySelector);
        }
        return elements.get(0);
    }

    private void installJQueryExtension() {
        JQueryPageExtension pageExtension = new JQueryPageExtension();
        GraphenePageExtensionsContext.getRegistryProxy().register(pageExtension);
        GraphenePageExtensionsContext.getInstallatorProviderProxy().installator(pageExtension.getName()).install();
    }
}
