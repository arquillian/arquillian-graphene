/*
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

package org.jboss.arquillian.graphene.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ISelect;
import org.openqa.selenium.support.ui.Quotes;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;

import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

/**
 * Temporary class for GrapheneSelect elements
 * Replace with <code>{@link org.openqa.selenium.support.ui.Select}</code> after resolving issues with getDomProperty() and getDomAttribute() methods
 * <p>
 * Models a SELECT tag, providing helper methods to select and deselect options.
 */
public class GrapheneSelectImpl implements GrapheneSelect {

    private final WebElement element;
    private boolean isMulti = false;

    /**
     * Constructor. A check is made that the given element is, indeed, a SELECT tag. If it is not,
     * then an UnexpectedTagNameException is thrown.
     *
     * @param element SELECT element to wrap
     * @throws UnexpectedTagNameException when element is not a SELECT
     */
    public GrapheneSelectImpl(WebElement element) {
        String tagName = element.getTagName();

        if (!"select".equalsIgnoreCase(tagName)) {
            throw new UnexpectedTagNameException("select", tagName);
        }

        this.element = element;
    }

    @Override
    public void setIsMulti(boolean isMulti) {
        this.isMulti = isMulti;
    }

    @Override
    public WebElement getWrappedElement() {
        return element;
    }

    /**
     * @return Whether this select element support selecting multiple options at the same time? This
     * is done by checking the value of the "multiple" attribute.
     */
    @Override
    public boolean isMultiple() {
        return isMulti;
    }

    /**
     * @return All options belonging to this select tag
     */
    @Override
    public List<WebElement> getOptions() {
        return element.findElements(By.tagName("option"));
    }

    /**
     * @return All selected options belonging to this select tag
     */
    @Override
    public List<WebElement> getAllSelectedOptions() {
        return getOptions().stream().filter(WebElement::isSelected).collect(Collectors.toList());
    }

    /**
     * @return The first selected option in this select tag (or the currently selected option in a
     * normal select)
     * @throws NoSuchElementException If no option is selected
     */
    @Override
    public WebElement getFirstSelectedOption() {
        return getOptions().stream().filter(WebElement::isSelected).findFirst()
                .orElseThrow(() -> new NoSuchElementException("No options are selected"));
    }

    /**
     * GrapheneSelect all options that display text matching the argument. That is, when given "Bar" this
     * would select an option like:
     * <p>
     * &lt;option value="foo"&gt;Bar&lt;/option&gt;
     *
     * @param text The visible text to match against
     * @throws NoSuchElementException If no matching option elements are found
     */
    @Override
    public void selectByVisibleText(String text) {
        // try to find the option via XPATH ...
        List<WebElement> options =
                element.findElements(By.xpath(".//option[normalize-space(.) = " + Quotes.escape(text) + "]"));

        for (WebElement option : options) {
            setSelected(option, true);
            if (!isMultiple()) {
                return;
            }
        }

        boolean matched = !options.isEmpty();
        if (!matched && text.contains(" ")) {
            String subStringWithoutSpace = getLongestSubstringWithoutSpace(text);
            List<WebElement> candidates;
            if ("".equals(subStringWithoutSpace)) {
                // hmm, text is either empty or contains only spaces - get all options ...
                candidates = element.findElements(By.tagName("option"));
            } else {
                // get candidates via XPATH ...
                candidates =
                        element.findElements(By.xpath(".//option[contains(., " +
                                Quotes.escape(subStringWithoutSpace) + ")]"));
            }

            String trimmed = text.trim();

            for (WebElement option : candidates) {
                if (trimmed.equals(option.getText().trim())) {
                    setSelected(option, true);
                    if (!isMultiple()) {
                        return;
                    }
                    matched = true;
                }
            }
        }

        if (!matched) {
            throw new NoSuchElementException("Cannot locate option with text: " + text);
        }
    }

    private String getLongestSubstringWithoutSpace(String s) {
        String result = "";
        StringTokenizer st = new StringTokenizer(s, " ");
        while (st.hasMoreTokens()) {
            String t = st.nextToken();
            if (t.length() > result.length()) {
                result = t;
            }
        }
        return result;
    }

    /**
     * GrapheneSelect the option at the given index. This is done by examining the "index" attribute of an
     * element, and not merely by counting.
     *
     * @param index The option at this index will be selected
     * @throws NoSuchElementException If no matching option elements are found
     */
    @Override
    public void selectByIndex(int index) {
        setSelectedByIndex(index, true);
    }

