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
import static org.jboss.test.selenium.locator.LocatorFactory.*;

import org.jboss.test.selenium.locator.IdLocator;
import org.jboss.test.selenium.locator.JQueryLocator;
import org.jboss.test.selenium.waiting.conditions.TextEquals;
import org.jboss.test.selenium.waiting.retrievers.TextRetriever;

/**
 * Sample of usage Wait object to implement waiting to satisfy condition.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class WaitingSample extends AbstractTestCase {

    static final JQueryLocator BUTTON_START = jq("#start");
    static final JQueryLocator BUTTON_INCREMENT = jq(":button");
    static final IdLocator TEXT_COUNT = id("#count");

    final TextEquals countEquals = textEquals.locator(TEXT_COUNT);
    final TextRetriever retrieveCount = retrieveText.locator(TEXT_COUNT);

    void usage() {
        selenium.click(BUTTON_START);

        // selenium-polling waiting
        waitModel.until(elementPresent.locator(TEXT_COUNT));
        assert "0".equals(selenium.getText(TEXT_COUNT));

        selenium.click(BUTTON_INCREMENT);
        // javascript waiting
        waitGui.until(countEquals.text("1"));

        selenium.click(BUTTON_INCREMENT);
        // javascript waiting
        waitGui.until(countEquals.text("2"));

        selenium.click(BUTTON_INCREMENT);
        // selenium-polling retriever usage sample
        String result = waitModel.waitForChangeAndReturn("2", retrieveCount);
        assert "3".equals(result);
    }
}
