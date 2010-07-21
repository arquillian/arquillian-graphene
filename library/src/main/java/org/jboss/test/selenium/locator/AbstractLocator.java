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

import org.apache.commons.lang.Validate;
import org.jboss.test.selenium.locator.type.LocationStrategy;
import static org.jboss.test.selenium.utils.text.SimplifiedFormat.format;

/**
 * <p>
 * Abstract implementation of locator.
 * </p>
 * 
 * <p>
 * Able to return the locator as string for use in Selenium {@link #getAsString()}
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public abstract class AbstractLocator implements Locator {

    private String locator;

    public AbstractLocator(String locator) {
        Validate.notNull(locator);
        this.locator = locator;
    }

    public String getAsString() {
        final LocationStrategy locationStrategy = getLocationStrategy();

        return format("{0}={1}", locationStrategy.getStrategyName(), getRawLocator());
    }

    public String getRawLocator() {
        return locator;
    }

    @Override
    public String toString() {
        return getAsString();
    }
}
