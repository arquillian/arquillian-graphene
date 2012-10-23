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

import java.net.URL;
import java.util.List;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@RunWith(Arquillian.class)
public class TestHandlingOfStaleElements {

    @Drone
    private WebDriver browser;

    @FindBy(id="root")
    private StaleElementPageFragment pageFragment;

    @FindBy(id="root")
    private List<StaleElementPageFragment> pageFragments;

    @FindBy(id="root")
    private WebElement rootElement;
    
    @FindBy(id="root")
    private List<WebElement> rootElements;

    public void loadPage() {
        URL page = this.getClass().getClassLoader().getResource("org/jboss/arquillian/graphene/ftest/enricher/staleelements.html");
        browser.get(page.toString());
    }

    @Test
    public void testElement() throws Exception {
        loadPage();
        rootElement.isDisplayed();
        pageFragment.makeStale();
        rootElement.isDisplayed();
    }

    @Test
    public void testListOfElements() throws Exception {
        loadPage();
        WebElement e = rootElements.get(0);
        e.isDisplayed();
        pageFragment.makeStale();
        e.isDisplayed();
    }

    @Test
    public void testPageFragment() throws Exception {
        loadPage();
        pageFragment.root.isDisplayed();
        pageFragment.makeStale();
        pageFragment.root.isDisplayed();
    }

    @Test
    public void testListOfPageFragments() throws Exception {
        loadPage();
        StaleElementPageFragment pf = pageFragments.get(0);
        pf.root.isDisplayed();
        pf.makeStale();
        pf.root.isDisplayed();
    }

    public static class StaleElementPageFragment {

        @Root
        private WebElement root;

        @FindBy(className="stale")
        private WebElement stale;
        @FindBy(className="make-stale")
        private WebElement makeStale;

        public void makeStale() {
            makeStale.click();
        }

    }

}
