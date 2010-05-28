/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
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
 */
var EventRecorder = function() {
	this.element_ = undefined;
	this.event_ = undefined;
}

EventRecorder.prototype.getDocument_ = function () {
	return window.document;
}

EventRecorder.prototype.getHandlerElement_ = function() {
	return this.getDocument_().getElementsByTagName("ActivityEventRecorder")[0];
}

EventRecorder.prototype.createEvent_ = function() {
	return this.getDocument_().createEvent("Events");
}

EventRecorder.prototype.initiliaze_ = function(eventName) {
	this.element_ = this.getHandlerElement_();
	this.event_ = this.createEvent_();
	this.event_.initEvent(eventName, true, false);
}

EventRecorder.prototype.dispatch_ = function() {
	this.element_.dispatchEvent(this.event_);
}

EventRecorder.prototype.isExtensionInstalled = function() {
	return (this.getHandlerElement_()) ? true : false;
}

EventRecorder.prototype.open = function() {
	this.initiliaze_("ActivityEventRecorderOpenFirebugEvent");
	this.dispatch_();
}

EventRecorder.prototype.close = function() {
	this.initiliaze_("ActivityEventRecorderCloseFirebugEvent");
	this.dispatch_();
}

EventRecorder.prototype.stopProfiler = function() {
	this.initiliaze_("ActivityEventRecorderStopProfilerEvent");
	this.dispatch_();
}

EventRecorder.prototype.markEvent = function(mark) {
	this.initiliaze_("ActivityEventRecorderMarkEvent");
	this.element_.setAttribute("mark", mark);
	this.dispatch_();
}

EventRecorder.prototype.flushEvents = function() {
	this.initiliaze_("ActivityEventRecorderFlushEvent");
	this.element_.setAttribute("data", "");
	this.dispatch_();
}

EventRecorder.prototype.isDataAvailable = function() {
	return this.getHandlerElement_().getAttribute("data") != "";
}

EventRecorder.prototype.getData = function() {
	var element = this.getHandlerElement_();
	var data = element.getAttribute("data");
	element.setAttribute("data", "");
	return data;
}