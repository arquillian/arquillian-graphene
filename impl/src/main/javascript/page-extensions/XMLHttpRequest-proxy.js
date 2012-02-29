/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
var Graphene = Graphene || {};

Graphene.XHRWrapper = function() {
	this.xhr;
	this.responseText= "";
	this.responseXml= null;
	this.readyState= 0;
	this.status= 0;
	this.statusText= 0;
	this.onreadystatechange= null;
};
	
Graphene.XHRWrapper.prototype.abort = function() {
    return this.xhr.abort();
};

Graphene.XHRWrapper.prototype.open = function(method, url, asyncFlag, userName, password) {
    asyncFlag = (asyncFlag !== false);
	return this.xhr.open(method, url, asyncFlag, userName, password);
};

Graphene.XHRWrapper.prototype.getAllResponseHeaders = function() {
    return this.xhr.getAllResponseHeaders();
};

Graphene.XHRWrapper.prototype.getResponseHeader = function(name) {
    return this.xhr.getResponseHeader(name);
};

Graphene.XHRWrapper.prototype.send = function(content) {
	return this.xhr.send(content);
};

Graphene.XHRWrapper.prototype.setRequestHeader = function(name, value) {
	return this.xhr.setRequestHeader(name, value);
};

Graphene.XHRWrapper.prototype.onreadystatechangeCallback = function(event) {
	if (this.onreadystatechange) {
		return this.onreadystatechange.call(this.xhr, event);
	}
}

if (window.ActiveXObject) {
	
	Graphene.ActiveXObject = window.ActiveXObject;
	
	Graphene.XHRActiveXObject = function(xhr) {
		Graphene.XHRWrapper.call(this);
		this.xhr = xhr;
		var proxy = this;
		this.xhr.onreadystatechange = function() {
			proxy.readyState = proxy.xhr.readyState;
			if (proxy.readyState == 4) {
				proxy.responseText = proxy.xhr.responseText;
				proxy.responseXML  = proxy.xhr.responseXML;
				proxy.status       = proxy.xhr.status;
				proxy.statusText   = proxy.xhr.statusText;
			}
			if (proxy.onreadystatechange) proxy.onreadystatechangeCallback(event);
	      };
	};

	Graphene.extend(Graphene.XHRActiveXObject, Graphene.XHRWrapper);

	var ActiveXObject = function(type, location) {
		var realActiveXObject = (location) ? new Graphene.ActiveXObject(type, location) : new Graphene.ActiveXObject(type);
		
		type = type.toLowerCase();
		if (type == "msxml2.xmlhttp" || type == "microsoft.xmlhttp") {
			var proxy = new Graphene.XHRActiveXObject(realActiveXObject);
		} else {
			var proxy = realActiveXObject;
		}
	
		return proxy;
	}
	
} else if (window.XMLHttpRequest) {
	
	Graphene.XMLHttpRequest = window.XMLHttpRequest;
	
	var XMLHttpRequest = function() {
		Graphene.XHRWrapper.call(this);
		this.xhr = new Graphene.XMLHttpRequest();
	};

	Graphene.extend(XMLHttpRequest, Graphene.XHRWrapper);
	
	XMLHttpRequest.prototype.onreadystatechangeWrapper = function() {
		var self = this;
		
		return function(event) {
			self.readyState = this.readyState;
			if (self.readyState == 4) {
				self.responseText = this.responseText;
				self.responseXML  = this.responseXML;
				self.status       = this.status;
				self.statusText   = this.statusText;
			}
			self.onreadystatechangeCallback(event);
		};
	};

	XMLHttpRequest.prototype.open = function(method, url, asyncFlag, userName, password) {
		this.xhr.onreadystatechange = this.onreadystatechangeWrapper();
		Graphene.XHRWrapper.prototype.open.call(this, method, url, asyncFlag, userName, password);
	}
}