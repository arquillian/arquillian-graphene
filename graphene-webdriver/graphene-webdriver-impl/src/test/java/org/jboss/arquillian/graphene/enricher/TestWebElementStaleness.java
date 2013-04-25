package org.jboss.arquillian.graphene.enricher;

import static org.mockito.Matchers.any;
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
import org.mockito.runners.MockitoJUnitRunner;
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
            return Answers.RETURNS_SMART_NULLS.get().answer(invocation);
        }
    }
}
