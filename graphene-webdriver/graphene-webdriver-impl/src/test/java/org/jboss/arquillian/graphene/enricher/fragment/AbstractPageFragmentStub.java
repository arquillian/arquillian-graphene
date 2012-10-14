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
package org.jboss.arquillian.graphene.enricher.fragment;

import java.util.List;

import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * 
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * 
 */
public class AbstractPageFragmentStub {

    @Root
    private WebElement root;
    
    @FindBy(className = "classNameRef")
    private WebElement locatorRefByClassName;

    @FindBy(name = "nameRef")
    private WebElement locatorRefByName;

    @FindBy(id = "idRef")
    private WebElement locatorRefById;

    @FindBy(tagName = "tagNameRef")
    private WebElement locatorRefByTagName;

    @FindBy(linkText = "linkTextRef")
    private WebElement locatorRefByLinkText;

    @FindBy(partialLinkText = "partiaLinkTextRef")
    private WebElement locatorRefByPartialLinkText;

    @FindBy(xpath = "//div[@class='refByXpath']")
    private WebElement locatorRefByXPath;

    @FindBy(css = "cssSelectorRef")
    private WebElement locatorRefByCssSelector;
    
    @FindBy(className="spans")
    private List<WebElement> spansInPageFragment;

    public String invokeMethodOnRoot() {
        return root.getText();
    }

    public String invokeMethodOnElementRefByClass() {
        return this.locatorRefByClassName.getText();
    }

    public String invokeMethodOnElementRefById() {
        return this.locatorRefById.getText();
    }

    public String invokeMethodOnElementRefByCSS() {
        return this.locatorRefByCssSelector.getText();
    }

    public String invokeMethodOnElementRefByName() {
        return this.locatorRefByName.getText();
    }

    public String invokeMethodOnElementRefByTagName() {
        return locatorRefByTagName.getAttribute("id");
    }

    public String invokeMethodOnElementRefByXpath() {
        return this.locatorRefByXPath.getText();
    }

    public String invokeMethodOnElementRefByLinkText() {
        return this.locatorRefByLinkText.getText();
    }

    public String invokeMethodOnElementRefByPartialLinkText() {
        return this.locatorRefByPartialLinkText.getText();
    }

    public WebElement getRootProxy() {
        return this.root;
    }

    public WebElement getLocatorRefByClassName() {
        return locatorRefByClassName;
    }

    public void setLocatorRefByClassName(WebElement locatorRefByClassName) {
        this.locatorRefByClassName = locatorRefByClassName;
    }

    public WebElement getLocatorRefByName() {
        return locatorRefByName;
    }

    public void setLocatorRefByName(WebElement locatorRefByName) {
        this.locatorRefByName = locatorRefByName;
    }

    public WebElement getLocatorRefById() {
        return locatorRefById;
    }

    public void setLocatorRefById(WebElement locatorRefById) {
        this.locatorRefById = locatorRefById;
    }

    public WebElement getLocatorRefByTagName() {
        return locatorRefByTagName;
    }

    public void setLocatorRefByTagName(WebElement locatorRefByTagName) {
        this.locatorRefByTagName = locatorRefByTagName;
    }

    public WebElement getLocatorRefByLinkText() {
        return locatorRefByLinkText;
    }

    public void setLocatorRefByLinkText(WebElement locatorRefByLinkText) {
        this.locatorRefByLinkText = locatorRefByLinkText;
    }

    public WebElement getLocatorRefByPartialLinkText() {
        return locatorRefByPartialLinkText;
    }

    public void setLocatorRefByPartialLinkText(WebElement locatorRefByPartialLinkText) {
        this.locatorRefByPartialLinkText = locatorRefByPartialLinkText;
    }

    public WebElement getLocatorRefByXPath() {
        return locatorRefByXPath;
    }

    public void setLocatorRefByXPath(WebElement locatorRefByXPath) {
        this.locatorRefByXPath = locatorRefByXPath;
    }

    public WebElement getLocatorRefByCssSelector() {
        return locatorRefByCssSelector;
    }

    public void setLocatorRefByCssSelector(WebElement locatorRefByCssSelector) {
        this.locatorRefByCssSelector = locatorRefByCssSelector;
    }

    public List<WebElement> getSpansInPageFragment() {
        return spansInPageFragment;
    }

    public void setSpans(List<WebElement> divs) {
        this.spansInPageFragment = divs;
    }

}
