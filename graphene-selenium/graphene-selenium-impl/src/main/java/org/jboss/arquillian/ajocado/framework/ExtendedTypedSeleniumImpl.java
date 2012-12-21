/**
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.ajocado.framework;

import java.util.LinkedList;
import java.util.List;

import org.jboss.arquillian.ajocado.browser.BrowserType;
import org.jboss.arquillian.ajocado.css.CssResolver;
import org.jboss.arquillian.ajocado.geometry.Point;
import org.jboss.arquillian.ajocado.locator.attribute.AttributeLocator;
import org.jboss.arquillian.ajocado.locator.element.ElementLocationStrategy;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;
import org.jboss.arquillian.ajocado.locator.element.IterableLocator;

/**
 * Type-safe selenium wrapper for Selenium API with extension of some useful commands defined in {@link ExtendedSelenium}
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class ExtendedTypedSeleniumImpl extends TypedSeleniumImpl implements ExtendedTypedSelenium {

    private boolean started = false;

    protected ExtendedSelenium getExtendedSelenium() {
        if (selenium instanceof ExtendedSelenium) {
            return (ExtendedSelenium) selenium;
        } else {
            throw new UnsupportedOperationException("Assigned Selenium isn't instance of ExtendedSelenium");
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.arquillian.ajocado.framework.ExtendedTypedSelenium#isStarted()
     */
    @Override
    public boolean isStarted() {
        return started;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.arquillian.ajocado.framework.TypedSeleniumImpl#start()
     */
    @Override
    public void start() {
        List<String> parameters = getDerivedParameters();
        StringBuffer parametersString = new StringBuffer();
        for (String parameter : parameters) {
            if (parametersString.length() > 0) {
                parametersString.append(',');
            }
            parametersString.append(parameter);
        }
        selenium.start(parametersString.toString());
        started = true;
    }

    private List<String> getDerivedParameters() {
        List<String> parameters = new LinkedList<String>();

        // network traffic enabled
        if (configuration.isSeleniumNetworkTrafficEnabled()) {
            parameters.add("captureNetworkTraffic=true");
        }

        // browser is of type chrome (http://seleniumhq.org/docs/05_selenium_rc.html)
        if (BrowserType.GOOGLE_CHROME == configuration.getBrowser().getType()) {
            parameters.add("commandLineFlags=--disable-web-security");
        }

        // other user-defined start parameters
        String startParameters = configuration.getStartParameters();
        if (startParameters != null && startParameters.length() != 0) {
            parameters.add(startParameters);
        }

        return parameters;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.arquillian.ajocado.framework.TypedSeleniumImpl#stop()
     */
    @Override
    public void stop() {
        super.stop();
        started = false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.arquillian.ajocado.framework.ExtendedTypedSelenium
     * #getStyle(org.jboss.test.selenium.locator.ElementLocator , org.jboss.arquillian.ajocado.css.CssProperty)
     */
    @Override
    public <R, T extends CssResolver<R>> R getStyle(ElementLocator<?> elementLocator, T cssResolver) {
        final String propertyValue = getExtendedSelenium().getStyle(elementLocator.inSeleniumRepresentation(),
                cssResolver.getPropertyName());
        return cssResolver.resolveProperty(propertyValue);
    }

    /*
     * (non-Javadoc)
     *
     * @see ExtendedTypedSelenium#scrollIntoView(org.jboss.arquillian.ajocado.locator.ElementLocator , boolean)
     */
    @Override
    public void scrollIntoView(ElementLocator<?> elementLocator, boolean alignToTop) {
        getExtendedSelenium().scrollIntoView(elementLocator.inSeleniumRepresentation(), String.valueOf(alignToTop));
    }

    /*
     * (non-Javadoc)
     *
     * @see ExtendedTypedSelenium#mouseOverAt(org.jboss.arquillian.ajocado.locator.ElementLocator ,
     * org.jboss.arquillian.ajocado.geometry.Point)
     */
    @Override
    public void mouseOverAt(ElementLocator<?> elementLocator, Point point) {
        getExtendedSelenium().mouseOverAt(elementLocator.inSeleniumRepresentation(), point.inSeleniumRepresentation());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.test.selenium.framework.ExtendedTypedSelenium#mouseOutAt(org.jboss.test.selenium.locator.ElementLocator ,
     * org.jboss.test.selenium.geometry.Point)
     */
    @Override
    public void mouseOutAt(ElementLocator<?> elementLocator, Point point) {
        getExtendedSelenium().mouseOutAt(elementLocator.inSeleniumRepresentation(), point.inSeleniumRepresentation());
    }

    /*
     * (non-Javadoc)
     *
     * @see ExtendedTypedSelenium#belongsClass(org.jboss.arquillian.ajocado.locator.ElementLocator , java.lang.String)
     */
    @Override
    public boolean belongsClass(ElementLocator<?> elementLocator, String className) {
        return getExtendedSelenium().belongsClass(elementLocator.inSeleniumRepresentation(), className);
    }

    /*
     * (non-Javadoc)
     *
     * @see ExtendedTypedSelenium#isAttributePresent(org.jboss.arquillian.ajocado.locator. AttributeLocator)
     */
    @Override
    public boolean isAttributePresent(AttributeLocator<?> attributeLocator) {
        final String elementLocator = attributeLocator.getAssociatedElement().inSeleniumRepresentation();
        final String attributeName = attributeLocator.getAttribute().getAttributeName();
        return getExtendedSelenium().isAttributePresent(elementLocator, attributeName);
    }

    /*
     * (non-Javadoc)
     *
     * @see DefaultTypedSelenium#getCount(org.jboss.arquillian.ajocado.locator.IterableLocator)
     */
    @Override
    public int getCount(IterableLocator<?> locator) {
        if (locator.getLocationStrategy() == ElementLocationStrategy.JQUERY) {
            return getExtendedSelenium().getJQueryCount(locator.getRawLocator()).intValue();
        }

        try {
            return super.getCount(locator);
        } catch (UnsupportedOperationException e) {
            throw new UnsupportedOperationException("Only JQuery and XPath locators are supported for counting");
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.arquillian.ajocado.framework.ExtendedTypedSelenium#check(org.jboss.test.selenium.locator.ElementLocator ,
     * boolean)
     */
    @Override
    public void check(ElementLocator<?> locator, boolean checked) {
        if (checked) {
            check(locator);
        } else {
            uncheck(locator);
        }
    }

    @Override
    public void doCommand(String command, String param1, String param2) {
        getExtendedSelenium().doCommand(command, param1, param2);
    }
}