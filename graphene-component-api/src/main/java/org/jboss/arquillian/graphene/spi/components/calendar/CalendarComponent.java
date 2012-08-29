package org.jboss.arquillian.graphene.spi.components.calendar;

import org.jboss.arquillian.graphene.spi.components.common.Component;
import org.joda.time.DateTime;

/**
 * Calendar component represents any type of calendar.
 * 
 * @author jhuska
 */
public interface CalendarComponent extends Component {

    /**
     * Returns the set date, that is the date which is in input after date setting.
     * 
     * @return the set date
     */
    DateTime getDateTime();

    /**
     * Sets the <code>dateTime</code>, that is it selects that date and also sets to the input.
     * 
     * @param dateTime the date to set
     */
    void gotoDateTime(DateTime dateTime);
}