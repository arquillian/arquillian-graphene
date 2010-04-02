Selenium.prototype.getStyle = function(locator, property) {
	var element = this.browserbot.findElementOrNull(locator);
	
	if (element == null) {
			throw new SeleniumError("null property value");
	}
	
	var currentDocument = this.browserbot.getCurrentWindow().document;
	
	return jQuery(element, currentDocument).css(property);
}

/**
 * Aligns screen to top (resp. bottom) of element given by locator.
 * 
 * TODO should be reimplemented to use of JQuery.scrollTo
 * 
 * @param locator of element which should be screen aligned to
 * @param alignToTop should be top border of screen aligned to top border of element
 */
Selenium.prototype.scrollIntoView = function(locator, alignToTop) {
	var elem = this.browserbot.findElementOrNull(locator);
	
	if (elem == null) {
		throw new SeleniumError("null property value");
	}
	
	if (elem.scrollIntoView == undefined) {
		throw new SeleniumError("scrollIntoView isn't supported at this element");
	}
	
	elem.scrollIntoView(alignToTop);
}
 
/**
 * Simulates a user hovering a mouse over the specified element at specific
 * coordinates relative to element.
 * 
 * @param locator
 *            element's locator
 * @param coordString
 *            specifies the x,y position (i.e. - 10,20) of the mouse event
 *            relative to the element returned by the locator.
 */
Selenium.prototype.doMouseOverAt = function(locator, coordString) {
	var element = this.browserbot.findElement(locator);
	var clientXY = getClientXY(element, coordString)

	this.browserbot.triggerMouseEvent(element, 'mouseover', true, clientXY[0],
			clientXY[1]);
}

/**
 * Returns the count of elements for given jQuery selector
 * 
 * @param selector
 *            jQuery selector
 * @return count of elements matching given selector
 */
Selenium.prototype.getJQueryCount = function(selector) {
	var inDocument = this.browserbot.getDocument();
	var found = $(inDocument).find(selector);
	return found.length;
}

/**
 * Gets the text of an element. This works for any element that contains
 * text. This command uses either the textContent (Mozilla-like browsers) or
 * the innerText (IE-like browsers) of the element, which is the rendered
 * text shown to the user.
 * 
 * If no element with given locator is found, returns null.
 *
 * @param locator an element locator
 * @return string the text of the element or null if element's wasn't found
 */
Selenium.prototype.getTextOrNull = function(locator) {
	var element = this.browserbot.findElementOrNull(locator);
	if (element == null) {
		throw new SeleniumError("element is not found");
	}
	return getText(element).trim();
};

/**
 * Add required script to page.
 * 
 * Use a id of script (ideally based on hash of script) to avoid adding the same script twice.
 * 
 * (This code should refuse adding script to the page duplicitly due to usage
 * of hash-id, so you can add it thought you are not sure script is already
 * added or not - this is usefull for adding libraries directly to the
 * page).
 * 
 * Script will be appended to end of the body tag within the script
 * tag.
 * 
 * @param script what should be added to the page
 */
Selenium.prototype.addScriptLocally = function(id, script) {
	var inDocument = this.browserbot.getCurrentWindow().document;
	if (inDocument.getElementById(id)) {
		return;
	}
	var scriptTag = inDocument.createElement('script');
	scriptTag.id = id;
	scriptTag.innerHTML = script;
	inDocument.body.appendChild(scriptTag);
};
 