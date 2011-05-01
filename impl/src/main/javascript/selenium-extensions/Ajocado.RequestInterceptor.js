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
var Ajocado = Ajocado || {};

Ajocado.RequestInterceptor = Ajocado.RequestInterceptor || {};

Ajocado.RequestInterceptor.getRequestDone = function() {
	return (Ajocado.getPage() === undefined) ? 'HTTP' : Ajocado.getPage().RequestGuard.getRequestDone();
};

Ajocado.RequestInterceptor.clearRequestDone = function() {
	var result;
	if (Ajocado.getPage() === undefined) {
		selenium.doWaitForPageToLoad();
		result = 'HTTP';
	} else {
		result = Ajocado.getPage().RequestGuard.clearRequestDone();
	};
	return result;
};

Selenium.prototype.doWaitForRequest = function(timeout) {
	return this.makeRequestChangeCondition(timeout);
};

Selenium.prototype.makeRequestChangeCondition = function(timeout) {
    if (timeout == null) {
        timeout = this.defaultTimeout;
    }
    // if the timeout is zero, we won't wait for the page to load before returning
    if (timeout == 0) {
	  // abort XHR request  
          this._abortXhrRequest(); 	   
    	  return;
    }
    return Selenium.decorateFunctionWithTimeout(fnBind(this._isRequestChanged, this), timeout, fnBind(this._abortXhrRequest, this));
};

Selenium.prototype._isRequestChanged = function() {
	var newPageLoaded = this.browserbot.isNewPageLoaded();
	return (this.browserbot.getCurrentWindow().RichFacesSelenium === undefined) ? newPageLoaded : this.browserbot.getCurrentWindow().RichFacesSelenium.requestDone != 'NONE' ;
};