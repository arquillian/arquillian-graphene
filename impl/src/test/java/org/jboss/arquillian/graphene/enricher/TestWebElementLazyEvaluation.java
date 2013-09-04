package org.jboss.arquillian.graphene.enricher;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class TestWebElementLazyEvaluation extends AbstractGrapheneEnricherTest {

    @Test
    public void when_WebElement_is_injected_then_it_is_first_evaluated_at_first_interaction() {
        WebElement element = mock(WebElement.class);
        when(browser.findElement(any(By.class))).thenReturn(element);

        TPageFragment fragment = new TPageFragment();
        getGrapheneEnricher().enrich(fragment);

        verifyZeroInteractions(browser, element);

        fragment.getElement().click();
        fragment.getElement().click();

        // the search for element should be done for every invocation
        verify(browser, times(2)).findElement(any(By.class));
        verify(element, times(2)).click();

        verifyNoMoreInteractions(browser, element);
    }

    @Test
    public void when_page_fragment_is_injected_then_searching_for_its_siblings_is_done_for_each_invocation() {
        WebElement rootElement = mock(WebElement.class);
        WebElement element = mock(WebElement.class);
        when(browser.findElement(any(By.class))).thenReturn(rootElement);
        when(rootElement.findElement(any(By.class))).thenReturn(element);

        TPage page = new TPage();
        getGrapheneEnricher().enrich(page);

        verifyZeroInteractions(browser, element);

        page.getFragment().getElement().click();
        page.getFragment().getElement().click();
        page.getFragment().getElement().click();

        // the search for element should be done for every invocation
        verify(browser, times(3)).findElement(any(By.class));
        verify(rootElement, times(3)).findElement(any(By.class));
        verify(element, times(3)).click();

        verifyNoMoreInteractions(browser, element);
    }

    public static class TPage {

        @FindBy(id = "test")
        TPageFragment fragment;

        public TPageFragment getFragment() {
            return fragment;
        }
    }

    public static class TPageFragment {

        @FindBy(id = "test")
        WebElement element;

        public WebElement getElement() {
            return element;
        }
    }
}
