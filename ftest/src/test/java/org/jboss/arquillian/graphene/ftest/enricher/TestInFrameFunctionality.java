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
package org.jboss.arquillian.graphene.ftest.enricher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.graphene.ftest.Resource;
import org.jboss.arquillian.graphene.ftest.Resources;
import org.jboss.arquillian.graphene.ftest.enricher.page.PageWithIFrames;
import org.jboss.arquillian.graphene.ftest.enricher.page.PageWithIFrames2;
import org.jboss.arquillian.graphene.ftest.enricher.page.fragment.PageFragmentWithSpan;
import org.jboss.arquillian.graphene.page.InFrame;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import qualifier.Browser1;
import qualifier.Browser2;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
@RunWith(Arquillian.class)
@RunAsClient
public class TestInFrameFunctionality {

    @Drone
    private WebDriver browser;

    @Page
    private PageWithIFrames page;

    @Page
    @InFrame(index = 0)
    private PageWithIFrames2 page2;

    @InFrame(nameOrId = "second")
    @FindBy(id = "root")
    private PageFragmentWithSpan myFragment;

    @InFrame(index = 0)
    @FindBy(tagName = "span")
    private WebElement span;

    @InFrame(index = 0)
    @FindByJQuery("span")
    private WebElement spanByJQuery;

    @FindBy(className = "divElement")
    private WebElement elementInDefaultFrame;

    @Browser1
    @Drone
    protected WebDriver browser1;

    @Browser2
    @Drone
    protected WebDriver browser2;

    @Browser2
    @Page
    private PageWithIFrames pageMultipleBrowsers;

    @Browser1
    @Page
    @InFrame(index = 0)
    private PageWithIFrames2 page2MultipleBrowsers;

    @Browser2
    @InFrame(nameOrId = "second")
    @FindBy(id = "root")
    private PageFragmentWithSpan myFragmentMultipleBrowsers;

    @Browser1
    @InFrame(index = 0)
    @FindBy(tagName = "span")
    private WebElement spanMultipleBrowsers;

    @Browser2
    @FindBy(className = "divElement")
    private WebElement elementInDefaultFrameMultipleBrowsers;

    @ArquillianResource
    private URL contextRoot;

    @Deployment
    public static WebArchive createTestArchive() {
        return Resources.inCurrentPackage().all().buildWar("test.war");
    }

    @Before
    public void loadPage() {
        loadTheRightPage(browser);
        loadTheRightPage(browser1);
        loadTheRightPage(browser2);
    }

    private void loadTheRightPage(WebDriver browser) {
        Resource.inCurrentPackage().find("inframe.html").loadPage(browser, contextRoot);
    }

    private static final String EXPECTED_WEB_ELEMENT_IN_FRAME_TEXT = "not correct";
    private static final String EXPECTED_WEB_ELEMENT_OUTSIDE_IFRAME_TEXT = "Outsider div element";
    private static final String EXPECTED_SELECT_FIRST_OPTION_TEXT = "option one";

    @Test
    public void testWebElementInFrameDefinedByIndex() {
        checkWebElementInFrame(page.getSpan());
    }

    @Test
    public void testWebElementInFrameDefinedByIndexMethodParam(@Page PageWithIFrames pageParam) {
        checkWebElementInFrame(pageParam.getSpan());
    }

    @Test
    public void testInFrameOverPageObject1() {
        checkWebElementInFrame(page2.getSpan());
    }

    @Test
    public void testInFrameOverPageObject1MethodParam(@Page @InFrame(index = 0) PageWithIFrames2 page2Param) {
        checkWebElementInFrame(page2Param.getSpan());
    }

    @Test
    public void testInFrameOverPageObject2() {
        checkWebElementInFrame(page2.getTextFromSpan());
    }

    @Test
    public void testInFrameOverPageObject2MethodParaem(@Page @InFrame(index = 0) PageWithIFrames2 page2Param) {
        checkWebElementInFrame(page2Param.getTextFromSpan());
    }

    @Test
    public void testInFrameOverPageObject4() {
        // it is deprecated to access fields in this way
        checkWebElementInFrame(page2.getSpan());
    }

    @Test
    public void testInFrameOverPageObject4MethodParam(@Page @InFrame(index = 0) PageWithIFrames2 page2Param) {
        // it is deprecated to access fields in this way
        checkWebElementInFrame(page2Param.getSpan());
    }

