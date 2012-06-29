package org.jboss.arquillian.graphene.spi.components.calendar;

import java.util.List;

import org.jboss.arquillian.graphene.spi.components.scrolling.ScrollingType;
import org.joda.time.DateTime;


/**
 * CalendarPopupComponent represents calendar nested in some kind of popup, it
 * has to be invoked to be shown and has an input associated with it.
 * 
 * @author jhuska
 * 
 */
public interface CalendarPopupComponent extends CalendarComponent {

	/**
	 * Clears the input which is showing the selected date.
	 */
	void clearInput();
	
	/**
	 * Displays the calendar only if it is not already displayed, that is when
	 * it can be displayed in the popup mode, it is shown up.
	 */
	void showCalendar();

	/**
	 * Hides the calendar if it is in the popup mode, that is it hides the
	 * calendar.
	 */
	void hideCalendar();
	
	/**
     * Set the date on the calendar
     *
     * @param dateTime the date to set
     * @param type the type of way to set the date, for example by mouse
     */
    void gotoDateTime(DateTime dateTime, ScrollingType type);

	/**
	 * Set the next day of the current set date.
	 * 
	 * @return the day which was set as the next day
	 */
	CalendarDay gotoNextDay();

	/**
	 * <p>
	 * Set the next month of the current set date. If the current set date is
	 * not part of the next month, then the last day of next month is set.
	 * </p>
	 * <p>
	 * For example when the current set date is January 30th and this method is
	 * invoked, then February 28th is set (February 29th in case of leap year)
	 * </p>
	 * 
	 * @return the month which was set as the next month
	 */
	CalendarMonth gotoNextMonth();

	/**
	 * <p>
	 * Set the next year of the current set date. If the current set date is not
	 * part of the next year, then the last day of the next year is set.
	 * </p>
	 * <p>
	 * For example when the current set date is February 29th (it is the leap
	 * year) and this method is invoked, then the February 28th is set.
	 * </p>
	 * 
	 * @return the year which was set as the next year
	 */
	CalendarYear gotoNextYear();

	/**
	 * Set the previous day of the current set date.
	 * 
	 * @return the day which was set as the previous day
	 */
	CalendarDay gotoPreviousDay();

	/**
	 * <p>
	 * Set the previous month of the current set date. If the current set date
	 * is not part of the previous month, then the last day of previous month is
	 * set.
	 * </p>
	 * <p>
	 * For example when the current set date is March 30th and this method is
	 * invoked, then February 28th is set (February 29th in case of leap year)
	 * </p>
	 * 
	 * @return the month which was set as the previous month
	 */
	CalendarMonth gotoPreviousMonth();

	/**
	 * <p>
	 * Set the previous year of the current set date. If the current set date is
	 * not part of the previous year, then the last day of the previous year is
	 * set.
	 * </p>
	 * <p>
	 * For example when the current set date is February 29th, 2012 (it is the
	 * leap year) and this method is invoked, then the February 28th, 2011 is
	 * set.
	 * </p>
	 * 
	 * @return the year which was set as the previous year
	 */
	CalendarYear gotoPreviousYear();

	/**
	 * Returns the day of the month, if there is a date set in the calendar then
	 * this date is considered, current date otherwise.
	 * 
	 * @return the day of the month of the current set date, or if nothing set
	 *         then the day of the month of the current date
	 */
	CalendarDay getDayOfMonth();

	/**
	 * Returns a specific day of the month, if there is a date set in the
	 * calendar then this date is considered, current date otherwise.
	 * 
	 * @param day
	 *            the number of the day in the month
	 * @return the particular day of the month of the current set date, or if
	 *         nothing set then the day of the month of the current date
	 */
	CalendarDay getDayOfMonth(int day);

	/**
	 * Returns all the days of the month, if there is a date set in the calendar
	 * then this date is considered, current date otherwise.
	 * 
	 * @return all the days of the month of the current set date, or if nothing
	 *         set then the days of the month of the current date
	 */
	List<CalendarDay> getDaysOfMonth();

	/**
	 * Returns the day of the week, if there is a date set in the calendar then
	 * this date is considered, current date otherwise.
	 * 
	 * @return the day of the week of the current set date, or if nothing set
	 *         then the day of the week of the current date
	 */
	CalendarDay getDayOfWeek();

