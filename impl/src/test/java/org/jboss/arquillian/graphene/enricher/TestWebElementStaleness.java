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
package org.jboss.arquillian.graphene.enricher;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@RunWith(MockitoJUnitRunner.class)
public class TestWebElementStaleness extends AbstractGrapheneEnricherTest {

    private Map<WebElement, Boolean> staleness = new HashMap<WebElement, Boolean>();

    private WebElement rootElement;
    private WebElement webElement;

    private Page page;

    @Before
    public void prepare() {
        rootElement = mock(WebElement.class, new StaleElementAnswer());
        webElement = mock(WebElement.class, new StaleElementAnswer());

        when(browser.findElement(any(By.class))).thenReturn(rootElement);
        when(rootElement.findElement(any(By.class))).thenReturn(webElement);

        page = new Page();
        getGrapheneEnricher().enrich(page);
    }

    @Test
    public void test_fragment_subelement_is_stale() {
        // when
        makeStale(webElement);
        page.getFragment().getElement().click();

        // then
        verify(browser, times(2)).findElement(any(By.class));
        verify(rootElement, times(2)).findElement(any(By.class));
        verify(webElement, times(2)).click();

        verifyNoMoreInteractions(browser, webElement);
    }

    @Test
    public void test_page_where_page_fragment_reference_is_stale() {
        // when
        makeStale(rootElement);
        makeStale(webElement);
        page.getFragment().getElement().click();

        // then
        verify(browser, times(2)).findElement(any(By.class));
        verify(rootElement, times(2)).findElement(any(By.class));
        verify(webElement, times(2)).click();

        verifyNoMoreInteractions(browser, webElement);
    }

    private boolean isStale(WebElement element) {
        Boolean stale = staleness.get(element);
        stale = (stale == null) ? false : stale;
        if (stale) {
            staleness.put(element, false);
        }
        return stale;
    }

    private void makeStale(WebElement element) {
        staleness.put(element, true);
    }

    private static class Page {

        @FindBy(id = "test")
        private PageFragment fragment;

        public PageFragment getFragment() {
            return fragment;
        }
    }

    public static class PageFragment {

        @FindBy(id = "test")
        private WebElement element;

        public WebElement getElement() {
            return element;
        }
    }

    public class StaleElementAnswer implements Answer<Object> {

        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
            if (isStale((WebElement) invocation.getMock())) {
                throw new StaleElementReferenceException("");
            }
            return Answers.RETURNS_SMART_NULLS.answer(invocation);
        }
    }
}
