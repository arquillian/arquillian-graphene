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
package org.jboss.arquillian.ajocado.locator;

import org.jboss.arquillian.ajocado.format.SimplifiedFormat;
import org.jboss.arquillian.ajocado.locator.element.AbstractIterableLocator;
import org.jboss.arquillian.ajocado.locator.element.ElementLocationStrategy;
import org.jboss.arquillian.ajocado.locator.element.ExtendedLocator;
import org.jboss.arquillian.ajocado.locator.element.FilterableLocator;

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
 * <li><tt>:textStartsWith(textPattern)</tt> - trimmed element text are matched to start with given textPattern</li>
 * <li><tt>:textEndsWith(textPattern)</tt> - trimmed element text are matched to end with given textPattern</li>
 * <li><tt>:textEquals(textPattern)</tt> - trimmed element text are compared to exact match with given textPattern</li>
 * </ul>
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class JQueryLocator extends AbstractIterableLocator<JQueryLocator> implements ExtendedLocator<JQueryLocator>,
    FilterableLocator<JQueryLocator> {

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
     * @see org.jboss.arquillian.ajocado.locator.Locator#getLocationStrategy()
     */
    @Override
    public ElementLocationStrategy getLocationStrategy() {
        return ElementLocationStrategy.JQUERY;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.arquillian.ajocado.locator.element.IterableLocator#get(int)
     */
    @Override
    public JQueryLocator get(int index) {
        return new JQueryLocator(SimplifiedFormat.format("{0}:eq({1})", getRawLocator(), index - 1));
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.jboss.arquillian.ajocado.locator.CompoundableLocator#getChild(org.jboss.test.selenium.locator.CompoundableLocator
     * )
     */
    @Override
    public JQueryLocator getChild(JQueryLocator elementLocator) {
        return new JQueryLocator(SimplifiedFormat.format("{0} > {1}", getRawLocator(), elementLocator.getRawLocator()));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.arquillian.ajocado.locator.CompoundableLocator#getDescendant
     * (org.jboss.arquillian.ajocado.locator.CompoundableLocator )
     */
    @Override
    public JQueryLocator getDescendant(JQueryLocator elementLocator) {
        return new JQueryLocator(SimplifiedFormat.format("{0} {1}", getRawLocator(), elementLocator.getRawLocator()));
    }

    @Override
    public JQueryLocator format(Object... args) {
        return (JQueryLocator) super.format(args);
    }

    @Override
    public JQueryLocator filter(String extension) {
        return new JQueryLocator(getRawLocator() + extension);
    }
}
