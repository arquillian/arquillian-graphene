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
String.prototype.startsWith = function(str) {
	return (this.match("^"+str)==str);
}

String.prototype.leftpad = function(length, character) {
	var result = '' + this;
	character = character == undefined ? ' ' : character;
	while (result.length < length) {
		result = character + result;
	}
	return result;
}

String.prototype.rightpad = function(length, character) {
	var result = '' + this;
	character = character == undefined ? ' ' : character;
	while (result.length < length) {
		result = result + character;
	}
	return result;
}

var Timeline = function() {
	this.rowList_ = new TimelineRowList();
	this.timelineStats_ = null;
};

Timeline.prototype.initialize = function(events) {
	var self = this;
	
	var sortedEvents = new Array();
	for (var i = 0; i < events.length; i++) {
		var event = new TimelineEvent(events[i]);
		if (!event.getIdentifier().startsWith("http://localhost:8444/selenium-server/")) {
			sortedEvents.push(event);
		}
	}
	events = null;
	
	sortedEvents.sort(this.sortEvents);
	
	for (var i = 0; i < sortedEvents.length; i++) {
		self.processEvent_(sortedEvents[i]);
	}
	
	this.countStats();
};

Timeline.prototype.initializeByJson = function(jsonData) {
	var events = JSON.parse(jsonData);
	this.initialize(events);
}

/**
 * @param {TimelineEvent}
 *            timelineEvent
 */
Timeline.prototype.processEvent_ = function(event) {
	var row = this.rowList_.getRow(event);
	row.addEvent(event);
};

Timeline.prototype.sortEvents = function(a, b) {
	return a.getStartTime() - b.getStartTime();
}

Timeline.prototype.countStats = function() {
	this.timelineStats_ = new TimelineStatistics();
	this.rowList_.countStats(this.timelineStats_);
}

Timeline.prototype.toString = function() {
	return "Timeline[ \n" + indent(this.timelineStats_.toString() + "\n" + this.rowList_.toString()) + "\n]";
}

var indent = function(string) {
	return '\t' + string.replace(/\n/g, '\n\t');
}





var TimelineRowList = function() {
	goog.Disposable.call(this);
	this.list_ = {};
}
goog.inherits(TimelineRowList, goog.Disposable);

TimelineRowList.prototype.getRow = function(event) {
	if (!this.list_[event.getIdentifier()]) {
		this.list_[event.getIdentifier()] = new TimelineRow();
	}
	return this.list_[event.getIdentifier()];
};

TimelineRowList.prototype.countStats = function(timelineStats) {
	for (var key in this.list_) {
		var row = this.list_[key];
		if (typeof row !== 'function') {
			row.countStats(timelineStats);
		}
	}
}

TimelineRowList.prototype.toString = function() {
	var result = [ ];
	for (var key in this.list_) {
		var row = this.list_[key];
		if (typeof row !== 'function') {
			result.push("Row [ identifier=" + key.toString() + "\n" + indent(row.toString()) + "\n]");
		}
	}
	return result.join(",\n");
}





var TimelineRow = function() {
	goog.Disposable.call(this);
	this.buckets_ = new Array();
	this.rowStats_ = new RowStatistics();
}
goog.inherits(TimelineRow, goog.Disposable);

TimelineRow.prototype.addEvent = function(event) {
	
	var bucket = this.getLastBucket();
	
	if (bucket == null || !bucket.pushEvent(event)) {
		bucket = new TimelineBucket();
		this.buckets_.push(bucket);
		bucket.pushEvent(event, this.rowStats_);
	}
}

TimelineRow.prototype.getLastBucket = function() {
	if (this.buckets_.length == 0) {
		return null;
	}
	return this.buckets_[this.buckets_.length - 1];
}

TimelineRow.prototype.countStats = function(timelineStats) {
	for (var i = 0; i < this.buckets_.length; i++) {
		var bucket = this.buckets_[i];
		bucket.countStats(timelineStats, this.rowStats_);
	}
}