    @Test
    public void testInFrameOverPageObjectMultipleBrowsers() {
        Resources.inCurrentPackage();
        checkWebElementInFrame(page2MultipleBrowsers.getSpan());
    }

    @Test
    public void testInFrameOverPageObjectMultipleBrowsersMethodParam(
        @Browser1 @Page @InFrame(index = 0) PageWithIFrames2 page2MultipleBrowsersParam) {
        Resources.inCurrentPackage();
        checkWebElementInFrame(page2MultipleBrowsersParam.getSpan());
    }

    @Test
    public void testWebElementInFrameDeclaredInTest() {
        checkWebElementInFrame(span);
    }

    @Test
    public void testWebElementInFrameFoundByJQueryDeclaredInTest() {
        checkWebElementInFrame(spanByJQuery);
    }

    @Test
    public void testWebElementInFrameDeclaredInTestMultipleBrowsers() {
        checkWebElementInFrame(spanMultipleBrowsers);
    }

    @Test
    public void testPageFragmentInFrameDefinedById() {
        checkPageFragmentInFrame(page.getMyFragment());
    }

    @Test
    public void testPageFragmentInFrameDefinedByIdMethodParam(@Page PageWithIFrames pageParam) {
        checkPageFragmentInFrame(pageParam.getMyFragment());
    }

    @Test
    public void testPageFragmentInFrameDefinedByIdMultipleBrowsers() {
        checkPageFragmentInFrame(pageMultipleBrowsers.getMyFragment());
    }

    @Test
    public void testPageFragmentInFrameDefinedByIdMultipleBrowsersMethodParam(
        @Browser2 @Page PageWithIFrames pageMultipleBrowsersParam) {
        checkPageFragmentInFrame(pageMultipleBrowsersParam.getMyFragment());
    }

    @Test
    public void testPageFragmentInFrameDeclaredInTest() {
        checkPageFragmentInFrame(myFragment);
    }

    @Test
    public void testPageFragmentInFrameDeclaredInTest2() {
        String text = myFragment.getSpanText();
        assertEquals("1", text);
    }

    @Test
    public void testPageFragmentInFrameDeclaredInTestMultipleBrowsers() {
        checkPageFragmentInFrame(myFragmentMultipleBrowsers);
    }

    @Test
    public void testSelectInFrameDefinedByName() {
        checkSelectInFrameDefinedByName(page);
    }

    @Test
    public void testSelectInFrameDefinedByNameMethodParam(@Page PageWithIFrames pageParam) {
        checkSelectInFrameDefinedByName(pageParam);
    }

    @Test
    public void testSelectInFrameDefinedByNameMultipleBrowsers() {
        checkSelectInFrameDefinedByName(pageMultipleBrowsers);
    }

    @Test
    public void testSelectInFrameDefinedByNameMultipleBrowsersMethodParam(
        @Browser2 @Page PageWithIFrames pageMultipleBrowsersParam) {
        checkSelectInFrameDefinedByName(pageMultipleBrowsersParam);
    }

    @Test
    public void testInFrameOverPageObject3() {
        checkSelectInFrameDefinedByName(page2);
    }

    @Test
    public void testInFrameOverPageObject3MethodParam(@Page @InFrame(index = 0) PageWithIFrames2 page2Param) {
        checkSelectInFrameDefinedByName(page2Param);
    }

    @Test
    public void testInFrameOverPageObject2MultipleBrowsers() {
        checkSelectInFrameDefinedByName(page2MultipleBrowsers);
    }

    @Test
    public void testInFrameOverPageObject2MultipleBrowsersMethodParam(
        @Browser1 @Page @InFrame(index = 0) PageWithIFrames2 page2MultipleBrowsersParam) {
        checkSelectInFrameDefinedByName(page2MultipleBrowsersParam);
    }

    @Test
    public void testElementInDefaultFrame() {
        checkWebElementInDefaultFrame(page.getElementInDefaultFrame());
    }

    @Test
    public void testElementInDefaultFrameMethodParam(@Page PageWithIFrames pageParam) {
        checkWebElementInDefaultFrame(pageParam.getElementInDefaultFrame());
    }

    @Test
    public void testElementInDefaultFrameMultipleBrowsers() {
        checkWebElementInDefaultFrame(pageMultipleBrowsers.getElementInDefaultFrame());
    }

