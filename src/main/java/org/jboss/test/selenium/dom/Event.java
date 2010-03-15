/*******************************************************************************
 * JBoss, Home of Professional Open Eventnew Even)( ource
 * Copyright 2009, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *******************************************************************************/
package org.jboss.test.selenium.dom;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class Event {

	private String eventName;

	public Event(String eventName) {
		this.eventName = eventName;
	}

	public String getEventName() {
		return eventName;
	}

	@Override
	public String toString() {
		return eventName;
	}

	public static final Event ABORT = new Event("abort");
	public static final Event BLUR = new Event("blur");
	public static final Event CHANGE = new Event("change");
	public static final Event CLICK = new Event("click");
	public static final Event ERROR = new Event("error");
	public static final Event FOCUS = new Event("focus");
	public static final Event KEYDOWN = new Event("keydown");
	public static final Event KEYPRESS = new Event("keypress");
	public static final Event KEYUP = new Event("keyup");
	public static final Event LOAD = new Event("load");
	public static final Event MOUSEDOWN = new Event("mousedown");
	public static final Event MOUSEMOVE = new Event("mousemove");
	public static final Event MOUSEOUT = new Event("mouseout");
	public static final Event MOUSEOVER = new Event("mouseover");
	public static final Event MOUSEUP = new Event("mouseup");
	public static final Event RESET = new Event("reset");
	public static final Event RESIZE = new Event("resize");
	public static final Event SELECT = new Event("select");
	public static final Event SUBMIT = new Event("submit");
	public static final Event UNLOAD = new Event("unload");
}
