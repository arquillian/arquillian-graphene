package org.jboss.arquillian.graphene.spi.components.autocomplete;

import java.util.List;

import org.jboss.arquillian.graphene.spi.components.common.Component;
import org.jboss.arquillian.graphene.spi.components.scrolling.ScrollingType;

/**
 * <p>
 * Autocomplete component represents an input with autocomplete function.
 * </p>
 * <p>
 * That is when you start typing, some suggestions are offered to you, which you can choose from, to fill in the input faster.
 * </p>
 * 
 * @author jhuska
 * 
 */
public interface AutocompleteComponent<T> extends Component {

    /**
     * Determines whether the suggestion list is visible, that is whether there are some suggestions rendered.
     * 
     * @return true when there are suggestions visible, false otherwise
     */
    boolean areSuggestionsAvailable();

    /**
     * Clears the input using of the method determined by the given parameter.
     * 
     * @param clearType
     */
    void clear(ClearType clearType);

    /**
     * <p>
     * Finishes the filling in the input, that is, fill in the input not by selecting from the suggestions list, but by direct
     * typing in.
     * </p>
     */
    void finish();

    /**
     * Returns all suggestions available, also these which need to be scrolled down to be visible. The suggestions value are
     * determined according to the parser object and its method parse.
     * 
     * @param parser suggestion parser which have implemented correctly parse method
     * @return all suggestions
     */
    List<Suggestion<T>> getAllSuggestions(SuggestionParser<T> parser);

    /**
     * <p>
     * Returns the selected suggestions from the input, the way of suggestions separating is determined by the currently set
     * separator.
     * </p>
     * <p>
     * Note that it does not return the values of the input which was not filled in by choosing from the suggestion list. That
     * is, it does not return the values filled in directly.
     * </p>
     * 
     * @return
     * @see #setSeparator(String)
     */
    List<Suggestion<T>> getSelectedSuggestions();

    /**
     * Returns the list of the input values, the values are separated according to the currently set separator, or when
     * separator is not set, then the default one is space.
     * 
     * @return
     * @see #setSeparator(String)
     */
    List<String> getInputValues();

    /**
     * Sets the separator of the input values. This separator is then used when retrieving input values.
     * 
     * @param regex
     */
    void setSeparator(String regex);

    /**
     * <p>
     * Returns first n suggestions, that is the the first n most top suggestions, if there are some available.
     * </p>
     * <p>
     * If there are no suggestions rendered, then empty list is returned.
     * </p>
     * <p>
     * If the requested number of suggestions is bigger than the actual number of suggestions, then only available suggestions
     * are returned.
     * </p>
     * 
     * @return
     */
    List<Suggestion<T>> getFirstNSuggestions(int n);

    /**
     * Returns the first suggestion if available, that is, the top suggestion from the list of suggestions. If there are no
     * suggestions, null is returned.
     * 
     * @return the first suggestion from the list of suggestions, of no suggestions available null is returned
     */
    Suggestion<T> getFirstSuggestion();

    /**
     * Returns the suggestion which is in order determined by param <code>order</code>. If there is no so many suggestions, then
     * null is returned. Note that the suggestions are ordered from the top of the suggestion list.
     * 
     * @param order
     * @return
     */
    Suggestion<T> getNthSuggestion(int order);

    /**
     * <p>
     * Types to the autocomplete input the provided string and returns the suggestions if provided.
     * </p>
     * <p>
     * That is it types the whole string value of the param <code>string</code> to the input on which the autocomplete function
     * is bound and returns all provided suggestions. If there are no suggestions, empty list is returned.
     * </p>
     * 
     * 
     * @param string the characters which are about to write to the autocomplete input
     * @return list with all provided suggestions, if there are no suggestions after typing empty list is returned
     */
    List<Suggestion<T>> typeString(String string);

    /**
     * <p>
     * Autocompletes the input with given suggestion
     * </p>
     * <p>
     * That is it chooses the suggestion from the list of available suggestions. If that particular suggestion is not available,
     * or there are no suggestions at all, then it is just ignored, and false is returned.
     * </p>
     * 
     * @param sugg the suggestion, it will be autocompleted with
     * @return true when it was successfully autocompleted by provided suggestion, false otherwise
     */
    boolean autocompleteWithSuggestion(Suggestion<T> sugg);

    /**
     * <p>
     * Autocompletes the input with suggestion determined by param <code>sugg</code>, and the suggestion is chosen by the way
     * determined by param <code>scrollingType</code>
     * </p>
     * <p>
     * That is, it chooses the suggestion from the list of available suggestions. If that particular suggestion is not
     * available, or there are no suggestions at all, then it is just ignored, and false is returned.
     * </p>
     * 
     * @param sugg the suggestion, it will be autocompleted with
     * @param scrollingType the scrolling type of the suggestion selection
     * @return true when it was successfully autocompleted by provided suggestion, false otherwise
     */
    boolean autocompleteWithSuggestion(Suggestion<T> sugg, ScrollingType scrollingType);
}
