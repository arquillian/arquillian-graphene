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
     * <p>
     * Clears the input. The method of clearing can be passed as argument to determine the way the input will be cleared
     * </p>
     * 
     * @param clearType
     * @throws IllegalArgumentException when there is illegal number of arguements passed
     */
    void clear(ClearType... clearType);

    /**
     * <p>
     * Finishes the filling in the input, that is, fill in the input not by selecting from the suggestions list, but by direct
     * typing in.
     * </p>
     */
    void finish();

    /**
     * Returns all suggestions available, also these which need to be scrolled down to be visible. The suggestions value are
     * determined according to the parser object which need to be set prior of executing this method.
     * 
     * @throws IllegalStateException if the suggestion parser was not set prior to execution of this method
     * @return all suggestions or null if there are no suggestions
     */
    List<Suggestion<T>> getAllSuggestions();

    /**
     * <p>
     * Returns the selected suggestions from the input, the way of suggestions separating is determined by the currently set
     * separator, the default one is space.
     * </p>
     * <p>
     * Note that it does not return the values of the input which was filled in directly, not by choosing from suggestion list.
     * </p>
     * 
     * @return
     * @see #setSeparator(String)
     */
    List<Suggestion<T>> getSelectedSuggestions();

    /**
     * Returns the list of the input values, the values are separated according to the currently set separator, default one is
     * space.
     * 
     * @see #setSeparator(String)
     * @return
     */
    List<String> getInputValues();

    /**
     * Returns the current value of input
     * 
     * @return
     */
    String getInputValue();

    /**
     * Returns the first input value, the values are separated according to the currently set separator, default one is space.
     * 
     * @see #setSeparator(String)
     * @return
     */
    String getFirstInputValue();

    /**
     * Sets the separator of the input values. This separator is then used when retrieving input values.
     * 
     * @param regex
     */
    void setSeparator(String regex);

    /**
     * <p>
     * Sets the suggestion parser. The default one is StringSuggestionParser
     * </p>
     * <p>
     * The suggestion parser determines how can be value of suggestion parsed and the suggestion object created
     * </p>
     * 
     * @param parser
     */
    void setSuggestionParser(SuggestionParser<T> parser);

    /**
     * <p>
     * Returns first n suggestions, that is the the first n most top suggestions, if there are some available. The suggestions
     * are determined according to the suggestion parser, which was set prior of executing of this method.
     * </p>
     * <p>
     * If there are no suggestions rendered null is returned.
     * </p>
     * <p>
     * If the requested number of suggestions is bigger than the actual number of suggestions, then only available suggestions
     * are returned.
     * </p>
     * 
     * @throws IllegalStateException if the suggestion parser was not set prior to execution of this method
     * @return the first n suggestions or null if there are no suggestions
     */
    List<Suggestion<T>> getFirstNSuggestions(int n);

    /**
     * Returns the first suggestion if available, that is, the top suggestion from the list of suggestions. If there are no
     * suggestions, null is returned.
     * 
     * @throws IllegalStateException if the suggestion parser was not set prior to execution of this method
     * @return the first suggestion from the list of suggestions, of no suggestions available null is returned
     */
    Suggestion<T> getFirstSuggestion();

    /**
     * Returns the suggestion which is in order determined by param <code>order</code>. If there is no so many suggestions, then
     * null is returned. Note that the suggestions are ordered from the top of the suggestion list and the ordering begins from
     * 1.
     * 
     * @throws IllegalStateException if the suggestion parser was not set prior to execution of this method
     * @param order
     * @return the suggestion which order is determined by the param <code>order</code> or null if there is not such a
     *         suggestion
     */
    Suggestion<T> getNthSuggestion(int order);

    /**
     * <p>
     * Types to the autocomplete input provided value and waits for suggestions. If there are no suggestions available nothing
     * happen.
     * <p>
     * 
     * <p>
     * The wait timeout is determined by <code>GUI_WAIT</code>
     * 
     * @param value
     */
    void type(String value);

    /**
     * <p>
     * Types to the autocomplete input the provided string and returns the suggestions if available.
     * </p>
     * <p>
     * That is it types the whole value of the param <code>string</code> to the input on which the autocomplete function is
     * bound and returns all available suggestions. If there are no suggestions, null is returned.
     * </p>
     * 
     * @param value the characters which are about to write to the autocomplete input
     * @throws IllegalStateException if the suggestion parser was not set prior to execution of this method
     * @return list with all provided suggestions, if there are no suggestions after typing null is returned
     */
    List<Suggestion<T>> typeAndReturn(String value);

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
     * @throws IllegalArgumentException when suggestion is null
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
     * @throws IllegalArgumentException if <code>sugg</code> or <code>scrollingType</code> is <code>null</code>
     * @return true when it was successfully autocompleted by provided suggestion, false otherwise
     */
    boolean autocompleteWithSuggestion(Suggestion<T> sugg, ScrollingType scrollingType);
}
