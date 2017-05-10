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

import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.graphene.page.InitialPage;
import org.jboss.arquillian.graphene.page.Location;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.graphene.request.RequestGuardException;
import org.jboss.arquillian.graphene.wait.WebDriverWait;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * <h1>Graphene Utility Class</h1>
 *
 * <p>
 * The {@link Graphene} utility is entry point for concise Graphene syntax of operating with browser.
 * </p>
 *
 * <p>
 * It contains useful methods not only for waiting on certain browser actions.
 * </p>
 *
 * <p>
 * It is recommended to import the class members statically:
 * </p>
 *
 * <pre>
 * import static org.jboss.arquillian.graphene.Graphene.*;
 * </pre>
 *
 * <p>
 * or let add the class static members to favorites in your IDE.
 * </p>
 *
 * <h2>Request Guards API</h2>
 *
 * <p>Request Guards are coarse-grained and concise way how to declare that given interaction with browser leads to a server request of certain type.</p>
 *
 * <ul>
 * <li>{@link #guardHttp(Object)}) / {@link #guardAjax(Object)} - guards that given request was done</li>
 * <li>{@link #waitForHttp(Object)} - guards that full page reload was done</li>
 * <li>{@link #guardNoRequest(Object)} - guards that no request was done</li>
 * </ul>
 *
 * <h2>Fluent Waiting API</h2>
 *
 * <p>This API serves as a base for defining explicit conditions for which the execution of browser should wait for.</p>
 *
 * <ul>
 * <li>{@link #waitGui()}  - guards fast GUI actions - very fast interactions without need to reach server or do any time-consuming</li>
 * <li>{@link #waitAjax()} - guards regular AJAX actions - fast interaction with tested server</li>
 * <li>{@link #waitModel()} - guards heavy computation or network-utilization (typically server-side)</li>
 * </ul>
 *
 * <h2>Navigation to Page Objects</h2>
 *
 * <ul>
 * <li>{@link #goTo(Class)} - navigates the browser into a page given by provided page object class and returns this page instance</li>
 * </ul>
 *
 * <h2>Page Fragments</h2>
 *
 * <ul>
 * <li>{@link #createPageFragment(Class, WebElement)} - creates a page fragments of given type by specifying a root element where given fragments is located</li>
 * </ul>
 *
 * @author <a href="https://community.jboss.org/people/lfryc">Lukas Fryc</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class Graphene {

    /**
     * Returns the guarded object checking whether the HTTP request is done during each object's method invocation. If the
     * request is not observed, the {@link org.jboss.arquillian.graphene.request.RequestGuardException} is thrown.
     *
     * @param <T> type of the given target
     * @param target object to be guarded
     * @return the guarded object
     *
     * @throws RequestGuardException when no HTTP request is observed
     */
    public static <T> T guardHttp(T target) {
        return instance().guardHttp(target);
    }

    /**
     * Returns the guarded object checking whether the Ajax (XHR) request is done during each method invocation. If the request
     * is not observed, the {@link org.jboss.arquillian.graphene.request.RequestGuardException} is thrown.
     *
     * @param <T> type of the given target
     * @param target object to be guarded
     * @return the guarded object
     *
     * @throws RequestGuardException when no AJAX (XHR) request is observed
     */
    public static <T> T guardAjax(T target) {
        return instance().guardAjax(target);
    }

    /**
     * Returns the guarded object checking that no request is done during each method invocation. If any request is observed,
     * the {@link org.jboss.arquillian.graphene.request.RequestGuardException} is thrown.
     *
     * @param <T> type of the given target
     * @param target object to be guarded
     * @return the guarded object
     *
     * @throws RequestGuardException when HTTP or AJAX request is observed
     */
    public static <T> T guardNoRequest(T target) {
        return instance().guardNoRequest(target);
    }

    /**
     * Returns the guarded object checking that HTTP request has happen after any object's method invocation. If AJAX request is
     * observed, the guarded object will ignore it and will wait for regular HTTP request instead.
     *
     * @param <T> type of the given target
     * @param target object to be guarded
     * @return the guarded object
     *
     * @throws RequestGuardException when no HTTP request is observed
     */
    public static <T> T waitForHttp(T target) {
        return instance().waitForHttp(target);
    }

    /**
     * {@link #waitAjax()} is entry point for fluent waiting API specification, e.g.:
     *
     * <pre>
     * waitAjax().until().element(button).isVisible();
     * </pre>
     *
     * {@link #waitAjax()} guards regular AJAX actions - fast interaction with tested server.
     *
     * @see #waitAjax(WebDriver)
     * @see #waitGui()
     * @see #waitModel()
     */
    public static WebDriverWait<Void> waitAjax() {
        return instance().waitAjax();
    }

    /**
     * {@link #waitAjax()} is entry point for fluent waiting API specification, e.g.:
     *
     * <pre>
     * waitAjax(browser).until().element(button).isVisible();
     * </pre>
     *
     * {@link #waitAjax()} guards regular AJAX actions - fast interaction with tested server.
     *
     * @see #waitAjax()
     * @see #waitGui(WebDriver)
     * @see #waitModel(WebDriver)
     */
    public static WebDriverWait<Void> waitAjax(WebDriver driver) {
        return instance().waitAjax(driver);
    }

    /**
     * {@link #waitGui()} is entry point for fluent waiting API specification, e.g.:
     *
     * <pre>
     * waitGui().until().element(popupPanel).isVisible();
     * </pre>
     *
     * {@link #waitGui()} guards fast GUI actions - very fast interactions without need to reach server or do any time-consuming
     * computations.
     *
     * @see #waitGui(WebDriver)
     * @see #waitAjax()
     * @see #waitModel()
     */
    public static WebDriverWait<Void> waitGui() {
        return instance().waitGui();
    }

    /**
     * {@link #waitGui(WebDriver)} is entry point for fluent waiting API specification, e.g.:
     *
     * <pre>
     * waitGui(browser).until().element(popupPanel).isVisible();
     * </pre>
     *
     * {@link #waitGui(WebDriver)} guards fast GUI actions - very fast interactions without need to reach server or do any
     * time-consuming computations.
     *
     * @see #waitGui()
     * @see #waitAjax(WebDriver)
     * @see #waitModel(WebDriver)
     */
    public static WebDriverWait<Void> waitGui(WebDriver driver) {
        return instance().waitGui(driver);
    }

    /**
     * {@link #waitModel()} is entry point for fluent waiting API specification, e.g.:
     *
     * <pre>
     * waitModel().until().element(button).isVisible();
     * </pre>
     *
     * {@link #waitModel()} guards heavy computation or network-utilization interactions (typically server-side).
     *
     * @see #waitGui()
     * @see #waitAjax()
     * @see #waitModel(WebDriver)
     */
    public static WebDriverWait<Void> waitModel() {
        return instance().waitModel();
    }

    /**
     * {@link #waitModel(WebDriver)} is entry point for fluent waiting API specification, e.g.:
     *
     * <pre>
     * waitModel(browser).until().element(button).isVisible();
     * </pre>
     *
     * {@link #waitModel(WebDriver)} guards heavy computation or network-utilization (typically server-side).
     *
     * @see #waitGui()
     * @see #waitAjax()
     * @see #waitModel(WebDriver)
     */
    public static WebDriverWait<Void> waitModel(WebDriver driver) {
        return instance().waitModel(driver);
    }

    /**
     * Creates page fragment of given type with given element as a root.
     *
     * @param type the page fragment class
     * @param root the root of a page fragment in a current page
     * @return the initialized page fragment
     *
     * @see Root
     */
    public static <T> T createPageFragment(Class<T> type, WebElement root) {
        return instance().createPageFragment(type, root);
    }

    /**
     * <p>
     * Requests navigation to a page represented by given page object.
     * </p>
     *
     * <p>
     * The {@link Page} object can be annotated with {@link Location} annotation in order to support navigation between pages.
     * </p>
     *
     * <p>
     * See documentation for {@link Page} in order to know how to define {@link Page} objects.
     * </p>
     *
     * <p>
     * See documentation for {@link InitialPage} in order to know how to define a page which should be used as initial page on a
     * start of a test.
     * </p>
     *
     * @param pageObject page object class
     * @return page object instance
     *
     * @see Page
     * @see InitialPage
     */
    public static <T> T goTo(Class<T> pageObject) {
        return instance().goTo(pageObject);
    }

    /**
     * <p>
     * Requests navigation to a page represented by given page object.
     * </p>
     *
     * <p>
     * The {@link Page} object can be annotated with {@link Location} annotation in order to support navigation between pages.
     * </p>
     *
     * <p>
     * See documentation for {@link Page} in order to know how to define {@link Page} objects.
     * </p>
     *
     * <p>
     * See documentation for {@link InitialPage} in order to know how to define a page which should be used as initial page on a
     * start of a test.
     * </p>
     *
     * @param pageObject page object class
     * @return page object instance
     *
     * @see Page
     * @see InitialPage
     */
    public static <T> T goTo(Class<T> pageObject, Class<?> browserQualifier) {
        return instance().goTo(pageObject, browserQualifier);
    }

    private static GrapheneRuntime instance() {
        return GrapheneRuntime.getInstance();
    }

    /**
     * Clicks in the middle of the given element. Equivalent to:
     * <i>Actions.moveToElement(onElement).click().perform()</i>
     *
     * @param element Element to click.
     */
    public static void click(WebElement element){
        instance().click(element);
    }

    /**
     * Performs a double-click at middle of the given element. Equivalent to:
     * <i>Actions.moveToElement(element).doubleClick().perform()</i>
     *
     * @param element Element to move to.
     */
    public static void doubleClick(WebElement element) {
        instance().doubleClick(element);
    }

    /**
     * Writes into the given element the given string. Equivalent to:
     * <i>Actions.moveToElement(element).click().sendKeys(text).perform()</i>
     *
     * @param element Element to move to.
     * @param text Text to write
     */
    public static void writeIntoElement(WebElement element, String text) {
       instance().writeIntoElement(element, text);
    }

}