	/**
	 * Returns a specific day of the week, if there is a date set in the
	 * calendar then this date is considered, current date otherwise.
	 * 
	 * @param day
	 *            the number of the day in the week
	 * @return the particular day of the week of the current set date, or if
	 *         nothing set then the day of the week of the current date
	 */
	CalendarDay getDayOfWeek(int day);

	/**
	 * Returns all the days of the week, if there is a date set in the calendar
	 * then this date is considered, current date otherwise.
	 * 
	 * @return all the days of the week of the current set date, or if nothing
	 *         set then the days of the week of the current date
	 */
	List<CalendarDay> getDaysOfWeek();

	/**
	 * Returns the day of the year, if there is a date set in the calendar then
	 * this date is considered, current date otherwise.
	 * 
	 * @return the day of the year of the current set date, or if nothing set
	 *         then the day of the year of the current date
	 */
	CalendarDay getDayOfYear();

	/**
	 * Returns a specific day of the year, if there is a date set in the
	 * calendar then this date is considered, current date otherwise.
	 * 
	 * @param day
	 *            the number of the day in the year
	 * @return the particular day of the year of the current set date, or if
	 *         nothing set then the day of the year of the current date
	 */
	CalendarDay getDayOfYear(int day);

	/**
	 * Returns all the days of the year, if there is a date set in the calendar
	 * then this date is considered, current date otherwise.
	 * 
	 * @return all the days of the year of the current set date, or if nothing
	 *         set then the days of the year of the current date
	 */
	List<CalendarDay> getDaysOfYear();

	/**
	 * Returns the week of the month, if there is a date set in the calendar
	 * then this date is considered, current date otherwise.
	 * 
	 * @return the week of the month of the current set date, or if nothing set
	 *         then the week of the month of the current date
	 */
	CalendarWeek getWeekOfMonth();

	/**
	 * Returns a specific week of the month, if there is a date set in the
	 * calendar then this date is considered, current date otherwise.
	 * 
	 * @param week
	 *            the number of the week in the currently set month
	 * @return the particular week of the month of the current set date, or if
	 *         nothing set then the week of the month of the current date
	 */
	CalendarWeek getWeekOfMonth(int week);

	/**
	 * Returns all the weeks of the month, if there is a date set in the
	 * calendar then this date is considered, current date otherwise.
	 * 
	 * @return all the weeks of the month of the current set date, or if nothing
	 *         set then the weeks of the month of the current date
	 */
	List<CalendarWeek> getWeeksOfMonth();

	/**
	 * Returns the week of the year, if there is a date set in the calendar then
	 * this date is considered, current date otherwise.
	 * 
	 * @return the week of the year of the current set date, or if nothing set
	 *         then the week of the year of the current date
	 */
	CalendarWeek getWeekOfYear();

	/**
	 * Returns a specific week of the year, if there is a date set in the
	 * calendar then this date is considered, current date otherwise.
	 * 
	 * @param week
	 *            the number of the week in the currently set year
	 * @return the particular week of the year of the current set date, or if
	 *         nothing set then the week of the year of the current date
	 */
	CalendarWeek getWeekOfYear(int week);

	/**
	 * Returns all the weeks of the year, if there is a date set in the calendar
	 * then this date is considered, current date otherwise.
	 * 
	 * @return all the weeks of the year of the current set date, or if nothing
	 *         set then the weeks of the year of the current date
	 */
	List<CalendarWeek> getWeeksOfYear();

	/**
	 * Returns the month, if there is a date set in the calendar then this date
	 * is considered, current date otherwise.
	 * 
	 * @return the month of the current set date, or if nothing set then the
	 *         month of the current date
	 */
	CalendarMonth getMonth();

	/**
	 * Returns a specific month, if there is a date set in the calendar then
	 * this date is considered, current date otherwise.
	 * 
	 * @param month
	 *            the number of the month in the set date
	 * @return the particular month of the current set date, or if nothing set
	 *         then the month of the current date
	 */
	CalendarMonth getMonth(int month);