    @Test
    public void testElementInDefaultFrameMultipleBrowsersMethodParam(
        @Browser2 @Page PageWithIFrames pageMultipleBrowsersParam) {
        checkWebElementInDefaultFrame(pageMultipleBrowsersParam.getElementInDefaultFrame());
    }

    @Test
    public void testElementInDefaultFrameDeclaredInTest() {
        checkWebElementInDefaultFrame(elementInDefaultFrame);
    }

    @Test
    public void testElementInDefaultFrameDeclaredInTestMultipleBrowsers() {
        checkWebElementInDefaultFrame(elementInDefaultFrameMultipleBrowsers);
    }

    @Test
    public void testPageFragmenInFrameMoreComplexInteractions() {
        checkPageFragmenInFrameMoreComplexInteractions(page);
    }

    @Test
    public void testPageFragmenInFrameMoreComplexInteractionsMethodParam(@Page PageWithIFrames pageParam) {
        checkPageFragmenInFrameMoreComplexInteractions(pageParam);
    }

    @Test
    public void testMoreComplexInteractingWithInFrameElements() {
        checkMoreComplexInteractingWithInFrameElements(page);
    }

    @Test
    public void testMoreComplexInteractingWithInFrameElementsMethodParam(@Page PageWithIFrames pageParam) {
        checkMoreComplexInteractingWithInFrameElements(pageParam);
    }

    private void checkMoreComplexInteractingWithInFrameElements(PageWithIFrames pageToCheck) {
        checkWebElementInFrame(pageToCheck.getSpan());
        checkWebElementInFrame(pageToCheck.getSpan());

        WebElement element = browser.findElement(By.className("divElement"));
        checkWebElementInDefaultFrame(element);

        checkPageFragmentInFrame(pageToCheck.getMyFragment());
    }

    private void checkPageFragmenInFrameMoreComplexInteractions(PageWithIFrames pageToCheck) {
        List<WebElement> spans = pageToCheck.getMyFragment().getSpans();
        WebElement span = pageToCheck.getMyFragment().getSpan();
        checkPageFragmentInFrame(pageToCheck.getMyFragment());

        checkWebElementInDefaultFrame(pageToCheck.getElementInDefaultFrame());

        String spanText = span.getText();
        assertEquals("1", spanText);
        int sizeOfList = spans.size();
        assertEquals(4, sizeOfList);

        for (int i = 0; i < sizeOfList; i++) {
            String text = pageToCheck.getMyFragment().getSpans().get(i).getText();
            assertTrue(!text.contains("fail"));
            assertEquals(Integer.valueOf(i + 1), Integer.valueOf(text.trim()));
        }
    }
    private void checkPageFragmentInFrame(PageFragmentWithSpan fragment) {
        List<WebElement> spans = fragment.getSpans();
        int sizeOfList = spans.size();
        assertEquals(4, sizeOfList);

        for (int i = 0; i < sizeOfList; i++) {
            String text = fragment.getSpans().get(i).getText();
            assertTrue(!text.contains("fail"));
            assertEquals(Integer.valueOf(i + 1), Integer.valueOf(text.trim()));
        }
    }

    private void checkWebElementInFrame(WebElement element) {
        String text = element.getText();
        checkWebElementInFrame(text);
    }

    private void checkWebElementInFrame(String elementText) {
        assertEquals(EXPECTED_WEB_ELEMENT_IN_FRAME_TEXT, elementText);
    }

    private void checkWebElementInDefaultFrame(WebElement element) {
        testWebElementInFrameDefinedByIndex();
        assertEquals(EXPECTED_WEB_ELEMENT_OUTSIDE_IFRAME_TEXT, page.getElementInDefaultFrame().getText());
    }

    private void checkSelectInFrameDefinedByName(PageWithIFrames pageToCheck) {
        checkSelectInFrameDefinedByName(pageToCheck.getSelect().getOptions());
    }

    private void checkSelectInFrameDefinedByName(PageWithIFrames2 pageToCheck) {
        checkSelectInFrameDefinedByName(pageToCheck.getSelect().getOptions());
    }

    private void checkSelectInFrameDefinedByName(List<WebElement> options) {
        assertEquals(3, options.size());
        assertEquals(EXPECTED_SELECT_FIRST_OPTION_TEXT, options.get(0).getText());
    }
}