/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@RunWith(Arquillian.class)
public class TestPageFragmentList {

    @Drone
    private WebDriver browser;

    @FindBy(id="root")
    private RootPageFragment root;

    @FindBy(css="#root span")
    private List<ChildPageFragment> children;

    @FindBy(id="root")
    private List<RootPageFragment> roots;

    @Page
    private PageWithChildren page;

    public void loadPage() {
        URL page = this.getClass().getClassLoader().getResource("org/jboss/arquillian/graphene/ftest/enricher/morepagefragments.html");
        browser.get(page.toString());
    }

    @Test
    public void testInClass() {
        loadPage();
        checkChildren(children);
    }

    @Test
    public void testInListOfPageFragments() {
        loadPage();
        checkChildren(roots.get(0).getChildren());
    }

    @Test
    public void testInPage() {
        loadPage();
        checkChildren(page.getChildren());
    }

    @Test
    public void testInPageFragment() {
        loadPage();
        checkChildren(root.getChildren());

    }

    protected void checkChildren(List<ChildPageFragment> childPageFragments) {
        Assert.assertEquals("Number of child page fragments doesn't match.", 4, childPageFragments.size());
        int index = 0;
        for (ChildPageFragment child: childPageFragments) {
            index++;
            Assert.assertEquals("Inner text of child page fragment <"+index+"> doesn't match", Integer.toString(index), child.getText());
        }
    }

    public static class RootPageFragment {
        @Root
        private WebElement root;
        @FindBy(tagName="span")
        private List<ChildPageFragment> children;

        public List<ChildPageFragment> getChildren() {
            return children;
        }

    }

    public static class ChildPageFragment {
        @Root
        private WebElement root;

        public String getText() {
            return root.getText().trim();
        }
    }

    public static class PageWithChildren {
        @FindBy(css="#root span")
        private List<ChildPageFragment> children;

        public List<ChildPageFragment> getChildren() {
            return children;
        }
    }

}
