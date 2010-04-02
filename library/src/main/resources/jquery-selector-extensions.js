/*******************************************************************************
 * JBoss, Home of Professional Open Source Copyright 2009, Red Hat, Inc. and
 * individual contributors by the
 * 
 * @authors tag. See the copyright.txt in the distribution for a full listing of
 *          individual contributors.
 * 
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 ******************************************************************************/

/**
 * Extension to String.
 * 
 * Finds out if string object starts with given string.
 * 
 * @return true if underlying string objects starts with given string; otherwise
 *         returns false
 */
String.prototype.startsWith = function(str) {
	return (this.match("^" + str) == str);
}

/**
 * Extension to String.
 * 
 * Finds out if string object ends with given string.
 * 
 * @return true if underlying string objects ends with given string; otherwise
 *         returns false
 */
String.prototype.endsWith = function(str) {
	return (this.match(str + "$") == str)
}

/**
 * Extensions to JQuery Expressions
 * 
 * Adds filter expression :textEquals(text) to JQuery selectors.
 * 
 * Usage: span:textEquals('some text')
 * 
 * Text of the element is trimmed before comparison to equality.
 */
jQuery.expr.filters['textEquals'] = function(elem, i, match, array) {
	return jQuery.trim(elem.textContent || elem.innerText || "") === match[3];
}

/**
 * Extensions to JQuery Expressions
 * 
 * Adds filter expression :textStartsWith(text) to JQuery selectors.
 * 
 * Usage: span:textStartsWith('abc')
 * 
 * Text of the element is trimmed before matching if the text starts with given
 * string.
 */
jQuery.expr.filters['textStartsWith'] = function(elem, i, match, array) {
	return jQuery.trim(elem.textContent || elem.innerText || "").startsWith(
			match[3]);
}

/**
 * Extensions to JQuery Expressions
 * 
 * Adds filter expression :textEndsWith(text) to JQuery selectors.
 * 
 * Usage: span:textEndsWith('abc')
 * 
 * Text of the element is trimmed before matching if the text ends with given
 * string.
 */
jQuery.expr.filters['textEndsWith'] = function(elem, i, match, array) {
	return jQuery.trim(elem.textContent || elem.innerText || "").endsWith(
			match[3]);
}

var jqFind = function(selector) {
	return $(selenium.browserbot.getCurrentWindow().document).find(selector);
}