package org.jboss.arquillian.ajocado.request;

/**
 * Request header which can be added to Selenium requests
 * 
 * @author <a href="mailto:kpiwko@redhat.com">Karel Piwko</a>
 * 
 */
public class Header
{
   private String name;

   private String value;

   /**
    * Creates a header with a given name and value
    * @param name Name of the header
    * @param value Value of the header
    */
   public Header(String name, String value)
   {
      this.name = name;
      this.value = value;
   }

   /**
    * @return the name
    */
   public String getName()
   {
      return name;
   }

   /**
    * @param name the name to set
    */
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * @return the value
    */
   public String getValue()
   {
      return value;
   }

   /**
    * @param value the value to set
    */
   public void setValue(String value)
   {
      this.value = value;
   }

}
