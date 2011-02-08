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
package org.jboss.arquillian.ajocado.samples;

import org.jboss.arquillian.ajocado.AbstractTestCase;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.*;

/**
 * Sample of iteration and composition of element locators.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class IterationAndCompositionSample extends AbstractTestCase {

    static final JQueryLocator LOC_TABLE = jq("#mytable");
    static final JQueryLocator LOC_COLUMN_2 = jq("td:nth-child(2)");
    static final JQueryLocator LOC_LINK = LOC_COLUMN_2.getChild(jq("span a"));

    void usage() {
        Iterable<JQueryLocator> rows;

        rows = LOC_TABLE.getChildren(jq("tr"));

        for (JQueryLocator row : rows) {
            final JQueryLocator column2 = row.getDescendant(LOC_LINK);

            String text = selenium.getText(column2);

            System.out.println(text);
        }
    }
}
