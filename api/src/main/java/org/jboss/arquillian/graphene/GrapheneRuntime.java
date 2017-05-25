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

import java.util.EmptyStackException;
import java.util.Stack;

import org.jboss.arquillian.graphene.wait.WebDriverWait;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Represents {@link Graphene} utility class public interface methods.
 *
 * @author Lukas Fryc
 */
public abstract class GrapheneRuntime {

    private static final ThreadLocal<Stack<GrapheneRuntime>> RUNTIMES_STACK = new ThreadLocal<Stack<GrapheneRuntime>>() {
        public Stack<GrapheneRuntime> initialValue() {
            return new Stack<GrapheneRuntime>();
        }
    };

    /**
     * @see Graphene#guardHttp(Object)
     */
    public abstract <T> T guardHttp(T target);

    /**
     * @see Graphene#guardNoRequest(Object)
     */
    public abstract <T> T guardNoRequest(T target);

    /**
     * @see Graphene#guardAjax(Object)
     */
    public abstract <T> T guardAjax(T target);

    /**
     * @see Graphene#waitForHttp(Object)
     */
    public abstract <T> T waitForHttp(T target);

    /**
     * @see Graphene#waitAjax()
     */
    public abstract WebDriverWait<Void> waitAjax();

    /**
     * @see Graphene#waitAjax(WebDriver)
     */
    public abstract WebDriverWait<Void> waitAjax(WebDriver driver);

    /**
     * @see Graphene#waitGui()
     */
    public abstract WebDriverWait<Void> waitGui();

    /**
     * @see Graphene#waitGui(WebDriver)
     */
    public abstract WebDriverWait<Void> waitGui(WebDriver driver);

    /**
     * @see Graphene#waitModel()
     */
    public abstract WebDriverWait<Void> waitModel();

    /**
     * @see Graphene#waitModel(WebDriver)
     */
    public abstract WebDriverWait<Void> waitModel(WebDriver driver);

    /**
     * @see Graphene#createPageFragment(Class, WebElement)
     */
    public abstract <T> T createPageFragment(Class<T> clazz, WebElement root);

    /**
     * @see Graphene#goTo(Class)
     */
    public abstract <T> T goTo(Class<T> clazz);

    /**
     * @see Graphene#goTo(Class, Class)
     */
    public abstract <T> T goTo(Class<T> pageObject, Class<?> browserQualifier);

    /**
     * Retrieves current thread-local instance of the Graphene runtime.
     */
    public static GrapheneRuntime getInstance() {
        try {
            return RUNTIMES_STACK.get().peek();
        } catch (EmptyStackException e) {
            throw new IllegalStateException("The Graphene runtime isn't initialized.");
        }
    }

    /**
     * Set ups current thread-local instance of the Graphene runtime on top of stack of current instances.
     */
    public static void pushInstance(final GrapheneRuntime grapheneRuntime) {
        if (!RUNTIMES_STACK.get().isEmpty()) {
            throw new IllegalStateException("There can be only one Graphene instance at the moment");
        }
        RUNTIMES_STACK.get().push(grapheneRuntime);
    }

    /**
     * Returns and removes current thread-local instance of the Graphene runtime from top of the stack of current instances.
     */
    public static GrapheneRuntime popInstance() {
        if (RUNTIMES_STACK.get().isEmpty()) {
            throw new IllegalStateException("There is no Graphene instance at the moment");
        }
        return RUNTIMES_STACK.get().pop();
    }

    /**
     * @see Graphene#doubleClick(WebElement)
     */
    public abstract void doubleClick(WebElement element);

    /**
     * @see Graphene#click(WebElement)
     */
    public abstract void click(WebElement element);

    /**
     * @see Graphene#writeIntoElement(WebElement)
     */
    protected abstract void writeIntoElement(WebElement element, String text);
}
