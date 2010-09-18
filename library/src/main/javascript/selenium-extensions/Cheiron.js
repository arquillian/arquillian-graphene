var Cheiron = function() {
	this.requestInterceptor = new Cheiron.RequestInterceptor();
};

Cheiron.RequestInterceptor = function() {
};

Cheiron.RequestInterceptor.prototype.getRequestTypeDone = function() {
	return (getRFS() === undefined) ? 'HTTP' : getRFS().getRequestDone();
};

Cheiron.RequestInterceptor.prototype.clearRequestDone = function() {
	var result;
	if (getRFS() === undefined) {
		selenium.doWaitForPageToLoad();
		result = 'HTTP';
	} else {
		result = getRFS().clearRequestDone();
	};
	return result;
};

Cheiron.RequestInterceptor.prototype.waitRequestChange = function() {
	return ((getRFS() === undefined) ? 'HTTP' : getRFS().getRequestDone()) != 'NONE' && selenium.browserbot.getCurrentWindow().document.body;
};

var cheiron = new Cheiron();



Selenium.prototype.doWaitForRequestChange = function(timeout) {
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