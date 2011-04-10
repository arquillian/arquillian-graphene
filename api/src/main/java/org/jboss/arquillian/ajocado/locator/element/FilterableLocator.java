package org.jboss.arquillian.ajocado.locator.element;

/**
 * Defines element locators which can be used to derive new locators by defining filters.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 * 
 * @param <T>
 *            type what will be used as result of composition
 */
public interface FilterableLocator<T extends FilterableLocator<T>> extends ElementLocator<T> {

    /**
     * Specify string as parameterizing extension which filters this locator
     * 
     * @param extension
     *            the string to filter this locator
     * @return new locator filtering this locator
     */
    T filter(String extension);
}
