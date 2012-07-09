package org.jboss.arquillian.graphene.spi.components.autocomplete;

import java.util.List;

/**
 * The suggestion provided by autocomplete function for particular input
 * value.
 * 
 * @author jhuska
 */
public interface Suggestion<T> {

	/**
	 * Returns the value of this suggestion.
	 * 
	 * @return
	 */
	T getValue();

	/**
	 * <p>
	 * Returns the list of inputs values, for which this suggestion was
	 * offered.
	 * </p>
	 * 
	 * @return
	 */
	List<String> getInputs();
	
	/**
	 * Sets input value for which this suggestion was returned.
	 * 
	 * @param value for which this suggestion was returned
	 */
	void setInput(String value);
}
