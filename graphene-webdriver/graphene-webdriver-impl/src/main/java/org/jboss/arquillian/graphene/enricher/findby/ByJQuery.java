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

import java.util.List;
import org.jboss.arquillian.core.spi.Validate;
import org.jboss.arquillian.graphene.javascript.JSInterfaceFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class ByJQuery extends By {

    private final String jquerySelector;
    private final JQuerySearchContext jQuerySearchContext = JSInterfaceFactory.create(JQuerySearchContext.class);

    public ByJQuery(String jquerySelector) {
        Validate.notNull(jquerySelector, "Cannot find elements when jquerySelector is null!");
        this.jquerySelector = jquerySelector;
    }

    public static ByJQuery jquerySelector(String selector) {
        return new ByJQuery(selector);
    }

    @Override
    public String toString() {
        return "By.jquerySelector " + jquerySelector;
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        List<WebElement> elements;
        try {
            // the element is referenced from parent web element
            if (context instanceof WebElement) {
                elements = jQuerySearchContext.findElementsInElement(jquerySelector, (WebElement) context);
            } else if (context instanceof WebDriver) { // element is not referenced from parent
                elements = jQuerySearchContext.findElements(jquerySelector);
            } else { // other unknown case
                throw new WebDriverException(
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
        List<WebElement> elements = findElements(context);
        if (elements == null || elements.isEmpty()) {
            throw new NoSuchElementException("Cannot locate element using: " + jquerySelector);
        }
        return elements.get(0);
    }

}
