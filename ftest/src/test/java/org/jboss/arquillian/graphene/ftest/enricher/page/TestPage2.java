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

import org.jboss.arquillian.graphene.ftest.enricher.page.fragment.AbstractPageFragmentStub;
import org.jboss.arquillian.graphene.page.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class TestPage2 {

    @FindBy(xpath = "//div[@id='rootElement']")
    private AbstractPageFragmentStub abstractPageFragment;

    @FindBy(xpath = "//div[@id='rootElement']")
    private WebElement element;

    @FindBy(xpath = "//input")
    private WebElement input;

    @Page
    private EmbeddedPage embeddedPage;

    public AbstractPageFragmentStub getAbstractPageFragment() {
        return abstractPageFragment;
    }

    public void setAbstractPageFragment(AbstractPageFragmentStub abstractPageFragment) {
        this.abstractPageFragment = abstractPageFragment;
    }

    public WebElement getElement() {
        return element;
    }

    public void setElement(WebElement element) {
        this.element = element;
    }

    public WebElement getInput() {
        return input;
    }

    public void setInput(WebElement input) {
        this.input = input;
    }

    public EmbeddedPage getEmbeddedPage() {
        return embeddedPage;
    }

    public void setEmbeddedPage(EmbeddedPage embeddedPage) {
        this.embeddedPage = embeddedPage;
    }
}
