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