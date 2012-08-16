package org.jboss.arquillian.graphene.spi.components.autocomplete;

import org.openqa.selenium.WebElement;

public interface SuggestionParser<T> {

    /**
     * Will construct suggestion from the given suggestion root.
     * 
     * @param rootOfSuggestion
     * @return
     */
    Suggestion<T> parse(WebElement rootOfSuggestion);
}