	/**
	 * Returns all the months of the year, if there is a date set in the
	 * calendar then this date is considered, current date otherwise.
	 * 
	 * @return all the months of the year of the current set date, or if nothing
	 *         set then the months of the current date
	 */
	List<CalendarMonth> getMonths();

	/**
	 * Returns the year, if there is a date set in the calendar then this date
	 * is considered, current date otherwise.
	 * 
	 * @return the year of the current set date, or if nothing set then the year
	 *         of the current date
	 */
	CalendarYear getYear();

	/**
	 * Returns a specific year, if there is a date set in the calendar then this
	 * date is considered, current date otherwise.
	 * 
	 * @return the particular year of the current set date, or if nothing set
	 *         then the year of the current date
	 */
	CalendarYear getYear(int year);

	/**
	 * Returns all the years from the range determined by <code>from</code> and
	 * <code>to</code>.
	 * 
	 * @param from
	 *            the starting year inclusively
	 * @param to
	 *            the ending year inclusively
	 * @return all the years from the range
	 */
	List<CalendarYear> getYears(int from, int to);

	/**
	 * Interface for a time unit.
	 * 
	 * @author jhuska
	 */
	public interface TimeUnit {
		/**
		 * Returns a integer representation of this time unit.
		 * 
		 * @return
		 */
		int toInt();
	}

	/**
	 * Interface for calendar day.
	 * 
	 * @author jhuska
	 */
	public interface CalendarDay extends TimeUnit {
		/**
		 * Returns whether the day is enabled, that is whether it can be used as
		 * a value to set to the calendar.
		 * 
		 * @return
		 */
		boolean isEnabled();

		/**
		 * Returns the month which contains this day.
		 * 
		 * @return
		 */
		CalendarMonth whichMonth();

		/**
		 * Returns the year which contains this day.
		 * 
		 * @return
		 */
		CalendarYear whichYear();
	}

	/**
	 * Interface for calendar week.
	 * 
	 * @author jhuska
	 */
	public interface CalendarWeek extends TimeUnit {
		/**
		 * Returns all days of this week.
		 * 
		 * @return
		 */
		List<CalendarDay> getDays();

		/**
		 * Returns a specific day of this week.
		 * 
		 * @param day
		 * @return
		 */
		CalendarDay getDay(int day);

		/**
		 * Returns the month which contains this week.
		 * 
		 * @return
		 */
		CalendarMonth whichMonth();

		/**
		 * Returns the year which contains this week.
		 * 
		 * @return
		 */
		CalendarYear whichYear();
	}

	/**
	 * Interface for calendar month.
	 * 
	 * @author jhuska
	 */
	public interface CalendarMonth extends TimeUnit {
		/**
		 * Returns all days of this month.
		 * 
		 * @return
		 */
		List<CalendarDay> getDays();

		/**
		 * Returns all weeks of this month.
		 * 
		 * @return
		 */
		List<CalendarWeek> getWeeks();

		/**
		 * Returns a specific day of this month.
		 * 
		 * @param day
		 * @return
		 */
		CalendarDay getDay(int day);

		/**
		 * Returns a specific week of this month.
		 * 
		 * @param week
		 * @return
		 */
		CalendarWeek getWeek(int week);

		/**
		 * Returns the year which contains this month.
		 * 
		 * @return
		 */
		CalendarYear whichYear();
	}

	/**
	 * Interface for calendar year.
	 * 
	 * @author jhuska
	 */
	public interface CalendarYear extends TimeUnit {
		/**
		 * Returns all days of this month.
		 * 
		 * @return
		 */
		List<CalendarDay> getDays();

		/**
		 * Returns all weeks of this month.
		 * 
		 * @return
		 */
		List<CalendarWeek> getWeeks();

		/**
		 * Returns all months of this year.
		 * 
		 * @return
		 */
		List<CalendarMonth> getMonths();

		/**
		 * Returns a specific day of this month.
		 * 
		 * @param day
		 * @return
		 */
		CalendarDay getDay(int day);

		/**
		 * Returns a specific week of this month.
		 * 
		 * @param week
		 * @return
		 */
		CalendarWeek getWeek(int week);

		/**
		 * Returns a specific month of this year.
		 * 
		 * @param week
		 * @return
		 */
		CalendarMonth getMonth(int month);
	}
}
