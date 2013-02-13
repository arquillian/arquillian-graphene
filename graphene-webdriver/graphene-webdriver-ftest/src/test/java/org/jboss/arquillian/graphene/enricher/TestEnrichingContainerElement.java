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
package org.jboss.arquillian.graphene.enricher;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.graphene.enricher.page.fragment.Panel;
import org.jboss.arquillian.graphene.enricher.page.fragment.TabPanel;
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
public class TestEnrichingContainerElement {

    @Drone
    private WebDriver browser;

    @FindBy(className = "tabpanel")
    private TabPanel tabPanel;

    @Before
    public void loadPage() {
        URL page = this.getClass().getClassLoader()
            .getResource("org/jboss/arquillian/graphene/ftest/enricher/container-elements.html");
        browser.get(page.toString());
    }

    @Test
    public void testTabPanelSwitching() {
        Panel tab3 = tabPanel.switchTo(2);
        ContentOfTab content = tab3.getContent(ContentOfTab.class);
        assertEquals("The tab panel was not switched to third tab correctly!", "Content of the tab 3", content.text.getText());
        
        Panel tab1 = tabPanel.switchTo(0);
        content = tab1.getContent(ContentOfTab.class);
        assertEquals("The tab panel was not switched to first tab correctly!", "Content of the tab 1", content.text.getText());
    }

    private static class ContentOfTab {

        @FindBy(className = "tab-text")
        public WebElement text;
    }
}
