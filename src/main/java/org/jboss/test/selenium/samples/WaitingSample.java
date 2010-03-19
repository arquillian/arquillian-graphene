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
import org.jboss.test.selenium.locator.*;
import org.jboss.test.selenium.waiting.conditions.TextEquals;

public class WaitingSample extends AbstractTestCase {

    final ElementLocator BUTTON_INCREMENT = new JQueryLocator(":button");
    final ElementLocator TEXT_COUNT = new IdLocator("#count");

    final TextEquals conditionTextCountEquals = conditionTextEquals.locator(TEXT_COUNT);

    void usage() {
        assert "0".equals(selenium.getText(TEXT_COUNT));

        selenium.click(BUTTON_INCREMENT);

        waitGuiInteraction.until(conditionTextCountEquals.text("1"));

        selenium.click(BUTTON_INCREMENT);

        waitGuiInteraction.until(conditionTextCountEquals.text("2"));
    }
}
