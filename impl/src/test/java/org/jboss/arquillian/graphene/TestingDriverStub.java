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
package org.jboss.arquillian.graphene;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

/**
 * @author Lukas Fryc
 */
public class TestingDriverStub implements TestingDriver {

    public void get(String url) {

    }

    public String getCurrentUrl() {

        return null;
    }

    public String getTitle() {

        return null;
    }

    public List<WebElement> findElements(By by) {

        return null;
    }

    public WebElement findElement(By by) {

        return null;
    }

    public String getPageSource() {

        return null;
    }

    public void close() {

    }

    public void quit() {

    }

    public Set<String> getWindowHandles() {

        return null;
    }

    public String getWindowHandle() {

        return null;
    }

    public TargetLocator switchTo() {

        return null;
    }

    public Navigation navigate() {

        return null;
    }

    public Options manage() {

        return null;
    }

    public Capabilities getCapabilities() {

        return null;
    }

    public WebElement findElementByClassName(String using) {

        return null;
    }

    public List<WebElement> findElementsByClassName(String using) {

        return null;
    }

    public WebElement findElementByCssSelector(String using) {

        return null;
    }

    public List<WebElement> findElementsByCssSelector(String using) {

        return null;
    }

    public WebElement findElementById(String using) {

        return null;
    }

    public List<WebElement> findElementsById(String using) {

        return null;
    }

    public WebElement findElementByLinkText(String using) {

        return null;
    }

    public List<WebElement> findElementsByLinkText(String using) {

        return null;
    }

    public WebElement findElementByPartialLinkText(String using) {

        return null;
    }

    public List<WebElement> findElementsByPartialLinkText(String using) {

        return null;
    }

    public WebElement findElementByName(String using) {

        return null;
    }

    public List<WebElement> findElementsByName(String using) {

        return null;
    }

    public WebElement findElementByTagName(String using) {

        return null;
    }

    public List<WebElement> findElementsByTagName(String using) {

        return null;
    }

    public WebElement findElementByXPath(String using) {

        return null;
    }

    public List<WebElement> findElementsByXPath(String using) {

        return null;
    }

    public Object executeScript(String script, Object... args) {

        return null;
    }

    public Object executeAsyncScript(String script, Object... args) {

        return null;
    }

    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {

        return null;
    }

}