    /**
     * GrapheneSelect all options that have a value matching the argument. That is, when given "foo" this
     * would select an option like:
     * <p>
     * &lt;option value="foo"&gt;Bar&lt;/option&gt;
     *
     * @param value The value to match against
     * @throws NoSuchElementException If no matching option elements are found
     */
    @Override
    public void selectByValue(String value) {
        for (WebElement option : findOptionsByValue(value)) {
            setSelected(option, true);
            if (!isMultiple()) {
                return;
            }
        }
    }

    /**
     * Clear all selected entries. This is only valid when the SELECT supports multiple selections.
     *
     * @throws UnsupportedOperationException If the SELECT does not support multiple selections
     */
    @Override
    public void deselectAll() {
        if (!isMultiple()) {
            throw new UnsupportedOperationException(
                    "You may only deselect all options of a multi-select");
        }

        for (WebElement option : getOptions()) {
            setSelected(option, false);
        }
    }

    /**
     * Deselect all options that have a value matching the argument. That is, when given "foo" this
     * would deselect an option like:
     * <p>
     * &lt;option value="foo"&gt;Bar&lt;/option&gt;
     *
     * @param value The value to match against
     * @throws NoSuchElementException        If no matching option elements are found
     * @throws UnsupportedOperationException If the SELECT does not support multiple selections
     */
    @Override
    public void deselectByValue(String value) {
        if (!isMultiple()) {
            throw new UnsupportedOperationException(
                    "You may only deselect options of a multi-select");
        }

        for (WebElement option : findOptionsByValue(value)) {
            setSelected(option, false);
        }
    }

    /**
     * Deselect the option at the given index. This is done by examining the "index" attribute of an
     * element, and not merely by counting.
     *
     * @param index The option at this index will be deselected
     * @throws NoSuchElementException        If no matching option elements are found
     * @throws UnsupportedOperationException If the SELECT does not support multiple selections
     */
    @Override
    public void deselectByIndex(int index) {
        if (!isMultiple()) {
            throw new UnsupportedOperationException(
                    "You may only deselect options of a multi-select");
        }

        setSelectedByIndex(index, false);
    }

    /**
     * Deselect all options that display text matching the argument. That is, when given "Bar" this
     * would deselect an option like:
     * <p>
     * &lt;option value="foo"&gt;Bar&lt;/option&gt;
     *
     * @param text The visible text to match against
     * @throws NoSuchElementException        If no matching option elements are found
     * @throws UnsupportedOperationException If the SELECT does not support multiple selections
     */
    @Override
    public void deselectByVisibleText(String text) {
        if (!isMultiple()) {
            throw new UnsupportedOperationException(
                    "You may only deselect options of a multi-select");
        }

        List<WebElement> options = element.findElements(By.xpath(
                ".//option[normalize-space(.) = " + Quotes.escape(text) + "]"));
        if (options.isEmpty()) {
            throw new NoSuchElementException("Cannot locate option with text: " + text);
        }

        for (WebElement option : options) {
            setSelected(option, false);
        }
    }

    private List<WebElement> findOptionsByValue(String value) {
        List<WebElement> options = element.findElements(By.xpath(
                ".//option[@value = " + Quotes.escape(value) + "]"));
        if (options.isEmpty()) {
            throw new NoSuchElementException("Cannot locate option with value: " + value);
        }
        return options;
    }

    private void setSelectedByIndex(int index, boolean select) {
        String match = String.valueOf(index);

        for (WebElement option : getOptions()) {
            if (match.equals(option.getAttribute("index"))) {
                setSelected(option, select);
                return;
            }
        }
        throw new NoSuchElementException("Cannot locate option with index: " + index);
    }

    /**
     * GrapheneSelect or deselect specified option
     *
     * @param option The option which state needs to be changed
     * @param select Indicates whether the option needs to be selected (true) or
     *               deselected (false)
     */
    private void setSelected(WebElement option, boolean select) {
        if (option.isSelected() != select) {
            option.click();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ISelect)) {
            return false;
        }
        GrapheneSelect select = (GrapheneSelect) o;
        return Objects.equals(element, select.getWrappedElement());
    }

    @Override
    public int hashCode() {
        return Objects.hash(element);
    }
}