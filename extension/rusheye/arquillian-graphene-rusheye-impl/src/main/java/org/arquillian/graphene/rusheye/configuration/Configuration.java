package org.arquillian.graphene.rusheye.configuration;

import org.arquillian.graphene.rusheye.exception.RushEyeConfigurationException;
import org.jboss.arquillian.core.spi.Validate;

import java.util.HashMap;
import java.util.Map;


public abstract class Configuration {

    private Map<String, String> configuration = new HashMap<String, String>();

    /**
     * @return configuration of extension
     */
    public Map<String, String> getConfiguration() {
        return this.configuration;
    }

    
    public void convertToSystemProperties(){
    	for(String key : configuration.keySet()){
    		System.setProperty("arquillian.extension.rusheye." + key, this.configuration.get(key));
    	}
    }
    
    /**
     * Gets configuration from Arquillian descriptor and creates instance of it.
     *
     * @param configuration configuration of extension from arquillian.xml
     * @return this
     * @throws IllegalArgumentException if {@code configuration} is a null object
     */
    public Configuration setConfiguration(Map<String, String> configuration) {
        Validate.notNull(configuration, "Properties for configuration of Arquillian Governor extension can not be a null object!");
        this.configuration = configuration;
        return this;
    }

    /**
     * Gets value of {@code name} property. In case a value for such name does not exist or is a null object or an empty string,
     * {@code defaultValue} is returned.
     *
     * @param name         name of a property you want to get the value of
     * @param defaultValue value returned in case {@code name} is a null string or it is empty
     * @return value of a {@code name} property of {@code defaultValue} when {@code name} is null or empty string
     * @throws IllegalArgumentException if {@code name} is a null object or an empty string or if {@code defaultValue} is a null
     *                                  object
     */
    public String getProperty(String name, String defaultValue) throws IllegalStateException {
        Validate.notNullOrEmpty(name, "Unable to get the configuration value of null or empty configuration key");
        Validate.notNull(defaultValue, "Unable to set configuration value of " + name + " to null object.");

        final String found = getConfiguration().get(name);

        if (found == null || found.isEmpty()) {
            return defaultValue;
        } else {
            return found;
        }
    }

    /**
     * Sets some property.
     *
     * @param name  acts as a key
     * @param value
     * @throws IllegalArgumentException if {@code name} is null or empty or {@code value} is null
     */
    public void setProperty(String name, String value) {
        Validate.notNullOrEmpty(name, "Name of property can not be a null object nor an empty string!");
        Validate.notNull(value, "Value of property can not be a null object!");

        configuration.put(name, value);
    }

    /**
     * Validates configuration.
     *
     * @throws GovernorConfigurationException when configuration of the extension is not valid
     */
    public abstract void validate() throws RushEyeConfigurationException;
}