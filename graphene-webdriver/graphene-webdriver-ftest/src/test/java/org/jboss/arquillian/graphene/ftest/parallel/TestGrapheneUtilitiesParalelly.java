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
package org.jboss.arquillian.graphene.ftest.parallel;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import qualifier.Browser1;
import qualifier.Browser2;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestGrapheneUtilitiesParalelly extends AbstractParallelTest {

    @Browser1
    @FindBy(tagName="h1")
    private WebElement header1;

    @Browser2
    @FindBy(tagName="h1")
    private WebElement header2;

    @FindBy(tagName="h1")
    private WebElement headerDefault;

    @Page
    @Browser1
    private SimplePage page1;

    @Page
    @Browser2
    private SimplePage page2;

    @Page
    private SimplePage pageDefault;


    @Test
    public void testWaitWithElements() {
        Graphene.waitGui()
                .until()
                .element(header1)
                .text()
                .equalTo("Page 1");

        Graphene.waitGui()
                .until()
                .element(header2)
                .text()
                .equalTo("Page 2");

        Graphene.waitGui()
                .until()
                .element(headerDefault)
                .text()
                .equalTo("Page Default");
    }

    @Test
    public void testWaitWithBys() {
        Graphene.waitGui(browser1)
                .until()
                .element(By.tagName("h1"))
                .text()
                .equalTo("Page 1");

        Graphene.waitGui(browser2)
                .until()
                .element(By.tagName("h1"))
                .text()
                .equalTo("Page 2");

        Graphene.waitGui(browserDefault)
                .until()
                .element(By.tagName("h1"))
                .text()
                .equalTo("Page Default");
    }

    @Test
    public void testGuardHttp() {
        Graphene.guardHttp(page1).http();
        Graphene.guardHttp(page2).http();
        Graphene.guardHttp(pageDefault).http();
    }

    @Test
    public void testGuardXhr() {
        Graphene.guardXhr(page1).xhr();
        Graphene.guardXhr(page2).xhr();
        Graphene.guardXhr(pageDefault).xhr();
    }

    public static class SimplePage {

        @FindBy
        private WebElement http;

        @FindBy
        private WebElement xhr;

        public void http() {
            http.click();
        }

        public void xhr() {
            xhr.click();
        }
    }

}