TimelineRow.prototype.toString = function() {
	var result = [];
	result.push(this.rowStats_.toString());
	for (var i = 0; i < this.buckets_.length; i++) {
		result.push("Bucket [ \n" + indent(this.buckets_[i].toString()) + "\n]");
	}
	return result.join(",\n");
}






var TimelineBucket = function() {
	goog.Disposable.call(this);
	this.events_ = new Array();
	this.bucketStats_ = new BucketStatistics();
}
goog.inherits(TimelineBucket, goog.Disposable);

TimelineBucket.prototype.pushEvent = function(event) {
	var lastEvent = this.getLastEvent();
	
	if (lastEvent != null && lastEvent.update(event)) {
		return true;
	}
	
	if (this.shouldAdd(event)) {
		this.events_.push(event);
		return true;
	}
	
	return false;
}

TimelineBucket.prototype.shouldAdd = function(event) {
	var lastType = this.getLastType();
	if (lastType == null) {
		return true;
	}
	if (this.getLastTime() + 20 < event.getStartTime()) {
		return false;
	}
	if (lastType.isStackable()) {
		return (lastType.getOrder() <= event.getType().getOrder());
	}
	return (lastType.getOrder() < event.getType().getOrder());
}

TimelineBucket.prototype.getLastTime = function() {
	var lastTime = 0;
	for (var i = 0; i < this.events_.length; i++) {
		var event = this.events_[i];
		lastTime = Math.max(lastTime, event.getEndTime());
	}
	return lastTime;
}

TimelineBucket.prototype.getLastType = function() {
	var lastEvent = this.getLastEvent();
	return lastEvent ? lastEvent.getType() : null;
}

TimelineBucket.prototype.getLastEvent = function() {
	if (this.events_.length == 0) {
		return null;
	}
	return this.events_[this.events_.length - 1];
}

TimelineBucket.prototype.countStats = function(timelineStats, rowStats) {
	for (var i = 0; i < this.events_.length; i++) {
		var event = this.events_[i];
		timelineStats.pushEvent(event);
		rowStats.pushEvent(event);
		this.bucketStats_.pushEvent(event);
	}
}

TimelineBucket.prototype.toString = function() {
	var result = [];
	result.push(this.bucketStats_.toString());
	for (var i = 0; i < this.events_.length; i++) {
		result.push(this.events_[i].toString());
	}
	return result.join(",\n");
}




var TimelineEvent = function(event) {
	this.event_ = event;
};

TimelineEvent.prototype.getIdentifier = function() {
	return this.event_["identifier_"];
}

TimelineEvent.prototype.getStartTime = function() {
	return this.event_["startTimeUsec_"];
}

TimelineEvent.prototype.getDuration = function() {
	return this.event_["durationUsec_"];
}

TimelineEvent.prototype.getEndTime = function() {
	return this.event_["startTimeUsec_"] + this.event_["durationUsec_"];
}

TimelineEvent.prototype.getType = function() {
	return TimelineEventType.ordinalToEventTypeMap_[this.event_["eventType_"]];
}

TimelineEvent.prototype.getIntensity = function() {
	return this.event_["eventIntensity_"];
}

TimelineEvent.prototype.update = function(newEvent) {
	if (this.isSimilarTo(newEvent)) {
		if (this.getStartTime() == newEvent.getStartTime()) {
			this.event_["durationUsec_"] = newEvent.event_["durationUsec_"];
			return true;
		} else if (this.getEndTime() == newEvent.getStartTime()) {
			this.event_["durationUsec_"] += newEvent.event_["durationUsec_"];
			return true;
		} else if (this.getEndTime() >= newEvent.getEndTime()) {
			return true;
		}
	}
	
	return false;
}
	
TimelineEvent.prototype.isSimilarTo = function(event) {
	if (!(event instanceof TimelineEvent)) {
		return false;
	}
	if (this.getIdentifier() !== event.getIdentifier()) {
		return false;
	}
	if (this.getType() !== event.getType()) {
		return false;
	}
	return true;
}

