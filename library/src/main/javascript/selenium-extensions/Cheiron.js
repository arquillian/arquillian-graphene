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