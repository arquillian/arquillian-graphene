/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.graphene.ftest.enricher.page;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.elements.GrapheneSelect;
import org.jboss.arquillian.graphene.ftest.enricher.page.fragment.PageFragmentWithSpan;
import org.jboss.arquillian.graphene.page.InFrame;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PageWithIFrames {

    @InFrame(nameOrId = "first")
    @FindBy(tagName = "select")
    private GrapheneSelect select;

    @InFrame(nameOrId = "second")
    @FindBy(id = "root")
    private PageFragmentWithSpan myFragment;

    @InFrame(index = 0)
    @FindBy(tagName = "span")
    private WebElement span;

    @FindBy(className = "divElement")
    private WebElement elementInDefaultFrame;

    @Drone
    private WebDriver browser;

    public GrapheneSelect getSelect() {
        return select;
    }

    public PageFragmentWithSpan getMyFragment() {
        return myFragment;
    }

    public WebElement getSpan() {
        return span;
    }

    public WebElement getElementInDefaultFrame() {
        return elementInDefaultFrame;
    }
}
