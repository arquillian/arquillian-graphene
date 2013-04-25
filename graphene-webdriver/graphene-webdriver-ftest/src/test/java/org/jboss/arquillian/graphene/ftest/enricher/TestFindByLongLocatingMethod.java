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
package org.jboss.arquillian.graphene.ftest.enricher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.graphene.enricher.findby.How;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
@RunWith(Arquillian.class)
public class TestFindByLongLocatingMethod {

    @Drone
    private WebDriver browser;

    @Before
    public void loadPage() {
        URL page = this.getClass().getClassLoader()
                .getResource("org/jboss/arquillian/graphene/ftest/enricher/empty-findby.html");
        browser.get(page.toString());
    }

    @FindBy(how = How.JQUERY, using = "#divWebElement")
    private WebElement divElementLocatedByGrapheneFindBy;

    @org.openqa.selenium.support.FindBy(how = org.openqa.selenium.support.How.ID, using = "divWebElement")
    private WebElement divElementLocatedBySeleniumFindBy;

    private static final String EXPECTED_TEXT = "WebElement content";

    @Test
    public void testLongLocatingMethodOnGrapheneFindBy() {
        assertNotNull(divElementLocatedByGrapheneFindBy);
        assertEquals(EXPECTED_TEXT, divElementLocatedByGrapheneFindBy.getText());
    }

    @Test
    public void testLongLocatingMethodOnSeleniumFindBy() {
        assertNotNull(divElementLocatedBySeleniumFindBy);
        assertEquals(EXPECTED_TEXT, divElementLocatedBySeleniumFindBy.getText());
    }
}
