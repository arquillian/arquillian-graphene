package org.jboss.arquillian.ajocado.request;

/**
 * Request header which can be added to Selenium requests
 * 
 * @author <a href="mailto:kpiwko@redhat.com">Karel Piwko</a>
 * 
 */
public class RequestHeader {
    private String name;

    private String value;

    /**
     * Creates a header with a given name and value
     * 
     * @param name
     *            Name of the header
     * @param value
     *            Value of the header
     */
    public RequestHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Returns the name of the header
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the header
     * 
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the value of the header
     * 
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the header
     * 
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

}