TimelineEvent.prototype.toString = function() {
	return "TimelineEvent[ " + TimelineEvent.formatTime_(this.getStartTime()) + " (" + TimelineEvent.formatTime_(this.getDuration()) + ") | " + this.getType() + " ]";
}

TimelineEvent.formatTime_ = function(timeUsec) {
	  var timeMillis = timeUsec / 1000;
	  var prettyTime;
	  if (timeMillis >= 1000) {
	    prettyTime = Math.floor(timeMillis / 1000) + '.';
	    prettyTime += Math.round(timeMillis % 1000) + 'ms';
	  } else {
	    prettyTime = Math.round(timeMillis) + 'ms';
	  }
	  return prettyTime;
	};




	
var TimelineEventGroup = {
		REQUEST : 1,
		JAVASCRIPT : 2
}

/*
 * Requests:
 * =========
 * 2			- 5 	- 1		- 3				- 4			- 7			- 5			- 8					- 9
 * DNS lookup	- Send	- Wait	- Connecting	- Connected	- Receive	- Cache Hit	- Data Available	- Paint
 * 
 * JavaScript:
 * ===========
 * 10		- 11
 * JS Parse	- JS Execute
 */
var TimelineEventType = function(id, group, order) {
	this.id_ = id;
	this.group_ = group;
	this.order_ = order;
	this.isStackable_ = false;
}


TimelineEventType.NETWORK_WAIT = new TimelineEventType(1, TimelineEventGroup.REQUEST, 30); // Wait
TimelineEventType.DNS_LOOKUP = new TimelineEventType(2, TimelineEventGroup.REQUEST, 10); // DNS Lookup
TimelineEventType.TCP_CONNECTING = new TimelineEventType(3, TimelineEventGroup.REQUEST, 40); // Connect
TimelineEventType.TCP_CONNECTED = new TimelineEventType(4, TimelineEventGroup.REQUEST, 50); // Connected
TimelineEventType.REQUEST_SENT = new TimelineEventType(5, TimelineEventGroup.REQUEST, 20); // Send
TimelineEventType.CACHE_HIT = new TimelineEventType(6, TimelineEventGroup.REQUEST, 70); // Cache Hit
TimelineEventType.SOCKET_DATA = new TimelineEventType(7, TimelineEventGroup.REQUEST, 60); // Receive
TimelineEventType.DATA_AVAILABLE = new TimelineEventType(8, TimelineEventGroup.REQUEST, 80); // Data Available
TimelineEventType.PAINT = new TimelineEventType(9, TimelineEventGroup.REQUEST, 90); // Paint
TimelineEventType.JS_PARSE = new TimelineEventType(10, TimelineEventGroup.JAVASCRIPT, 10); // JS parse
TimelineEventType.JS_EXECUTE = new TimelineEventType(11, TimelineEventGroup.JAVASCRIPT, 20); // JS execute

TimelineEventType.prototype.getId = function() {
	return this.id_;
}

TimelineEventType.prototype.getOrder = function() {
	return this.order_;
}

TimelineEventType.prototype.getGroup = function() {
	return this.group_;
}

TimelineEventType.prototype.isStackable = function() {
	return this.isStackable_;
}

TimelineEventType.prototype.toString = function() {
	for (var key in TimelineEventType) {
		if (TimelineEventType[key] instanceof TimelineEventType) {
			if (TimelineEventType[key] == this) {
				return key;
			}
		}
	}
}

TimelineEventType.stackables = [
	TimelineEventType.SOCKET_DATA,
	TimelineEventType.DATA_AVAILABLE,
	TimelineEventType.JS_EXECUTE,
	TimelineEventType.JS_PARSE
];

for (var i = 0; i < TimelineEventType.stackables.length; i++) {
	var stackable = TimelineEventType.stackables[i];
	stackable.isStackable_ = true;
}

