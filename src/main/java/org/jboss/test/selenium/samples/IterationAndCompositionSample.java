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
package org.jboss.test.selenium.samples;

import org.jboss.test.selenium.AbstractTestCase;
import org.jboss.test.selenium.locator.JQueryLocator;

public class IterationAndCompositionSample extends AbstractTestCase {
    
    final JQueryLocator LOC_TABLE = new JQueryLocator("#mytable");
    final JQueryLocator LOC_TABLE_ROW = new JQueryLocator("tr");
    final JQueryLocator LOC_COLUMN_2 = new JQueryLocator("td:nth-child(2)"); 
    final JQueryLocator LOC_LINK = LOC_COLUMN_2.getChild(new JQueryLocator("span a"));
    
    
    void usage() {
        final JQueryLocator rows = LOC_TABLE.getChild(LOC_TABLE_ROW);
        
        for (JQueryLocator row : rows.iterateChilds()) {
            final JQueryLocator column2 = row.getDescendant(LOC_LINK);
            
            String text = selenium.getText(column2);
            
            System.out.println(text);
        }
    }
}
