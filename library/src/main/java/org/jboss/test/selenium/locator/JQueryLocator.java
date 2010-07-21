/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.jboss.test.selenium.locator;

import org.jboss.test.selenium.locator.iteration.ChildElementList;
import org.jboss.test.selenium.locator.iteration.ElementOcurrenceList;
import org.jboss.test.selenium.locator.type.LocationStrategy;
import static org.jboss.test.selenium.utils.text.SimplifiedFormat.format;

/**
 * <p>
 * Locates the element using <a href="http://api.jquery.com/category/selectors/">JQuery Selector</a> syntax.
 * </p>
 * 
 * <p>
 * This syntax is extended in AjaxSelenium by new filters similar to <tt><a
 * href="http://api.jquery.com/contains-selector/">:contains(text)</a></tt>
 * </p>
 * 
 * <ul>
 * <li><tt>:textStartsWith(textPattern)</tt> - trimmed element's text are matched to start with given textPattern</li>
 * <li><tt>:textEndsWith(textPattern)</tt> - trimmed element's text are matched to end with given textPattern</li>
 * <li><tt>:textEquals(textPattern)</tt> - trimmed element's text are compared to exact match with given
 * textPattern</li>
 * </ul>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class JQueryLocator extends AbstractElementLocator implements IterableLocator<JQueryLocator>,
    CompoundableLocator<JQueryLocator> {

    /**
     * Instantiates a new jQuery locator.
     * 
     * @param jquerySelector
     *            the jquery selector
     */
    public JQueryLocator(String jquerySelector) {
        super(jquerySelector);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.test.selenium.locator.Locator#getLocationStrategy()
     */
    public LocationStrategy getLocationStrategy() {
        return LocationStrategy.JQUERY;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.test.selenium.locator.IterableLocator#getNthChildElement(int)
     */
    public JQueryLocator getNthChildElement(int index) {
        return new JQueryLocator(format("{0}:nth-child({1})", getRawLocator(), index + 1));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.test.selenium.locator.IterableLocator#getNthOccurence(int)
     */
    public JQueryLocator getNthOccurence(int index) {
        return new JQueryLocator(format("{0}:eq({1})", getRawLocator(), index));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.test.selenium.locator.IterableLocator#getAllChildren()
     */
    public Iterable<JQueryLocator> getAllChildren() {
        return new ChildElementList<JQueryLocator>(this.getChild(LocatorFactory.jq("*")));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.test.selenium.locator.IterableLocator#getChildren(org.jboss.test.selenium.locator.IterableLocator)
     */
    public Iterable<JQueryLocator> getChildren(JQueryLocator elementLocator) {
        return new ChildElementList<JQueryLocator>(this.getChild(elementLocator));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jboss.test.selenium.locator.IterableLocator#getDescendants(org.jboss.test.selenium.locator.IterableLocator)
     */
    public Iterable<JQueryLocator> getDescendants(JQueryLocator elementLocator) {
        return new ElementOcurrenceList<JQueryLocator>(this.getDescendant(elementLocator));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jboss.test.selenium.locator.CompoundableLocator#getChild(org.jboss.test.selenium.locator.CompoundableLocator)
     */
    public JQueryLocator getChild(JQueryLocator elementLocator) {
        return new JQueryLocator(format("{0} > {1}", getRawLocator(), elementLocator.getRawLocator()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jboss.test.selenium.locator.CompoundableLocator#getDescendant
     * (org.jboss.test.selenium.locator.CompoundableLocator
     * )
     */
    public JQueryLocator getDescendant(JQueryLocator elementLocator) {
        return new JQueryLocator(format("{0} {1}", getRawLocator(), elementLocator.getRawLocator()));
    }

}