TimelineEventType.ordinalToEventTypeMap_ = [ null,
		TimelineEventType.NETWORK_WAIT,
		TimelineEventType.DNS_LOOKUP,
		TimelineEventType.TCP_CONNECTING,
		TimelineEventType.TCP_CONNECTED,
		TimelineEventType.REQUEST_SENT,
		TimelineEventType.CACHE_HIT,
		TimelineEventType.SOCKET_DATA,
		TimelineEventType.DATA_AVAILABLE,
		TimelineEventType.PAINT,
		TimelineEventType.JS_PARSE,
		TimelineEventType.JS_EXECUTE ];






/*
 * Statistics
 */
var Statistics = function() {
	this.startTime_ = undefined;
	this.endTime_ = 0;
	this.duration_ = undefined;
	this.typeStats_ = {};
}

Statistics.prototype.NAME = "Statistics";

Statistics.prototype.pushEvent = function(event) {
	this.startTime_ = (this.startTime_) ? Math.min(this.startTime_, event.getStartTime()) : event.getStartTime();
	this.endTime_ = Math.max(this.endTime_, event.getEndTime());
	this.duration_ = this.endTime_ - this.startTime_;
	
	var typeStats = this.getOrCreateTypeStats(event);
	typeStats.addEvent(event);
}

Statistics.prototype.getOrCreateTypeStats = function(event) {
	if (this.typeStats_[event.getType()] == null) {
		this.typeStats_[event.getType()] = new TypeStats();
	}
	return this.typeStats_[event.getType()];
}

Statistics.prototype.toString = function() {
	var result = [];
	result.push(this.NAME + " [ " + TimelineEvent.formatTime_(this.startTime_) + " ~ " + TimelineEvent.formatTime_(this.endTime_) + " (" + TimelineEvent.formatTime_(this.duration_) + ")")
	result.push("{");
	
	var maxLengthKey = 0;
	var maxLengthCount = 0;
	var maxLengthDuration = 0;
	for (var key in this.typeStats_) {
		var typeStat = this.typeStats_[key];
		if (typeStat instanceof TypeStats) {
			maxLengthKey = Math.max(maxLengthKey, key.length);
			maxLengthCount = Math.max(maxLengthCount, ('' + typeStat.getCount()).length);
			maxLengthDuration = Math.max(maxLengthDuration, ('' + typeStat.getDurationAsString()).length);
		}
	}
	for (var key in this.typeStats_) {
		var typeStat = this.typeStats_[key];
		if (this.typeStats_[key] instanceof TypeStats) {
			result.push(indent(key.rightpad(maxLengthKey) + " = " + typeStat.getDurationAsString().leftpad(maxLengthDuration) + " [" + new String(typeStat.getCount()).leftpad(maxLengthCount) + "]"));
		}
	}
	result.push("}");
	return result.join("\n");
}

/*
 * BucketStatistics
 */
var BucketStatistics = function() {
	Statistics.call(this);
}
goog.inherits(BucketStatistics, Statistics);
BucketStatistics.prototype.NAME = "BucketStatistics";

/*
 * RowStatistics
 */
var RowStatistics = function() {
	Statistics.call(this);
}
goog.inherits(RowStatistics, Statistics);
RowStatistics.prototype.NAME = "RowStatistics";


/*
 * TimelineStatistics
 */
var TimelineStatistics = function() {
	Statistics.call(this);
}
goog.inherits(TimelineStatistics, Statistics);
TimelineStatistics.prototype.NAME = "TimelineStatistics";








/*
 * Stats
 */
var Stats = function() {
	this.count_ = 0;
	this.duration_ = 0;
}

Stats.prototype.getCount = function() {
	return this.count_;
}

Stats.prototype.getDuration = function() {
	return this.duration_;
}

Stats.prototype.getDurationAsString = function() {
	return TimelineEvent.formatTime_(this.duration_);
}

Stats.prototype.addEvent = function(event) {
	this.count_ += 1;
	this.duration_ += event.getDuration();
}



/*
 * TypeStats
 */
var TypeStats = function() {
	Stats.call(this);
}
goog.inherits(TypeStats, Stats);